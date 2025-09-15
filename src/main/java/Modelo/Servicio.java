package Modelo;

/**
 * Clase Servicio que representa un servicio ofrecido en el sistema.
 * Contiene atributos relacionados con su identificación, nombre, descripción,
 * precio y el rol al que está asociado.
 */
public class Servicio {
    // --- Atributos privados ---
    /** Identificador único del servicio (clave primaria) */
    private int id;

    /** Nombre del servicio */
    private String nombre;

    /** Descripción detallada del servicio */
    private String descripcion;

    /** Precio del servicio */
    private double precio;

    /** Identificador del rol asociado al servicio (FK hacia roles) */
    private int id_tipo_usuario;

    // --- Métodos Getter y Setter ---

    /** Obtiene el identificador del servicio */
    public int getId() {
        return id;
    }

    /** Asigna un valor al identificador del servicio */
    public void setId(int id) {
        this.id = id;
    }

    /** Obtiene el nombre del servicio */
    public String getNombre() {
        return nombre;
    }

    /** Asigna un valor al nombre del servicio */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Obtiene la descripción del servicio */
    public String getDescripcion() {
        return descripcion;
    }

    /** Asigna un valor a la descripción del servicio */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** Obtiene el precio del servicio */
    public double getPrecio() {
        return precio;
    }

    /** Asigna un valor al precio del servicio */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /** Obtiene el ID del rol asociado al servicio */
    public int getId_tipo_usuario() {
        return id_tipo_usuario;
    }

    /** Asigna un valor al rol asociado al servicio */
    public void setId_tipo_usuario(int id_tipo_usuario) {
        this.id_tipo_usuario = id_tipo_usuario;
    }
}



