/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USUARIO
 */
public class EstadoUsuario {
    
     private int id;
   
    private String nombre;

    // -------------------- CONSTRUCTORES -------------------- //

    public EstadoUsuario() {
    }
    public EstadoUsuario(int id, String nombre) {
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
