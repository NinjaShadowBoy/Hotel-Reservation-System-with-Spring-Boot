package cm.sji.hotel_reservation.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientReservationDTO {
    Integer id;
    Integer clientId;
    Integer roomTypeId;

    String hotelName;
    String roomType;

    Double price;
    Double totalAmount;
    Double commissionAmount;

    LocalDateTime date;
    LocalDateTime checkinDate;

    Boolean cancelable;

    // New fields for booking status and refund information
    String status;
    Boolean refunded;
    LocalDateTime cancellationDate;
    Double refundAmount;
}
