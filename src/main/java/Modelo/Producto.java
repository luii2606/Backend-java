package Modelo;

public class Producto {
    private int id;
    private String nombreProducto;
    private String descripcion;
    private double precio;
     private int cantidad;  // 
    private int idEstadoProducto;
    private String nombreEstado;

    // Constructor vacío (requerido por frameworks, JSON, etc.)
    public Producto() {}

    // ✅ Constructor con todos los parámetros
    public Producto(int id, String nombreProducto, String descripcion, double precio, int cantidad, int idEstadoProducto) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idEstadoProducto = idEstadoProducto;
    }

    // Getters y setters
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNombreProducto() { 
        return nombreProducto; 
    }
    public void setNombreProducto(String nombreProducto) { 
        this.nombreProducto = nombreProducto; 
    }

    public String getDescripcion() { 
        return descripcion; 
    }
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }

    public double getPrecio() { 
        return precio; 
    }
    public void setPrecio(double precio) { 
        this.precio = precio; 
    }
     public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getIdEstadoProducto() { 
        return idEstadoProducto; 
    }
    public void setIdEstadoProducto(int idEstadoProducto) { 
        this.idEstadoProducto = idEstadoProducto; 
    }
    public String getNombreEstado() {
    return nombreEstado;
}

public void setNombreEstado(String nombreEstado) {
    this.nombreEstado = nombreEstado;
}
}

