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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", updatable = false)
    User client = null;

    @ManyToOne
    @JoinColumn(name = "fk_room_type_id", updatable = false)
    RoomType roomType = null;

    LocalDateTime date = null;

    LocalDateTime checkinDate = null;
}
