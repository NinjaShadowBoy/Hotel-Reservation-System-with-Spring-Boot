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
    @Column(name = "client_id", insertable = false, updatable = false)
    private Integer clientId;

    @Id
    @Column(name = "room_type_id", insertable = false, updatable = false)
    private Integer roomTypeId;

    @Transient
    private User client;

    @Transient
    private RoomType roomType;

    private LocalDateTime bookingTime;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @PrePersist
    protected void onCreate() {
        this.bookingTime = LocalDateTime.now();
    }
}
