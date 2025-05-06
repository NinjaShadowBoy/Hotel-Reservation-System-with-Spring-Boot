package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class BookingKey implements Serializable {
    private int clientID;
    private int room_TypeID;

    //Default constructor
    public BookingKey() {}

    public BookingKey(int clientID, int roomTypeID) {
        this.clientID = clientID;
        this.room_TypeID = roomTypeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingKey bookingKey)) return false;
        return clientID == bookingKey.clientID && room_TypeID == bookingKey.room_TypeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, room_TypeID);
    }
}
