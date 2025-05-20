package cm.sji.hotel_reservation.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class FAQKey implements Serializable {
    private Integer client;
    private Integer hotel;

    //Default constructor
    public FAQKey() {}

    public FAQKey(Integer client, Integer hotel) {
        this.client = client;
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FAQKey faqKey)) return false;
        return Objects.equals(client, faqKey.client) && Objects.equals(hotel, faqKey.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, hotel);
    }
}

// This composite key is used in two different entities having the same composite key
// Those are FAQ and Booking