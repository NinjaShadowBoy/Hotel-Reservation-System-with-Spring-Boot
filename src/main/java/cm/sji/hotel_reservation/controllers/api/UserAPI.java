package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.AuthenticationRequest;
import cm.sji.hotel_reservation.dtos.AuthenticationResponse;
import cm.sji.hotel_reservation.dtos.UserDTO;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.AuthenticationService;
import cm.sji.hotel_reservation.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;


@RestController
@AllArgsConstructor
public class UserAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/api/owner/login")
    public ResponseEntity<UserDTO> login(@RequestBody Map<String, String> userData, HttpServletResponse response) {
        String email = userData.get("email");
        String password = userData.get("password");

        logger.info("Attempting to log in user with email: {}", email);
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest, response);

            return ResponseEntity.ok().body(userService.getUserDTO(authenticationResponse.getUser()));
        }else{
            logger.warn("User with email {} not found", email);
            return ResponseEntity.status(404).body(null);
        }
    }


}
