package Modelo;

/**
 * Clase que representa el modelo de una modalidad dentro del sistema.
 * 
 * Esta clase almacena información básica sobre una modalidad, incluyendo:
 * - Un identificador único.
 * - El nombre de la modalidad.
 * 
 * Se utiliza como entidad en la capa de modelo.
 */
public class TipoModalidad {
    
    // -------------------------
    // Atributos
    // -------------------------
    
    /** Identificador único de la modalidad (clave primaria en la BD). */
    private int id;
    
    /** Nombre de la modalidad (ejemplo: "Presencial", "Domicilio"). */
    private String nombre;

    // -------------------------
    // Métodos Getters y Setters
    // -------------------------
    
    /**
     * Obtiene el identificador de la modalidad.
     * 
     * @return id identificador único de la modalidad.
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna un valor al identificador de la modalidad.
     * 
     * @param id identificador único a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la modalidad.
     * 
     * @return nombre de la modalidad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna un valor al nombre de la modalidad.
     * 
     * @param nombre nombre de la modalidad a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

