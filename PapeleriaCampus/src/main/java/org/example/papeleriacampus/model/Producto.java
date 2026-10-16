
package org.example.papeleriacampus.model;

import java.math.BigDecimal;

public class Producto {

    // Atributos del producto
    private int idProducto;
    private String codigo;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int stock;

    // Constructor
    public Producto(
            int idProducto,
            String codigo,
            String nombre,
            String categoria,
            BigDecimal precio,
            int stock) {

        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters

    public int getIdProducto() {
        return idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    // Setters

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
