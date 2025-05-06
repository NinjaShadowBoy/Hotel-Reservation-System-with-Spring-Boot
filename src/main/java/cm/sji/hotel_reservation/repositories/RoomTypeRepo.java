package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepo extends JpaRepository<RoomType, Integer> {
}
