package Controlador;

import DAO.FacturaDAO;
import Modelo.FacturaDetalle;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/facturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacturaServicio {
    
    // 👉 Método para añadir cabeceras CORS
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                //.header("Access-Control-Allow-Origin", "http://localhost:5173") // puedes activarlo si quieres restringir
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Credentials", "true");
    }

    // ✅ Consultar factura detallada de una orden
    @GET
    @Path("/orden/{idOrden}")
    public Response getFacturaPorOrden(@PathParam("idOrden") int idOrden) {
        try {
            if (!FacturaDAO.existeFactura(idOrden)) {
                return addCorsHeaders(Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No existe factura para esta orden\"}"))
                        .build();
            }

            FacturaDetalle detalle = FacturaDAO.obtenerFacturaDetallada(idOrden);
            return addCorsHeaders(Response.ok(detalle)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return addCorsHeaders(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener la factura\"}"))
                    .build();
        }
    }

    // ✅ Crear factura (cuando la orden pasa a CONFIRMADA)
    @POST
    @Path("/crear/{idOrden}")
    public Response crearFactura(@PathParam("idOrden") int idOrden) {
        try {
            if (FacturaDAO.existeFactura(idOrden)) {
                return addCorsHeaders(Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"La factura ya existe para esta orden\"}"))
                        .build();
            }

            // Obtenemos detalle de la orden para calcular total
            FacturaDetalle detalle = FacturaDAO.obtenerFacturaDetallada(idOrden);
            int idFactura = FacturaDAO.crearFactura(idOrden, detalle.getTotalFinal());

            if (idFactura > 0) {
                return addCorsHeaders(Response.ok("{\"mensaje\":\"Factura creada\",\"idFactura\":" + idFactura + "}"))
                        .build();
            } else {
                return addCorsHeaders(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"No se pudo crear la factura\"}"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return addCorsHeaders(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al crear la factura\"}"))
                    .build();
        }
    }
    
    @GET
@Path("/fecha/{fecha}")
@Produces(MediaType.APPLICATION_JSON)
public Response getFacturasPorFecha(@PathParam("fecha") String fecha) {
    List<FacturaDetalle> facturas = FacturaDAO.listarFacturasPorFecha(fecha);
    return Response.ok(facturas).build();
}


    // ✅ OPTIONS para preflight CORS
    @OPTIONS
    public Response options() {
        return addCorsHeaders(Response.ok()).build();
    }
}


