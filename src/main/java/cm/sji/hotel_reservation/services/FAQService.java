package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.FaqDTO;
import cm.sji.hotel_reservation.dtos.QuestionDTO;
import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.entities.Question;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.FAQRepo;
import cm.sji.hotel_reservation.repositories.QuestionRepo;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FAQService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FAQRepo faqRepo;

    private final HotelRepo hotelRepo;

    private final UserRepo userRepo;

    public List<FaqDTO> getHotelFaqs(Integer hotelId) {
        try {
            logger.info("Retrieving FAQs for hotel ID: {}", hotelId);

            // Get faqs of a particular hotel.
            List<FAQ> faqs = faqRepo.findByHotel_Id(hotelId);

            logger.info("Found {} FAQs for hotel ID: {}", faqs.size(), hotelId);

            // Convert to dto and return.
            return faqs.stream().map(this::getHotelFAQDTO).toList();
        } catch (Exception e) {
            logger.error("Error retrieving FAQs for hotel ID {}: {} - {}",
                    hotelId, e.getClass().getSimpleName(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public FaqDTO saveHotelFaq(FaqDTO faq, Integer hotelId) {
        try {
            logger.info("Saving new FAQ for hotel ID: {}", hotelId);

            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> {
                        logger.error("Hotel not found with ID: {}", hotelId);
                        return new IllegalArgumentException("Hotel not found");
                    });

            logger.debug("Creating FAQ entity with question: {}", faq.getQuestion());

            FAQ faq1 = FAQ.builder()
                    .hotel(hotel)
                    .faqQuestion(faq.getQuestion())
                    .faqAnswer(faq.getAnswer())
                    .build();

            FAQ savedFaq = faqRepo.save(faq1);
            logger.info("Successfully saved FAQ with ID: {} for hotel: {}", savedFaq.getId(), hotelId);

            return getHotelFAQDTO(savedFaq);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input when saving FAQ: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving FAQ for hotel ID {}: {} - {}",
                    hotelId, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // Helper functions to get Faqdto.
    private FaqDTO getHotelFAQDTO(FAQ faq) {
        try {
            logger.debug("Converting FAQ entity to DTO, FAQ ID: {}", faq.getId());

            return FaqDTO.builder()
                    .question(faq.getFaqQuestion())
                    .answer(faq.getFaqAnswer())
                    .build();
        } catch (Exception e) {
            logger.error("Error converting FAQ entity to DTO: {} - {}",
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}
