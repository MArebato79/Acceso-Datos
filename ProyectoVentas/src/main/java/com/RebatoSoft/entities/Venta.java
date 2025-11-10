package com.RebatoSoft.entities;

import com.RebatoSoft.service.ServicioVenta;
import com.RebatoSoft.utiles.Instanciacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Venta {
    private static Set<Venta> productosNumero;

    private int id;
    private Cliente cliente;
    private ArrayList<LineaVenta> lineas;
    private double total;

    private final Instanciacion instanciacion = new Instanciacion();

    public Venta(int id, Cliente cliente,double total) {
        this.id = id;
        this.cliente = cliente;
        this.total = 0;
    }
    public Venta(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;

    }

    public Venta(Cliente cliente) {
        this.cliente = cliente;
    }

    public Venta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<LineaVenta> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaVenta> lineas) {
        this.lineas = lineas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta:\nid=").append(id)
                .append("\ncliente=").append(cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "null")
                .append("\ntotal=").append(total)
                .append("\nlineas=[\n");

        if (lineas != null) {
            for (LineaVenta linea : lineas) {
                sb.append("   ").append(linea).append("\n");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

}
