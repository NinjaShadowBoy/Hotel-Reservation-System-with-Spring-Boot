package cm.sji.hotel_reservation.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
@Entity
@IdClass(FAQKey.class)
public class FAQ {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", insertable = false, updatable = false)
    Hotel hotel;

    String faqQuestion;
    String faqAnswer;

    public FAQ() {}

    public FAQ(User client, Hotel hotel, String faqQuestion, String faqAnswer) {
        this.client = client;
        this.hotel = hotel;
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

}
