package com.RebatoSoft.DAO;

import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Venta;

import java.sql.Connection;
import java.util.ArrayList;

public interface IVentaDAO {
    ArrayList<Venta> cogerVentasAll();
    Venta cogerVentaId(int id);
    int insertarVenta(Venta venta , Connection connection);
    ArrayList<LineaVenta> cogerLineas(int idVenta);
}
