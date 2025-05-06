package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer> {
}
