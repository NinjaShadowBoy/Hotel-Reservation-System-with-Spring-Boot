package cm.sji.hotel_reservation.controllers.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
* This controller sends all the thymeleaf pages concerning the client that reserves hotels.
* */
@Controller
public class ClientViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/client/home")
    String clientHome() {
        return "client/index";
    }
}
