package cm.sji.hotel_reservation.controllers.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OwnerViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/owner/home")
    String ownerHome() {
        return "owner/index";
    }

}
