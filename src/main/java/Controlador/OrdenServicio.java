package Controlador;

import DAO.OrdenDAO;
import Modelo.Orden;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ordenes")
public class OrdenServicio {

    // --- Obtener todas las órdenes ---
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrdenes() {
        List<Orden> ordenes = OrdenDAO.listarOrdenes();
        return Response.ok(ordenes).build();
    }

    // --- Crear una nueva orden ---
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrden(Orden orden) {
         System.out.println("📩 Orden recibida: " + orden);
        try {
            int idOrden = OrdenDAO.insertarOrden(orden);
            if (idOrden > 0) {
                return Response.ok("{\"mensaje\": \"Orden creada con éxito\", \"idOrden\": " + idOrden + "}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("{\"error\": \"No se pudo crear la orden\"}")
                               .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}")
                           .build();
        }
    }

    // --- Obtener órdenes por cliente ---
    @GET
    @Path("/cliente/{idCliente}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdenesPorCliente(@PathParam("idCliente") int idCliente) {
        List<Orden> ordenes = OrdenDAO.listarOrdenesPorCliente(idCliente);
        return Response.ok(ordenes).build();
    }

    // --- Obtener órdenes por trabajador ---
    @GET
    @Path("/trabajador/{idTrabajador}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdenesPorTrabajador(@PathParam("idTrabajador") int idTrabajador) {
        List<Orden> ordenes = OrdenDAO.listarOrdenesPorTrabajador(idTrabajador);
        return Response.ok(ordenes).build();
    }
    //--------  Horas Ocupadas  -------------
        @GET
    @Path("/ocupadas/{idTrabajador}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHorasOcupadas(
            @PathParam("idTrabajador") int idTrabajador,
            @PathParam("fecha") String fecha) {
        try {
            List<String> horas = OrdenDAO.obtenerHorasOcupadas(idTrabajador, fecha);
            return Response.ok(horas).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"No se pudieron obtener las horas ocupadas\"}")
                    .build();
        }
    }


    // --- Eliminar una orden ---
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrden(@PathParam("id") int id) {
        // Necesitas implementar OrdenDAO.delete(int id)
        boolean eliminada = false; // 🚨 placeholder
        if (eliminada) {
            return Response.ok("{\"mensaje\": \"Orden eliminada exitosamente\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Error al eliminar la orden (método DAO no implementado)\"}")
                .build();
    }
    
     // --- Pagar una orden ---
    @PUT
    @Path("/{id}/pagar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pagarOrden(@PathParam("id") int idOrden) {
        try {
            boolean exito = OrdenDAO.actualizarEstadoOrden(idOrden, "confirmado");
            if (exito) {
                return Response.ok("{\"mensaje\":\"Orden pagada (confirmada)\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo actualizar el estado de la orden\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al pagar la orden\"}").build();
        }
    }

    // --- Cancelar una orden ---
    @PUT
    @Path("/{id}/cancelar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelarOrden(@PathParam("id") int idOrden) {
        try {
            boolean exito = OrdenDAO.actualizarEstadoOrden(idOrden, "cancelado");
            if (exito) {
                return Response.ok("{\"mensaje\":\"Orden cancelada\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo actualizar el estado de la orden\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al cancelar la orden\"}").build();
        }
    }
}



