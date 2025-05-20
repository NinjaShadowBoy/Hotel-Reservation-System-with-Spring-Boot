package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.FAQ;
import cm.sji.hotel_reservation.entities.FAQKey;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepo extends JpaRepository<FAQ, FAQKey> {
    List<FAQ> findByHotel(Hotel hotel);
    List<FAQ> findByHotelId(Long hotelId);
    List<FAQ> findByClient(User client);
    List<FAQ> findByClientId(Long clientId);

    @Query("SELECT f FROM FAQ f WHERE f.hotel.id = :hotelId AND f.response IS NOT NULL")
    List<FAQ> findAnsweredFAQsByHotelId(Long hotelId);

    @Query("SELECT f FROM FAQ f WHERE f.hotel.id = :hotelId AND f.response IS NULL")
    List<FAQ> findUnansweredFAQsByHotelId(Long hotelId);
}
