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
    private int client;
    private int room_Type;

    //Default constructor
    public BookingKey() {}

    public BookingKey(int clientID, int roomTypeID) {
        this.client = clientID;
        this.room_Type = roomTypeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingKey bookingKey)) return false;
        return client == bookingKey.client && room_Type == bookingKey.room_Type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, room_Type);
    }
}
