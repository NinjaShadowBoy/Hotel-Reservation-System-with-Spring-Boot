package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@IdClass(OfferKey.class)
public class Offer {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_roomType", insertable = false, updatable = false)
    RoomType roomType;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_roomService", insertable = false, updatable = false)
    RoomService roomService;

    public Offer() {}

    public Offer(RoomType roomType, RoomService roomService) {
        this.roomType = roomType;
        this.roomService = roomService;
    }
}
