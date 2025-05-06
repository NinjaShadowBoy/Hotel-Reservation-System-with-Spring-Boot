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
    private int room_TypeID;
    private String roomServiceLabel;

    //Default constructor
    public OfferKey() {}

    public OfferKey(int roomTypeID, String roomServiceLabel) {
        this.room_TypeID = roomTypeID;
        this.roomServiceLabel = roomServiceLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferKey offerKey)) return false;
        return room_TypeID == offerKey.room_TypeID && Objects.equals(roomServiceLabel, offerKey.roomServiceLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room_TypeID, roomServiceLabel);
    }
}
