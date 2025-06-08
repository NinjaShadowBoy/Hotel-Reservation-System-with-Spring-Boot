package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.ApiResponse;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.UserRepo;
import cm.sji.hotel_reservation.entities.User.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardApi {

    private final UserRepo userRepo;
    private final HotelRepo hotelRepo;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        try {
            Map<String, Long> stats = new HashMap<>();
            stats.put("userCount", userRepo.countAllUsers());
            stats.put("ownerCount", userRepo.countOwners());
            stats.put("hotelCount", hotelRepo.count());

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Inner DTO class for the dashboard stats
    private static class DashboardStatsDto {
        private final long userCount;
        private final long ownerCount;
        private final long hotelCount;

        public DashboardStatsDto(long userCount, long ownerCount, long hotelCount) {
            this.userCount = userCount;
            this.ownerCount = ownerCount;
            this.hotelCount = hotelCount;
        }

        // Getters
        public long getUserCount() { return userCount; }
        public long getOwnerCount() { return ownerCount; }
        public long getHotelCount() { return hotelCount; }
    }
}
