package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Question;
import cm.sji.hotel_reservation.entities.FAQKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, FAQKey> {

    List<Question> findByHotel_Id(Integer hotelId);
}
