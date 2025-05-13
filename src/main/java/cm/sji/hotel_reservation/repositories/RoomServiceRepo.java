package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.RoomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomServiceRepo extends JpaRepository<RoomService, String> {
    List<RoomService> findByLabelContainingIgnoreCase(String keyword);
}
