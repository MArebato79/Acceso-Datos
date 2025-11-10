package com.RebatoSoft.DAO;

import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ClienteDaoImp implements IClienteDAO{

    @Override
    public ArrayList<Cliente> cogerClientesAll() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Clientes")){
            try (ResultSet rs = pr.executeQuery()){
                while(rs.next()){
                    Cliente cliente = mapearCliente(rs);
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    @Override
    public ArrayList<Cliente> cogerClientesOrderByApellidos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Clientes ORDER BY apellidos")){
            try (ResultSet rs = pr.executeQuery()){
                while(rs.next()){
                    Cliente cliente = mapearCliente(rs);
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientes;
    }

    @Override
    public Cliente cogerClienteByDni(String dni) {
        Cliente cliente = null;
        // 1. Limpieza y Normalización CRÍTICA: trim() y toUpperCase()
        String dniLimpioYNormalizado = dni.trim().toUpperCase();

        if (dniLimpioYNormalizado.isEmpty()) {
            return null;
        }

        try(Connection connection=Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Clientes WHERE dni = ?")){

            pr.setString(1,dniLimpioYNormalizado); // Pasar el DNI limpio

            try (ResultSet rs = pr.executeQuery()){
                if (rs.next()){cliente = mapearCliente(rs);}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }
    @Override
    public Cliente cogerClientesById(int id) {
        Cliente cliente = null;
        try(Connection connection=Conexion.getConexion();PreparedStatement pr = connection.prepareStatement("SELECT * FROM Clientes WHERE id = ?")){
            pr.setInt(1, id);
            try (ResultSet rs = pr.executeQuery()){
                if(rs.next()){ // ⬅️ AGREGAR: Mover el cursor a la primera (y única) fila
                    cliente = mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente: " + e.getMessage());
        }
        return cliente;
    }

    public Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getInt("id"),
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("telefonoContacto"),
                rs.getString("direccionHabitual"),
                rs.getString("direccionEnvio")
        );
        return cliente;
    }

    @Override
    public boolean insertarCliente(Cliente c, Connection connection)throws SQLException {
           try( PreparedStatement pr= connection.prepareStatement("INSERT INTO Clientes (dni, nombre, apellidos, telefonoContacto,direccionHabitual,direccionEnvio) values(?,?,?,?,?,?) ")){
            pr.setString(1, c.getDni());
            pr.setString(2, c.getNombre());
            pr.setString(3, c.getApellido());
            pr.setString(4, c.getTelefonoContacto());
            pr.setString(5, c.getDireccionHabitual());
            pr.setString(6, c.getDireccionEnvio());

            return pr.executeUpdate()>0;
        }

    }



    public String obtenerResumenVentasClientePorIdSinFechas(int idCliente) {
        StringBuilder informe = new StringBuilder();

        // SQL Simplificado: Eliminamos el filtro por fecha
        String sql = "SELECT C.nombre, C.apellidos, " +
                "COUNT(V.id) AS numVentas, SUM(V.total) AS volTotal, " +
                "MAX(V.total) AS maximaVenta, MIN(V.total) AS minimaVenta " +
                "FROM Clientes C JOIN Ventas V ON C.id = V.clienteId " +
                "WHERE C.id = ? " + // Solo filtramos por el ID del cliente
                "GROUP BY C.id, C.nombre, C.apellidos";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pr = conn.prepareStatement(sql)) {

            // 1. Asignar el único parámetro: ID del cliente
            pr.setInt(1, idCliente);

            informe.append(String.format("--- RESUMEN DE VENTAS TOTALES PARA CLIENTE ID: %d ---\n", idCliente));
            informe.append("----------------------------------------------------------\n");

            try (ResultSet rs = pr.executeQuery()) {
                if (rs.next()) {
                    // Si encuentra resultados, construye el informe
                    informe.append(String.format("CLIENTE: %s %s\n",
                            rs.getString("nombre"),
                            rs.getString("apellidos")));
                    informe.append(String.format("  Ventas Realizadas: %d\n", rs.getLong("numVentas")));
                    informe.append(String.format("  Volumen Total: %.2f €\n", rs.getDouble("volTotal")));
                    informe.append(String.format("  Máxima Venta: %.2f €\n", rs.getDouble("maximaVenta")));
                    informe.append(String.format("  Mínima Venta: %.2f €\n", rs.getDouble("minimaVenta")));
                    informe.append("----------------------------------------------------------\n");
                } else {
                    // Si no encuentra resultados para el cliente
                    informe.append("No se encontraron ventas para el cliente ID " + idCliente + ".\n");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen de ventas por cliente ID: " + e.getMessage());
            return "ERROR: No se pudo generar el informe para el cliente ID " + idCliente + ".";
        }
        return informe.toString();
    }
}
