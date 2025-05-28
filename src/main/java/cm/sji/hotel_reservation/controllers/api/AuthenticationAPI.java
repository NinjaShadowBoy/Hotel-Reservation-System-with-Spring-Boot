package cm.sji.hotel_reservation.controllers.api;


import cm.sji.hotel_reservation.dtos.AuthenticationRequest;
import cm.sji.hotel_reservation.dtos.AuthenticationResponse;
import cm.sji.hotel_reservation.dtos.RegistrationRequest;
import cm.sji.hotel_reservation.dtos.UserDTO;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.AuthenticationService;
import cm.sji.hotel_reservation.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthenticationAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {

        try {
            // Authenticate the request
            AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest, response);
            return ResponseEntity.ok(userService.getUserDTO(authResponse.getUser()));
        } catch (AuthenticationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody RegistrationRequest registrationRequest,
            HttpServletResponse response
    ) {
        try {
            User newUser = User.builder()
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .email(registrationRequest.getEmail())
                    .password(registrationRequest.getPassword())
                    .role(User.UserRole.CLIENT)
                    .active(true)
                    .build();

            newUser = userService.createClient(newUser);

            logger.info("New user created: {}", newUser.toString());

            AuthenticationRequest authReq = AuthenticationRequest.builder()
                    .username(registrationRequest.getEmail())
                    .password(registrationRequest.getPassword())
                    .build();

            // Authenticate the request
            AuthenticationResponse authResponse = authenticationService.authenticate(authReq, response);
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserDTO(authResponse.getUser()));

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
