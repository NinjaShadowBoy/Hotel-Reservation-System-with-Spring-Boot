package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(BookingKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    RoomType roomType;

    LocalDateTime date;

}
