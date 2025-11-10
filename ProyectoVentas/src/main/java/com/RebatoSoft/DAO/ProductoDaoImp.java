package com.RebatoSoft.DAO;

import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.Producto;

import java.sql.*;
import java.util.ArrayList;

public class ProductoDaoImp implements IProductoDAO{

    @Override
    public ArrayList<Producto> cogerProductosAll() {
        ArrayList<Producto> productos = new ArrayList<>();
        try(var connection = Conexion.getConexion();
            var st=connection.prepareStatement("SELECT * FROM Productos")){
            try(var rs=st.executeQuery()){
                while(rs.next()){
                    Producto producto = mapearProducto(rs);
                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos;
    }

    @Override
    public Producto cogerProductoId(int id) {
        Producto producto = null;
        try(Connection connection= Conexion.getConexion();
                PreparedStatement pr = connection.prepareStatement("SELECT * FROM Productos WHERE id = ?")){
            pr.setInt(1,id);
            try(ResultSet rs=pr.executeQuery()){
                if(rs.next()){
                    producto = mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el id de producto:"+e.getMessage());
        }
        return producto;
    }

    @Override
    public ArrayList<Producto> ordenarProductosPorDescripcion() {
        ArrayList<Producto> productos = new ArrayList<>();
        try(Connection connection= Conexion.getConexion();
                PreparedStatement pr = connection.prepareStatement("SELECT * FROM Productos ORDER BY descripcion")){
            try(ResultSet rs=pr.executeQuery()){
                while(rs.next()){
                    Producto producto = mapearProducto(rs);
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos;
    }

    public Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto  nuevoProducto = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precioRecomendado"),
                rs.getInt("stock")
        );
        return nuevoProducto;
    }

    @Override
    public boolean insertarProducto(Producto producto, Connection connection) throws SQLException {
        try (PreparedStatement pr = connection.prepareStatement("INSERT INTO Productos (nombre,descripcion,precioRecomendado,stock) VALUE (?,?,?,?)")){
            pr.setString(1,producto.getNombre());
            pr.setString(2,producto.getDescripcion());
            pr.setDouble(3,producto.getPrecioRecomendado());
            pr.setInt(4,producto.getStock());

            return pr.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto:"+e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminarProducto(int id) {
        try(Connection connection = Conexion.getConexion();
                PreparedStatement pr= connection.prepareStatement("DELETE FROM Productos WHERE id = ?")){
            pr.setInt(1,id);
            return pr.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto:"+e.getMessage());
            return false;
        }
    }

    public boolean actualizarProductoStock(Producto producto, Connection connection) throws SQLException {
        try (PreparedStatement pr = connection.prepareStatement("UPDATE Productos SET stock = ? WHERE id = ?")) {
            pr.setInt(1, producto.getStock());
            pr.setInt(2, producto.getId());
            return pr.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el stock del producto: " + e.getMessage());
            return false;
        }
    }

}
