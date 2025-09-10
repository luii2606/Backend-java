package Modelo;

import java.util.List;

public class Orden {
    private int id;
    private int id_usuario;
    private int id_trabajador;
    private int id_servicio;
    private int id_modalidad;
    private String fecha;
    private String hora;
    private String modalidad_nombre;
    private String usuario_nombre;
    private String trabajador_nombre;
    private String servicio_nombre;
    private int id_estado;
    private int id_usuarios_orden;
    private String estado_nombre;
    
     private List<DetalleOrdenProducto> productos;

    public List<DetalleOrdenProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleOrdenProducto> productos) {
        this.productos = productos;
    }

    // 🔹 Constructor vacío (requerido por muchos frameworks)
    public Orden() {
    }

    // 🔹 Constructor con parámetros principales
    public Orden(int id_trabajador, int id_usuario, int id_servicio, int id_modalidad, String fecha, String hora) {
        this.id_trabajador = id_trabajador;
        this.id_usuario = id_usuario;
        this.id_servicio = id_servicio;
        this.id_modalidad = id_modalidad;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }



    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public String getTrabajador_nombre() {
        return trabajador_nombre;
    }

    public void setTrabajador_nombre(String trabajador_nombre) {
        this.trabajador_nombre = trabajador_nombre;
    }

    public String getServicio_nombre() {
        return servicio_nombre;
    }

    public void setServicio_nombre(String servicio_nombre) {
        this.servicio_nombre = servicio_nombre;
    }
       public int getId_modalidad() {
        return id_modalidad;
    }

    public void setId_modalidad(int id_modalidad) {
        this.id_modalidad = id_modalidad;
    }

    public String getModalidad_nombre() {
        return modalidad_nombre;
    }

    public void setModalidad_nombre(String modalidad_nombre) {
        this.modalidad_nombre = modalidad_nombre;
    }
    public int getId_estado() { return id_estado; }
    public void setId_estado(int id_estado) { this.id_estado = id_estado; }

    public int getId_usuarios_orden() { return id_usuarios_orden; }
    public void setId_usuarios_orden(int id_usuarios_orden) { this.id_usuarios_orden = id_usuarios_orden; }

    public String getEstado_nombre() { return estado_nombre; }
    public void setEstado_nombre(String estado_nombre) { this.estado_nombre = estado_nombre; }
}
