package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.RoomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomServiceRepo extends JpaRepository<RoomService, String> {
}
