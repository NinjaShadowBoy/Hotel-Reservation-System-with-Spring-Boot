//package cm.sji.hotel_reservation.services;
//
//import cm.sji.hotel_reservation.entities.User;
//import cm.sji.hotel_reservation.repositories.UserRepo;
//import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class UserService {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final UserRepo userRepo;
//
//    public Optional<User> findByEmail(String email) {
//        return userRepo.findByEmail(email);
//    }
//}
package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.UserDTO;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
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

    public User save(User user) {
        logger.debug("Saving user with email: {}", user.getEmail());
        return userRepo.save(user);
    }

    public List<User> findAll() {
        logger.debug("Finding all users");
        return userRepo.findAll();
    }

    public List<User> findByRole(String role) {
        try {
            // Convert the input string to the enum value
            User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
            return userRepo.findByRole(userRole);
        } catch (IllegalArgumentException e) {
            // Handle invalid role strings gracefully
            throw new IllegalArgumentException(
                    "Invalid role: " + role + ". Valid roles are: " +
                            Arrays.toString(User.UserRole.values())
            );
        }
    }

    public void deleteById(Integer id) {
        logger.debug("Deleting user with id: {}", id);
        userRepo.deleteById(id);
    }

    public Optional<User> findById(Integer id) {
        logger.debug("Finding user by id: {}", id);
        return userRepo.findById(id);
    }
}
