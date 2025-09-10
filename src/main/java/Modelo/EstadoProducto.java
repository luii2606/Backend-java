
package Modelo;


public class EstadoProducto {
      private int id;
   
    private String nombre;
    private String nombreEstado;

    // -------------------- CONSTRUCTORES -------------------- //

    public EstadoProducto() {
    }
    public EstadoProducto(int id, String nombre) {
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
    public String getNombreEstado() {
    return nombreEstado;
}

public void setNombreEstado(String nombreEstado) {
    this.nombreEstado = nombreEstado;
}
}
