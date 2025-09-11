package DAO;

import Modelo.DetalleOrdenProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleOrdenProductoDAO {

    public static boolean insertarDetalle(Connection con, DetalleOrdenProducto detalle) throws SQLException {
        String sqlInsert = "INSERT INTO detalle_orden_producto (id_orden, id_productos, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ? AND cantidad >= ?";

        try (
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)
        ) {
            // 🔹 Insertar detalle
            psInsert.setInt(1, detalle.getId_orden());
            psInsert.setInt(2, detalle.getId_producto());
            psInsert.setInt(3, detalle.getCantidad());
            psInsert.setDouble(4, detalle.getSubtotal());
            int filasInsertadas = psInsert.executeUpdate();

            if (filasInsertadas == 0) {
                return false;
            }

            // 🔹 Actualizar stock del producto
            psUpdate.setInt(1, detalle.getCantidad());
            psUpdate.setInt(2, detalle.getId_producto());
            psUpdate.setInt(3, detalle.getCantidad());
            int filasActualizadas = psUpdate.executeUpdate();

            // ⚠️ Si no se pudo actualizar stock (ej. stock insuficiente), hacemos rollback
            if (filasActualizadas == 0) {
                con.rollback();
                throw new SQLException("Stock insuficiente para el producto ID " + detalle.getId_producto());
            }

            return true;
        }
    }
    public static List<DetalleOrdenProducto> listarPorOrden(int idOrden) {
    List<DetalleOrdenProducto> lista = new ArrayList<>();
    String sql = "SELECT dop.id, dop.id_orden, dop.id_productos, dop.cantidad, dop.subtotal, " +
                 "p.nombre_producto, p.precio " +
                 "FROM detalle_orden_producto dop " +
                 "INNER JOIN productos p ON dop.id_productos = p.id " +
                 "WHERE dop.id_orden = ?";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idOrden);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DetalleOrdenProducto d = new DetalleOrdenProducto();
                d.setId(rs.getInt("id"));
                d.setId_orden(rs.getInt("id_orden"));
                d.setId_producto(rs.getInt("id_productos"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setSubtotal(rs.getDouble("subtotal"));
                d.setNombre_producto(rs.getString("nombre_producto"));
                d.setPrecio(rs.getDouble("precio"));
                lista.add(d);
            }
        }
    } catch (SQLException e) {
        System.out.println("❌ Error al listar productos por orden: " + e.getMessage());
    }
    return lista;
}

}
