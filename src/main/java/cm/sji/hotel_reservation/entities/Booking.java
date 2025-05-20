package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@IdClass(BookingKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client = null;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_room_type_id", insertable = false, updatable = false)
    RoomType roomType = null;

    LocalDateTime date = null;

    LocalDateTime checkinDate = null;
}
