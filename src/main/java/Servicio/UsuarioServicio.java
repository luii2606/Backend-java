package Servicio;

import DAO.UsuarioDAO;
import Modelo.Usuarios;
import java.util.List;

public class UsuarioServicio {

    private UsuarioDAO usuarioDAO;

    public UsuarioServicio() {
        usuarioDAO = new UsuarioDAO();
    }

    // Obtener todos los usuarios
    public List<Usuarios> getUsuarios() {
        return UsuarioDAO.getUsuarios();
    }

    // Obtener usuario por ID
    public Usuarios getUsuarioPorId(int id) {
        return UsuarioDAO.getUsuarioPorId(id);
    }

    // Crear usuario
    public boolean crearUsuario(Usuarios u) {
        if (usuarioDAO.existeNombreUsuario(u.getNombre())) {
            System.out.println("El nombre de usuario ya existe");
            return false;
        }
        return usuarioDAO.insertarUsuarioSimple(u);
    }

    // Login
    public Usuarios login(String nombre, String contrasena) {
        return usuarioDAO.verificarLogin(nombre, contrasena);
    }

    // Actualizar usuario
    public boolean actualizarUsuario(int id, Usuarios u) {
        u.setId(id);
        return usuarioDAO.actualizarUsuario(u);
    }
    
    public boolean eliminarUsuario(int id) {
        return usuarioDAO.eliminarUsuario(id);
    }
    public List<Usuarios> getTrabajadores() {
    return UsuarioDAO.getTrabajadores();
}
    
  
}


   

