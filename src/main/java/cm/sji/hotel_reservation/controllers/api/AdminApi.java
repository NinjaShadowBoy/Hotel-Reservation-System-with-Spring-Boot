package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.ApiResponse;
import cm.sji.hotel_reservation.dtos.UserResponse;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminApi {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;

    // Get all users (admin only)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsersAdmin() {
        try {
            logger.info("Admin retrieving all users");
            List<User> users = userService.findAll();

            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .toList();

            return ResponseEntity.ok(
                    new ApiResponse(true, "All users retrieved successfully", userResponses)
            );
        } catch (Exception e) {
            logger.error("Admin error retrieving users: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving users: " + e.getMessage(), null));
        }
    }

    // Get user by ID (admin only)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer id) {
        try {
            logger.info("Admin retrieving user with ID: {}", id);
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse(true, "User retrieved successfully",
                            UserResponse.fromUser(userOptional.get()))
            );
        } catch (Exception e) {
            logger.error("Admin error retrieving user with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving user: " + e.getMessage(), null));
        }
    }

    // Deactivate user (admin only)
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deactivateUser(@PathVariable Integer id) {
        try {
            logger.info("Admin deactivating user with ID: {}", id);
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            User user = userOptional.get();
            user.setActive(false);
            userService.save(user);

            return ResponseEntity.ok(
                    new ApiResponse(true, "User deactivated successfully",
                            UserResponse.fromUser(user))
            );
        } catch (Exception e) {
            logger.error("Admin error deactivating user with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deactivating user: " + e.getMessage(), null));
        }
    }

    // Reactivate user (admin only)
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> activateUser(@PathVariable Integer id) {
        try {
            logger.info("Admin activating user with ID: {}", id);
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            User user = userOptional.get();
            user.setActive(true);
            userService.save(user);

            return ResponseEntity.ok(
                    new ApiResponse(true, "User activated successfully",
                            UserResponse.fromUser(user))
            );
        } catch (Exception e) {
            logger.error("Admin error activating user with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error activating user: " + e.getMessage(), null));
        }
    }

    // Change user role (admin only)
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> changeUserRole(
            @PathVariable Integer id,
            @RequestParam String newRole) {
        try {
            logger.info("Admin changing role for user ID: {} to {}", id, newRole);
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null));
            }

            User user = userOptional.get();

            try {
                User.UserRole role = User.UserRole.valueOf(newRole.toUpperCase());
                user.setRole(role);
                userService.save(user);

                return ResponseEntity.ok(
                        new ApiResponse(true, "User role updated successfully",
                                UserResponse.fromUser(user))
                );
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid role specified", null));
            }
        } catch (Exception e) {
            logger.error("Admin error changing role for user ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error changing user role: " + e.getMessage(), null));
        }
    }
}
