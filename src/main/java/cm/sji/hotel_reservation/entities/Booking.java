package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@IdClass(Booking.class)
public class Booking {
    @EmbeddedId
    private BookingKey id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_room_type_id", insertable = false, updatable = false)
    RoomType room_Type;

    public Booking() {}

    public Booking(BookingKey id, User client, RoomType room_Type) {
        this.id = id;
        this.client = client;
        this.room_Type = room_Type;
    }
}
