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

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client = null;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_room_type_id", insertable = false, updatable = false)
    RoomType roomType = null;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime bookingTime;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @PrePersist
    protected void onCreate() {
        this.bookingTime = LocalDateTime.now();
    }
}
