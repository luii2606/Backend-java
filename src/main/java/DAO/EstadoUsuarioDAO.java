/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.EstadoUsuario;
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
public class EstadoUsuarioDAO {
    // ✅ Obtener todos los estados de usuario
    public static List<EstadoUsuario> getEstados() {
        
        List<EstadoUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado_usuarios;"; // ajusta si el nombre de tu tabla/campos es distinto

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                
                EstadoUsuario estado = new EstadoUsuario();
                estado.setId(rs.getInt("id"));
                estado.setNombre(rs.getString("nombre"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener estados de usuario: " + e.getMessage());
        }

        return lista;
    }
}
