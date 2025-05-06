package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class OfferKey implements Serializable {
    private int roomType;
    private String roomService;

    //Default constructor
    public OfferKey() {}

    public OfferKey(int roomTypeID, String roomService) {
        this.roomType = roomTypeID;
        this.roomService = roomService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferKey offerKey)) return false;
        return roomType == offerKey.roomType && Objects.equals(roomService, offerKey.roomService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomType, roomService);
    }
}
