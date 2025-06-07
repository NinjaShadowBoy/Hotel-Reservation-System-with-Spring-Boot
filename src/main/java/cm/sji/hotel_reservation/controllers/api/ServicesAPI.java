package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.services.RoomServiceService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ServicesAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomServiceService roomServiceService;

    @GetMapping("/api/services")
    public ResponseEntity<Set<String>> services() {
        try {
            return new ResponseEntity<>(roomServiceService.allServices(), HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new HashSet<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
