package cm.sji.hotel_reservation.entities;

/**
 * Enum representing the possible states of a booking.
 */
public enum BookingStatus {
    /**
     * Booking is created but payment is not yet confirmed
     */
    PENDING,
    
    /**
     * Payment is confirmed and booking is active
     */
    CONFIRMED,
    
    /**
     * Booking has been cancelled
     */
    CANCELLED
}