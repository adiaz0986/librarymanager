package com.prueba.ezertech.librarymanager.security.jwt;

import com.prueba.ezertech.librarymanager.user.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(TokenJwtConfig.HEADER);

        if (authHeader != null && authHeader.startsWith(TokenJwtConfig.PREFIX)) {

            String token = authHeader.substring(TokenJwtConfig.PREFIX.length()).trim();

            if (!token.isEmpty()) {
                try {
                    String username = jwtUtil.extractUsername(token);

                    if (username != null &&
                            SecurityContextHolder.getContext().getAuthentication() == null) {

                        var userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtUtil.validateToken(token)) {
                            var authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                            authToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                } catch (io.jsonwebtoken.MalformedJwtException e) {
                    // Token mal formado
                    logger.warn("JWT mal formado: " + e.getMessage());
                } catch (io.jsonwebtoken.ExpiredJwtException e) {
                    // Token expirado
                    logger.warn("JWT expirado: " + e.getMessage());
                } catch (Exception e) {
                    // Otros errores
                    logger.error("Error procesando JWT: " + e.getMessage());
                }
            } else {
                logger.warn("JWT vacío en el encabezado Authorization");
            }
        }

        // Continuar con el chain
        filterChain.doFilter(request, response);
    }
}
