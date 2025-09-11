package DAO;

import Modelo.Orden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.DetalleOrdenProducto;

public class OrdenDAO {

    // ✅ Insertar nueva orden
   public static int insertarOrden(Orden orden) {
    String sqlUsuariosOrden = "INSERT INTO usuarios_orden (id_usuario_trabajador, id_usuario_cliente) VALUES (?, ?)";
    String sqlOrden = "INSERT INTO orden (fecha_servicio, hora_servicio, id_tipo_modalidad, id_servicios, id_Estado_orden, id_usuarios_orden) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = ConexionDB.getConnection()) {
        con.setAutoCommit(false);
        int idUsuariosOrden = -1;
        int idOrden = -1;

        // 1. Insertar en usuarios_orden
        try (PreparedStatement ps = con.prepareStatement(sqlUsuariosOrden, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orden.getId_trabajador());
            ps.setInt(2, orden.getId_usuario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) idUsuariosOrden = rs.getInt(1);
            }
        }

        // 2. Insertar en orden
        try (PreparedStatement ps = con.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, orden.getFecha());
            ps.setString(2, orden.getHora());
            ps.setInt(3, orden.getId_modalidad());
            ps.setInt(4, orden.getId_servicio());
            ps.setInt(5, 1); // estado inicial "pendiente"
            ps.setInt(6, idUsuariosOrden);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) idOrden = rs.getInt(1);
            }
        }

         // 3. Insertar productos solo si existen
         if (orden.getProductos() != null && !orden.getProductos().isEmpty()) {
             for (DetalleOrdenProducto p : orden.getProductos()) {
                 p.setId_orden(idOrden);
                 DetalleOrdenProductoDAO.insertarDetalle(con, p); // ✅ misma conexión
             }
         } else {
             System.out.println("⚠️ Orden insertada sin productos (opcional)");
         }  
         
                // 🚨 Validar disponibilidad antes de insertar
          if (existeCita(orden.getId_trabajador(), orden.getFecha(), orden.getHora())) {
              System.out.println("⚠️ El trabajador ya tiene una cita en esa hora.");
              return -2; // código especial para "ocupado"
          }

        con.commit();
        return idOrden;

    } catch (SQLException e) {
        System.out.println("❌ Error al insertar orden: " + e.getMessage());
        return -1;
    }


}


    // ✅ Obtener todas las órdenes
    public static List<Orden> listarOrdenes() {
    List<Orden> lista = new ArrayList<>();
    String sql = "SELECT o.id, o.fecha_servicio, o.hora_servicio, " +
                 "o.id_tipo_modalidad, m.nombre AS modalidad_nombre, " +
                 "s.nombre AS servicio_nombre, " +
                 "eo.nombre AS estado_nombre, " +
                 "uc.nombre AS usuario_nombre, " +
                 "ut.nombre AS trabajador_nombre " +
                 "FROM orden o " +
                 "INNER JOIN servicios s ON o.id_servicios = s.id " +
                 "INNER JOIN estado_orden eo ON o.id_Estado_orden = eo.id " +
                 "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                 "INNER JOIN usuarios uc ON uo.id_usuario_cliente = uc.id " +
                 "INNER JOIN usuarios ut ON uo.id_usuario_trabajador = ut.id " +
                 "LEFT JOIN tipo_modalidad m ON o.id_tipo_modalidad = m.id"; // <-- ajustar nombre de la tabla si es 'modalidades'

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(mapOrden(rs));
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al listar órdenes: " + e.getMessage());
    }
    return lista;
}


    // ✅ Buscar órdenes por cliente
    public static List<Orden> listarOrdenesPorCliente(int idCliente) {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.id, o.fecha_servicio, o.hora_servicio, o.id_tipo_modalidad, " +
                     "s.nombre AS servicio_nombre, " +
                     "eo.nombre AS estado_nombre, " +
                     "uc.nombre AS usuario_nombre, " +
                     "ut.nombre AS trabajador_nombre " +
                     "FROM orden o " +
                     "INNER JOIN servicios s ON o.id_servicios = s.id " +
                     "INNER JOIN estado_orden eo ON o.id_Estado_orden = eo.id " +
                     "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                     "INNER JOIN usuarios uc ON uo.id_usuario_cliente = uc.id " +
                     "INNER JOIN usuarios ut ON uo.id_usuario_trabajador = ut.id " +
                     "WHERE uc.id = ?"; 

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapOrden(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar órdenes por cliente: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Buscar órdenes por trabajador
    public static List<Orden> listarOrdenesPorTrabajador(int idTrabajador) {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.id, o.fecha_servicio, o.hora_servicio, o.id_tipo_modalidad, " +
                     "s.nombre AS servicio_nombre, " +
                     "eo.nombre AS estado_nombre, " +
                     "uc.nombre AS usuario_nombre, " +
                     "ut.nombre AS trabajador_nombre " +
                     "FROM orden o " +
                     "INNER JOIN servicios s ON o.id_servicios = s.id " +
                     "INNER JOIN estado_orden eo ON o.id_Estado_orden = eo.id " +
                     "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                     "INNER JOIN usuarios uc ON uo.id_usuario_cliente = uc.id " +
                     "INNER JOIN usuarios ut ON uo.id_usuario_trabajador = ut.id " +
                     "WHERE uo.id_usuario_trabajador = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTrabajador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapOrden(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar órdenes por trabajador: " + e.getMessage());
        }
        return lista;
    }
    
    // ✅ Listar órdenes por trabajador y fecha
public static List<Orden> listarOrdenesPorTrabajadorYFecha(int idTrabajador, String fecha) {
    List<Orden> lista = new ArrayList<>();
    String sql = "SELECT o.id, o.fecha_servicio, o.hora_servicio, id_tipo_modalidad, " +
                 "s.nombre AS servicio_nombre, " +
                 "eo.nombre AS estado_nombre, " +
                 "uc.nombre AS usuario_nombre, " +
                 "ut.nombre AS trabajador_nombre " +
                 "FROM orden o " +
                 "INNER JOIN servicios s ON o.id_servicios = s.id " +
                 "INNER JOIN estado_orden eo ON o.id_Estado_orden = eo.id " +
                 "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                 "INNER JOIN usuarios uc ON uo.id_usuario_cliente = uc.id " +
                 "INNER JOIN usuarios ut ON uo.id_usuario_trabajador = ut.id " +
                 "WHERE uo.id_usuario_trabajador = ? AND o.fecha_servicio = ?";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idTrabajador);
        ps.setString(2, fecha);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapOrden(rs));
            }
        }
    } catch (SQLException e) {
        System.out.println("❌ Error al listar órdenes por trabajador y fecha: " + e.getMessage());
    }
    return lista;
}
        
        


    
    // ✅ Verificar si un trabajador ya tiene una cita en esa fecha y hora
public static boolean existeCita(int idTrabajador, String fecha, String hora) {
    String sql = "SELECT COUNT(*) FROM orden o " +
                 "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                 "WHERE uo.id_usuario_trabajador = ? AND o.fecha_servicio = ? AND o.hora_servicio = ?";
    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idTrabajador);
        ps.setString(2, fecha);
        ps.setString(3, hora);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // true si ya hay cita
            }
        }

    } catch (SQLException e) {
        System.out.println("❌ Error verificando disponibilidad: " + e.getMessage());
    }
    return false;
}

        public static List<String> obtenerHorasOcupadas(int idTrabajador, String fecha) {
        List<String> horas = new ArrayList<>();
        String sql = "SELECT o.hora_servicio " +
                     "FROM orden o " +
                     "INNER JOIN usuarios_orden uo ON o.id_usuarios_orden = uo.id " +
                     "WHERE uo.id_usuario_trabajador = ? AND o.fecha_servicio = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTrabajador);
            ps.setString(2, fecha);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    horas.add(rs.getString("hora_servicio"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener horas ocupadas: " + e.getMessage());
        }
        return horas;
    }
        
          // Actualizar el estado de una orden por nombre de estado
    public static boolean actualizarEstadoOrden(int idOrden, String nombreEstado) throws Exception {
        String sql = """
             UPDATE orden o
                    SET o.id_Estado_orden = (
                        SELECT eo.id FROM estado_orden eo WHERE eo.nombre = ?
                    )
                    WHERE o.id = ?
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreEstado); 
            ps.setInt(2, idOrden);

            return ps.executeUpdate() > 0;
        }
    }
    
    public static List<String> listarEstados() {
    List<String> estados = new ArrayList<>();
    String sql = "SELECT nombre FROM estado_orden ORDER BY id";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            estados.add(rs.getString("nombre"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return estados;
}

    
    public static boolean eliminarOrden(int idOrden) {
    String sql = "DELETE FROM orden WHERE id = ?";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idOrden);
        int filas = stmt.executeUpdate();
        return filas > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}




    // ✅ Mapper común para Orden
    private static Orden mapOrden(ResultSet rs) throws SQLException {
        Orden o = new Orden();
        o.setId(rs.getInt("id"));
        o.setFecha(rs.getString("fecha_servicio"));
        o.setHora(rs.getString("hora_servicio"));
        o.setServicio_nombre(rs.getString("servicio_nombre"));
        o.setId_modalidad(rs.getInt("id_tipo_modalidad"));
        o.setUsuario_nombre(rs.getString("usuario_nombre"));
        o.setTrabajador_nombre(rs.getString("trabajador_nombre"));
        o.setEstado_nombre(rs.getString("estado_nombre"));
        return o;
    }
}

