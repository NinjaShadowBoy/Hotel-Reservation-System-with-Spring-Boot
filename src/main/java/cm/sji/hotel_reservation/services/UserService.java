package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.UserDTO;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User createClient(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public UserDTO getUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }
}
