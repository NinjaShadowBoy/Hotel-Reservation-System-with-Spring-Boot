package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
public class RoomService {
    @Id
    String label;

    public RoomService() {}

    public RoomService(String label) {
        this.label = label;
    }
}
