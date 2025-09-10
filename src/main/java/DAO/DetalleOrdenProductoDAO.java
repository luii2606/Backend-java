package DAO;

import Modelo.DetalleOrdenProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
