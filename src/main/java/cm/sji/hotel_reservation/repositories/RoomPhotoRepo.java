package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.RoomPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPhotoRepo extends JpaRepository<RoomPhoto, Integer> {
}
