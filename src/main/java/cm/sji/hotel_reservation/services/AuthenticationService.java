package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.config.JwtService;
import cm.sji.hotel_reservation.dtos.AuthenticationRequest;
import cm.sji.hotel_reservation.dtos.AuthenticationResponse;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepo repo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response)
            throws AuthenticationException {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Determine if input is email or username and fetch user
        User user = userService.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If authentication succeeds generate jwt token
        // Do not include the password in the payload
        user.setPassword(null);
        var jwtToken = jwtService.generateToken(user);

        // Set the token in HttpOnly cookie
        Cookie cookie = new Cookie("JWT", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // Cookie expires in 30 minutes

        response.addCookie(cookie);

        // Set the token in Authorization header
        response.addHeader("Authorization", "Bearer " + jwtToken);

        return AuthenticationResponse
                .builder()
                .user(user)
                .token(jwtToken)
                .build();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        }
        return authentication.getName();
    }
}
