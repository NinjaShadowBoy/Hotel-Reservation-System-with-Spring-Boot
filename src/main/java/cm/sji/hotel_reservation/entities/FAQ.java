package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @ColumnDefault("")
    String faqQuestion = "";

    @ColumnDefault("")
    String faqAnswer = "";
}
