package cm.sji.hotel_reservation.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    Integer id;

    private String firstName = "";

    private String lastName = "";

    private String email = "";

    private String role = "";
}
