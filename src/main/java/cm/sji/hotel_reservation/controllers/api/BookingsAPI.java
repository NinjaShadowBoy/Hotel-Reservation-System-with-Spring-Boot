package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.services.BookingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookingsAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final BookingService bookingService;

    @GetMapping("/api/bookings")
    public ResponseEntity<List<ClientReservationDTO>> getAllBookings() {
        try {
            var bookings = bookingService.getAllClientReservations();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/bookings/{clientId}")
    public ResponseEntity<List<ClientReservationDTO>> getClientBookings(@PathVariable Integer clientId) {
        try {
            var bookings = bookingService.getClientReservations(clientId);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
