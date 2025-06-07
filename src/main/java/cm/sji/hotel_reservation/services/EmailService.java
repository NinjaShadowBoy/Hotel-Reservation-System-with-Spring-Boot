package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.sending.enabled}")
    private boolean emailSendingEnabled;

    @Value("${email.template.confirmation}")
    private String confirmationTemplatePath;

    @Value("${email.template.cancellation}")
    private String cancellationTemplatePath;

    @Value("${email.template.refund}")
    private String refundTemplatePath;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends a booking confirmation email to the client
     *
     * @param booking The booking that was confirmed
     */
    public void sendBookingConfirmationEmail(Booking booking) {
        if (!emailSendingEnabled) {
            logger.info("Email sending is disabled. Would have sent confirmation email for booking ID: {}", booking.getId());
            return;
        }

        User client = booking.getClient();
        RoomType roomType = booking.getRoomType();
        Hotel hotel = roomType.getHotel();

        Context context = new Context(Locale.getDefault());
        context.setVariable("booking", booking);
        context.setVariable("client", client);
        context.setVariable("roomType", roomType);
        context.setVariable("hotel", hotel);
        context.setVariable("formattedCheckinDate",
                booking.getCheckinDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
        context.setVariable("formattedBookingDate",
                booking.getDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));

        String emailContent = templateEngine.process(confirmationTemplatePath, context);

        try {
            sendEmail(client.getEmail(), "Your Hotel Reservation Confirmation", emailContent);
            logger.info("Sent booking confirmation email to {} for booking ID: {}", client.getEmail(), booking.getId());
        } catch (MessagingException e) {
            logger.error("Failed to send booking confirmation email: {}", e.getMessage());
        }
    }

    /**
     * Sends a booking cancellation email to the client
     *
     * @param booking The booking that was cancelled
     */
    public void sendBookingCancellationEmail(Booking booking) {
        if (!emailSendingEnabled) {
            logger.info("Email sending is disabled. Would have sent cancellation email for booking ID: {}", booking.getId());
            return;
        }

        User client = booking.getClient();
        RoomType roomType = booking.getRoomType();
        Hotel hotel = roomType.getHotel();

        Context context = new Context(Locale.getDefault());
        context.setVariable("booking", booking);
        context.setVariable("client", client);
        context.setVariable("roomType", roomType);
        context.setVariable("hotel", hotel);
        context.setVariable("formattedCheckinDate",
                booking.getCheckinDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
        context.setVariable("formattedCancellationDate",
                booking.getCancellationDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")));
        context.setVariable("refundAmount", booking.getRefundAmount());

        String emailContent = templateEngine.process(cancellationTemplatePath, context);

        try {
            sendEmail(client.getEmail(), "Your Hotel Reservation Cancellation Confirmation", emailContent);
            logger.info("Sent booking cancellation email to {} for booking ID: {}", client.getEmail(), booking.getId());
        } catch (MessagingException e) {
            logger.error("Failed to send booking cancellation email: {}", e.getMessage());
        }
    }

    /**
     * Sends a refund confirmation email to the client
     *
     * @param booking The booking for which the refund was processed
     */
    public void sendRefundConfirmationEmail(Booking booking) {
        if (!emailSendingEnabled) {
            logger.info("Email sending is disabled. Would have sent refund confirmation email for booking ID: {}", booking.getId());
            return;
        }

        User client = booking.getClient();
        RoomType roomType = booking.getRoomType();
        Hotel hotel = roomType.getHotel();

        Context context = new Context(Locale.getDefault());
        context.setVariable("booking", booking);
        context.setVariable("client", client);
        context.setVariable("roomType", roomType);
        context.setVariable("hotel", hotel);
        context.setVariable("formattedCheckinDate",
                booking.getCheckinDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
        context.setVariable("formattedCancellationDate",
                booking.getCancellationDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")));
        context.setVariable("refundAmount", booking.getRefundAmount());

        String emailContent = templateEngine.process(refundTemplatePath, context);

        try {
            sendEmail(client.getEmail(), "Your Refund Confirmation", emailContent);
            logger.info("Sent refund confirmation email to {} for booking ID: {}", client.getEmail(), booking.getId());
        } catch (MessagingException e) {
            logger.error("Failed to send refund confirmation email: {}", e.getMessage());
        }
    }

    /**
     * Helper method to send an email
     */
    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true indicates HTML content

        mailSender.send(message);
    }
}
