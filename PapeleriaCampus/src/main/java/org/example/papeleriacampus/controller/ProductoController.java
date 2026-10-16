
package org.example.papeleriacampus.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.papeleriacampus.dao.ProductoDAO;
import org.example.papeleriacampus.model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProductoController {

    // Campos del formulario

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    // Componentes de búsqueda

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkConExistencias;

    // Tabla y contador

    @FXML private TableView<Producto> tblProductos;

    @FXML private TableColumn<Producto, Integer> colIdProducto;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private Label lblContador;

    // Lista observable de productos

    private final ObservableList<Producto> listaProductos =
            FXCollections.observableArrayList();

    // Acceso a los datos de PostgreSQL

    private final ProductoDAO productoDAO = new ProductoDAO();


    // Inicializar el formulario y cargar los productos

    @FXML
    private void initialize() {

        configurarTableView();
        configurarSeleccionTableView();

        cmbFiltroCategoria.setValue("Todas");

        cargarProductos();
    }


    // Configurar las columnas del TableView

    private void configurarTableView() {

        colIdProducto.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getIdProducto()
                ).asObject()
        );

        colCodigo.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCodigo()
                )
        );

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNombre()
                )
        );

        colCategoria.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCategoria()
                )
        );

        colPrecio.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getPrecio()
                )
        );

        colStock.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getStock()
                ).asObject()
        );

        tblProductos.setItems(listaProductos);
    }


    // Cargar los datos de la fila seleccionada al formulario

    private void configurarSeleccionTableView() {

        tblProductos.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, nuevo) -> {

                    if (nuevo != null) {
                        cargarProductoEnFormulario(nuevo);
                    }

                });
    }


    // Mostrar un producto seleccionado en el formulario

    private void cargarProductoEnFormulario(Producto producto) {

        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());

        cmbCategoria.setValue(producto.getCategoria());

        txtPrecio.setText(
                producto.getPrecio().toPlainString()
        );

        txtStock.setText(
                String.valueOf(producto.getStock())
        );
    }


    // Consultar PostgreSQL y actualizar el TableView

    private void cargarProductos() {

        String texto = txtBuscar.getText().trim();

        String categoria = cmbFiltroCategoria.getValue();

        boolean conExistencias =
                chkConExistencias.isSelected();

        try {

            List<Producto> productos =
                    productoDAO.consultarProductos(
                            texto,
                            categoria,
                            conExistencias
                    );

            listaProductos.setAll(productos);

            actualizarContador();

        } catch (SQLException e) {

            lblContador.setText("Error al consultar productos");

            mostrarErrorBaseDatos(
                    "Consultar productos",
                    e
            );
        }
    }


    // Actualizar la cantidad de productos visibles

    private void actualizarContador() {

        lblContador.setText(
                "Productos visibles: " + listaProductos.size()
        );
    }


    // Validar y obtener los datos del formulario

    private Producto obtenerProductoFormulario(int idProducto) {

        String codigo = txtCodigo.getText()
                .trim()
                .toUpperCase(Locale.ROOT);

        String nombre = txtNombre.getText().trim();

        String categoria = cmbCategoria.getValue();

        String precioTexto = txtPrecio.getText().trim();

        String stockTexto = txtStock.getText().trim();

        // Validar código

        if (codigo.isEmpty() || codigo.length() > 10) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Código incorrecto",
                    "El código debe tener entre 1 y 10 caracteres."
            );

            txtCodigo.requestFocus();
            return null;
        }

        // Validar nombre

        if (nombre.length() < 3 || nombre.length() > 80) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Nombre incorrecto",
                    "El nombre debe tener entre 3 y 80 caracteres."
            );

            txtNombre.requestFocus();
            return null;
        }

        // Validar categoría

        if (categoria == null ||
                !(categoria.equals("Papelería")
                        || categoria.equals("Tecnología")
                        || categoria.equals("Accesorios"))) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Categoría incorrecta",
                    "Seleccione una categoría válida."
            );

            return null;
        }

        // Validar precio

        BigDecimal precio;

        try {

            if (!precioTexto.matches("\\d+(\\.\\d{1,2})?")) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio incorrecto",
                        "Ingrese un precio positivo con máximo 2 decimales."
                );

                txtPrecio.requestFocus();
                return null;
            }

            precio = new BigDecimal(precioTexto);

            if (precio.compareTo(new BigDecimal("0.01")) < 0
                    || precio.compareTo(
                    new BigDecimal("99999999.99")
            ) > 0) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio fuera de rango",
                        "El precio debe estar entre 0.01 y 99999999.99."
                );

                return null;
            }

        } catch (NumberFormatException e) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Precio incorrecto",
                    "Ingrese un precio numérico válido."
            );

            return null;
        }

        // Validar stock

        int stock;

        try {

            if (!stockTexto.matches("\\d+")) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Stock incorrecto",
                        "El stock debe ser un número entero no negativo."
                );

                txtStock.requestFocus();
                return null;
            }

            stock = Integer.parseInt(stockTexto);

        } catch (NumberFormatException e) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Stock incorrecto",
                    "El stock debe estar entre 0 y 2147483647."
            );

            return null;
        }

        // Normalizar los campos antes de guardar

        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);

        return new Producto(
                idProducto,
                codigo,
                nombre,
                categoria,
                precio,
                stock
        );
    }


    // Limpiar los campos para ingresar un producto nuevo

    @FXML
    private void nuevoProducto() {

        limpiarFormulario();
        txtCodigo.requestFocus();
    }


    // Limpiar el formulario sin modificar los filtros

    private void limpiarFormulario() {

        tblProductos.getSelectionModel().clearSelection();

        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();

        cmbCategoria.setValue(null);
    }


    // Guardar un producto nuevo

    @FXML
    private void guardarProducto() {

        Producto producto = obtenerProductoFormulario(0);

        if (producto == null) {
            return;
        }

        try {

            // Comprobar que el código no exista

            if (productoDAO.existeCodigo(
                    producto.getCodigo(),
                    null
            )) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Código duplicado",
                        "El código ya pertenece a otro producto."
                );

                return;
            }

            int filasAfectadas =
                    productoDAO.guardarProducto(producto);

            if (filasAfectadas > 0) {

                limpiarFormulario();
                cargarProductos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Producto guardado",
                        "El producto se guardó correctamente."
                );
            }

        } catch (SQLException e) {

            mostrarErrorBaseDatos("Guardar producto", e);
        }
    }


    // Actualizar un producto seleccionado

    @FXML
    private void actualizarProducto() {

        Producto seleccionado =
                tblProductos.getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Producto no seleccionado",
                    "Seleccione un producto antes de actualizar."
            );

            return;
        }

        Producto producto = obtenerProductoFormulario(
                seleccionado.getIdProducto()
        );

        if (producto == null) {
            return;
        }

        try {

            // Comprobar códigos duplicados, excluyendo el propio ID

            if (productoDAO.existeCodigo(
                    producto.getCodigo(),
                    producto.getIdProducto()
            )) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Código duplicado",
                        "El código pertenece a otro producto."
                );

                return;
            }

            int filasAfectadas =
                    productoDAO.actualizarProducto(producto);

            if (filasAfectadas > 0) {

                limpiarFormulario();
                cargarProductos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Producto actualizado",
                        "El producto se actualizó correctamente."
                );

            } else {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Producto no encontrado",
                        "El producto ya no existe en la base de datos."
                );

                cargarProductos();
            }

        } catch (SQLException e) {

            mostrarErrorBaseDatos("Actualizar producto", e);
        }
    }


    // Eliminar un producto seleccionado con confirmación

    @FXML
    private void eliminarProducto() {

        Producto seleccionado =
                tblProductos.getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Producto no seleccionado",
                    "Seleccione un producto antes de eliminar."
            );

            return;
        }

        Alert confirmacion =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar producto");

        confirmacion.setContentText(
                "¿Desea eliminar el producto "
                        + seleccionado.getNombre() + "?"
        );

        Optional<ButtonType> respuesta =
                confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {

            return;
        }

        try {

            int filasAfectadas =
                    productoDAO.eliminarProducto(
                            seleccionado.getIdProducto()
                    );

            if (filasAfectadas > 0) {

                limpiarFormulario();
                cargarProductos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Producto eliminado",
                        "El producto se eliminó correctamente."
                );

            } else {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Producto no encontrado",
                        "El producto ya no existe en la base de datos."
                );

                cargarProductos();
            }

        } catch (SQLException e) {

            mostrarErrorBaseDatos("Eliminar producto", e);
        }
    }


    // Buscar productos aplicando los filtros seleccionados

    @FXML
    private void buscarProductos() {

        cargarProductos();

        if (lblContador.getText().startsWith("Error")) {
            return;
        }

        if (listaProductos.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Sin coincidencias",
                    "No existen productos con esos filtros."
            );
        }
    }


    // Limpiar los filtros de búsqueda

    @FXML
    private void limpiarFiltros() {

        txtBuscar.clear();

        cmbFiltroCategoria.setValue("Todas");

        chkConExistencias.setSelected(false);

        cargarProductos();
    }


    // Volver a consultar los productos en PostgreSQL

    @FXML
    private void recargarProductos() {

        cargarProductos();
    }


    // Mostrar mensajes al usuario

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje
    ) {

        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }


    // Gestionar los errores de PostgreSQL

    private void mostrarErrorBaseDatos(
            String operacion,
            SQLException e
    ) {

        System.err.println(
                "Operación: " + operacion
                        + " | SQLState: " + e.getSQLState()
        );

        String mensaje;

        if ("23505".equals(e.getSQLState())) {

            mensaje = "El código ingresado ya existe.";

        } else if ("23514".equals(e.getSQLState())) {

            mensaje = "Los datos no cumplen las restricciones de la base de datos.";

        } else {

            mensaje = "No se pudo completar la operación. Revise la conexión con PostgreSQL.";
        }

        mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error de base de datos",
                mensaje
        );
    }
}
