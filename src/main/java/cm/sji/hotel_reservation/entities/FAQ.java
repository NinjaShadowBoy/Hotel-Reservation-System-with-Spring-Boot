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
@IdClass(FAQ.class)
public class FAQ {
    @EmbeddedId
    private FAQKey id;

    @ManyToOne
    @JoinColumn(name = "fk_client_id", insertable = false, updatable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "fk_hotel_id", insertable = false, updatable = false)
    Hotel hotel;

    String faqQuestion;
    String faqAnswer;

    public FAQ() {}

    public FAQ(FAQKey id, User client, Hotel hotel, String faqQuestion, String faqAnswer) {
        this.id = id;
        this.client = client;
        this.hotel = hotel;
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

}
