package cm.sji.hotel_reservation.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
    Long id;

    String label;
    List<String> services;
    Number price;
    String image;
}
