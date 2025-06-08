package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

    List<Question> findByHotel_Id(Integer hotelId);
}
