package cm.sji.hotel_reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return "Welcome to Hotel Reservation. Under construction 🏗";
    }
}
