package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.FaqDTO;
import cm.sji.hotel_reservation.dtos.QuestionDTO;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.services.FAQService;
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
public class FaqAPI {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FAQService faqService;

    @GetMapping("/api/faqs/{hotelId}")
    public ResponseEntity<List<FaqDTO>> getHotelFaqs(@PathVariable("hotelId") Integer hotelId) {
        try {
            List<FaqDTO> faqs = faqService.getHotelFaqs(hotelId);
            return new ResponseEntity<>(faqs, HttpStatus.OK);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/{hotelId}/faqs/add")
    public ResponseEntity<FaqDTO> addFAQ(@PathVariable("hotelId") Integer hotelId, @RequestBody FaqDTO faqDTO) {
        try {
            logger.info("Received faq data: {}", faqDTO);

//            // Validate required fields
            if (faqDTO == null) {
                logger.warn("faq data is null");
                return new ResponseEntity<>(new FaqDTO(), HttpStatus.BAD_REQUEST);
            }

            String question = (String) faqDTO.getQuestion();
            String answer = (String) faqDTO.getAnswer();

            logger.info("Extracted faq properties: question={}, answer={}}", question, answer);

            FaqDTO newFaq = faqService.saveHotelFaq(faqDTO, hotelId);

            if (newFaq == null) {
                logger.warn("FAQ creation returned null");
                return new ResponseEntity<>(new FaqDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("successfully created faq: {}", newFaq);

            return new ResponseEntity<>(newFaq, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Business logic error adding faq: ", e);
            return new ResponseEntity<>(new FaqDTO(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error("Error adding faq: {}", e.getMessage(), e);
            return new ResponseEntity<>(new FaqDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
