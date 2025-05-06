package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Data
@Entity
@IdClass(FAQKey.class)
public class Review {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_clientID", insertable = false, updatable = false)
    User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_hotelID", insertable = false, updatable = false)
    Hotel hotel;

    String reviewText;
    LocalDate reviewDate;
    int rating;

    public Review() {}

    public Review(User client, Hotel hotel, String reviewText, LocalDate reviewDate, int rating) {
        this.client = client;
        this.hotel = hotel;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.rating = rating;
    }

}
