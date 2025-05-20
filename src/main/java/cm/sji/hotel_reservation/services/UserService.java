package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepo userRepo;

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
