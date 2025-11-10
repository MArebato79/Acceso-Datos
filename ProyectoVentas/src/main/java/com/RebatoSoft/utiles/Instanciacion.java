package com.RebatoSoft.utiles;

import com.RebatoSoft.DAO.*;
import com.RebatoSoft.exportacion.ExportadorDAO;
import com.RebatoSoft.service.ServicioCliente;
import com.RebatoSoft.service.ServicioLineaVenta;
import com.RebatoSoft.service.ServicioProducto;
import com.RebatoSoft.service.ServicioVenta;

public class Instanciacion {
    private ClienteDaoImp clienteDao = new ClienteDaoImp();
    private ProductoDaoImp productoDao = new ProductoDaoImp();

    private VentaDaoImp ventaDao = new VentaDaoImp();
    private LineaVentaDaoImp lineaVentaDao = new LineaVentaDaoImp(productoDao);


    private ServicioCliente servicioCliente = new ServicioCliente(clienteDao);
    private ServicioProducto servicioProducto = new ServicioProducto(productoDao);
    private ExportadorDAO exportadorDAO= new ExportadorDAO(new ClienteDaoImp(),new ProductoDaoImp(),new EmpresaDaoImp());

    private ServicioLineaVenta servicioLineaVenta;
    private ServicioVenta servicioVenta;

    public Instanciacion(){
        lineaVentaDao.setVentaDaoImp(ventaDao);

        ventaDao.setLineaVentaDaoImp(lineaVentaDao);
        servicioLineaVenta = new ServicioLineaVenta(lineaVentaDao, servicioProducto);

        servicioVenta = new ServicioVenta(servicioCliente, servicioLineaVenta, ventaDao);
    }

    public ServicioLineaVenta getServicioLineaVenta() {
        return servicioLineaVenta;
    }

    public ServicioVenta getServicioVenta() {
        return servicioVenta;
    }

    public ServicioProducto getServicioProducto() {
        return servicioProducto;
    }

    public ServicioCliente getServicioCliente() {
        return servicioCliente;
    }

    public LineaVentaDaoImp getLineaVentaDao() {
        return lineaVentaDao;
    }

    public VentaDaoImp getVentaDao() {
        return ventaDao;
    }

    public ProductoDaoImp getProductoDao() {
        return productoDao;
    }

    public ClienteDaoImp getClienteDao() {
        return clienteDao;
    }
}
