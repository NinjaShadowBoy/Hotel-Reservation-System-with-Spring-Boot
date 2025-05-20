package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.RoomTypeDTO;
import cm.sji.hotel_reservation.services.RoomTypeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class RoomTypeAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomTypeService roomTypeService;

    @GetMapping("/api/roomtype/{hotelId}")
    public ResponseEntity<List<RoomTypeDTO>> getRoomType(@PathVariable Integer hotelId) {
        try {
            List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypes(hotelId);
            return new ResponseEntity<>(roomTypes, HttpStatus.OK);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
