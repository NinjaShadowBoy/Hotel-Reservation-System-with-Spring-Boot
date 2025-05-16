package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@IdClass(FAQKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAQ {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", insertable = false, updatable = false)
    Hotel hotel;

    String faqQuestion;
    String faqAnswer;

}
