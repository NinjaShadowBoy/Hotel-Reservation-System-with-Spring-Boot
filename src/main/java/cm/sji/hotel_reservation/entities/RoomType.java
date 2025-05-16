package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_hotel")
    Hotel hotel;

    String label;
    Integer totalNumber;
    Integer numberAvailable;
    Double price;

}
