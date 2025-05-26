package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.dtos.ReservationDTO;
import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.RoomPhoto;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    public List<ClientReservationDTO> getClientReservations(User client) {
        return bookingRepo.findByClient(client).stream()
                .map(this::getReservationDTO).toList();
    }

    public List<ClientReservationDTO> getAllClientReservations() {
        return bookingRepo.findAll().stream().map(this::getReservationDTO).toList();
    }

    private ClientReservationDTO getReservationDTO(Booking booking) {
        Boolean cancelable = booking.getCheckinDate().isAfter(LocalDateTime.now()
                .plusHours(cancellationDeadline));

        return ClientReservationDTO
                .builder()
                .clientId(booking.getClient().getId())
                .roomTypeId(booking.getRoomType().getId())
                .roomType(booking.getRoomType().getLabel())
                .price(booking.getRoomType().getPrice())
                .date(booking.getDate())
                .checkinDate(booking.getCheckinDate())
                .hotelName(booking.getRoomType().getHotel().getName())
                .cancelable(cancelable)
                .build();
    }

    public PaymentIntent createPaymentIntent(ReservationDTO reservationDTO) throws StripeException {
        Long amountInCents = (long) (roomTypeRepo.findById(reservationDTO.getRoomTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Inexisting roomtype")
                ).getPrice() * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .setTransferData(
                        PaymentIntentCreateParams.TransferData.builder()
                                .setDestination("acct_1RQq99QjidQV5ShK") // The connected account
                                .build()
                )
                .putMetadata("roomTypeId", reservationDTO.getRoomTypeId().toString())
                .putMetadata("clientId", reservationDTO.getClientId().toString())
                .putMetadata("checkinDatetime", reservationDTO.getCheckinDatetime().toString())
                .build();
        logger.info("Created payment intent");
        return PaymentIntent.create(params);
    }

    public ClientReservationDTO completeBooking(Map<String, String> bookingInfo) {
        try {
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .roomTypeId(Integer.parseInt(bookingInfo.get("roomTypeId")))
                    .clientId(Integer.parseInt(bookingInfo.get("clientId")))
                    .checkinDatetime(LocalDateTime.parse(bookingInfo.get("checkinDatetime")))
                    .build();

            RoomType roomType = roomTypeRepo.findById(reservationDTO.getRoomTypeId()).get();
            User client = userRepo.findById(reservationDTO.getClientId()).get();

            logger.info("Client {} booked room {}", client, roomType);
            Booking booking = Booking.builder()
                    .roomType(roomType)
                    .client(client)
                    .checkinDate(reservationDTO.getCheckinDatetime())
                    .date(LocalDateTime.now())
                    .build();

            logger.info("Booking completed {}", booking);

            booking = bookingRepo.save(booking);

            return getReservationDTO(booking);
        } catch (Exception e) {
            logger.error("Error when completing booking {}", e.getMessage());
            return null;
        }
    }

}
