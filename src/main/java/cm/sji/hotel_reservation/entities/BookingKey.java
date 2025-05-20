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
    private Integer client;
    private Integer roomType;

    //Default constructor
    public BookingKey() {}

    public BookingKey(Integer clientID, Integer roomTypeID) {
        this.client = clientID;
        this.roomType = roomTypeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingKey bookingKey)) return false;
        return Objects.equals(client, bookingKey.client) && Objects.equals(roomType, bookingKey.roomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, roomType);
    }
}
