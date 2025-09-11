package Controlador;

import DAO.FacturaDAO;
import DAO.OrdenDAO;
import Modelo.FacturaDetalle;
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
    System.out.println("📌 Entró al servicio getOrdenesPorCliente con idCliente=" + idCliente);
    List<Orden> ordenes = OrdenDAO.listarOrdenesPorCliente(idCliente);
    System.out.println("📌 Ordenes encontradas: " + ordenes.size());
    return Response.ok(ordenes).build();
}

@GET
@Path("/estados")
@Produces(MediaType.APPLICATION_JSON)
public Response getEstados() {
    try {
        List<String> estados = OrdenDAO.listarEstados();
        return Response.ok(estados).build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"No se pudieron obtener los estados\"}")
                       .build();
    }
}



    // --- Obtener órdenes por trabajador ---
    @GET
    @Path("/trabajador/{idTrabajador}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdenesPorTrabajador(@PathParam("idTrabajador") int idTrabajador) {
        List<Orden> ordenes = OrdenDAO.listarOrdenesPorTrabajador(idTrabajador);
        return Response.ok(ordenes).build();
    }
    
    // --- Obtener órdenes por trabajador y fecha ---
@GET
@Path("/trabajador/{idTrabajador}/fecha/{fecha}")
@Produces(MediaType.APPLICATION_JSON)
public Response getOrdenesPorTrabajadorYFecha(
        @PathParam("idTrabajador") int idTrabajador,
        @PathParam("fecha") String fecha) {
    try {
        List<Orden> ordenes = OrdenDAO.listarOrdenesPorTrabajadorYFecha(idTrabajador, fecha);
        return Response.ok(ordenes).build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"No se pudieron obtener las órdenes\"}")
                       .build();
    }
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
    try {
        boolean eliminada = OrdenDAO.eliminarOrden(id);
        if (eliminada) {
            return Response.ok("{\"mensaje\": \"Orden eliminada exitosamente\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"No se pudo eliminar la orden\"}")
                    .build();
        }
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error en el servidor al eliminar la orden\"}")
                .build();
    }
}

    
// --- Pagar una orden ---
@PUT
@Path("/{id}/pagar")
@Produces(MediaType.APPLICATION_JSON)
public Response pagarOrden(@PathParam("id") int idOrden) {
    try {
        boolean exito = OrdenDAO.actualizarEstadoOrden(idOrden, "confirmada"); // o "confirmado"
        if (!exito) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"No se pudo actualizar el estado de la orden\"}").build();
        }

        // *** Generar factura automáticamente si no existe aún ***
        try {
            if (!FacturaDAO.existeFactura(idOrden)) {
                FacturaDetalle detalle = FacturaDAO.obtenerFacturaDetallada(idOrden);
                double total = detalle.getTotalFinal();

                int idFactura = FacturaDAO.crearFactura(idOrden, total);
                if (idFactura > 0) {
                    detalle.setIdFactura(idFactura);
                    // devolver el detalle de la factura en la respuesta
                    return Response.ok(detalle).build();
                } else {
                    // Aunque no se pudo guardar la factura, la orden quedó confirmada.
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                 .entity("{\"error\":\"Orden confirmada pero no se pudo crear la factura\"}")
                                 .build();
                }
            } else {
                // Ya existía factura; retornamos mensaje con código 409
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Ya existe factura para esta orden\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Orden confirmada, error generando factura\"}").build();
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
        boolean exito = OrdenDAO.actualizarEstadoOrden(idOrden, "cancelada"); // 👈 corregido
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
// --- Actualizar estado genérico ---
// --- Actualizar estado genérico ---
@PUT
@Path("/{id}/estado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response actualizarEstado(@PathParam("id") int idOrden, Orden orden) {
    try {
        if (orden.getEstado_nombre() == null || orden.getEstado_nombre().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Debe enviar un estado válido\"}")
                           .build();
        }

        boolean exito = OrdenDAO.actualizarEstadoOrden(idOrden, orden.getEstado_nombre());

        if (exito) {
            return Response.ok("{\"mensaje\":\"Estado actualizado a " + orden.getEstado_nombre() + "\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"No se pudo actualizar el estado\"}")
                           .build();
        }
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"Error en el servidor\"}")
                       .build();
    }
}

}




