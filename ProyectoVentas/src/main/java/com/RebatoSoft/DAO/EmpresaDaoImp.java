package com.RebatoSoft.DAO;

import com.RebatoSoft.conexion.Conexion;
import com.RebatoSoft.entities.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmpresaDaoImp implements IEmpresaDAO {

    @Override
    public ArrayList<Empresa> cogerEmpresasAll() {
        ArrayList<Empresa> empresas = new ArrayList<>();
        try(Connection connection = Conexion.getConexion(); PreparedStatement st=connection.prepareStatement("select * from Empresas")){
            try(ResultSet rs=st.executeQuery()){
                while(rs.next()){
                    Empresa empresa = mapearEmpresa(rs);
                    empresas.add(empresa);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return empresas;
    }

    public Empresa cogerEmpresaCIF(String cif) {
        Empresa empresa;
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM Empresa WHERE id= ?")) {
            pr.setString(1, cif);
            try (ResultSet rs = pr.executeQuery()) {
                empresa = mapearEmpresa(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return empresa;
    }

    public Empresa mapearEmpresa(ResultSet rs) throws SQLException {
        Empresa nuevaEmpresa=new Empresa(
                rs.getInt("id"),
                rs.getString("cif"),
                rs.getString("nombre"),
                rs.getString("domicilio"),
                rs.getString("localidad")
        );
        return nuevaEmpresa;
    }

    public boolean insertarEmpresa(Empresa empresa) {
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr= connection.prepareStatement("INSERT INTO Empresa (cif, nombre, domicilio, localidad) values (?,?,?,?)")){
            pr.setString(1, empresa.getCif());
            pr.setString(2, empresa.getNombre());
            pr.setString(3, empresa.getDomicilio());
            pr.setString(4, empresa.getLocalidad());

            return pr.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar empresa: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarEmpresa(int id) {
        try(Connection connection= Conexion.getConexion();
            PreparedStatement pr = connection.prepareStatement("DELETE FROM Empresa WHERE id = ?")){
            pr.setInt(1, id);
            return pr.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar empresa: " + e.getMessage());
            return false;
        }
    }
}
