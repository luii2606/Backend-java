    package Controlador;

    import Servicio.RolServicio;
    import Modelo.Rol;
    import java.util.List;
    import javax.ws.rs.*;
    import javax.ws.rs.core.MediaType;
    import javax.ws.rs.core.Response;



    @Path("/roles")
    public class RolControlador {

            private RolServicio rolServicio = new RolServicio();

                private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
            return response
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type")
                    .header("Access-Control-Allow-Credentials", "true");
        }

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getUsuarios() {
            List<Rol> roles = rolServicio.getRoles();
            return addCorsHeaders(Response.ok(roles)).build();
        }
    }
