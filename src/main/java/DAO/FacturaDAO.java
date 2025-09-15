package DAO;

import Modelo.FacturaDetalle;
import Modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // Crea factura y devuelve id generado (o -1 en error)
    public static int crearFactura(int idOrden, double total) {
        String sql = "INSERT INTO factura (id_orden, total) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idOrden);
            ps.setDouble(2, total);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error crearFactura: " + e.getMessage());
        }
        return -1;
    }

    // Verifica si ya existe factura para la orden
    public static boolean existeFactura(int idOrden) {
        String sql = "SELECT id FROM factura WHERE id_orden = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("❌ Error existeFactura: " + e.getMessage());
        }
        return false;
    }

    // Obtiene los datos detallados de la orden para mostrar en factura
    // (Versión basada en lo que ya compartiste; ajústala si nombres de columnas cambian)
    public static FacturaDetalle obtenerFacturaDetallada(int idOrden) {
        FacturaDetalle factura = new FacturaDetalle();

        String sqlOrden = "SELECT o.id, o.fecha_servicio, o.hora_servicio, " +
                "c.nombre AS clienteNombre, t.nombre AS trabajadorNombre, " +
                "s.nombre AS servicioNombre, s.precio AS servicioPrecio, " +
                "m.nombre AS modalidadNombre " +
                "FROM orden o " +
                "JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                "JOIN usuarios c ON uo.id_usuario_cliente = c.id " +
                "JOIN usuarios t ON uo.id_usuario_trabajador = t.id " +
                "JOIN servicios s ON o.id_servicios = s.id " +
                "LEFT JOIN tipo_modalidad m ON o.id_tipo_modalidad = m.id " +
                "WHERE o.id = ?";

        String sqlProductos = "SELECT p.id, p.nombre_producto, p.precio " +
                "FROM detalle_orden_producto dop " +
                "JOIN productos p ON dop.id_productos = p.id " +
                "WHERE dop.id_orden = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement psOrden = con.prepareStatement(sqlOrden);
             PreparedStatement psProductos = con.prepareStatement(sqlProductos)) {

            psOrden.setInt(1, idOrden);
            try (ResultSet rs = psOrden.executeQuery()) {
                if (rs.next()) {
                    factura.setIdOrden(rs.getInt("id"));
                    factura.setClienteNombre(rs.getString("clienteNombre"));
                    factura.setTrabajadorNombre(rs.getString("trabajadorNombre"));
                    factura.setFechaServicio(rs.getString("fecha_servicio"));
                    factura.setHoraServicio(rs.getString("hora_servicio"));
                    factura.setTipoModalidad(rs.getString("modalidadNombre"));
                    factura.setServicioNombre(rs.getString("servicioNombre"));
                    factura.setServicioPrecio(rs.getDouble("servicioPrecio"));
                }
            }

            psProductos.setInt(1, idOrden);
            List<Producto> productos = new ArrayList<>();
            double totalProductos = 0;
            try (ResultSet rs = psProductos.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombreProducto(rs.getString("nombre_producto"));
                    p.setPrecio(rs.getDouble("precio"));
                    productos.add(p);
                    totalProductos += p.getPrecio();
                }
            }
            factura.setProductos(productos);

            double totalFinal = factura.getServicioPrecio() + totalProductos;
            factura.setTotalFinal(totalFinal);

        } catch (SQLException e) {
            System.out.println("❌ Error obtenerFacturaDetallada: " + e.getMessage());
        }

        return factura;
    }
    
    public static List<FacturaDetalle> listarFacturasPorFecha(String fecha) {
    List<FacturaDetalle> lista = new ArrayList<>();
        String sql = "SELECT \n" +
                "    f.id, \n" +
                "    o.hora_servicio AS hora, \n" +
                "    f.total, \n" +
                "    uc.nombre AS cliente_nombre, \n" +
                "    ut.nombre AS trabajador_nombre, \n" +
                "    s.nombre AS servicio_nombre\n" +
                "FROM factura f\n" +
                "INNER JOIN orden o ON f.id_orden = o.id\n" +
                "INNER JOIN servicios s ON o.id_servicios = s.id\n" +
                "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id\n" +
                "INNER JOIN usuarios uc ON uo.id_usuario_cliente = uc.id\n" +
                "INNER JOIN usuarios ut ON uo.id_usuario_trabajador = ut.id\n" +
                "WHERE o.fecha_servicio = ?\n" +
                "ORDER BY o.hora_servicio ASC;";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, fecha);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FacturaDetalle factura = new FacturaDetalle();
                factura.setIdFactura(rs.getInt("id"));
                factura.setHoraServicio(rs.getString("hora"));
                factura.setTotalFinal(rs.getDouble("total"));
                factura.setClienteNombre(rs.getString("cliente_nombre"));
                factura.setTrabajadorNombre(rs.getString("trabajador_nombre"));
                factura.setServicioNombre(rs.getString("servicio_nombre"));
                lista.add(factura);
            }
        }
    } catch (SQLException e) {
        System.out.println("❌ Error al listar facturas por fecha: " + e.getMessage());
    }
    return lista;
}

}

