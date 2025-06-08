package cm.sji.hotel_reservation.controllers.view;

import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class OwnerViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;


    @GetMapping("/owner")
    String ownerLogin() {
        logger.debug("Owner login page accessed");
        return "owner/loginO";
    }


    @GetMapping("/owner/home")
    String ownerHome() {
        return "owner/index";
    }

}
