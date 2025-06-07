package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.FAQDTO;
import cm.sji.hotel_reservation.dtos.ReviewDTO;
import cm.sji.hotel_reservation.dtos.RoomTypeDTO;
import cm.sji.hotel_reservation.services.FAQService;
import cm.sji.hotel_reservation.services.RoomTypeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class FAQAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FAQService faqService;

    @GetMapping("/api/faq/{hotelId}")
    public ResponseEntity<List<FAQDTO>> getHotelFaqs(@PathVariable("hotelId") Integer hotelId) {
        try {
            List<FAQDTO> faqs = faqService.getFaqs(hotelId);
            return new ResponseEntity<>(faqs, HttpStatus.OK);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/faq/{hotelId}/{clientId}")
    public ResponseEntity<FAQDTO> leaveReview(@RequestBody FAQDTO faq, @PathVariable Integer clientId, @PathVariable Integer hotelId){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(faqService.saveFaq(faq, hotelId, clientId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }}
