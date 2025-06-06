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

    LocalDateTime date = null;  // Booking creation date
    LocalDateTime checkinDate = null;

    // Payment and commission fields
    Double totalAmount = 0.0;  // Total amount paid
    Double commissionAmount = 0.0;  // 5% commission amount

    @Enumerated(EnumType.STRING)
    BookingStatus status = BookingStatus.PENDING;

    String paymentIntentId;  // Store Stripe payment intent ID

    // Cancellation and refund fields
    Boolean refunded = false;
    LocalDateTime cancellationDate = null;
    Double refundAmount = 0.0;
}
