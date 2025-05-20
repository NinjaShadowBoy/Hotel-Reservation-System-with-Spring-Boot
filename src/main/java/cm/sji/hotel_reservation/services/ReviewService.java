package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.FAQKey;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.Review;
import cm.sji.hotel_reservation.repositories.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cm.sji.hotel_reservation.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepository;

    public Review createReview(Review review) {
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Optional<Review> findById(User client, Hotel hotel) {
        return reviewRepository.findById(new FAQKey(client.getId(), hotel.getId()));
    }

    public List<Review> findByHotelId(Integer hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    public List<Review> findByClientId(Integer clientId) {
        return reviewRepository.findByClientId(clientId);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(User client, Hotel hotel) {
        reviewRepository.deleteById(new FAQKey(client.getId(), hotel.getId()));
    }

    public Double getAverageRatingForHotel(Integer hotelId) {
        return reviewRepository.calculateAverageRatingForHotel(hotelId);
    }
}