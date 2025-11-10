package com.RebatoSoft.DAO;

import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Venta;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ILineaVentaDAO {
    ArrayList<LineaVenta> listarLineas();
    LineaVenta cogerById(int id);
    double calcularTotal(int idVenta,Connection connection);
    int insertarLineaVenta(LineaVenta lineaVenta, Connection connection) throws SQLException;
    ArrayList<LineaVenta> listarLineaVentaByVenta(int idVenta);
}
