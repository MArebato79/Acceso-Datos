package com.RebatoSoft.DAO;

import com.RebatoSoft.entities.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IProductoDAO {
    ArrayList<Producto> cogerProductosAll();
    Producto cogerProductoId(int id);
    ArrayList<Producto> ordenarProductosPorDescripcion();
    boolean insertarProducto(Producto producto, Connection connection) throws SQLException;
    boolean eliminarProducto(int id);
}
