package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.BookingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking, BookingKey> {
}
