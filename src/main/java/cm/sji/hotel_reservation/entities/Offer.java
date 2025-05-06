package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@IdClass(Offer.class)
public class Offer {
    @EmbeddedId
    private OfferKey id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_roomService", insertable = false, updatable = false)
    RoomService roomService;

    public Offer() {}

    public Offer(OfferKey id, User client, RoomService roomService) {
        this.id = id;
        this.client = client;
        this.roomService = roomService;
    }
}
