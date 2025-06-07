package cm.sji.hotel_reservation.controllers.api;


import cm.sji.hotel_reservation.dtos.ReviewDTO;
import cm.sji.hotel_reservation.services.ReviewService;
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
public class ReviewAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReviewService reviewService;

    @GetMapping("/api/reviews/{hotelId}")
    public ResponseEntity<List<ReviewDTO>> getHotelReviews(@PathVariable("hotelId") Integer hotelId) {
        try{
            List<ReviewDTO> reviews =  reviewService.getHotelReviews(hotelId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/reviews/{hotelId}/{clientId}")
    public ResponseEntity<ReviewDTO> leaveReview(@RequestBody ReviewDTO review, @PathVariable Integer clientId, @PathVariable Integer hotelId){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.saveReview(review, hotelId, clientId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
