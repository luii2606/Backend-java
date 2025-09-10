
package Controlador;

import DAO.EstadoProductoDAO;
import Modelo.EstadoProducto;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/estado_productos")
public class EstadoProductoControlador {

    // 👉 Método para añadir cabeceras CORS
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                //.header("Access-Control-Allow-Origin", "http://localhost:5173") 
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Credentials", "true");
    }

    // ✅ Obtener todos los estados de producto
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstados() {
        List<EstadoProducto> lista = EstadoProductoDAO.getEstados();
        return addCorsHeaders(Response.ok(lista)).build();
    }

    // ✅ Responder preflight (OPTIONS)
    @OPTIONS
    public Response options() {
        return addCorsHeaders(Response.ok()).build();
    }
}

