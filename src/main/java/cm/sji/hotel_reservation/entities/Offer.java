package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@IdClass(OfferKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_roomType", insertable = false, updatable = false)
    RoomType roomType;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_roomService", insertable = false, updatable = false)
    RoomService roomService;

}
