package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Offer;
import cm.sji.hotel_reservation.entities.OfferKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepo extends JpaRepository<Offer, OfferKey> {
}
