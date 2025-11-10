package com.RebatoSoft.service;

import com.RebatoSoft.DAO.VentaDaoImp;
import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.Cliente;
import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Venta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicioVenta {
    private final ServicioCliente servicioCliente;
    private final ServicioLineaVenta servicioLineaVenta;
    private final VentaDaoImp ventaDao;

    public ServicioVenta(ServicioCliente clienteDao, ServicioLineaVenta lineaVentaDao, VentaDaoImp ventaDao) {
        this.servicioCliente = clienteDao;
        this.servicioLineaVenta = lineaVentaDao;
        this.ventaDao = ventaDao;
    }

    public void validarVenta(Venta venta) {
        // Lógica para validar la venta
        ArrayList<String> errores = new ArrayList<>();
        if (venta == null) {
            errores.add("Venta vacía");
        }

        Cliente cliente = servicioCliente.obtenerClientePorId(venta.getCliente().getId());
        if (cliente == null) {
            errores.add("El cliente no existe");
        }

        if (venta == null || venta.getLineas() == null || venta.getLineas().isEmpty()) {
            errores.add("La venta debe tener al menos una línea");
        }

    }

    public List<Venta> mostrarVentas() {
        return ventaDao.cogerVentasAll();
    }

    public Venta cogerVentaById(int id) {
        return ventaDao.cogerVentaId(id);
    }

    public boolean insertarVenta(Venta venta, List<LineaVenta> lineas)  {
        Connection conexion = null;
        try {
            conexion = Conexion.getConexion();
            conexion.setAutoCommit(false);

            int id=ventaDao.insertarVenta(venta, conexion);
            venta.setId(id);

            for(LineaVenta linea : lineas){
                linea.setVenta(venta);
            }

            servicioLineaVenta.insertarLineas(lineas, conexion);
            venta.setTotal(calcularCostoVenta(venta.getId(), conexion));
            actualizarVenta(venta, conexion);
            validarVenta(venta);
            conexion.commit();

        } catch (Exception e){
                // rollback si algo falla
                if (conexion != null) {
                    try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
               return false;
            } finally {
                // cerrar conexión siempre
                if (conexion != null) {
                    try { conexion.close(); } catch (SQLException ex) { ex.printStackTrace(); }
                }

            }
        return true;
    }

    public ServicioLineaVenta getServicioLineaVenta() {
        return servicioLineaVenta;
    }
    public Cliente obtenerClientePorId(int id) {
        return servicioCliente.obtenerClientePorId(id);
    }
    public double calcularCostoVenta(int idVenta,Connection connection) throws SQLException {
         return servicioLineaVenta.calcularCostoVenta(idVenta,connection);
    }

    public String resumenVentasClientesFecha(Date fechaInicio, Date fechaFin) {
        return ventaDao.obtenerResumenVentasPorClienteFecha(fechaInicio,fechaFin);
    }
    public void actualizarVenta(Venta venta,Connection connection) throws SQLException {
        ventaDao.actualizarVentaTotal(venta, connection);
    }
}




