package cm.sji.hotel_reservation.controllers.api;


import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.services.HotelService;
import cm.sji.hotel_reservation.services.RoomServiceService;
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
public class HotelAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HotelService hotelService;

    private final RoomServiceService roomServiceService;

    @GetMapping("/api/hotels")
    public ResponseEntity<List<HotelDetailsDTO>> getAllHotels() {
        try {
            List<HotelDetailsDTO> hotels = hotelService.getAllHotels();
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/hotels/{hotelId}")
    public ResponseEntity<HotelDetailsDTO> getHotel(@PathVariable Integer hotelId) {
        try {
            HotelDetailsDTO hotels = hotelService.getHotel(hotelId);
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new HotelDetailsDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
