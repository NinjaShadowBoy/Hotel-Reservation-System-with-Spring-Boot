package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.RoomTypeDTO;
import cm.sji.hotel_reservation.entities.Offer;
import cm.sji.hotel_reservation.entities.RoomService;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomTypeRepo roomTypeRepo;

    private final RoomPhotoRepo roomPhotoRepo;

    private final OfferRepo offerRepo;

    private final HotelRepo hotelRepo;

    private final RoomServiceRepo roomServiceRepo;

    @Value("${roomphoto.upload.dir}")
    private String roomphotoDir;

    public RoomTypeService(RoomTypeRepo roomTypeRepo, RoomPhotoRepo roomPhotoRepo, OfferRepo offerRepo, HotelRepo hotelRepo, RoomServiceRepo roomServiceRepo) {
        this.roomTypeRepo = roomTypeRepo;
        this.roomPhotoRepo = roomPhotoRepo;
        this.offerRepo = offerRepo;
        this.hotelRepo = hotelRepo;
        this.roomServiceRepo = roomServiceRepo;
    }

//    public Set<String> toStringSet(Object obj) {
//        Set<String> result = new HashSet<>();
//        if (obj instanceof Set<?>) {
//            for (Object o : (Set<?>) obj) {
//                if (o instanceof String) {
//                    result.add((String) o);
//                }
//            }
//        }
//        return result;
//    }

    @Transactional
    public RoomType addRoomType(Integer hotelId, String label, Double price, Integer totalNumber, Set<String> services) {
        try {
            logger.info("Starting addRoomType: hotelId={}, label='{}', price={}, totalNumber={}, services={}",
                    hotelId, label, price, totalNumber, services);

            // Validate hotel exists
            logger.info("Looking up hotel with ID: {}", hotelId);
            var hotel = hotelRepo.findById(hotelId);
            if (hotel.isEmpty()) {
                logger.error("Hotel not found with ID: {}", hotelId);
                throw new IllegalArgumentException("Hotel not found with ID: " + hotelId);
            }
            logger.info("Found hotel: {}", hotel.get().getId());

            // Create room type
            logger.info("Creating room type entity");
            RoomType roomType = RoomType.builder()
                    .hotel(hotel.get())
                    .label(label)
                    .price(price)
                    .totalNumber(totalNumber)
                    .numberAvailable(totalNumber) // Initially all rooms are available
                    .build();

            // Save room type first to get its ID
            logger.info("Saving room type to database");
            roomType = roomTypeRepo.save(roomType);
            logger.info("Saved room type with ID: {}", roomType.getId());

            // Process services
            if (services != null && !services.isEmpty()) {
                logger.info("Processing {} services", services.size());
                int successCount = 0;
                int failCount = 0;

                for (String serviceLabel : services) {
                    try {
                        logger.info("Processing service: '{}'", serviceLabel);

                        // Find the room service by label
                        RoomService roomService = roomServiceRepo.findByLabel(serviceLabel);
                        if (roomService == null) {
                            logger.warn("Room service not found for label: '{}'", serviceLabel);
                            failCount++;
                            continue;
                        }
                        logger.info("Found room service with ID: {} for label: '{}'", roomService.getId(), serviceLabel);

                        // Check if offer already exists
                        boolean offerExists = offerRepo.existsByRoomTypeAndRoomService(roomType, roomService);
                        if (offerExists) {
                            logger.info("Offer already exists for room type {} and service {}", roomType.getId(), serviceLabel);
                            successCount++;
                            continue;
                        }

                        // Create and save the offer
                        Offer offer = Offer.builder()
                                .roomType(roomType)
                                .roomService(roomService)
                                .build();

                        offer = offerRepo.save(offer);
                        logger.info("Created offer with ID: {} for service '{}' and room type {}",
                                offer.getRoomType(), serviceLabel, roomType.getId());
                        successCount++;

                    } catch (Exception e) {
                        logger.error("Error adding service '{}' to room type {}: {}",
                                serviceLabel, roomType.getId(), e.getMessage(), e);
                        failCount++;
                    }
                }

                logger.info("Service processing completed: {} success, {} failed", successCount, failCount);
                
                // If all services failed, log a warning but don't fail the operation
                if (successCount == 0 && failCount > 0) {
                    logger.warn("No services were successfully added to room type {}", roomType.getId());
                }
            } else {
                logger.info("No services to process for room type {}", roomType.getId());
            }

            logger.info("Successfully completed addRoomType for room type ID: {}", roomType.getId());
            return roomType;

        } catch (Exception e) {
            logger.error("Error in addRoomType: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add room type: " + e.getMessage(), e);
        }
    }

    public List<RoomTypeDTO> getRoomTypes(Integer hotelId) {
        try {
            logger.info("Fetching roomtypes for hotel ID: {}", hotelId);
            List<RoomType> roomTypes = roomTypeRepo.findByHotel_Id(hotelId);
            return roomTypes.stream().map(this::getRoomTypeDTO).collect(Collectors.toList());
        }catch (Exception e){
            logger.error("Error fetching roomtypes for hotel ID {}: {}", hotelId, e.getMessage(), e);
            throw e;
        }
    }

    private RoomTypeDTO getRoomTypeDTO(RoomType roomType) {
        // Find the room photo
        var photo = roomPhotoRepo.findByRoomType(roomType).orElse(null);

        // Get the amenities of a room formated correctly.
        Set<String> amenities = offerRepo.findByRoomType(roomType).stream().map(
                offer -> offer.getRoomService().getLabel() + ":" +
                        offer.getRoomService().getFontawsome_icon_class()
        ).collect(Collectors.toSet());

        // Construct the photo link.
        String imageUrl;
        if (photo != null) {
            imageUrl = "/" + roomphotoDir + "/" + photo.getFilename();
        }else{
            imageUrl = "/" + roomphotoDir + "/placeholder.png";
        }

        // Build the dto and return it.
        return RoomTypeDTO.builder()
                .id(roomType.getId())
                .image(imageUrl)
                .label(roomType.getLabel())
                .services(amenities)
                .numberAvailable(roomType.getNumberAvailable())
                .totalNumber(roomType.getTotalNumber())
                .price(roomType.getPrice())
                .build();
    }
}
