package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.FAQDTO;
import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.repositories.FAQRepo;
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

    public List<FAQDTO> getFaqs(Integer hotelId) {
        // Get faqs of a particular hotel.
        List<FAQ> faqs = faqRepo.findByHotel_Id(hotelId);

        // Convert to dto and return.
        return faqs.stream().map(this::getFAQDTO).toList();
    }

    // Helper functions to get FAQDTO.
    private FAQDTO getFAQDTO(FAQ faq) {
        return FAQDTO.builder()
                .question(faq.getFaqQuestion())
                .answer(faq.getFaqAnswer())
                .build();
    }
}
