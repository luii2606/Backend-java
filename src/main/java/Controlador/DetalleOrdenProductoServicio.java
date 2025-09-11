package Controlador;

import DAO.ConexionDB;
import DAO.DetalleOrdenProductoDAO;
import Modelo.DetalleOrdenProducto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.List;

@Path("/detalleOrdenProducto")
public class DetalleOrdenProductoServicio {

    // 👉 Método para añadir cabeceras CORS
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                //.header("Access-Control-Allow-Origin", "http://localhost:5173") // puedes activarlo si quieres restringir
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Credentials", "true");
    }

    // ✅ Obtener productos por orden
    @GET
    @Path("/orden/{idOrden}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductosPorOrden(@PathParam("idOrden") int idOrden) {
        List<DetalleOrdenProducto> productos = DetalleOrdenProductoDAO.listarPorOrden(idOrden);
        return addCorsHeaders(Response.ok(productos)).build();
    }

    // ✅ Insertar detalle de orden
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarDetalle(DetalleOrdenProducto detalle) {
        try (Connection con = ConexionDB.getConnection()) {
            con.setAutoCommit(false); // 🔹 iniciar transacción

            boolean insertado = DetalleOrdenProductoDAO.insertarDetalle(con, detalle);

            if (insertado) {
                con.commit();
                return addCorsHeaders(Response.ok("{\"mensaje\": \"Detalle creado y stock actualizado con éxito\"}")).build();
            } else {
                con.rollback();
                return addCorsHeaders(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity("{\"error\": \"No se pudo crear el detalle de orden\"}")
                ).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return addCorsHeaders(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}")
            ).build();
        }
    }

    // ✅ Responder preflight (OPTIONS)
    @OPTIONS
    @Path("{any: .*}")
    public Response options() {
        return addCorsHeaders(Response.ok()).build();
    }
}



