package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomService {
    @Id
    String label;
    String fontawsome_icon_class = "fa fa-square";
}
