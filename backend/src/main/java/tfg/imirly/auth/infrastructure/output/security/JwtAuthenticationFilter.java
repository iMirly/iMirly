package tfg.imirly.auth.infrastructure.output.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenAdapter jwtTokenAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Obtener el token de la cabecera "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Quitamos la palabra "Bearer "
        }

        // 2. Si hay token y es válido, autenticamos al usuario
        if (token != null && jwtTokenAdapter.validateToken(token)) {
            String email = jwtTokenAdapter.getEmailFromToken(token);
            String userId = jwtTokenAdapter.getIdFromToken(token); // Sacamos el ID

            // Creamos un objeto de autenticación y lo guardamos en el contexto de Spring (Le pasamos el ID como credencial para usarlo luego)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, userId, new ArrayList<>()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. Continuar con la petición
        filterChain.doFilter(request, response);
    }
}