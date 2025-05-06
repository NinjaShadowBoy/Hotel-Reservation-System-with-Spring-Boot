package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Data
@Entity
public class Review {

    @EmbeddedId
    private FAQKey id;

    @ManyToOne
    @JoinColumn(name = "fk_clientID", insertable = false, updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_hotelID", insertable = false, updatable = false)
    Hotel hotel;

    String reviewText;
    LocalDate reviewDate;
    int rating;

    public Review() {}

    public Review(FAQKey id, User client, Hotel hotel, String reviewText, LocalDate reviewDate, int rating) {
        this.id = id;
        this.client = client;
        this.hotel = hotel;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.rating = rating;
    }

}
