package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.RoomPhoto;
import cm.sji.hotel_reservation.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomPhotoRepo extends JpaRepository<RoomPhoto, Integer> {
    Optional<RoomPhoto> findByRoomType(RoomType roomType);
}
