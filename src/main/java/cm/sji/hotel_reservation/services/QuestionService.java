package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.QuestionDTO;
import cm.sji.hotel_reservation.entities.Question;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
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
public class QuestionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final QuestionRepo questionRepo;

    private final HotelRepo hotelRepo;

    private final UserRepo userRepo;

    public List<QuestionDTO> getFaqs(Integer hotelId) {
        try {
            logger.info("Retrieving FAQs for hotel ID: {}", hotelId);

            // Get faqs of a particular hotel.
            List<Question> questions = questionRepo.findByHotel_Id(hotelId);

            logger.info("Found {} FAQs for hotel ID: {}", questions.size(), hotelId);

            // Convert to dto and return.
            List<QuestionDTO> faqDTOS = questions.stream().map(this::getFAQDTO).toList();
            return faqDTOS;
        } catch (Exception e) {
            logger.error("Error retrieving FAQs for hotel ID {}: {} - {}", 
                    hotelId, e.getClass().getSimpleName(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public QuestionDTO saveFaq(QuestionDTO faq, Integer hotelId, Integer clientId) {
        try {
            logger.info("Saving new FAQ for hotel ID: {}, client ID: {}", hotelId, clientId);

            User client = userRepo.findById(clientId)
                    .orElseThrow(() -> {
                        logger.error("Client not found with ID: {}", clientId);
                        return new IllegalArgumentException("Client not found");
                    });

            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> {
                        logger.error("Hotel not found with ID: {}", hotelId);
                        return new IllegalArgumentException("Hotel not found");
                    });

            logger.debug("Creating FAQ entity with question: {}", faq.getQuestion());

            Question question1 = Question.builder()
                    .client(client)
                    .hotel(hotel)
                    .faqQuestion(faq.getQuestion())
                    .faqAnswer("")
                    .build();

            Question savedQuestion = questionRepo.save(question1);
            logger.info("Successfully saved FAQ with ID: {} for hotel: {}", savedQuestion.getId(), hotelId);

            return getFAQDTO(savedQuestion);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input when saving FAQ: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving FAQ for hotel ID {}, client ID {}: {} - {}", 
                    hotelId, clientId, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // Helper functions to get FAQDTO.
    private QuestionDTO getFAQDTO(Question question) {
        try {
            logger.debug("Converting FAQ entity to DTO, FAQ ID: {}", question.getId());

            QuestionDTO dto = QuestionDTO.builder()
                    .question(question.getFaqQuestion())
                    .answer(question.getFaqAnswer())
                    .build();

            return dto;
        } catch (Exception e) {
            logger.error("Error converting FAQ entity to DTO: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}
