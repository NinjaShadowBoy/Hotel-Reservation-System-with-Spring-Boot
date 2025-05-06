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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    int id;

    String name;
    String location;
    int rating;
    String description;

    @ManyToOne
    @JoinColumn(name = "fk_owner")
    private User owner;

    public Hotel() {}

    public Hotel(int id, String name, String location, int rating, String description, User owner) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.description = description;
        this.owner = owner;
    }
}
