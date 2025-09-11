package Modelo;

import java.util.List;

public class FacturaDetalle {
    private int idFactura;
    private int idOrden;
    private String clienteNombre;
    private String trabajadorNombre;
    private String fechaServicio;
    private String horaServicio;
    private String tipoModalidad;
    private String servicioNombre;
    private double servicioPrecio;
    private List<Producto> productos;
    private double totalFinal;

    // Getters y setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getTrabajadorNombre() { return trabajadorNombre; }
    public void setTrabajadorNombre(String trabajadorNombre) { this.trabajadorNombre = trabajadorNombre; }

    public String getFechaServicio() { return fechaServicio; }
    public void setFechaServicio(String fechaServicio) { this.fechaServicio = fechaServicio; }

    public String getHoraServicio() { return horaServicio; }
    public void setHoraServicio(String horaServicio) { this.horaServicio = horaServicio; }

    public String getTipoModalidad() { return tipoModalidad; }
    public void setTipoModalidad(String tipoModalidad) { this.tipoModalidad = tipoModalidad; }

    public String getServicioNombre() { return servicioNombre; }
    public void setServicioNombre(String servicioNombre) { this.servicioNombre = servicioNombre; }

    public double getServicioPrecio() { return servicioPrecio; }
    public void setServicioPrecio(double servicioPrecio) { this.servicioPrecio = servicioPrecio; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public double getTotalFinal() { return totalFinal; }
    public void setTotalFinal(double totalFinal) { this.totalFinal = totalFinal; }
}

