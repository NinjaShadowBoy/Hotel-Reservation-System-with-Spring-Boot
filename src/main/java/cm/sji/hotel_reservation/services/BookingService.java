package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.dtos.ReservationDTO;
import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.BookingStatus;
import cm.sji.hotel_reservation.entities.RoomPhoto;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HotelRepo hotelRepo;

    private final OfferRepo offerRepo;

    private final RoomTypeRepo roomTypeRepo;

    private final HotelPhotoRepo hotelPhotoRepo;

    private final BookingRepo bookingRepo;

    private final UserRepo userRepo;

    private final EmailService emailService;

    @Value("${system.default.cancellation.deadline.hours}")
    private Short cancellationDeadline;

    @Value("${system.commission.percentage:5.0}")
    private Double commissionPercentage;

    public BookingService(HotelRepo hotelRepo, OfferRepo offerRepo, RoomTypeRepo roomTypeRepo, 
                         HotelPhotoRepo hotelPhotoRepo, BookingRepo bookingRepo, 
                         UserRepo userRepo, EmailService emailService) {
        this.hotelRepo = hotelRepo;
        this.offerRepo = offerRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.hotelPhotoRepo = hotelPhotoRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    // Calculate commission amount
    private Double calculateCommission(Double amount) {
        return amount * (commissionPercentage / 100.0);
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
                .id(booking.getId())
                .clientId(booking.getClient().getId())
                .roomTypeId(booking.getRoomType().getId())
                .roomType(booking.getRoomType().getLabel())
                .price(booking.getRoomType().getPrice())
                .totalAmount(booking.getTotalAmount())
                .commissionAmount(booking.getCommissionAmount())
                .date(booking.getDate())
                .checkinDate(booking.getCheckinDate())
                .hotelName(booking.getRoomType().getHotel().getName())
                .cancelable(cancelable && booking.getStatus() != BookingStatus.CANCELLED)
                .status(booking.getStatus().toString())
                .refunded(booking.getRefunded())
                .cancellationDate(booking.getCancellationDate())
                .refundAmount(booking.getRefundAmount())
                .build();
    }

    public PaymentIntent createPaymentIntent(ReservationDTO reservationDTO) throws StripeException {
        RoomType roomType = roomTypeRepo.findById(reservationDTO.getRoomTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Inexisting roomtype"));

        Double totalAmount = roomType.getPrice();
        Double commissionAmount = calculateCommission(totalAmount);

        // Amount in cents for Stripe
        Long amountInCents = (long) (totalAmount * 100);

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
                                .setAmount((long)((totalAmount - commissionAmount) * 100)) // Transfer amount minus commission
                                .build()
                )
                .putMetadata("roomTypeId", reservationDTO.getRoomTypeId().toString())
                .putMetadata("clientId", reservationDTO.getClientId().toString())
                .putMetadata("checkinDatetime", reservationDTO.getCheckinDatetime().toString())
                .putMetadata("commissionAmount", commissionAmount.toString())
                .putMetadata("totalAmount", totalAmount.toString())
                .build();

        logger.info("Created payment intent with commission: {}", commissionAmount);
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

            Double totalAmount = Double.parseDouble(bookingInfo.get("totalAmount"));
            Double commissionAmount = Double.parseDouble(bookingInfo.get("commissionAmount"));

            logger.info("Client {} booked room {}", client, roomType);
            Booking booking = Booking.builder()
                    .roomType(roomType)
                    .client(client)
                    .checkinDate(reservationDTO.getCheckinDatetime())
                    .date(LocalDateTime.now())
                    .totalAmount(totalAmount)
                    .commissionAmount(commissionAmount)
                    .status(BookingStatus.CONFIRMED)
                    .paymentIntentId(bookingInfo.get("payment_intent"))
                    .build();

            logger.info("Booking completed with commission: {}", commissionAmount);

            booking = bookingRepo.save(booking);

            return getReservationDTO(booking);
        } catch (Exception e) {
            logger.error("Error when completing booking {}", e.getMessage());
            return null;
        }
    }

    /**
     * Cancels a booking and processes a refund if applicable
     * @param bookingId The ID of the booking to cancel
     * @return The updated reservation DTO or null if cancellation failed
     */
    public ClientReservationDTO cancelBooking(Integer bookingId) {
        try {
            Optional<Booking> optionalBooking = bookingRepo.findById(bookingId);
            if (optionalBooking.isEmpty()) {
                logger.error("Booking not found: {}", bookingId);
                return null;
            }

            Booking booking = optionalBooking.get();

            // Check if booking is already cancelled
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                logger.warn("Booking already cancelled: {}", bookingId);
                return getReservationDTO(booking);
            }

            // Check if booking is cancelable (based on check-in date)
            Boolean cancelable = booking.getCheckinDate().isAfter(LocalDateTime.now()
                    .plusHours(cancellationDeadline));

            if (!cancelable) {
                logger.error("Booking is not cancelable: {}", bookingId);
                return null;
            }

            // Process refund through Stripe
            Double refundAmount = booking.getTotalAmount() - booking.getCommissionAmount();

            try {
                // Create refund in Stripe
                RefundCreateParams params = RefundCreateParams.builder()
                        .setPaymentIntent(booking.getPaymentIntentId())
                        .setAmount((long)(refundAmount * 100)) // Convert to cents
                        .build();

                Refund refund = Refund.create(params);

                // Update booking status
                booking.setStatus(BookingStatus.CANCELLED);
                booking.setCancellationDate(LocalDateTime.now());
                booking.setRefunded(true);
                booking.setRefundAmount(refundAmount);

                booking = bookingRepo.save(booking);

                logger.info("Booking cancelled with refund: {}", refundAmount);
                return getReservationDTO(booking);
            } catch (StripeException e) {
                logger.error("Error processing refund: {}", e.getMessage());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error when cancelling booking: {}", e.getMessage());
            return null;
        }
    }
}
