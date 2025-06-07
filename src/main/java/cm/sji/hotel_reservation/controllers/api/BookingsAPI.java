package cm.sji.hotel_reservation.controllers.api;

import cm.sji.hotel_reservation.dtos.ClientReservationDTO;
import cm.sji.hotel_reservation.dtos.ReservationDTO;
import cm.sji.hotel_reservation.services.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookingsAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

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

    /**
     * Endpoint for cancelling a booking
     *
     * @param bookingId The ID of the booking to cancel
     * @return The updated booking information or error message
     */
    @PostMapping("/api/bookings/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer bookingId) {
        try {
            ClientReservationDTO result = bookingService.cancelBooking(bookingId);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Booking cannot be cancelled"));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error cancelling booking: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to cancel booking"));
        }
    }

    /**
     * DELETE endpoint for cancelling a booking
     *
     * @param bookingId The ID of the booking to cancel
     * @return The updated booking information or error message
     */
    @DeleteMapping("/api/bookings/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer bookingId) {
        try {
            ClientReservationDTO result = bookingService.cancelBooking(bookingId);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Booking cannot be cancelled"));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error cancelling booking: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to cancel booking"));
        }
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createIntent(@RequestBody ReservationDTO request) {
        try {
            PaymentIntent intent = bookingService.createPaymentIntent(request);
            return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "api/stripe/webhook", consumes = {"application/json", "application/xml", "*/*"})
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("stripe-signature");

        // Log request details for debugging
        logger.debug("Webhook request content type: {}", request.getContentType());
        logger.debug("Webhook request headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                logger.debug("  {}: {}", headerName, request.getHeader(headerName))
        );

        if (sigHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Stripe signature header");
        }

        // Read the request body as bytes to preserve exact payload
        byte[] requestBytes;
        try {
            requestBytes = request.getInputStream().readAllBytes();
            payload = new String(requestBytes, "UTF-8");
        } catch (IOException e) {
            logger.error("Failed to read request body: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to read request body");
        }

        // Check if the content is XML and convert it to JSON if needed
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/xml")) {
            logger.debug("Received XML content, attempting to process as raw payload");
            // For XML content, we'll just use the raw payload and trust Stripe's signature verification
            // This is because Stripe's webhook signature is based on the raw payload
        }

        // Verify that the webhook secret is configured
        if (endpointSecret == null || endpointSecret.isEmpty()) {
            logger.error("Stripe webhook secret is not configured");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook secret not configured");
        }

        Event event;
        try {
            logger.debug("Payload length: {}", payload.length());
            // Log first 50 chars of payload for debugging (avoid logging entire payload for security)
            if (payload.length() > 0) {
                logger.debug("Payload starts with: {}", payload.substring(0, Math.min(50, payload.length())) + "...");
            }
            logger.debug("Signature: {}", sigHeader);
            logger.debug("Endpoint Secret: {}", endpointSecret.substring(0, 10) + "...");

            // Construct the event with the raw payload
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            logger.error("Invalid signature: {}", e.getMessage());
            logger.error("This could be due to:");
            logger.error("1. Incorrect webhook secret in application.properties");
            logger.error("2. Payload being modified before verification");
            logger.error("3. Request not coming directly from Stripe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            // Other errors
            logger.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
        logger.info("Received event {}", event.getType());
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);
                if (paymentIntent != null) {
                    // Fulfill the purchase, e.g., update reservation status
                    logger.info("Received payment intent. Now creating a new reservation");

                    // Get the metadata and add the payment_intent_id and charge_id
                    Map<String, String> bookingInfo = paymentIntent.getMetadata();
                    bookingInfo.put("payment_intent", paymentIntent.getId());

                    // Get the first charge ID if available
                    if (paymentIntent.getLatestChargeObject() != null) {
                        bookingInfo.put("charge_id", paymentIntent.getLatestChargeObject().getId());
                        logger.info("Added charge ID: {}", paymentIntent.getLatestChargeObject().getId());
                    } else {
                        logger.warn("No charge ID found in payment intent");
                    }

                    bookingService.completeBooking(bookingInfo);
                }
                break;
            case "payment_intent.payment_failed":
                // Handle failed payment
                break;
            case "refund.updated":
                // Handle successful refund
                Refund refund = (Refund) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);
                if (refund != null && "succeeded".equals(refund.getStatus())) {
                    logger.info("Received refund confirmation: {}", refund.getId());
                    // Convert amount from cents to dollars
                    Double refundAmount = refund.getAmount() / 100.0;
                    bookingService.confirmRefund(refund.getId(), refundAmount);
                }
                break;

            // ... handle other event types
            default:
                // Unexpected event type
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unhandled event type");
        }

        return ResponseEntity.ok("Event received");
    }
}
