package DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    //URL de conexión a la base de datos(host, puerto y nombre de la base de datos)
    private static final String URL = "jdbc:mysql://localhost:3306/proyectoacej";
    
    //credenciales de acceso a la base de datos
    private static final String USER = "valemidx";
    private static final String PASSWORD = "luisa123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL registrado correctamente (ConexionDB)");
        } catch (ClassNotFoundException e) {
            System.err.println("No se pudo registrar el driver MySQL");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
