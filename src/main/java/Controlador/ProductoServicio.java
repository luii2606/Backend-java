package Controlador;

import DAO.ProductoDAO;
import Modelo.Producto;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Servicio REST para manejar operaciones de productos.
 */
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoServicio {

    // 🔹 Método privado para añadir cabeceras CORS
    private Response.ResponseBuilder addCorsHeaders(Response.ResponseBuilder response) {
        return response
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Access-Control-Allow-Credentials", "true");
    }

    // 🔹 GET: Obtener todos los productos
    @GET
    public Response getProductos() {
        List<Producto> lista = ProductoDAO.listar();
        return addCorsHeaders(Response.ok(lista)).build();
    }

    // 🔹 GET: Obtener producto por ID
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        Producto producto = ProductoDAO.obtenerPorId(id);
        if (producto != null) {
            return addCorsHeaders(Response.ok(producto)).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensaje\":\"Producto no encontrado\"}")).build();
        }
    }

    // 🔹 POST: Registrar nuevo producto
    @POST
    public Response registrarProducto(Producto producto) {
        boolean exito = ProductoDAO.insertar(producto);
        if (exito) {
            return addCorsHeaders(Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Producto registrado correctamente\"}")).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error al registrar producto\"}")).build();
        }
    }

    // 🔹 PUT: Actualizar producto
    @PUT
    @Path("/{id}")
    public Response actualizarProducto(@PathParam("id") int id, Producto producto) {
        producto.setId(id); // aseguramos que el id venga de la URL
        boolean exito = ProductoDAO.actualizar(producto);
        if (exito) {
            return addCorsHeaders(Response.ok("{\"mensaje\":\"Producto actualizado correctamente\"}")).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error al actualizar producto\"}")).build();
        }
    }

    // 🔹 DELETE: Eliminar producto
    @DELETE
    @Path("/{id}")
    public Response eliminarProducto(@PathParam("id") int id) {
        boolean exito = ProductoDAO.eliminar(id);
        if (exito) {
            return addCorsHeaders(Response.ok("{\"mensaje\":\"Producto eliminado correctamente\"}")).build();
        } else {
            return addCorsHeaders(Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error al eliminar producto\"}")).build();
        }
    }

    // 🔹 OPTIONS: Responder preflight CORS
    @OPTIONS
    public Response options() {
        return addCorsHeaders(Response.ok()).build();
    }
}



