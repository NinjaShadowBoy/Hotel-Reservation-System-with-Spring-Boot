package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.QuestionDTO;
import cm.sji.hotel_reservation.services.QuestionService;
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
public class QuestionAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final QuestionService questionService;

    @GetMapping("/api/question/{hotelId}")
    public ResponseEntity<List<QuestionDTO>> getHotelFaqs(@PathVariable("hotelId") Integer hotelId) {
        try {
            List<QuestionDTO> faqs = questionService.getFaqs(hotelId);
            return new ResponseEntity<>(faqs, HttpStatus.OK);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/question/{hotelId}/{clientId}")
    public ResponseEntity<QuestionDTO> leaveReview(@RequestBody QuestionDTO faq, @PathVariable Integer clientId, @PathVariable Integer hotelId){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(questionService.saveFaq(faq, hotelId, clientId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }}
