package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByFirstName(String firstName);
    Optional<User> findByEmail(String email);
    List<User> findByRole(User.UserRole role);
    long countByRole(User.UserRole role);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'OWNER'")
    long countOwners();

    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();
}

