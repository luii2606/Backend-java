package Seguridad;

import Modelo.Usuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;

import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Collections;

public class JWTUtil {

    private static final String SECRET = "c4mb14_5up3r_53cr3t4_debes_cambiarla_y_mas_larga";
    private static final String ISSUER = "mi-backend";

    // Expiración
    private static final long EXP_MILLIS_ACCESS = 2 * 60 * 60 * 1000; // 2 horas
    private static final long EXP_MILLIS_REFRESH = 7 * 24 * 60 * 60 * 1000; // 7 días

    // Genera Access Token incluyendo la lista de permisos
    public static String generarAccessToken(Usuarios u, List<String> permisos) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(u.getCorreo())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXP_MILLIS_ACCESS))
                .claim("id", u.getId())
                .claim("correo", u.getCorreo())
                .claim("id_tipo_usuario", u.getId_tipo_usuario())
                .claim("permisos", permisos)         // <- aquí van los permisos
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // Genera Refresh Token (sin permisos, solo id)
    public static String generarRefreshToken(Usuarios u) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(u.getCorreo())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXP_MILLIS_REFRESH))
                .claim("id", u.getId())
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // Valida token y devuelve claims (lanza JwtException si inválido)
    public static Claims validarYObtenerClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean esTokenValido(String token) {
        try {
            validarYObtenerClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // Helpers para extraer datos
    public static String obtenerCorreo(String token) {
        return validarYObtenerClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> obtenerPermisos(String token) {
        try {
            Claims c = validarYObtenerClaims(token);
            Object o = c.get("permisos");
            if (o instanceof List) {
                return (List<String>) o;
            }
            return Collections.emptyList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}