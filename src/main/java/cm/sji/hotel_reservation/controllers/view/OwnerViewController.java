package cm.sji.hotel_reservation.controllers.view;

import org.springframework.web.bind.annotation.GetMapping;

public class OwnerViewController {

    @GetMapping("/owner/home")
    String ownerHome() {
        return "owner/index";
    }

}
