package cm.sji.hotel_reservation.controllers.view;

import cm.sji.hotel_reservation.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * This controller sends all the thymeleaf pages concerning the admin that reserves hotels.
 * */
@Controller
public class AdminViewController {

    @GetMapping("/admin/home")
    String clientHome() {
        return "admin/index";
    }
}
