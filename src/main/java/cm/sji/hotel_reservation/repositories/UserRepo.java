package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
}
