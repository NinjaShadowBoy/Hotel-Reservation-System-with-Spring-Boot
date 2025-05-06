package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.HotelPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelPhotoRepo extends JpaRepository<HotelPhoto, Integer> {
}
