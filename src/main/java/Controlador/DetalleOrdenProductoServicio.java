package Controlador;

import DAO.ConexionDB;
import DAO.DetalleOrdenProductoDAO;
import Modelo.DetalleOrdenProducto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;

@Path("/detalleOrdenProducto")
public class DetalleOrdenProductoServicio {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarDetalle(DetalleOrdenProducto detalle) {
        try (Connection con = ConexionDB.getConnection()) {
            con.setAutoCommit(false); // 🔹 iniciar transacción

            boolean insertado = DetalleOrdenProductoDAO.insertarDetalle(con, detalle);

            if (insertado) {
                con.commit();
                return Response.ok("{\"mensaje\": \"Detalle creado y stock actualizado con éxito\"}").build();
            } else {
                con.rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("{\"error\": \"No se pudo crear el detalle de orden\"}")
                               .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}")
                           .build();
        }
    }
}


