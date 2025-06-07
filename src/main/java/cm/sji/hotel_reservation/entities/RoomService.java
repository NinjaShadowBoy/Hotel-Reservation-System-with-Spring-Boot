package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;
    String label;
    String fontawsome_icon_class = "fa fa-square";
}
