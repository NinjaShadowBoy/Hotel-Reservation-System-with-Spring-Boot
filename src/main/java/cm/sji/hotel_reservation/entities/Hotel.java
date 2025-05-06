package cm.sji.hotel_reservation.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.*;

@Setter
@Getter
@Data
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    int id;

    String name;
    String location;
    int rating;

    @ManyToOne
    @JoinColumn(name = "fk_owner")
    private User owner;

    public Hotel() {}

    public Hotel(int id, String name, String location, int rating, User owner) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.owner = owner;
    }
}
