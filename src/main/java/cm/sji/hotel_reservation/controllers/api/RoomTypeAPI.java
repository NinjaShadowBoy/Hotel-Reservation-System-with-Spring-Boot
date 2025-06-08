package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
import cm.sji.hotel_reservation.dtos.RoomTypeDTO;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.services.RoomTypeService;
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

import java.util.*;

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

    @PostMapping("/api/owner/{ownerId}/{hotelId}/roomType/add")
    public ResponseEntity<?> addRoomType(
            @PathVariable Integer ownerId,
            @PathVariable Integer hotelId,
            @RequestBody Map<String, Object> roomTypeData) {
        try {
            logger.info("Received room type data: {}", roomTypeData);

            // Validate required fields
            if (roomTypeData == null || roomTypeData.isEmpty()) {
                logger.warn("Room type data is null or empty");
                return ResponseEntity.badRequest().body("Room type data cannot be empty");
            }

            // Extract and validate data
            String label = (String) roomTypeData.get("room-type");
            Object priceObj = roomTypeData.get("room-price");
            Object totalNumberObj = roomTypeData.get("room-total");
            Object servicesObj = roomTypeData.get("services");

            // Validate required fields
            if (label == null || label.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Room type label is required");
            }
            if (priceObj == null) {
                return ResponseEntity.badRequest().body("Room price is required");
            }
            if (totalNumberObj == null) {
                return ResponseEntity.badRequest().body("Total number of rooms is required");
            }
            // Note: Services can be null or empty - this should be allowed

            // Parse numeric values
            double price;
            int totalNumber;
            try {
                price = Double.parseDouble(priceObj.toString());
                if (price <= 0) {
                    return ResponseEntity.badRequest().body("Price must be greater than 0");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid price format");
            }

            try {
                totalNumber = Integer.parseInt(totalNumberObj.toString());
                if (totalNumber <= 0) {
                    return ResponseEntity.badRequest().body("Total number must be greater than 0");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid total number format");
            }

            // Convert services to Set<String> - allow null/empty services
            Set<String> services = new HashSet<>();
            if (servicesObj != null) {
                try {
                    services = toStringSet(servicesObj);
                } catch (Exception e) {
                    logger.error("Error converting services: {}", e.getMessage());
                    return ResponseEntity.badRequest().body("Invalid services format: " + e.getMessage());
                }
            }

            // Log final parameters before service call
            logger.info("Calling service with: hotelId={}, label='{}', price={}, totalNumber={}, services={}",
                    hotelId, label, price, totalNumber, services);

            // Create room type
            RoomType addedRoomType = roomTypeService.addRoomType(
                    hotelId,
                    label,
                    price,
                    totalNumber,
                    services
            );

            logger.info("Successfully created room type with ID: {}", addedRoomType.getId());

            return new ResponseEntity<>(addedRoomType, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding room type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding room type: " + e.getMessage());
        }
    }

    private Set<String> toStringSet(Object obj) {
        Set<String> result = new HashSet<>();

        if (obj == null) {
            return result;
        }

        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            for (Object item : list) {
                if (item instanceof String) {
                    result.add((String) item);
                } else if (item != null) {
                    result.add(item.toString());
                }
            }
        } else if (obj instanceof Set<?>) {
            Set<?> set = (Set<?>) obj;
            for (Object item : set) {
                if (item instanceof String) {
                    result.add((String) item);
                } else if (item != null) {
                    result.add(item.toString());
                }
            }
        } else if (obj instanceof String) {
            result.add((String) obj);
        } else {
            // Try to convert other types to string
            result.add(obj.toString());
        }

        return result;
    }

    @GetMapping("/api/owner/{ownerId}/{hotelId}/roomtypes")
    public ResponseEntity<List<RoomTypeDTO>> getAllHotelRoomTypes(@PathVariable Integer ownerId, @PathVariable Integer hotelId) {
        try {
            logger.info("Fetching roomtypes for hotel ID {} belonging to owner Id {} ", hotelId, ownerId);
            // Validate owner ID
            if (ownerId == null || ownerId <= 0 || hotelId == null || hotelId <= 0) {
                logger.warn("Invalid owner ID {} or hotel ID {}", ownerId, hotelId);
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
            List<RoomTypeDTO> rooms = roomTypeService.getRoomTypes(hotelId);
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e){
            logger.error("Error loading roomtypes: " + "{}", e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
