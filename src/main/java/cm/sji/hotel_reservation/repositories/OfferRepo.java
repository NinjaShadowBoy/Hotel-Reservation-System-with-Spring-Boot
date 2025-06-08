package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Offer;
import cm.sji.hotel_reservation.entities.OfferKey;
import cm.sji.hotel_reservation.entities.RoomService;
import cm.sji.hotel_reservation.entities.RoomType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepo extends JpaRepository<Offer, OfferKey> {
    List<Offer> findByRoomType(RoomType roomType);
    List<Offer> findByRoomTypeId(Integer roomTypeId);
    List<Offer> findByRoomService(RoomService roomService);
    Optional<Offer> findByRoomTypeAndRoomService(RoomType roomType, RoomService roomService);
}
