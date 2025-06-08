package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.ApiResponse;
import cm.sji.hotel_reservation.dtos.UserCreateRequest;
import cm.sji.hotel_reservation.dtos.UserResponse;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserApi {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Single endpoint for getting all users
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            logger.info("Retrieving all users");
            List<User> users = userService.findAll();

            // Convert to UserResponse objects
            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .toList();

            logger.info("Successfully retrieved {} users", userResponses.size());
            return ResponseEntity.ok(new ApiResponse(true, "Users retrieved successfully", userResponses));

        } catch (Exception e) {
            logger.error("Error retrieving users: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving users: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        try {
            logger.info("Creating user with email: {}", request.getEmail());

            // Check if email already exists
            Optional<User> existingUser = userService.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                logger.warn("User creation failed - email already exists: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(false, "Email already exists", null));
            }

            // Create new user
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .build();

            User savedUser = userService.save(user);
            logger.info("User created successfully with ID: {}", savedUser.getId());

            // Return user response without password
            UserResponse userResponse = UserResponse.fromUser(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User created successfully", userResponse));

        } catch (Exception e) {
            logger.error("Error creating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error creating user: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
        try {
            logger.info("Deleting user with ID: {}", id);

            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            userService.deleteById(id);
            logger.info("User deleted successfully with ID: {}", id);

            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully", null));

        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deleting user: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UserCreateRequest request) {
        try {
            logger.info("Updating user with ID: {}", id);

            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            User existingUser = userOptional.get();

            // Check if email is being changed and if new email already exists
            if (!existingUser.getEmail().equals(request.getEmail())) {
                Optional<User> emailCheck = userService.findByEmail(request.getEmail());
                if (emailCheck.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ApiResponse(false, "Email already exists", null));
                }
            }

            // Update user fields
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setEmail(request.getEmail());
            existingUser.setRole(request.getRole());
            existingUser.setActive(request.getActive() != null ? request.getActive() : true);

            // Only update password if provided
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            User updatedUser = userService.save(existingUser);
            UserResponse userResponse = UserResponse.fromUser(updatedUser);

            logger.info("User updated successfully with ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully", userResponse));

        } catch (Exception e) {
            logger.error("Error updating user with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating user: " + e.getMessage(), null));
        }
    }
    @GetMapping("/by-role/{role}")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable String role) {
        try {
            logger.info("Retrieving users with role: {}", role);

            List<User> users = userService.findByRole(role);
            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .toList();

            return ResponseEntity.ok(
                    new ApiResponse(true, "Users retrieved successfully", userResponses)
            );
        } catch (Exception e) {
            logger.error("Error retrieving users by role: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving users: " + e.getMessage(), null));
        }
    }
}