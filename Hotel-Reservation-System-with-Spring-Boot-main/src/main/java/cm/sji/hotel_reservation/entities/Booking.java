package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
@Entity
@IdClass(BookingKey.class)
public class Booking {
//    @EmbeddedId
//    private BookingKey id;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_room_type_id", insertable = false, updatable = false)
    RoomType room_Type;

    LocalDate date;

    public Booking() {}

    public Booking(User client, RoomType room_Type, LocalDate date) {
        this.client = client;
        this.room_Type = room_Type;
        this.date = date;
    }
}
