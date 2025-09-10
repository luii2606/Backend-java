
package DAO;

import Modelo.EstadoProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class EstadoProductoDAO {
    // ✅ Obtener todos los estados de producto
    public static List<EstadoProducto> getEstados() {
        
        List<EstadoProducto> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado_producto;"; // ajusta si tu tabla tiene otro nombre

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoProducto estado = new EstadoProducto();
                estado.setId(rs.getInt("id"));
                estado.setNombre(rs.getString("nombre"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener estados de producto: " + e.getMessage());
        }

        return lista;
    }
}

