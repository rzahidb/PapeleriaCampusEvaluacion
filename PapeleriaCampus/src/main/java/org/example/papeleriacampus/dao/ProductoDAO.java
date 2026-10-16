
package org.example.papeleriacampus.dao;

import org.example.papeleriacampus.connection.DataBaseConnection;
import org.example.papeleriacampus.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductoDAO {

    // Consultar productos aplicando los filtros de búsqueda

    public List<Producto> consultarProductos(
            String texto,
            String categoria,
            boolean conExistencias
    ) throws SQLException {

        List<Producto> productos = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    id_producto,
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    stock
                FROM public.producto
                WHERE 1 = 1
                """);

        // Buscar por código o nombre

        boolean buscarTexto =
                texto != null && !texto.trim().isEmpty();

        if (buscarTexto) {

            sql.append("""
                    AND (
                        codigo ILIKE ?
                        OR nombre ILIKE ?
                    )
                    """);
        }

        // Filtrar por categoría

        boolean filtrarCategoria =
                categoria != null
                        && !categoria.equals("Todas")
                        && !categoria.isBlank();

        if (filtrarCategoria) {

            sql.append("""
                    AND categoria = ?
                    """);
        }

        // Filtrar productos con existencias

        if (conExistencias) {

            sql.append("""
                    AND stock > 0
                    """);
        }

        // Ordenar por código ascendente

        sql.append(" ORDER BY codigo ASC");

        try (
                Connection conexion =
                        DataBaseConnection.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql.toString())
        ) {

            int indice = 1;

            // Asignar los parámetros de búsqueda

            if (buscarTexto) {

                String busqueda = "%" + texto.trim() + "%";

                ps.setString(indice++, busqueda);
                ps.setString(indice++, busqueda);
            }

            if (filtrarCategoria) {

                ps.setString(indice++, categoria);
            }

            // Ejecutar consulta y crear objetos Producto

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Producto producto = new Producto(

                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    );

                    productos.add(producto);
                }
            }
        }

        return productos;
    }


    // Verificar si existe un código registrado

    public boolean existeCodigo(
            String codigo,
            Integer idProductoExcluir
    ) throws SQLException {

        String sql = """
                SELECT 1
                FROM public.producto
                WHERE codigo = ?
                """;

        // Excluir el mismo producto durante una actualización

        if (idProductoExcluir != null) {

            sql += " AND id_producto <> ?";
        }

        try (
                Connection conexion =
                        DataBaseConnection.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    codigo.trim().toUpperCase(Locale.ROOT)
            );

            if (idProductoExcluir != null) {

                ps.setInt(2, idProductoExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // Guardar un producto nuevo en PostgreSQL

    public int guardarProducto(
            Producto producto
    ) throws SQLException {

        String sql = """
                INSERT INTO public.producto
                (
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    stock
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conexion =
                        DataBaseConnection.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    producto.getCodigo()
            );

            ps.setString(
                    2,
                    producto.getNombre()
            );

            ps.setString(
                    3,
                    producto.getCategoria()
            );

            ps.setBigDecimal(
                    4,
                    producto.getPrecio()
            );

            ps.setInt(
                    5,
                    producto.getStock()
            );

            return ps.executeUpdate();
        }
    }


    // Actualizar los datos de un producto por su ID

    public int actualizarProducto(
            Producto producto
    ) throws SQLException {

        String sql = """
                UPDATE public.producto
                SET
                    codigo = ?,
                    nombre = ?,
                    categoria = ?,
                    precio = ?,
                    stock = ?
                WHERE id_producto = ?
                """;

        try (
                Connection conexion =
                        DataBaseConnection.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    producto.getCodigo()
            );

            ps.setString(
                    2,
                    producto.getNombre()
            );

            ps.setString(
                    3,
                    producto.getCategoria()
            );

            ps.setBigDecimal(
                    4,
                    producto.getPrecio()
            );

            ps.setInt(
                    5,
                    producto.getStock()
            );

            ps.setInt(
                    6,
                    producto.getIdProducto()
            );

            return ps.executeUpdate();
        }
    }


    // Eliminar un producto de PostgreSQL por su ID

    public int eliminarProducto(
            int idProducto
    ) throws SQLException {

        String sql = """
                DELETE FROM public.producto
                WHERE id_producto = ?
                """;

        try (
                Connection conexion =
                        DataBaseConnection.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(1, idProducto);

            return ps.executeUpdate();
        }
    }
}
