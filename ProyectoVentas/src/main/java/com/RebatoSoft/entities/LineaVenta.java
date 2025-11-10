package com.RebatoSoft.entities;

public class LineaVenta {

    private int id;
    private Venta venta;
    private Producto producto;
    private int cantidad;
    private double precioVenta;
    private int descuento;

    public LineaVenta(int id,Venta venta, Producto producto, int cantidad, double precioVenta, int descuento) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.descuento = descuento;
    }

    public LineaVenta(Producto producto, int cantidad, double precioVenta, int descuento) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public double getImporteLinea() {
        double importe = cantidad * precioVenta;
        if (descuento > 0) {
            importe -= importe * (descuento / 100.0);
        }
        return importe;
    }

    @Override
    public String toString() {
        return "LineaVenta:" +
                "id=" + id +
                "\nproducto=" + (producto != null ? producto.getDescripcion() : "null") +
                "\ncantidad=" + cantidad +
                "\nprecioVenta=" + precioVenta +
                "\ndescuento=" + descuento +
                "\nimporteLinea=" + getImporteLinea() +
                '\n';
    }
}

