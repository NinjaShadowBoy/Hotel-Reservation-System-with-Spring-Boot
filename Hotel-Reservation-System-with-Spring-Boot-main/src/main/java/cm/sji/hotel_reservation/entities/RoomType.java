package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    int id;

    @ManyToOne
    @JoinColumn(name = "fk_hotel")
    Hotel hotel;

    String label;
    int totalNumber;
    int numberAvailable;
    double price;

    public RoomType() {}

    public RoomType(int id, Hotel hotel, String label, int totalNumber, int numberAvailable, double price) {
        this.id = id;
        this.hotel = hotel;
        this.label = label;
        this.totalNumber = totalNumber;
        this.numberAvailable = numberAvailable;
        this.price = price;
    }
}
