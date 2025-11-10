package com.RebatoSoft.DAO;

import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Venta;

import java.sql.*;
import java.util.ArrayList;

public class LineaVentaDaoImp implements ILineaVentaDAO {
VentaDaoImp ventaDaoImp ;
ProductoDaoImp productoDaoImp ;

    public LineaVentaDaoImp(ProductoDaoImp productoDaoImp) {
        this.productoDaoImp = productoDaoImp;

    }

    public void setVentaDaoImp(VentaDaoImp ventaDaoImp) {
        this.ventaDaoImp = ventaDaoImp;
    }

    @Override
    public ArrayList<LineaVenta> listarLineas() {
        ArrayList<LineaVenta> lista = new ArrayList<>();
        try(Connection connection = Conexion.getConexion();
                PreparedStatement pr = connection.prepareStatement("SELECT * FROM Lineas_venta")){
            try (ResultSet rs = pr.executeQuery()){
                while (rs.next()) {
                    lista.add(mapearLineaVenta(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en LineaVentaDaoImp.listarLineas()");
        }
        return lista;
    }

    @Override
    public LineaVenta cogerById(int id){
        LineaVenta lineaVenta = null;

        try(Connection  connection = Conexion.getConexion();
                PreparedStatement pr = connection.prepareStatement("SELECT * FROM Lineas_venta WHERE id = ?")){
            pr.setInt(1, id);
            try (ResultSet rs = pr.executeQuery()){
                return  mapearLineaVenta(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error en LineaVentaDaoImp.cogerById()");
            return lineaVenta;
        }

    }

    @Override
    public ArrayList<LineaVenta> listarLineaVentaByVenta(int idVenta) {
        ArrayList<LineaVenta> lista = new ArrayList<>();
        try(Connection connection = Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Lineas_venta WHERE idVenta = ?")){
            pr.setInt(1, idVenta);
            try (ResultSet rs = pr.executeQuery()){
                while (rs.next()) {
                    lista.add(mapearLineaVenta(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en LineaVentaDaoImp.listarLineaVentaByVenta()");
        }
        return lista;
    }


    public LineaVenta mapearLineaVenta(ResultSet rs) throws SQLException {
        int idVenta = rs.getInt("idVenta");

        Venta ventaParcial = new Venta();
        ventaParcial.setId(idVenta); // Solo contiene el ID de la Venta
        LineaVenta lineaVenta = new LineaVenta(
                rs.getInt("id"),
                ventaParcial,
                productoDaoImp.cogerProductoId(rs.getInt("idProducto")),
                rs.getInt("cantidad"),
                rs.getDouble("precioVenta"),
                rs.getInt("descuento")
        );
        return lineaVenta;
    }

    @Override
    public int insertarLineaVenta(LineaVenta lineaVenta, Connection connection) throws SQLException {
        int idGenerado = 0;
        try(PreparedStatement pr = connection.prepareStatement("INSERT INTO Lineas_venta (idVenta, idProducto, cantidad, precioVenta, descuento) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            pr.setInt(1, lineaVenta.getVenta().getId());
            pr.setInt(2, lineaVenta.getProducto().getId());
            pr.setInt(3, lineaVenta.getCantidad());
            pr.setDouble(4, lineaVenta.getPrecioVenta());
            pr.setInt(5, lineaVenta.getDescuento());
            pr.executeUpdate();

            try (ResultSet rs = pr.getGeneratedKeys()) { // 🔹 luego lees el ID
                if (rs.next()) {
                    idGenerado=rs.getInt(1); // o rs.getInt("id") si el alias coincide
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error en LineaVentaDaoImp.insertarLineaVenta()");
        }
        return idGenerado ;
    }

    @Override
    public double calcularTotal(int idVenta,Connection connection) {
        double total = 0;
        try(CallableStatement cl = connection.prepareCall("{?= CALL CALCULAR_TOTAL_LINEAS_VENTA(?)}")) {
                cl.registerOutParameter(1, Types.DOUBLE); // Primer parámetro: salida
                cl.setInt(2, idVenta); // Segundo parámetro: entrada
                cl.execute();
                total = cl.getDouble(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total;
    }
}
