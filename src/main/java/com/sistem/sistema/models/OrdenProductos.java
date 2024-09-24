package com.sistem.sistema.models;

public class OrdenProductos {
    private Long productoId;
    private String producto;
    private Integer cantidad;
    private Double precio;

    public OrdenProductos() {
    }

    public OrdenProductos(String producto, Integer cantidad, Double precio) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "OrdenProductos [producto=" + producto + ", cantidad=" + cantidad + ", precio=" + precio + "]";
    }

 

    
}
