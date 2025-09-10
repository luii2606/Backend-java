package Controlador;

import DAO.UsuarioDAO;
import Servicio.UsuarioServicio;
import Modelo.Usuarios;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/usuarios")
public class UsuarioControlador {
    
    private UsuarioServicio usuarioServicio = new UsuarioServicio();

    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Access-Control-Allow-Credentials", "true");
    }

    // Obtener todos los usuarios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        List<Usuarios> usuarios = usuarioServicio.getUsuarios();
        return addCorsHeaders(Response.ok(usuarios)).build();
    }

    // Obtener usuario por ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioById(@PathParam("id") int id) {
        Usuarios usuario = usuarioServicio.getUsuarioPorId(id);
        if (usuario != null) {
            return addCorsHeaders(Response.ok(usuario)).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.NOT_FOUND)).build();
        }
    }


    // Actualizar usuario
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@PathParam("id") int id, Usuarios u) {
        u.setId(id); // <-- corregido
        boolean actualizado = usuarioServicio.actualizarUsuario(id, u);
        if (actualizado) {
            return addCorsHeaders(Response.ok()).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.NOT_FOUND)).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(@PathParam("id") int id) {
        try {
            boolean eliminado = usuarioServicio.eliminarUsuario(id);

            if (eliminado) {
                return Response.ok("{\"mensaje\":\"Usuario eliminado con éxito\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"error\":\"Usuario no encontrado\"}")
                               .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Error al eliminar el usuario\"}")
                           .build();
        }
    }
        @GET
      @Path("/trabajadores")
      @Produces(MediaType.APPLICATION_JSON)
      public Response getTrabajadores() {
          List<Usuarios> trabajadores = usuarioServicio.getTrabajadores();
          return addCorsHeaders(Response.ok(trabajadores)).build();
      }



    // CORS OPTIONS
    @OPTIONS
    @Path("{path : .*}")
    public Response handlePreflight() {
        return addCorsHeaders(Response.ok()).build();
    }
}


