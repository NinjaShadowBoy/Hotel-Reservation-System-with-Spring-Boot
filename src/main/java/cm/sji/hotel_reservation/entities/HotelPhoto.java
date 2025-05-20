package cm.sji.hotel_reservation.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "fk_hotel_id")
    Hotel hotel;

    String filename;
}
