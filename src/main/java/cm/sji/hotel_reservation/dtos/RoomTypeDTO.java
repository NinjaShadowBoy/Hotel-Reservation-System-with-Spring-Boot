package cm.sji.hotel_reservation.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
    Integer id;

    String label;
    Set<String> services;
    Number price;
    String image;
    Integer numberAvailable;
    Integer totalNumber;
}
