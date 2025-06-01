package cm.sji.hotel_reservation.config;

import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that handles JWT-based authentication for incoming HTTP requests.
 * Extracts the token from either a cookie or the Authorization header,
 * validates it, and sets up the Spring Security context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Performs the filtering logic for each request. Extracts JWT from header or cookies,
     * validates the token, and sets the user authentication in the security context if valid.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException in case of servlet-related issues
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            // If request does not have authorization but has a token, check validity of token and generate authorization.
            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.extractUsername(jwt);

                if (username != null) {
                    User user = userService.findByEmail(username).orElse(null);

                    if (user == null) throw new AssertionError("User not found for token");
                    if (jwtService.isTokenValid(jwt, user)) {
                        // The authorization contains the whole user information.
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (AssertionError | JwtException e) {
            // Redirect to root path when assertion error or JWT-related exceptions occur
            response.sendRedirect("/");
        }
    }

    /**
     * Attempts to extract the JWT from the request. Checks both cookies and the Authorization header.
     *
     * @param request the HTTP request
     * @return the JWT token if found, null otherwise
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // Check cookies first
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Fallback to Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
