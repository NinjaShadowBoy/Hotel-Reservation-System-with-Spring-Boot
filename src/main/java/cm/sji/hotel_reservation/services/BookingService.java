package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.Booking.Status;
import cm.sji.hotel_reservation.entities.BookingKey;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.BookingRepo;
import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final RoomTypeRepo roomTypeRepository;

    @Transactional
    public Booking createBooking(Integer clientId, Integer roomTypeId,
                                 LocalDate checkInDate, LocalDate checkOutDate) {
        // Verify room exists and is available
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Room type not found"));

        if (roomType.getNumberAvailable() <= 0) {
            throw new IllegalStateException("No rooms available for selected type");
        }

        // Check for overlapping bookings
        int overlappingBookings = bookingRepository.countOverlappingBookings(
                roomTypeId, checkInDate, checkOutDate);

        if (overlappingBookings >= roomType.getNumberAvailable()) {
            throw new IllegalStateException("No rooms available for selected dates");
        }

        // Create booking
        Booking booking = Booking.builder()
                .clientId(clientId)
                .roomTypeId(roomTypeId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .status(Status.PENDING)
                .build();

        // Update room availability
        roomType.setNumberAvailable(roomType.getNumberAvailable() - 1);
        roomTypeRepository.save(roomType);

        return bookingRepository.save(booking);
    }

    public Optional<Booking> findById(Integer clientId, Integer roomTypeId) {
        BookingKey id = new BookingKey(clientId, roomTypeId);
        return bookingRepository.findById(id);
    }

    public List<Booking> findByClientId(Integer clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    public List<Booking> findByHotelId(Integer hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    public List<Booking> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByDateRange(startDate, endDate);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Transactional
    public Booking updateBookingStatus(Integer clientId, Integer roomTypeId, Status newStatus) {
        BookingKey id = new BookingKey(clientId, roomTypeId);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Status oldStatus = booking.getStatus();

        // If cancelling an active booking, increment room availability
        if (newStatus == Status.CANCELLED &&
                (oldStatus == Status.CONFIRMED || oldStatus == Status.PENDING)) {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("Room type not found"));
            roomType.setNumberAvailable(roomType.getNumberAvailable() + 1);
            roomTypeRepository.save(roomType);
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Integer clientId, Integer roomTypeId) {
        BookingKey id = new BookingKey(clientId, roomTypeId);
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            // If deleting an active booking, increment room availability
            if (booking.getStatus() == Status.CONFIRMED ||
                    booking.getStatus() == Status.PENDING) {
                RoomType roomType = roomTypeRepository.findById(roomTypeId)
                        .orElseThrow(() -> new IllegalArgumentException("Room type not found"));
                roomType.setNumberAvailable(roomType.getNumberAvailable() + 1);
                roomTypeRepository.save(roomType);
            }

            bookingRepository.deleteById(id);
        }
    }
}