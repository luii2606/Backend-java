package Controlador;

import DAO.TipoModalidadDAO;
import Modelo.TipoModalidad;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// Ruta base del servicio REST para modalidades
@Path("/modalidades")
// Define que el servicio produce y consume JSON
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModalidadServicio {

    // Método auxiliar para agregar cabeceras CORS a la respuesta
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS") // Métodos permitidos
                .header("Access-Control-Allow-Headers", "Content-Type") // Headers permitidos
                .header("Access-Control-Allow-Credentials", "true"); // Permitir credenciales (cookies)
    }

    // Maneja la solicitud GET a /modalidades para obtener todas las modalidades
    @GET
    public Response obtenerTodos() {
        // Llama al DAO para obtener todas las modalidades desde la base de datos
        List<TipoModalidad> modalidades = TipoModalidadDAO.obtenerTodas();
        // Construye la respuesta con la lista y añade cabeceras CORS
        return addCorsHeaders(Response.ok(modalidades)).build();
    }
    
    // Maneja la solicitud GET a /modalidades/{id} para obtener una modalidad por su ID
    @GET
    @Path("/{id}")
    public Response obtenerModalidadPorId(@PathParam("id") int id) {
        // Llama al DAO para obtener la modalidad específica por ID
        TipoModalidad modalidad = TipoModalidadDAO.obtenerPorId(id);

        // Si la modalidad existe, devuelve estado 200 con el objeto modalidad
        if (modalidad != null) {
            return addCorsHeaders(Response.ok(modalidad)).build();
        } else {
            // Si no se encontró, devuelve estado 404 Not Found
            return addCorsHeaders(Response.status(Response.Status.NOT_FOUND)).build();
        }
    }
}


