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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/owner/{ownerId}/hotels")
    public ResponseEntity<List<HotelDetailsDTO>> getAllOwnerHotels(@PathVariable Integer ownerId) {
        try {
            logger.info("Fetching hotels for owner ID: {}", ownerId);
            // Validate owner ID
            if (ownerId == null || ownerId <= 0) {
                logger.warn("Invalid owner ID: {}", ownerId);
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
            List<HotelDetailsDTO> hotels = hotelService.getHotelsByOwnerId(ownerId);
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (Exception e){
            logger.error("Error loading hotels: " + "{}", e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/hotels/add")
    public ResponseEntity<Hotel> addHotel(@RequestBody Map<String, Object> hotelData) {
        try {
            logger.info("Received hotel data: {}", hotelData);

//            // Validate required fields
            if (hotelData == null || hotelData.isEmpty()) {
                logger.warn("Hotel data is null or empty");
                return new ResponseEntity<>(new Hotel(), HttpStatus.BAD_REQUEST);
            }

            Integer id = Integer.parseInt(hotelData.get("id").toString());
            String name = (String) hotelData.get("name");
            String location = (String) hotelData.get("location");
            String description = (String) hotelData.get("description");
            Float rating = Float.parseFloat(hotelData.get("rating").toString());

            logger.info("Extracted hotel properties: name={}, location={}, description={}, rating={}", name, location, description, rating);

            Hotel newHotel = hotelService.addHotel(id, name, location, description, rating);

            if (newHotel == null) {
                logger.warn("Hotel creation returned null");
                return new ResponseEntity<>(new Hotel(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("successfully created hotel: {}", newHotel);

            return new ResponseEntity<>(newHotel, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Business logic error adding hotel: ", e);
            return new ResponseEntity<>(new Hotel(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error("Error adding hotel: {}", e.getMessage(), e);
            return new ResponseEntity<>(new Hotel(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/hotels/{hotelId}")
    public ResponseEntity<HotelDetailsDTO> getHotel(@PathVariable Integer hotelId) {
        try {
            // Validate hotel ID
            if (hotelId == null || hotelId <= 0) {
                logger.warn("Invalid hotel ID: {}", hotelId);
                return new ResponseEntity<>(new HotelDetailsDTO(), HttpStatus.BAD_REQUEST);
            }

            HotelDetailsDTO hotels = hotelService.getHotel(hotelId);
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Hotel not found with ID {}: ", hotelId, e);
            return new ResponseEntity<>(new HotelDetailsDTO(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new HotelDetailsDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
