package cm.sji.hotel_reservation.config;

import jakarta.servlet.Filter; // Import Filter.
import lombok.RequiredArgsConstructor; // Import RequiredArgsConstructor.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain; // Import SecurityFilterChain.


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final Filter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Allow any request for testing purposes
                // Role-based access for admin and project manager paths
                                // Publicly accessible paths (no authentication required)
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                                .requestMatchers("/**", "/logout", "/login", "/css/**", "/js/**", "/images/**", "/api/user/auth", "/scss/**", "/img/**").permitAll()
                                .requestMatchers(org.springframework.http.HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/owner/login").permitAll()
                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .authenticationProvider(authenticationProvider) // Custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class) // JWT filter for API requests
                .build();
    }
}
