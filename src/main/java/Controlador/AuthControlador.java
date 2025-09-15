package Controlador;

import Servicio.AuthServicio;
import Modelo.Usuarios;
import Seguridad.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/**
 * Servlet encargado de la autenticación de usuarios.
 * Endpoints disponibles:
 * - /api/auth/login
 * - /api/auth/refresh
 * - /api/auth/register
 */
@WebServlet("/api/auth/*")
public class AuthControlador extends HttpServlet {

    private final AuthServicio authServicio = new AuthServicio();

    // 👉 Método para añadir headers CORS
    private void addCorsHeaders(HttpServletResponse resp) {
        // resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // tu frontend
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String path = req.getPathInfo(); // ejemplo: /login, /refresh, /register
    addCorsHeaders(resp);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");

    if (path == null) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
        return;
    }

    switch (path) {
        case "/login":
            login(req, resp);
            break;
        case "/refresh":
            refresh(req, resp);
            break;
        case "/register":
            register(req, resp);
            break;
        case "/register-trabajador":
            registerTrabajador(req, resp);
            break;
        default:
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
    }
}

    // 🔹 GET: validate, me
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        addCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();

        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        switch (path) {
            case "/validate":
                validate(req, resp);
                break;
            case "/me":
                me(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
        }
    }

    /**
     * Login de usuario: recibe JSON {correo, contrasena}, devuelve tokens
     */
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        Map<String, String> body = new Gson().fromJson(reader, Map.class);

        String correo = body.get("correo");
        String contrasena = body.get("contrasena");

        Map<String, Object> tokens = authServicio.login(correo, contrasena);

        if (tokens == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Credenciales inválidas\"}");
        } else {
            resp.getWriter().write(new Gson().toJson(tokens));
        }
    }

    /**
     * Refresh token: recibe JSON {refresh_token}
     */
    private void refresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        Map<String, String> body = new Gson().fromJson(reader, Map.class);

        String refreshToken = body.get("refresh_token");

        String newAccessToken = authServicio.refresh(refreshToken);

        if (newAccessToken == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Refresh token inválido\"}");
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("access_token", newAccessToken);
            resp.getWriter().write(new Gson().toJson(response));
        }
    }

    /**
     * Registro de un nuevo usuario: recibe JSON con datos del usuario
     */
    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Usuarios nuevoUsuario = mapper.readValue(req.getInputStream(), Usuarios.class);


        // valores por defecto
        if (nuevoUsuario.getId_roles() == 0) {
            nuevoUsuario.setId_roles(2); // cliente
        }
        if (nuevoUsuario.getId_estado_usuarios() == 0) {
            nuevoUsuario.setId_estado_usuarios(1); // disponible
        }
            // Verificar si ya existe el correo ANTES de registrar
        if (authServicio.existeCorreo(nuevoUsuario.getCorreo())) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409 = Conflicto
            resp.getWriter().write("{\"error\":\"El correo ya está registrado\"}");
            return;
        }

        boolean creado = authServicio.registrarUsuario(nuevoUsuario);

        if (creado) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Usuario registrado con éxito\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"No se pudo registrar el usuario\"}");
        }
    }
    
    

    // 🔑 validar access token
    private void validate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Falta token de autorización\"}");
            return;
        }

        String token = authHeader.substring(7);

        if (authServicio.esAccessTokenValido(token)) {
            resp.getWriter().write("{\"valid\":true}");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"valid\":false}");
        }
    }

    // 🔑 devolver información del usuario autenticado
    private void me(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Falta token de autorización\"}");
            return;
        }

        String token = authHeader.substring(7);
        Usuarios usuario = authServicio.obtenerUsuarioDesdeToken(token);

        if (usuario == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
            return;
        }

        try {
            List<String> permisos = JWTUtil.obtenerPermisos(token);

            Map<String, Object> data = new HashMap<>();
            data.put("id", usuario.getId());
            data.put("nombre", usuario.getNombre());
            data.put("correo", usuario.getCorreo());
            data.put("id_roles", usuario.getId_roles());
            data.put("permisos", permisos);

            String json = new Gson().toJson(data);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
        }
    }
    /**
 * Registro de un nuevo trabajador: recibe JSON con datos del trabajador
 */
private void registerTrabajador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    BufferedReader reader = req.getReader();
    Usuarios nuevoTrabajador = new Gson().fromJson(reader, Usuarios.class);

    // valores por defecto
if (nuevoTrabajador.getId_estado_usuarios() == 0) {
    nuevoTrabajador.setId_estado_usuarios(1); // disponible
}
if (nuevoTrabajador.getId_roles() == 0) {
    nuevoTrabajador.setId_roles(3); // trabajador
}


    boolean creado = authServicio.RegistrarTrabajador(nuevoTrabajador);

    if (creado) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("{\"message\":\"Trabajador registrado con éxito\"}");
    } else {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\":\"No se pudo registrar el trabajador\"}");
    }
}

    
}


