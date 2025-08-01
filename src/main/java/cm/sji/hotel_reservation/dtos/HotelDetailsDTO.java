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
public class HotelDetailsDTO {
    Integer id;

    String name;
    String image;
    String location;
    String desc;

    Double lowestPrice;

    Float rating;

    Set<String> services;
}

