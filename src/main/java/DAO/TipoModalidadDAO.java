package DAO;

import Modelo.TipoModalidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para manejar operaciones CRUD relacionadas
 * con la tabla "tipo_modalidad".
 * 
 * Ahora utiliza la clase ConexionDB para obtener la conexión a la base de datos,
 * evitando repetir credenciales en cada DAO.
 */
public class TipoModalidadDAO {

    /**
     * Obtiene todas las modalidades almacenadas en la tabla "tipo_modalidad".
     * @return Lista de objetos TipoModalidad con todos los registros encontrados.
     */
    public static List<TipoModalidad> obtenerTodas() {
        // Lista que almacenará las modalidades recuperadas de la base de datos
        List<TipoModalidad> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los registros
        String sql = "SELECT id, nombre FROM tipo_modalidad";

        // Try-with-resources: garantiza el cierre automático de conexión, statement y resultset
        try (Connection conn = ConexionDB.getConnection(); // Usando la nueva conexión centralizada
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorremos los resultados y mapeamos cada fila a un objeto TipoModalidad
            while (rs.next()) {
                TipoModalidad m = new TipoModalidad();
                m.setId(rs.getInt("id"));           // ID de la modalidad
                m.setNombre(rs.getString("nombre")); // Nombre de la modalidad
                lista.add(m); // Agregamos la modalidad a la lista
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener modalidades: " + e.getMessage());
        }

        // Retornamos la lista (puede estar vacía si no hubo resultados)
        return lista;
    }

    /**
     * Obtiene una modalidad específica según su ID.
     * @param id ID de la modalidad que se desea obtener.
     * @return Objeto TipoModalidad correspondiente al ID, o null si no existe.
     */
    public static TipoModalidad obtenerPorId(int id) {
        TipoModalidad modalidad = null;

        // Consulta SQL con parámetro para filtrar por ID
        String sql = "SELECT id, nombre FROM tipo_modalidad WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection(); // Nueva conexión centralizada
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Se establece el valor del parámetro en la consulta
            stmt.setInt(1, id);

            // Ejecutamos la consulta y procesamos el resultado
            try (ResultSet rs = stmt.executeQuery()) {
                // Si se encuentra el registro, mapearlo a objeto TipoModalidad
                if (rs.next()) {
                    modalidad = new TipoModalidad();
                    modalidad.setId(rs.getInt("id"));         // ID de la modalidad
                    modalidad.setNombre(rs.getString("nombre")); // Nombre de la modalidad
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener modalidad por ID: " + e.getMessage());
        }

        // Retornamos el objeto modalidad (puede ser null si no se encontró)
        return modalidad;
    }
}





