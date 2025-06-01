package cm.sji.hotel_reservation.controllers.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * This controller sends all the thymeleaf pages concerning the error pages.
 * */
@Controller
public class ErrorViewController {

    @RequestMapping("/error")
    String error() {
        return "error";
    }
}
