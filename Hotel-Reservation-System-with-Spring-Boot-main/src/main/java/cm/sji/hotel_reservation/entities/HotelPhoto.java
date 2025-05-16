package cm.sji.hotel_reservation.entities;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.Data;

@Setter
@Getter
@Data
@Entity
public class HotelPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    int id;

    @OneToOne
    @JoinColumn(name = "fk_hotel_id")
    Hotel hotel;

    public HotelPhoto() {}

    public HotelPhoto(int id, Hotel hotel) {
        this.id = id;
        this.hotel = hotel;
    }
}
