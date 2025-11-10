package com.RebatoSoft.service;

import com.RebatoSoft.DAO.LineaVentaDaoImp;
import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Producto;
import com.RebatoSoft.exception.ValidationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioLineaVenta {

    private final LineaVentaDaoImp lineaVentaDAO;
    private final ServicioProducto servicioProducto;

    public ServicioLineaVenta(LineaVentaDaoImp lineaVentaDAO, ServicioProducto servicioProducto) {
        this.lineaVentaDAO = lineaVentaDAO;
        this.servicioProducto = servicioProducto;
    }

    public void validarLineaVenta(LineaVenta linea) throws ValidationException {
        ArrayList<String> errores = new ArrayList<>();

        if (linea.getCantidad() <= 0) {
            errores.add("La cantidad debe ser mayor que cero.\n");
        }

        Producto producto = servicioProducto.obtenerPorId(linea.getProducto().getId());
        if (producto == null) {
            errores.add("El producto no existe.\n");
        } else if (linea.getCantidad() > producto.getStock()) {
            errores.add("No hay stock suficiente para el producto: " + producto.getNombre() + ".\n");
        }

        if (linea.getPrecioVenta() > (producto.getPrecioRecomendado() * 1.2)|| linea.getPrecioVenta() < (producto.getPrecioRecomendado()*0.8) ) {
            errores.add("El precio de venta no puede exceder o no llegar al precio recomendado más el IVA (20%).\n");
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }

    public void insertarLineas(List<LineaVenta> lineas, Connection connection) throws SQLException {

        for (LineaVenta linea : lineas) {
            validarLineaVenta(linea);
            //Insertar la línea
            lineaVentaDAO.insertarLineaVenta(linea, connection) ;
            //Restar el stock
            servicioProducto.restarStock(linea.getProducto().getId(), linea.getCantidad(),connection);
        }
        calcularCostoVenta(lineas.get(0).getVenta().getId(),connection);
    }
    public Producto obtenerPorId(int idProducto) {
        return servicioProducto.obtenerPorId(idProducto);
    }
    public double calcularCostoVenta(int idLinea,Connection connection) throws SQLException {
        return lineaVentaDAO.calcularTotal(idLinea,connection);
    }
}

