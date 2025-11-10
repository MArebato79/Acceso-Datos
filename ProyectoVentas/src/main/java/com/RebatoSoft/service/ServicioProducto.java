package com.RebatoSoft.service;

import com.RebatoSoft.DAO.ProductoDaoImp;
import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.Producto;
import com.RebatoSoft.exception.ValidationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioProducto {

    private final ProductoDaoImp productoDao;

    public ServicioProducto(ProductoDaoImp productoDao) {

        this.productoDao = productoDao;
    }



    public void validarProducto(Producto producto) throws ValidationException {
        List<String> errores = new ArrayList<String>();
        // Lógica de validación de producto
        if (producto == null) {
            errores.add("Cliente vacío");
            throw new ValidationException(errores);
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            errores.add("Nombre  de producto obligatorio");
        } else if (producto.getNombre().trim().length() > 100) {
            errores.add("Nombre demasiado largo (máx 100)");
        }

        if (producto.getPrecioRecomendado() < 0) {
            errores.add("El precio no puede ser negativo");
        }

        if (producto.getStock() < 0) {
            errores.add("El stock no puede ser negativo");
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }

    public void restarStock(int idProducto, int cantidad, Connection connection)throws SQLException {
        Producto p = productoDao.cogerProductoId(idProducto);
        if (p != null) {
            int nuevoStock = p.getStock() - cantidad;
            p.setStock(nuevoStock);
            productoDao.actualizarProductoStock(p, connection);
        }
    }

    public Producto obtenerPorId(int id) {
        return productoDao.cogerProductoId(id);
    }

    public List<Producto> obtenerTodos() {
        return productoDao.cogerProductosAll();
    }

    public boolean eliminarProducto(int id) {
        return productoDao.eliminarProducto(id);
    }

    public List<Producto> ordenarProductosPorDescripcion() {
        return productoDao.ordenarProductosPorDescripcion();
    }

    public boolean insertarProducto(Producto producto){
        Connection conexion = null;
        try {
            conexion = Conexion.getConexion();
            conexion.setAutoCommit(false);
            validarProducto(producto);
            productoDao.insertarProducto(producto, conexion);
            conexion.commit();
            return true;
        } catch (Exception e) {
                // rollback si algo falla
                if (conexion != null) {
                    try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
                return false;
            } finally {
                // cerrar conexión siempre
                if (conexion!= null) {
                    try { conexion.close(); } catch (SQLException ex) { ex.printStackTrace(); }
                }

            }
        }
    public ProductoDaoImp getProductoDao() {
        return productoDao;
    }

    }


