package com.RebatoSoft.DAO;

import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class VentaDaoImp implements IVentaDAO{
ClienteDaoImp clienteDaoImp = new ClienteDaoImp();
LineaVentaDaoImp lineaVentaDaoImp ;

    public VentaDaoImp() {

    }
    public void setLineaVentaDaoImp(LineaVentaDaoImp lineaVentaDaoImp) {
        this.lineaVentaDaoImp = lineaVentaDaoImp;
    }

    @Override
    public ArrayList<Venta> cogerVentasAll() {
        ArrayList<Venta> ventas = new ArrayList<>();
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Ventas");
            ResultSet rs = pr.executeQuery()){
            while (rs.next()){
                Venta venta = mapearVentaHecha(rs);
                venta.setLineas(lineaVentaDaoImp.listarLineaVentaByVenta(venta.getId()));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            return ventas;
        }
        return ventas;
    }



    @Override
    public Venta cogerVentaId(int id) {
        Venta venta = null;
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Ventas WHERE id = ?")){
            pr.setInt(1, id);
            try (ResultSet rs = pr.executeQuery()){
                if (rs.next()) {
                    venta = mapearVentaHecha(rs);
                    venta.setLineas(lineaVentaDaoImp.listarLineaVentaByVenta(venta.getId()));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en VentaDaoImp.cogerVentaId()");
        }
        return venta;
    }

    @Override
    public ArrayList<LineaVenta> cogerLineas(int idVenta) {
        return lineaVentaDaoImp.listarLineas();
    }

    public Venta mapearVentaHecha(ResultSet rs) {
        try {
            Venta venta = new Venta(
                    rs.getInt("id"),
                    clienteDaoImp.cogerClientesById(rs.getInt("clienteId")),
                    rs.getDouble("total")
            );
            return venta;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Venta mapearVentaNueva(ResultSet rs) {
        try {
            Venta venta = new Venta(
                    rs.getInt("id"),
                    clienteDaoImp.cogerClientesById(rs.getInt("clienteId"))
            );
            return venta;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insertarVenta(Venta venta,Connection  connection) {
        int idGenerado = 0;
        try(PreparedStatement pr = connection.prepareStatement("INSERT INTO Ventas (clienteId) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            pr.setInt(1, venta.getCliente().getId());
            pr.executeUpdate();

            try (ResultSet rs = pr.getGeneratedKeys()) { // luego lees el ID
                if (rs.next()) {
                    idGenerado=rs.getInt(1); // o rs.getInt("id") si el alias coincide
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en VentaDaoImp.insertarVenta()");
        }
        return idGenerado;
    }

    public void actualizarVentaTotal(Venta venta,Connection connection) {
        try(PreparedStatement pr = connection.prepareStatement("UPDATE Ventas SET total = ? WHERE id = ?")) {
            pr.setDouble(1, venta.getTotal());
            pr.setInt(2, venta.getId());
            pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en VentaDaoImp.actualizarVentaTotal()");
        }
    }

    public String obtenerResumenVentasPorClienteFecha(Date fechaInicio, Date fechaFin) {
        // StringBuilder es más eficiente para construir cadenas largas
        StringBuilder informe = new StringBuilder();

        // SQL Simplificado
        String sql = "SELECT C.id AS idCliente, C.nombre, C.apellidos, " +
                "COUNT(V.id) AS numVentas, SUM(V.total) AS volTotal, " +
                "MAX(V.total) AS maximaVenta, MIN(V.total) AS minimaVenta " +
                "FROM Clientes C JOIN Ventas V ON C.id = V.clienteId " +
                "WHERE V.fecha_venta BETWEEN ? AND ? " +
                "GROUP BY C.id, C.nombre, C.apellidos " +
                "ORDER BY C.apellidos, C.nombre";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pr = conn.prepareStatement(sql)) {

            // Asignar parámetros de fecha
            pr.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            pr.setDate(2, new java.sql.Date(fechaFin.getTime()));

            informe.append("--- INFORME DE VENTAS POR CLIENTE ---\n");
            informe.append(String.format("Período: %s a %s\n", fechaInicio.toString(), fechaFin.toString()));
            informe.append("---------------------------------------\n");

            try (ResultSet rs = pr.executeQuery()) {
                if (!rs.isBeforeFirst()) { // Verifica si el ResultSet está vacío
                    informe.append("No se encontraron ventas para el período especificado.\n");
                    return informe.toString();
                }

                while (rs.next()) {
                    // Formateo de cada línea del informe
                    informe.append(String.format("CLIENTE: [%d] %s %s\n",
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("apellidos")));
                    informe.append(String.format("  Ventas Realizadas: %d\n", rs.getLong("numVentas")));
                    informe.append(String.format("  Volumen Total: %.2f €\n", rs.getDouble("volTotal")));
                    informe.append(String.format("  Máxima Venta: %.2f €\n", rs.getDouble("maximaVenta")));
                    informe.append(String.format("  Mínima Venta: %.2f €\n", rs.getDouble("minimaVenta")));
                    informe.append("---\n");
                }
            }
        } catch (SQLException e) {
            // Manejo de error: devolver un string de error
            System.err.println("Error al obtener resumen de ventas por cliente: " + e.getMessage());
            return "ERROR: No se pudo generar el informe debido a un problema de base de datos.";
        }
        return informe.toString();
    }
}
