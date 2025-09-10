package Modelo;

/**
 * Clase que representa un rol dentro del sistema.
 * 
 * Corresponde a la tabla "roles" en la base de datos.
 * 
 * Atributos:
 * - id: identificador único del rol.
 * - nombre: nombre del rol (ej. manicurista, estilista, cosmetólogo).
 * 
 * Autor: [Tu Nombre o Equipo]
 * Fecha: [Fecha Actual]
 */
public class Rol {
    
    /** Identificador único del rol en la base de datos */
    private int id;
    
    /** Nombre del rol (ejemplo: manicurista, estilista, cosmetólogo, etc.) */
    private String nombre;

    // -------------------- CONSTRUCTORES -------------------- //

    public Rol() {
    }

    public Rol(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // -------------------- MÉTODOS GETTERS Y SETTERS -------------------- //

    /** Obtiene el ID del rol */
    public int getId() {
        return id;
    }

    /** Establece el ID del rol */
    public void setId(int id) {
        this.id = id;
    }

    /** Obtiene el nombre del rol */
    public String getNombre() {
        return nombre;
    }

    /** Establece el nombre del rol */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

