package DAO;

import Modelo.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para manejar operaciones relacionadas
 * con la tabla "roles".
 * 
 * Utiliza la clase ConexionDB para obtener la conexión a la base de datos.
 */
public class RolDAO {

    /**
     * Obtiene todos los roles disponibles en la tabla "roles".
     * @return Lista de objetos Rol con id y nombre de rol.
     */
    public static List<Rol> obtenerRoles() {
        // Lista que almacenará los roles recuperados de la base de datos
        List<Rol> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los registros de roles
        String sql = "SELECT id, nombre FROM roles";

        // Try-with-resources: garantiza el cierre automático de conexión, statement y resultset
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorremos los resultados y mapeamos cada fila a un objeto Rol
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(rs.getInt("id"));           // ID del rol
                rol.setNombre(rs.getString("nombre")); // Nombre del rol
                lista.add(rol); // Agregamos el rol a la lista
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener roles: " + e.getMessage());
        }

        // Retornamos la lista de roles (puede estar vacía si no hay resultados)
        return lista;
    }
}



