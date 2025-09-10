package Controlador;

import DAO.ServicioDAO;
import Modelo.Servicio;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// Define la ruta base para los servicios relacionados con "servicios"
@Path("/servicios")
// Indica que el servicio produce JSON
@Produces(MediaType.APPLICATION_JSON)
// Indica que el servicio consume JSON
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioServicio {
    // Método privado para añadir cabeceras CORS a la respuesta
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS") // Métodos HTTP permitidos
                .header("Access-Control-Allow-Headers", "Content-Type") // Encabezados permitidos
                .header("Access-Control-Allow-Credentials", "true"); // Permite enviar cookies y credenciales
    }

    // Método GET para obtener todos los servicios
    @GET
    public Response obtenerTodos() {
        List<Servicio> servicios = ServicioDAO.obtenerTodos(); // Obtiene la lista de servicios desde DAO
        return addCorsHeaders(Response.ok(servicios)).build(); // Retorna respuesta con la lista y cabeceras CORS
    }

   // Método GET para obtener servicios filtrados por código de tipo de rol
    @GET
    @Path("/rol/{id_roles}") // Ruta con parámetro para el código del tipo de rol
    public Response obtenerPorRol(@PathParam("id_roles") int id_roles) {
        List<Servicio> servicios = ServicioDAO.obtenerPorRol(id_roles); // Obtiene servicios filtrados
        return addCorsHeaders(Response.ok(servicios)).build(); // Retorna respuesta con servicios filtrados y CORS
    }
}
