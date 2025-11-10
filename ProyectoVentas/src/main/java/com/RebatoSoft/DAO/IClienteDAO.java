package com.RebatoSoft.DAO;

import com.RebatoSoft.entities.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IClienteDAO {
    ArrayList<Cliente> cogerClientesAll();
    ArrayList<Cliente> cogerClientesOrderByApellidos();
    Cliente cogerClientesById(int id);
    Cliente cogerClienteByDni(String dni);
    boolean insertarCliente(Cliente c, Connection connection) throws SQLException;
}
