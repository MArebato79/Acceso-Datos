package com.RebatoSoft.exportacion;

import com.RebatoSoft.DAO.ClienteDaoImp;
import com.RebatoSoft.DAO.EmpresaDaoImp;
import com.RebatoSoft.DAO.ProductoDaoImp;
import com.RebatoSoft.entities.Cliente;
import com.RebatoSoft.entities.Producto;
import com.RebatoSoft.entities.Empresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper; // 👈 Clave para XML
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.List;

public class ExportadorDAO {

    private final ClienteDaoImp clienteDAO;
    private final ProductoDaoImp productoDAO;
    private final EmpresaDaoImp empresaDAO;

    public ExportadorDAO(ClienteDaoImp clienteDAO, ProductoDaoImp productoDAO, EmpresaDaoImp empresaDAO) {
        this.clienteDAO = clienteDAO;
        this.productoDAO = productoDAO;
        this.empresaDAO = empresaDAO;
    }

    // ==========================================================
    // MÉTODO 1: EXPORTAR TODO A JSON (3 Archivos)
    // ==========================================================
    public void exportarTablasAJSON() {
        // Usamos el ObjectMapper estándar para JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Formato legible

        try {
            // Clientes
            List<Cliente> clientes = clienteDAO.cogerClientesAll();
            mapper.writeValue(new File("Clientes.json"), clientes);

            // Productos
            List<Producto> productos = productoDAO.cogerProductosAll();
            mapper.writeValue(new File("Productos.json"), productos);

            // Empresas
            List<Empresa> empresas = empresaDAO.cogerEmpresasAll();
            mapper.writeValue(new File("Empresas.json"), empresas);

            System.out.println("Datos exportados con éxito a 3 archivos JSON.");
        } catch (Exception e) {
            System.err.println("Error al exportar a JSON: " + e.getMessage());
        }
    }

    // ==========================================================
    // MÉTODO 2: EXPORTAR TODO A XML (3 Archivos)
    // ==========================================================
    public void exportarTablasAXML() {
        // Usamos el XmlMapper (de jackson-dataformat-xml) para XML
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Clientes
            List<Cliente> clientes = clienteDAO.cogerClientesAll();
            // Jackson envuelve automáticamente la lista con una etiqueta raíz <ArrayList>
            // a menos que se defina la anotación @JacksonXmlRootElement en la clase Cliente.
            mapper.writeValue(new File("Clientes.xml"), clientes);

            // Productos
            List<Producto> productos = productoDAO.cogerProductosAll();
            mapper.writeValue(new File("Productos.xml"), productos);

            // Empresas
            List<Empresa> empresas = empresaDAO.cogerEmpresasAll();
            mapper.writeValue(new File("Empresas.xml"), empresas);

            System.out.println("Datos exportados con éxito a 3 archivos XML.");
        } catch (Exception e) {
            System.err.println("Error al exportar a XML. Verifique las anotaciones @JsonProperty: " + e.getMessage());
        }
    }
}