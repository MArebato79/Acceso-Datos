package com.RebatoSoft.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "producto")
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precioRecomendado;
    private int stock;

    public Producto(int id, String nombre, String descripcion, double precioRecomendado, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioRecomendado = precioRecomendado;
        this.stock = stock;
    }

    public Producto(String nombre, String descripcion, double precioRecomendado, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioRecomendado = precioRecomendado;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioRecomendado() {
        return precioRecomendado;
    }

    public void setPrecioRecomendado(double precioRecomendado) {
        this.precioRecomendado = precioRecomendado;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producto:" +
                "id=" + id +
                "\nnombre='" + nombre + '\'' +
                "\ndescripcion='" + descripcion + '\'' +
                "\nprecioRecomendado=" + precioRecomendado +
                "\nstock=" + stock +
                "\n------------------------\n";
    }

}
