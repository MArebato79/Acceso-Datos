package com.RebatoSoft;

import com.RebatoSoft.DAO.*;
import com.RebatoSoft.entities.Cliente;
import com.RebatoSoft.entities.LineaVenta;
import com.RebatoSoft.entities.Producto;
import com.RebatoSoft.entities.Venta;
import com.RebatoSoft.exportacion.ExportadorDAO;
import com.RebatoSoft.service.ServicioCliente;
import com.RebatoSoft.service.ServicioLineaVenta;
import com.RebatoSoft.service.ServicioProducto;
import com.RebatoSoft.service.ServicioVenta;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        ClienteDaoImp clienteDao = new ClienteDaoImp();
        ProductoDaoImp productoDao = new ProductoDaoImp();

        VentaDaoImp ventaDao = new VentaDaoImp();
        LineaVentaDaoImp lineaVentaDao = new LineaVentaDaoImp(productoDao);

        lineaVentaDao.setVentaDaoImp(ventaDao);

        ventaDao.setLineaVentaDaoImp(lineaVentaDao);

        ServicioCliente servicioCliente = new ServicioCliente(clienteDao);
        ServicioProducto servicioProducto = new ServicioProducto(productoDao);

        ServicioLineaVenta servicioLineaVenta = new ServicioLineaVenta(lineaVentaDao, servicioProducto);

        ServicioVenta servicioVenta = new ServicioVenta(servicioCliente, servicioLineaVenta, ventaDao);

        ExportadorDAO exportadorDAO= new ExportadorDAO(new ClienteDaoImp(),new ProductoDaoImp(),new EmpresaDaoImp());


        int salida = 0;
        do {
            mostrarMenu();
            salida = sc.nextInt();
            switch (salida) {
                case 1 -> System.out.println(servicioCliente.obtenerTodosLosClientes().toString());
                case 2 -> System.out.println(servicioProducto.obtenerTodos().toString());
                case 3 -> System.out.println(servicioVenta.mostrarVentas().toString());
                case 4 -> mostrarClienteDNI(sc,servicioCliente);
                case 5 -> mostrarProductoById(sc,servicioProducto);
                case 6 -> insertarCliente(sc,servicioCliente);
                case 7 -> insertarProducto(sc,servicioProducto);
                case 8 -> insertarVenta(sc,servicioVenta);
                case 9 -> System.out.println(servicioCliente.ordenarByApellidos().toString());
                case 10 -> System.out.println(servicioProducto.ordenarProductosPorDescripcion().toString());
                case 11 -> ventasPorFecha(sc,servicioVenta);
                case 12 -> resumenVentasCliente(sc,servicioCliente);
                case 13 -> exportadorDAO.exportarTablasAJSON();
                case 14 -> exportadorDAO.exportarTablasAXML();
                case 0 -> salir();
                default -> System.out.println("Opcion no valida");
            }
        }while(salida!=0);
    }
    public static void mostrarMenu() {
        System.out.println("------MENU------");
        System.out.println("1. Listar Clientes");
        System.out.println("2. Listar Productos");
        System.out.println("3. Listar Ventas");
        System.out.println("4. Seleccionar Cliente por dni");
        System.out.println("5. Seleccionar Venta por id");
        System.out.println("6. Insertar Cliente");
        System.out.println("7. Insertar Producto");
        System.out.println("8. Insertar Venta");
        System.out.println("9. Ordenar Clientes por Apellidos");
        System.out.println("10. Ordenar Productos por Descripcion");
        System.out.println("11. Ventas por Fecha");
        System.out.println("12. Resumen de Ventas por Cliente");
        System.out.println("13. Exportar datos a JSON");
        System.out.println("14. Exportar datos a XML");
        System.out.println("0. Salir");
        System.out.println("Seleccione una opcion:");
    }
    public static void mostrarClienteDNI(Scanner sc,ServicioCliente servicioCliente) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        System.out.println("ingrese el DNI del cliente:");
        String dni = sc.nextLine();
        System.out.println(servicioCliente.obtenerClientePorDni(dni));
    }

    public static void mostrarProductoById(Scanner sc,ServicioProducto servicioProducto) {
        System.out.println("ingrese el id del producto:");
        int id = sc.nextInt();
        System.out.println(servicioProducto.obtenerPorId(id));
    }

    public static void insertarCliente(Scanner sc,ServicioCliente servicioCliente) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        System.out.println("ingrese el dni del cliente:");
        String dni = sc.nextLine();
        System.out.println("ingrese nombre del cliente:");
        String nombre = sc.nextLine();
        System.out.println("ingrese apellido del cliente:");
        String apellido = sc.nextLine();
        System.out.println("ingrese telefono del cliente:");
        String telefono = sc.nextLine();
        System.out.println("ingrese direccion habitual  del cliente:");
        String direccionHab = sc.nextLine();
        System.out.println("ingrese direccion envio del cliente:");
        String direccionEnvio = sc.nextLine();
        Cliente nuevoCliente= new Cliente(dni,nombre,apellido,telefono,direccionHab,direccionEnvio);
        System.out.println(servicioCliente.guardarCliente(nuevoCliente)?"Cliente insertado":"Error al insertar Cliente");
    }
    public static void insertarProducto(Scanner sc,ServicioProducto servicioProducto) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        System.out.println("ingrese nombre del producto:");
        String nombre = sc.nextLine();
        System.out.println("ingrese descripcion del producto:");
        String descripcion = sc.nextLine();
        System.out.println("ingrese precio del producto:");
        double precio = sc.nextDouble();
        System.out.println("ingrese stock del producto:");
        int stock = sc.nextInt();
        Producto nuevo= new Producto(nombre,descripcion,precio,stock);
        System.out.println(servicioProducto.insertarProducto(nuevo)?"Producto insertado":"Error al insertar Producto");
    }

    public static void insertarVenta(Scanner sc,ServicioVenta servicioVenta) {

        ArrayList<LineaVenta> lineasVenta=new ArrayList<>();
        System.out.println("ingrese id del cliente:");
        int id = sc.nextInt();
        boolean insertarLineas=true;
        do{

            System.out.println("INGRESE LINEA DE VENTAS--------:");

            System.out.println("Ingrese id de producto:");
            int productoId = sc.nextInt();

            System.out.println("Ingrese cantidad de producto:");
            int cantidadProducto = sc.nextInt();

            System.out.println("Ingrese precio del producto:");
            double precioProducto = sc.nextDouble();

            System.out.println("Ingrese descuento del producto:");
            int descuentoProducto = sc.nextInt();

            LineaVenta lineaNueva= new LineaVenta(servicioVenta.getServicioLineaVenta().obtenerPorId(productoId),
                    cantidadProducto,precioProducto,descuentoProducto);
            lineasVenta.add(lineaNueva);

            System.out.println("¿Desea agregar otra línea de venta? (s/n):");
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            String respuesta = sc.nextLine();
            if (!respuesta.equalsIgnoreCase("s")) {
                insertarLineas = false;
            }
        }
        while(insertarLineas);
        Venta venta= new Venta(servicioVenta.obtenerClientePorId(id));
        System.out.println(servicioVenta.insertarVenta(venta,lineasVenta)?"venta insertado":"Error al insertar Venta");
    }

    public static void ventasPorFecha(Scanner sc,ServicioVenta servicioVenta) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        System.out.println("ingrese fecha inicio (YYYY-MM-DD):");
        String fechaInicioStr = sc.nextLine();
        System.out.println("ingrese fecha fin (YYYY-MM-DD):");
        String fechaFinStr = sc.nextLine();
        Date fechaInicio = Date.valueOf(fechaInicioStr);
        Date fechaFin = Date.valueOf(fechaFinStr);
        System.out.println(servicioVenta.resumenVentasClientesFecha(fechaInicio,fechaFin));
    }

    public static void resumenVentasCliente(Scanner sc,ServicioCliente servicioCliente) {
        System.out.println("ingrese id del cliente:");
        int id = sc.nextInt();
        System.out.println(servicioCliente.obtenerResumenClientes(id));
    }
    public static void salir() {
        System.out.println("Saliendo del programa...");
        exit(0);
    }
}