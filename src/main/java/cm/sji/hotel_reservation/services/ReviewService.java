package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ReviewDTO;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.Review;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.ReviewRepo;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReviewRepo reviewRepo;

    private final UserRepo userRepo;

    private final HotelRepo hotelRepo;

    public List<ReviewDTO> getHotelReviews(int hotelId) {
        // Get the reviews for the particular hotel.
        List<Review> reviews = reviewRepo.findByHotel_Id(hotelId);

        // Convert to dto and return.
        return reviews.stream().map(this::getReviewDTO).toList();
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO, Integer hotelId, Integer clientId) {
        User client = userRepo.findById(clientId).orElseThrow();
        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow();
        Review review = Review.builder()
                .client(client)
                .hotel(hotel)
                .reviewDate(LocalDateTime.now())
                .reviewText(reviewDTO.getText())
                .rating(reviewDTO.getRating())
                .build();

        return getReviewDTO(reviewRepo.save(review));
    }

    // Helper function to get ReviewDTO.
    private ReviewDTO getReviewDTO(Review review) {
        return ReviewDTO.builder()
                .author(
                        review.getClient().getLastName()
                + " " + review.getClient().getFirstName())
                .date(review.getReviewDate())
                .text(review.getReviewText())
                .rating(review.getRating())
                .build();
    }
}
