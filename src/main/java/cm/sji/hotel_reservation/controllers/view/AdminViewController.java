package cm.sji.hotel_reservation.controllers.view;

import cm.sji.hotel_reservation.entities.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * This controller sends all the thymeleaf pages concerning the admin that reserves hotels.
 * */
@Controller
public class AdminViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/admin/home")
    String clientHome() {
        logger.debug("Admin home {}", "fdsf");
        return "admin/index";
    }
}
