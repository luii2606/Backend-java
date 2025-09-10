package Modelo;

public class DetalleOrdenProducto {
    private int id;
    private int id_orden;
    private int id_producto;
    private int cantidad;
    private double subtotal;

    public DetalleOrdenProducto() {}

    public DetalleOrdenProducto(int id_orden, int id_producto, int cantidad, double subtotal) {
        this.id_orden = id_orden;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getId_orden() { return id_orden; }
    public void setId_orden(int id_orden) { this.id_orden = id_orden; }

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

