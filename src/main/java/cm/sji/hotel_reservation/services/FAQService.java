package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.FAQDTO;
import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.Review;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.FAQRepo;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.ReviewRepo;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FAQService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FAQRepo faqRepo;

    private final HotelRepo hotelRepo;

    private final UserRepo userRepo;

    public List<FAQDTO> getFaqs(Integer hotelId) {
        try {
            logger.info("Retrieving FAQs for hotel ID: {}", hotelId);

            // Get faqs of a particular hotel.
            List<FAQ> faqs = faqRepo.findByHotel_Id(hotelId);

            logger.info("Found {} FAQs for hotel ID: {}", faqs.size(), hotelId);

            // Convert to dto and return.
            List<FAQDTO> faqDTOs = faqs.stream().map(this::getFAQDTO).toList();
            return faqDTOs;
        } catch (Exception e) {
            logger.error("Error retrieving FAQs for hotel ID {}: {} - {}", 
                    hotelId, e.getClass().getSimpleName(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public FAQDTO saveFaq(FAQDTO faq, Integer hotelId, Integer clientId) {
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

            FAQ faq1 = FAQ.builder()
                    .client(client)
                    .hotel(hotel)
                    .faqQuestion(faq.getQuestion())
                    .faqAnswer("")
                    .build();

            FAQ savedFaq = faqRepo.save(faq1);
            logger.info("Successfully saved FAQ with ID: {} for hotel: {}", savedFaq.getId(), hotelId);

            return getFAQDTO(savedFaq);
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
    private FAQDTO getFAQDTO(FAQ faq) {
        try {
            logger.debug("Converting FAQ entity to DTO, FAQ ID: {}", faq.getId());

            FAQDTO dto = FAQDTO.builder()
                    .question(faq.getFaqQuestion())
                    .answer(faq.getFaqAnswer())
                    .build();

            return dto;
        } catch (Exception e) {
            logger.error("Error converting FAQ entity to DTO: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}
