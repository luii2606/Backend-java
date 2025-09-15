package Servicio;

import DAO.UsuarioDAO;
import Modelo.Usuarios;
import Seguridad.JWTUtil;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServicio {

    private final UsuarioDAO usuarioDAO;

    public AuthServicio() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Login de usuario: verifica correo y contraseña.
     * @param correo correo del usuario
     * @param contrasena contraseña en texto plano
     * @return Mapa con access_token y refresh_token si las credenciales son válidas; null si no lo son
     */
public Map<String, Object> login(String correo, String contrasena) {
    Usuarios u = usuarioDAO.buscarPorCorreo(correo);
    if (u == null) return null;

    // Verificar contraseña con BCrypt
    if (!BCrypt.checkpw(contrasena, u.getContrasena())) return null;

    // Obtener permisos del usuario
    List<String> permisos = usuarioDAO.obtenerPermisosPorUsuario(u.getId());
    System.out.println("Permisos obtenidos: " + permisos);

    // Generar tokens JWT
    String accessToken = JWTUtil.generarAccessToken(u, permisos); // válido 2h
    String refreshToken = JWTUtil.generarRefreshToken(u);         // válido 7d

    // Respuesta
    Map<String, Object> respuesta = new HashMap<>();
    respuesta.put("access_token", accessToken);
    respuesta.put("refresh_token", refreshToken);
    respuesta.put("id_usuario", u.getId());
    respuesta.put("id_roles", u.getId_roles()); // 👈 importante para redirección
    respuesta.put("correo", u.getCorreo());

    return respuesta;
}


    /**
     * Refrescar access token usando un refresh token válido
     * @param refreshToken token de refresco
     * @return nuevo access token si el refresh token es válido; null si no lo es
     */
    public String refresh(String refreshToken) {
        if (JWTUtil.esTokenValido(refreshToken)) {
            String correo = JWTUtil.obtenerCorreo(refreshToken);
            Usuarios u = usuarioDAO.buscarPorCorreo(correo);
            if (u != null) {
                List<String> permisos = usuarioDAO.obtenerPermisosPorUsuario(u.getId());
                return JWTUtil.generarAccessToken(u, permisos);
            }
        }
        return null; // refresh inválido
    }

    /**
     * Registrar un nuevo usuario con contraseña encriptada
     * @param usuario objeto usuario con contraseña en texto plano
     * @return true si se registró correctamente, false si hubo error
     */
    public boolean registrarUsuario(Usuarios usuario) {
        // Encriptar contraseña con BCrypt antes de guardar
        String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt(12));
        usuario.setContrasena(hashed);

        // Insertar usuario mediante DAO
        return usuarioDAO.insertarUsuarioSimple(usuario);
    }
    
    public boolean RegistrarTrabajador (Usuarios usuario){
                String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt(12));
        usuario.setContrasena(hashed);

        // Insertar usuario mediante DAO
        return usuarioDAO.insertarUsuario(usuario);
    }
     public boolean existeCorreo(String correo) {
        return usuarioDAO.buscarPorCorreo(correo) != null;
    }
    
        /**
     * ✅ Valida un Access Token y devuelve los claims.
     * Sirve para verificar si un usuario sigue autenticado.
     */
    public Claims validarAccessToken(String token) {
        return JWTUtil.validarYObtenerClaims(token);
    }

    /**
     * ✅ Método auxiliar: verifica si un token de acceso sigue siendo válido.
     */
    public boolean esAccessTokenValido(String token) {
        return JWTUtil.esTokenValido(token);
    }

    /**
     * ✅ Método auxiliar: devuelve el usuario asociado a un accessToken.
     */
    public Usuarios obtenerUsuarioDesdeToken(String token) {
        try {
            String correo = JWTUtil.obtenerCorreo(token);
            return usuarioDAO.buscarPorCorreo(correo);
        } catch (Exception e) {
            return null;
        }
    }
}
