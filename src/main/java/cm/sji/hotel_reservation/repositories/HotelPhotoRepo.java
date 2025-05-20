package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.HotelPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelPhotoRepo extends JpaRepository<HotelPhoto, Integer> {
    Optional<HotelPhoto> findByHotel(Hotel hotel);
    Optional<HotelPhoto> findByHotelId(Integer hotelId);
}
