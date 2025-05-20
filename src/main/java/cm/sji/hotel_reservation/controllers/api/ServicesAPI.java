package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.services.RoomServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ServicesAPI {

    private final RoomServiceService roomServiceService;

    @GetMapping("/api/services")
    public ResponseEntity<List<String>> services() {
        try {
            var services = roomServiceService.getAllServices();

            List<String> result = services.stream().map(service ->
                            service.getLabel()+":"+service.getFontawsome_icon_class())
                    .toList();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
