package Modelo;

public class Usuarios {
    private int id;
    private String nombre;
    private String contrasena;
    private String correo;
    private String telefono;
    private int id_tipo_usuario;
    private int id_roles;
    private int id_estado_usuarios;
    private String nombreRol;
    private String nombreEstado;
    private String nombre_tipo_usuario;

    // Constructor sin parámetros
    public Usuarios() {}

    // Constructor con rol (9 parámetros)
    public Usuarios(int id, String nombre, String contrasena, String correo,
                    String telefono, int id_tipo_usuario, int id_roles,
                    int id_estado_usuarios, String nombreRol) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_roles = id_roles;
        this.id_estado_usuarios = id_estado_usuarios;
        this.nombreRol = nombreRol;
    }

    // Constructor con rol + estado (10 parámetros)
    public Usuarios(int id, String nombre, String contrasena, String correo,
                    String telefono, int id_tipo_usuario, int id_roles,
                    int id_estado_usuarios, String nombreRol, String nombreEstado) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_roles = id_roles;
        this.id_estado_usuarios = id_estado_usuarios;
        this.nombreRol = nombreRol;
        this.nombreEstado = nombreEstado;
    }

    // ✅ Constructor con rol + estado + tipo_usuario (11 parámetros)
    public Usuarios(int id, String nombre, String contrasena, String correo,
                    String telefono, int id_tipo_usuario, int id_roles,
                    int id_estado_usuarios, String nombreRol,
                    String nombreEstado, String nombre_tipo_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_roles = id_roles;
        this.id_estado_usuarios = id_estado_usuarios;
        this.nombreRol = nombreRol;
        this.nombreEstado = nombreEstado;
        this.nombre_tipo_usuario = nombre_tipo_usuario;
    }

    // Getter y Setter para nombre_tipo_usuario
    public String getNombre_tipo_usuario() {
        return nombre_tipo_usuario;
    }

    public void setNombre_tipo_usuario(String nombre_tipo_usuario) {
        this.nombre_tipo_usuario = nombre_tipo_usuario;
    }

    // Resto de getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getId_tipo_usuario() { return id_tipo_usuario; }
    public void setId_tipo_usuario(int id_tipo_usuario) { this.id_tipo_usuario = id_tipo_usuario; }

    public int getId_roles() { return id_roles; }
    public void setId_roles(int id_roles) { this.id_roles = id_roles; }

    public int getId_estado_usuarios() { return id_estado_usuarios; }
    public void setId_estado_usuarios(int id_estado_usuarios) { this.id_estado_usuarios = id_estado_usuarios; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    // Comparación por id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Usuarios other = (Usuarios) obj;
        return this.id == other.id;
    }
}




