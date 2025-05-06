package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class FAQKey implements Serializable {
    private int clientID;
    private int HotelID;

    //Default constructor
    public FAQKey() {}

    public FAQKey(int clientID, int hotelID) {
        this.clientID = clientID;
        this.HotelID = hotelID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FAQKey faqKey)) return false;
        return clientID == faqKey.clientID && HotelID == faqKey.HotelID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, HotelID);
    }
}

// This composite key is used in two different entities having the same composite key
// Those are FAQ and Booking