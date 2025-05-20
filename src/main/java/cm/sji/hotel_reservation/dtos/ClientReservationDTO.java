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
    Integer clientId;
    Integer roomTypeId;

    String hotelName;
    String roomType;

    Double price;

    LocalDateTime date;
    LocalDateTime checkinDate;

    Boolean cancelable;
}
