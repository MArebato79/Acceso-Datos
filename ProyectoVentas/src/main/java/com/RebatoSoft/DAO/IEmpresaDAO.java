package com.RebatoSoft.DAO;

import com.RebatoSoft.entities.Empresa;

import java.util.ArrayList;

public interface IEmpresaDAO {
    ArrayList<Empresa> cogerEmpresasAll();
    Empresa cogerEmpresaCIF(String cif);
    boolean insertarEmpresa(Empresa empresa);
    boolean eliminarEmpresa(int id);

}
