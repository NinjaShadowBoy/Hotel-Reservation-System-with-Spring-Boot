package cm.sji.hotel_reservation.controllers.view;


import cm.sji.hotel_reservation.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
* This controller sends all the thymeleaf pages concerning the client that reserves hotels.
* */
@Controller
public class ClientViewController {

    @GetMapping("/client/home")
    String clientHome() {
        return "admin/index";
    }

    @GetMapping("/")
    String home() {
        return "under-construction";
    }
}
