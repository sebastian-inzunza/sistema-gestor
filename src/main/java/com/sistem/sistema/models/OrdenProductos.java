package com.sistem.sistema.models;

public class OrdenProductos {
    private Long ordenProductoId;
    private Long productoId;
    private String producto;
    private Integer cantidad;
    private Double precio;
    private Boolean atendido;

    public OrdenProductos() {
    }

    public OrdenProductos(Long ordenProductoId, Long productoId, String producto, Integer cantidad, Double precio, Boolean atendido) {
        this.ordenProductoId = ordenProductoId;
        this.productoId = productoId;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.atendido = atendido;
    }

    public Long getOrdenProductoId() {
        return ordenProductoId;
    }

    public void setOrdenProductoId(Long ordenProductoId) {
        this.ordenProductoId = ordenProductoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public String getProducto() {
        return producto;
    }
    public void setProducto(String producto) {
        this.producto = producto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public Double getPrecio() {
        return precio;
    }
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getAtendido() {
        return atendido;
    }

    public void setAtendido(Boolean atendido) {
        this.atendido = atendido;
    }

    @Override
    public String toString() {
        return "OrdenProductos [producto=" + producto + ", cantidad=" + cantidad + ", precio=" + precio + "]";
    }

   
}
