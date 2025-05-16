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
public class HotelDetailsDTO {
    Long id;

    String name;
    String image;
    String location;
    String desc;

    Float rating;

    List<String> services;
}

