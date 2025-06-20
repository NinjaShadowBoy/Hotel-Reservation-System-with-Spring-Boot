package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", updatable = false)
    Hotel hotel;

    String faqQuestion = "";
    String faqAnswer = "";

}
