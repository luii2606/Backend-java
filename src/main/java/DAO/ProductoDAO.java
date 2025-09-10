package DAO;

import Modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para manejar operaciones CRUD relacionadas
 * con la tabla "productos".
 *
 * Ahora utiliza la clase ConexionDB para obtener la conexión a la base de datos,
 * evitando repetir credenciales en cada DAO.
 */
public class ProductoDAO {

    // 🔹 Insertar
    public static boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre_producto, descripcion, precio, cantidad, id_estado_producto) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getCantidad());
            ps.setInt(5, producto.getIdEstadoProducto());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Actualizar
    public static boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre_producto=?, descripcion=?, precio=?, cantidad=?, id_estado_producto=? WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getCantidad());
            ps.setInt(5, producto.getIdEstadoProducto());
            ps.setInt(6, producto.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Eliminar
    public static boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Buscar por ID
    public static Producto obtenerPorId(int id) {
        String sql = "SELECT p.id, p.nombre_producto, p.descripcion, p.precio, p.cantidad, " +
                     "p.id_estado_producto, e.nombre AS nombreEstado " +
                     "FROM productos p " +
                     "LEFT JOIN estado_producto e ON p.id_estado_producto = e.id " +
                     "WHERE p.id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombreProducto(rs.getString("nombre_producto"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setCantidad(rs.getInt("cantidad"));
                    p.setIdEstadoProducto(rs.getInt("id_estado_producto"));
                    p.setNombreEstado(rs.getString("nombreEstado"));
                    return p;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
        }

        return null;
    }

    // 🔹 Listar todos (con JOIN para incluir el estado)
    public static List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre_producto, p.descripcion, p.precio, p.cantidad, " +
                     "p.id_estado_producto, e.nombre AS nombreEstado " +
                     "FROM productos p " +
                     "LEFT JOIN estado_producto e ON p.id_estado_producto = e.id";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombreProducto(rs.getString("nombre_producto"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setIdEstadoProducto(rs.getInt("id_estado_producto"));
                p.setNombreEstado(rs.getString("nombreEstado"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }
}



