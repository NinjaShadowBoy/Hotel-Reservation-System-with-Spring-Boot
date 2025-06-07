package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.entities.*;
import cm.sji.hotel_reservation.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private HotelRepo hotelRepo;

    @Mock
    private OfferRepo offerRepo;

    @Mock
    private RoomTypeRepo roomTypeRepo;

    @Mock
    private HotelPhotoRepo hotelPhotoRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EmailService emailService;

    private BookingService bookingService;

    private Booking testBooking;
    private User testClient;
    private Hotel testHotel;
    private RoomType testRoomType;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(
                hotelRepo, offerRepo, roomTypeRepo, hotelPhotoRepo, bookingRepo, userRepo, emailService
        );

        // Set the cancellation deadline to 24 hours
        ReflectionTestUtils.setField(bookingService, "cancellationDeadline", (short) 24);
        ReflectionTestUtils.setField(bookingService, "commissionPercentage", 5.0);

        // Create test data
        testClient = new User();
        testClient.setId(1);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");

        testHotel = new Hotel();
        testHotel.setId(1);
        testHotel.setName("Test Hotel");

        testRoomType = new RoomType();
        testRoomType.setId(1);
        testRoomType.setLabel("Deluxe Room");
        testRoomType.setPrice(100.0);
        testRoomType.setHotel(testHotel);

        testBooking = Booking.builder()
                .id(1)
                .client(testClient)
                .roomType(testRoomType)
                .checkinDate(LocalDateTime.now().plusDays(7)) // 7 days in the future
                .date(LocalDateTime.now())
                .totalAmount(100.0)
                .commissionAmount(5.0)
                .status(BookingStatus.CONFIRMED)
                .paymentIntentId("pi_test123456")
                .build();
    }

    @Test
    void testCancelBooking() throws StripeException {
        // Arrange
        when(bookingRepo.findById(1)).thenReturn(Optional.of(testBooking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(testBooking);

        // Mock Stripe Refund.create
        Refund mockRefund = mock(Refund.class);
        when(mockRefund.getId()).thenReturn("re_test123456");

        try (MockedStatic<Refund> mockedStatic = mockStatic(Refund.class)) {
            mockedStatic.when(() -> Refund.create(any(RefundCreateParams.class), any(RequestOptions.class)))
                    .thenReturn(mockRefund);

            // Act
            ClientReservationDTO result = bookingService.cancelBooking(1);

            // Assert
            assertNotNull(result);
            assertEquals(BookingStatus.CANCELLED.toString(), result.getStatus());

            // Verify booking was updated correctly
            ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepo).save(bookingCaptor.capture());
            Booking savedBooking = bookingCaptor.getValue();

            assertEquals(BookingStatus.CANCELLED, savedBooking.getStatus());
            assertEquals("re_test123456", savedBooking.getRefundId());
            assertNotNull(savedBooking.getCancellationDate());
            assertFalse(savedBooking.getRefunded()); // Should not be marked as refunded yet
            assertEquals(0.0, savedBooking.getRefundAmount()); // Refund amount should not be set yet

            // Verify email was sent
            verify(emailService).sendBookingCancellationEmail(savedBooking);
        }
    }

    @Test
    void testConfirmRefund() {
        // Arrange
        String refundId = "re_test123456";
        Double refundAmount = 95.0; // Total minus commission

        testBooking.setStatus(BookingStatus.CANCELLED);
        testBooking.setRefundId(refundId);
        testBooking.setCancellationDate(LocalDateTime.now());
        testBooking.setRefunded(false);
        testBooking.setRefundAmount(0.0);

        when(bookingRepo.findByRefundId(refundId)).thenReturn(testBooking);
        when(bookingRepo.save(any(Booking.class))).thenReturn(testBooking);

        // Act
        bookingService.confirmRefund(refundId, refundAmount);

        // Assert
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepo).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();

        assertTrue(savedBooking.getRefunded()); // Should now be marked as refunded
        assertEquals(refundAmount, savedBooking.getRefundAmount()); // Refund amount should be set

        // Verify refund confirmation email was sent
        verify(emailService).sendRefundConfirmationEmail(savedBooking);
    }

    @Test
    void testCancelBooking_AlreadyCancelled() {
        // Arrange
        testBooking.setStatus(BookingStatus.CANCELLED);
        when(bookingRepo.findById(1)).thenReturn(Optional.of(testBooking));

        // Act
        ClientReservationDTO result = bookingService.cancelBooking(1);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED.toString(), result.getStatus());

        // Verify booking was not updated
        verify(bookingRepo, never()).save(any(Booking.class));
        verify(emailService, never()).sendBookingCancellationEmail(any(Booking.class));
    }

    @Test
    void testCancelBooking_PastCancellationDeadline() {
        // Arrange
        testBooking.setCheckinDate(LocalDateTime.now().plusHours(12)); // Less than 24 hours in the future
        when(bookingRepo.findById(1)).thenReturn(Optional.of(testBooking));

        // Act
        ClientReservationDTO result = bookingService.cancelBooking(1);

        // Assert
        assertNull(result); // Should return null as booking is not cancelable

        // Verify booking was not updated
        verify(bookingRepo, never()).save(any(Booking.class));
        verify(emailService, never()).sendBookingCancellationEmail(any(Booking.class));
    }
}