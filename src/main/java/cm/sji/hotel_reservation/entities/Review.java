package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(FAQKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", insertable = false, updatable = false)
    Hotel hotel;

    String reviewText = "";
    LocalDateTime reviewDate;
    Float rating = 0.0f;
}
