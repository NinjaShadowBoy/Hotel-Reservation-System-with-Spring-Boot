package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
public class RoomPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "fk_roomType_id")
    RoomType roomType;

    public RoomPhoto() {}

    public RoomPhoto(int id, RoomType roomType) {
        this.id = id;
        this.roomType = roomType;
    }
}
