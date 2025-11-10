
    package com.RebatoSoft.service;

    import com.RebatoSoft.DAO.ClienteDaoImp;
    import com.RebatoSoft.conexion.Conexion;
    import com.RebatoSoft.entities.Cliente;
    import com.RebatoSoft.exception.ValidationException;

    import java.sql.Connection;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.regex.Pattern;

    public class ServicioCliente {

        private final ClienteDaoImp clienteDAO;

        private static final Pattern DNI_REGEX = Pattern.compile( "^(\\d{8}[A-Z]|[XYZ]\\d{7}[A-Z])$");
        private static final Pattern PHONE_REGEX = Pattern.compile("^\\+?[0-9]{7,15}$");

        public ServicioCliente(ClienteDaoImp clienteDAO) {
            this.clienteDAO = clienteDAO;
        }


        public void validarCliente(Cliente c) {
            List<String> errores = new ArrayList<>();

            if (c == null) {
                errores.add("Cliente vacío");
                throw new ValidationException(errores);
            }

            if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
                errores.add("Nombre obligatorio");
            } else if (c.getNombre().trim().length() > 100) {
                errores.add("Nombre demasiado largo (máx 100)");
            }

            if (c.getApellido() != null && c.getApellido().trim().length() > 100) {
                errores.add("Apellido demasiado largo (máx 100)");
            }

            if (c.getDni() == null || c.getDni().trim().isEmpty()) {
                errores.add("DNI obligatorio");
            } else if (!DNI_REGEX.matcher(c.getDni().trim()).matches()) {
                errores.add("Formato de DNI inválido");
            } else {
                Cliente existente = clienteDAO.cogerClienteByDni(c.getDni().trim());
                if (existente != null && existente.getId() != c.getId()) {
                    errores.add("DNI ya existe");
                }
            }

            if (c.getTelefonoContacto() != null && !c.getTelefonoContacto().trim().isEmpty()) {
                if (!PHONE_REGEX.matcher(c.getTelefonoContacto().trim()).matches()) {
                    errores.add("Formato de teléfono inválido");
                }
            }

            if (!errores.isEmpty()) {
                throw new ValidationException(errores);
            }
        }

        public boolean guardarCliente(Cliente c) {
            Connection conexion = null;
            try{
                conexion= Conexion.getConexion();
                conexion.setAutoCommit(false);
                validarCliente(c);

                clienteDAO.insertarCliente(c,conexion );
                conexion.commit();
                return true;
            } catch (Exception e){
                if (conexion != null) {
                    try {
                        conexion.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                return false;
            }finally{
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }


        }
        public Cliente obtenerPorId(int id) {

            return clienteDAO.cogerClientesById(id);
        }

        public ArrayList<Cliente> obtenerTodosLosClientes() {
            return clienteDAO.cogerClientesAll();
        }

        public Cliente obtenerClientePorDni(String dni) {
            System.out.println("Consulta ejecutada con DNI = " + dni);
            return clienteDAO.cogerClienteByDni(dni);
        }

        public Cliente obtenerClientePorId(int id) {return clienteDAO.cogerClientesById(id);}

        public ArrayList<Cliente> ordenarByApellidos(){return clienteDAO.cogerClientesOrderByApellidos();}

        public String obtenerResumenClientes(int idCliente) {
            return clienteDAO.obtenerResumenVentasClientePorIdSinFechas(idCliente);
        }

    }

