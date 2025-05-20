package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.entities.FAQKey;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.FAQRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQService {

    private final FAQRepo faqRepository;

    public FAQ createFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }

    public Optional<FAQ> findById(User client, Hotel hotel) {
        FAQKey id = new FAQKey(client.getId(), hotel.getId());
        return faqRepository.findById(id);
    }

    public List<FAQ> findByHotelId(Long hotelId) {
        return faqRepository.findByHotelId(hotelId);
    }

    public List<FAQ> findAnsweredFAQsByHotelId(Long hotelId) {
        return faqRepository.findAnsweredFAQsByHotelId(hotelId);
    }

    public List<FAQ> findUnansweredFAQsByHotelId(Long hotelId) {
        return faqRepository.findUnansweredFAQsByHotelId(hotelId);
    }

    public List<FAQ> findByClientId(Long clientId) {
        return faqRepository.findByClientId(clientId);
    }

    public List<FAQ> findAll() {
        return faqRepository.findAll();
    }

    public FAQ updateFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }

    public FAQ answerFAQ(User client, Hotel hotel, String answer) {
        FAQKey id = new FAQKey(client.getId(), hotel.getId());
        Optional<FAQ> optionalFAQ = faqRepository.findById(id);

        if (optionalFAQ.isPresent()) {
            FAQ faq = optionalFAQ.get();
            faq.setFaqAnswer(answer);
            return faqRepository.save(faq);
        }
        throw new IllegalArgumentException(
                "FAQ not found for client ID: " + client.getId() +
                        " and hotel ID: " + hotel.getId()
        );
    }


    public void deleteFAQ(User client, Hotel hotel) {
        FAQKey id = new FAQKey(client.getId(), hotel.getId());
        faqRepository.deleteById(id);
    }
}
