package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.entities.HotelPhoto;
import cm.sji.hotel_reservation.entities.RoomPhoto;
import cm.sji.hotel_reservation.services.PhotoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class PhotoAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PhotoService photoService;

    @PostMapping("/roomphotos/upload/{roomTypeId}")
    public ResponseEntity<?> uploadRoomPhoto(@RequestParam("photo") MultipartFile file, @PathVariable("roomTypeId") Integer roomTypeId, RedirectAttributes redirectAttributes) throws IOException {
        try {
            RoomPhoto photo = photoService.saveRoomPhoto(file, roomTypeId);
            return ResponseEntity.ok().body(photo);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/hotelphotos/upload/{hotelId}")
    public ResponseEntity<?> uploadHotelPhoto(@RequestParam("photo") MultipartFile file, @PathVariable("hotelId") Integer hotelId, RedirectAttributes redirectAttributes) throws IOException {
        try {
            HotelPhoto photo = photoService.saveHotelPhoto(file, hotelId);
            return ResponseEntity.ok().body(photo);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
