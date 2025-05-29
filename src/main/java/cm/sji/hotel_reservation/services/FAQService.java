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
        // Get faqs of a particular hotel.
        List<FAQ> faqs = faqRepo.findByHotel_Id(hotelId);

        // Convert to dto and return.
        return faqs.stream().map(this::getFAQDTO).toList();
    }

    public FAQDTO saveFaq(FAQDTO faq, Integer hotelId, Integer clientId) {
        User client = userRepo.findById(clientId).orElseThrow();
        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow();
        FAQ faq1 = FAQ.builder()
                .client(client)
                .hotel(hotel)
                .faqQuestion(faq.getQuestion())
                .faqAnswer("")
                .build();

        return getFAQDTO(faqRepo.save(faq1));
    }

    // Helper functions to get FAQDTO.
    private FAQDTO getFAQDTO(FAQ faq) {
        return FAQDTO.builder()
                .question(faq.getFaqQuestion())
                .answer(faq.getFaqAnswer())
                .build();
    }
}
