package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ReviewDTO;
import cm.sji.hotel_reservation.entities.Review;
import cm.sji.hotel_reservation.repositories.ReviewRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReviewRepo reviewRepo;

    public List<ReviewDTO> getHotelReviews(int hotelId) {
        // Get the reviews for the particular hotel.
        List<Review> reviews = reviewRepo.findByHotel_Id(hotelId);

        // Convert to dto and return.
        return reviews.stream().map(this::getReviewDTO).toList();
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
