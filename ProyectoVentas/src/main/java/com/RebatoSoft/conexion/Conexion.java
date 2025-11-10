package com.RebatoSoft.conexion;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Configuración MySQL
    private static final String MARIADB_URL = "jdbc:mariadb://localhost:3306/Reservas?useUnicode=true&characterEncoding=UTF-8";
    private static final String MARIADB_USER = "root";
    private static final String MARIADB_PASSWORD = "root";

    public static Connection getConexion() throws SQLException {
      return DriverManager.getConnection(MARIADB_URL, MARIADB_USER, MARIADB_PASSWORD);
    }

    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✓ Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
