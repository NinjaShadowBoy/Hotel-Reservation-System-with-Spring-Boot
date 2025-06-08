package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", updatable = false)
    Hotel hotel;

    String reviewText = "";
    LocalDateTime reviewDate;
    Float rating = 0.0f;
}
