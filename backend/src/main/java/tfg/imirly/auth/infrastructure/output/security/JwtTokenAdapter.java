package tfg.imirly.auth.infrastructure.output.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import tfg.imirly.auth.domain.model.User;
import tfg.imirly.auth.domain.port.out.TokenProviderPort;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenAdapter implements TokenProviderPort {

    // Llave secreta para firmar el token (En producción esto va en el application.properties o .env)
    // Usamos una clave de 256 bits generada aleatoriamente por la librería para este ejemplo
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Tiempo de expiración del token (Ej: 24 horas)
    private final long EXPIRATION_TIME = 86400000;

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("tipo", user.getTipo().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extrae el Email del token
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Extrae el ID del token que guardamos antes
    public String getIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody().get("id", String.class);
    }

    // Comprueba si el token es válido y no ha caducado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token falso, caducado o mal formado
        }
    }
}