package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final HotelRepo hotelRepo;

    private final OfferRepo offerRepo;

    private final RoomTypeRepo roomTypeRepo;

    private final HotelPhotoRepo hotelPhotoRepo;

    private final BookingRepo bookingRepo;

    private final UserRepo userRepo;

    @Value("${system.default.cancellation.deadline.hours}")
    private Short cancellationDeadline;

    public BookingService(HotelRepo hotelRepo, OfferRepo offerRepo, RoomTypeRepo roomTypeRepo, HotelPhotoRepo hotelPhotoRepo, BookingRepo bookingRepo, UserRepo userRepo) {
        this.hotelRepo = hotelRepo;
        this.offerRepo = offerRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.hotelPhotoRepo = hotelPhotoRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    public List<ClientReservationDTO> getClientReservations(Integer clientId) {
        return bookingRepo.findByClient_Id(clientId).stream()
                .map(this::getReservationDTO).toList();
    }

    public List<ClientReservationDTO> getClientReservations(User client){
        return bookingRepo.findByClient(client).stream()
                .map(this::getReservationDTO).toList();
    }

    public List<ClientReservationDTO> getAllClientReservations() {
        return bookingRepo.findAll().stream().map(this::getReservationDTO).toList();
    }
    private ClientReservationDTO getReservationDTO(Booking booking){
        Boolean cancelable = booking.getCheckinDate().isBefore(LocalDateTime.now()
                .minusHours(cancellationDeadline));

        return ClientReservationDTO
                .builder()
                .clientId(booking.getClient().getId())
                .roomTypeId(booking.getRoomType().getId())
                .roomType(booking.getRoomType().getLabel())
                .price(booking.getRoomType().getPrice())
                .date(booking.getDate())
                .hotelName(booking.getRoomType().getHotel().getName())
                .cancelable(cancelable)
                .build();
    }

}
