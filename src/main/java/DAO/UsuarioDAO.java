package DAO;

import Modelo.Usuarios;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Obtener todos los usuarios
   public static List<Usuarios> getUsuarios() {
    List<Usuarios> lista = new ArrayList<>();
    String sql = "SELECT u.id, u.nombre AS usuario, u.contrasena, u.correo, u.telefono, u.id_tipo_usuario, " +
             "u.id_roles, u.id_Estado_usuarios, " +
             "r.nombre AS nombreRol, " +
             "e.nombre AS nombreEstado, " +
             "t.nombre AS nombre_tipo_usuario " + // 👈 Aquí traemos el nombre del tipo
             "FROM usuarios u " +
             "LEFT JOIN roles r ON u.id_roles = r.id " +
             "LEFT JOIN estado_usuarios e ON u.id_Estado_usuarios = e.id " +
             "LEFT JOIN tipo_usuario t ON u.id_tipo_usuario = t.id;"; // 👈 Hacemos join con tipo_usuario


    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
Usuarios u = new Usuarios(
    rs.getInt("id"),
    rs.getString("usuario"),
    rs.getString("contrasena"),
    rs.getString("correo"),
    rs.getString("telefono"),
    rs.getInt("id_tipo_usuario"),
    rs.getInt("id_roles"),
    rs.getInt("id_Estado_usuarios"),
    rs.getString("nombreRol"),
    rs.getString("nombreEstado"),
    rs.getString("nombre_tipo_usuario")
);

            lista.add(u);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}


    // Obtener usuario por ID
    public static Usuarios getUsuarioPorId(int id) {
        Usuarios u = null;
        String sql = "SELECT u.*, r.nombre AS nombre_rol " +
                     "FROM usuarios u " +
                     "LEFT JOIN roles r ON u.id_roles = r.id " +
                     "WHERE u.id = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuarios(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("contrasena"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getInt("id_tipo_usuario"),
                            rs.getInt("id_roles"),
                            rs.getInt("id_Estado_usuarios"),
                            rs.getString("nombre_rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por ID: " + e.getMessage());
        }
        return u;
    }
    
    // Insertar usuario sin rol ni estado
public static boolean insertarUsuarioSimple(Usuarios u) {
    String sql = "INSERT INTO usuarios (nombre, contrasena, correo, telefono, id_roles, id_Estado_usuarios) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, u.getNombre());
        pstmt.setString(2, u.getContrasena());
        pstmt.setString(3, u.getCorreo());
        pstmt.setString(4, u.getTelefono());
        pstmt.setInt(5, u.getId_roles());
        pstmt.setInt(6, u.getId_estado_usuarios());
        

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al insertar usuario simple: " + e.getMessage());
        return false;
    }
}


    // Insertar usuario
    public static boolean insertarUsuario(Usuarios u) {
        String sql = "INSERT INTO usuarios (nombre, contrasena, correo, telefono, id_tipo_usuario, id_roles, id_Estado_usuarios) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getContrasena());
            pstmt.setString(3, u.getCorreo());
            pstmt.setString(4, u.getTelefono());
            pstmt.setInt(5, u.getId_tipo_usuario());
            pstmt.setInt(6, u.getId_roles());
            pstmt.setInt(7, u.getId_estado_usuarios());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // Verificar si existe nombre de usuario
    public static boolean existeNombreUsuario(String nombre) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar nombre de usuario: " + e.getMessage());
        }
        return false;
    }

  public static Usuarios verificarLogin(String correo, String contrasena) {
    Usuarios u = null;
    String sql = "SELECT u.*, r.nombre AS nombre_rol " +
                 "FROM usuarios u " +
                 "LEFT JOIN roles r ON u.id_roles = r.id " +
                 "WHERE u.correo = ? AND u.contrasena = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, correo);
        stmt.setString(2, contrasena);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                u = new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contrasena"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("id_tipo_usuario"),
                        rs.getInt("id_roles"),
                        rs.getInt("id_Estado_usuarios"),
                        rs.getString("nombre_rol")
                );
            }
        }

    } catch (SQLException e) {
        System.out.println("Error al verificar login: " + e.getMessage());
    }
    return u;
}
  
  public boolean existeCorreo(String correo, int idUsuario) throws SQLException {
    String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ? AND id <> ?";
        try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, correo);
        stmt.setInt(2, idUsuario);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
    // Buscar por correo (para login)
    public Usuarios buscarPorCorreo(String correo) {
        Usuarios usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuarios();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setId_roles(rs.getInt("id_roles"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }
       /**
     * (Opcional) Obtiene permisos directamente por id de usuario,
     * útil si quieres evitar dos llamadas (usuario -> rol -> permisos).
     */
public List<String> obtenerPermisosPorUsuario(int idUsuario) {
    List<String> permisos = new ArrayList<>();
    String sql = "SELECT p.nombre\n" +
                    "FROM permisos p\n" +
                    "JOIN permisos_tipo_rol pr ON p.id = pr.id_permisos\n" +
                    "JOIN roles r ON pr.id_roles = r.id\n" +
                    "JOIN usuarios u ON u.id_roles = r.id\n" +
                    "WHERE u.id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            permisos.add(rs.getString("nombre"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return permisos;
}


    // ✅ Listar todos los usuarios con su tipo de usuario
public List<Usuarios> listar() {
    List<Usuarios> lista = new ArrayList<>();
    String sql = "SELECT u.id, u.nombre, u.correo, u.telefono, u.contrasena, t.nombre AS roles\n" +
                "FROM usuarios u\n" +
                "JOIN roles t ON u.id_roles = t.id;";

    try (Connection conn = ConexionDB.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Usuarios u = new Usuarios();
            u.setId(rs.getInt("id"));
            u.setNombre(rs.getString("nombre"));
            u.setCorreo(rs.getString("correo"));
            u.setTelefono(rs.getString("telefono"));
            u.setContrasena(rs.getString("contrasena"));
            u.setId_roles(rs.getInt("id_roles"));
            lista.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}


// ✅ Listar usuarios por tipo de usuario
public List<Usuarios> listarPorTipoUsuario(int idTipoUsuario) {
    List<Usuarios> lista = new ArrayList<>();
    String sql = "SELECT u.id, u.nombre, u.correo, u.telefono, u.contrasena, u.id_roles " +
                 "FROM usuarios u " +
                 "WHERE u.id_roles = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idTipoUsuario);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Usuarios u = new Usuarios();
            u.setId(rs.getInt("id"));
            u.setNombre(rs.getString("nombre"));
            u.setCorreo(rs.getString("correo"));
            u.setTelefono(rs.getString("telefono"));
            u.setContrasena(rs.getString("contrasena"));
            u.setId_roles(rs.getInt("id_roles"));
            lista.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}


    // Actualizar usuario
    public static boolean actualizarUsuario(Usuarios u) {
        String sql = "UPDATE usuarios SET nombre=?, contrasena=?, correo=?, telefono=?, id_roles=?, id_tipo_usuario=?, id_estado_usuarios=? WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getContrasena());
            pstmt.setString(3, u.getCorreo());
            pstmt.setString(4, u.getTelefono());
            pstmt.setInt(5, u.getId_roles());
            pstmt.setInt(6, u.getId_tipo_usuario());
            pstmt.setInt(7, u.getId_estado_usuarios());
            pstmt.setInt(8, u.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // Obtener usuarios por tipo (ej. trabajadores id_tipo_usuario = 3)
    public static List<Usuarios> obtenerUsuariosPorTipo(int tipo) {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT u.*, t.nombre AS nombre_tipo_usuario \n" +
"                     FROM usuarios u \n" +
"                     LEFT JOIN tipo_usuario t ON u.id_tipo_usuario= t.id \n" +
"                     WHERE u.id_roles = 3;";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuarios u = new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contrasena"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("id_roles"),
                         rs.getInt("id_tipo_usuario"),
                        rs.getInt("id_Estado_usuarios"),
                        rs.getString("nombre_rol")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios por rol: " + e.getMessage());
        }
        return lista;
    }
    
    
        public static boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
}
        public static List<Usuarios> getTrabajadores() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.telefono, t.nombre AS nombre_tipo_usuario\n" +
"             FROM usuarios u \n" +
"             LEFT JOIN tipo_usuario t ON u.id_tipo_usuario = t.id \n" +
"             WHERE u.id_roles = 3;";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setNombre_tipo_usuario(rs.getString("nombre_tipo_usuario"));
                u.setTelefono(rs.getString("telefono"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    
}





