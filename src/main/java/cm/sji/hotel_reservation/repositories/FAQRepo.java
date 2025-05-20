package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.entities.FAQKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepo extends JpaRepository<FAQ, FAQKey> {

    List<FAQ> findByHotel_Id(Integer hotelId);
}
