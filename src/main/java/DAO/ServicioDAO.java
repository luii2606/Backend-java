package DAO;

import Modelo.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para manejar operaciones CRUD relacionadas
 * con la tabla "servicios".
 * 
 * Ahora utiliza la clase ConexionDB para obtener la conexión a la base de datos,
 * evitando repetir credenciales en cada DAO.
 */
public class ServicioDAO {

    /**
     * Obtiene todos los registros de la tabla "servicios".
     * @return Lista de objetos Servicio con todos los registros encontrados.
     */
    public static List<Servicio> obtenerTodos() {
        // Lista que almacenará los servicios recuperados de la base de datos
        List<Servicio> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los registros de la tabla
        String sql = "SELECT * FROM servicios";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorremos los resultados de la consulta
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setId(rs.getInt("id"));                 
                s.setNombre(rs.getString("nombre"));      
                s.setDescripcion(rs.getString("descripcion")); 
                s.setPrecio(rs.getDouble("precio"));      
                s.setId_roles(rs.getInt("id_roles"));
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener servicios: " + e.getMessage());
        }

        return lista;
    }
    /**
     * Obtiene los servicios filtrados por el ID del rol.
     * @param id_roles ID del rol que se usará para filtrar.
     * @return Lista de objetos Servicio asociados a ese rol.
     */
    public static List<Servicio> obtenerPorRol(int id_roles) {
        List<Servicio> lista = new ArrayList<>();

        String sql = "SELECT * FROM servicios WHERE id_roles = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_roles);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Servicio s = new Servicio();
                    s.setId(rs.getInt("id"));                 
                    s.setNombre(rs.getString("nombre"));      
                    s.setDescripcion(rs.getString("descripcion")); 
                    s.setPrecio(rs.getDouble("precio"));      
                    s.setId_roles(rs.getInt("id_roles"));
                    lista.add(s);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener servicios por rol: " + e.getMessage());
        }

        return lista;
    }
}

