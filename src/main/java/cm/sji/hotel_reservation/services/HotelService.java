package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.HotelPhoto;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HotelRepo hotelRepo;


    private final OfferRepo offerRepo;

    private final RoomTypeRepo roomTypeRepo;

    private final HotelPhotoRepo hotelPhotoRepo;

    private final UserRepo userRepo;

    @Value("${hotelphoto.upload.dir}")
    private String hotelphotoDir;

    @Value("${roomphoto.upload.dir}")
    private String roomphotoDir;

    public HotelService(HotelRepo hotelRepo, OfferRepo offerRepo, RoomTypeRepo roomTypeRepo, HotelPhotoRepo hotelPhotoRepo, UserRepo userRepo) {
        this.hotelRepo = hotelRepo;
        this.offerRepo = offerRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.hotelPhotoRepo = hotelPhotoRepo;
        this.userRepo = userRepo;
    }

    public ResponseEntity<List<Hotel>> getAllHotelsForDashboard() {
        try {
            List<Hotel> hotels = hotelRepo.findAll(); // Changed from HotelRepo to hotelRepo
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            logger.error("Error fetching hotels", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<HotelDetailsDTO> getAllHotels() {
        try {
            List<Hotel> hotels = hotelRepo.findAll();
            logger.info("Found {} hotels in database", hotels.size());

            return hotels.stream()
                    .map(this::getHotelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in getAllHotels: ", e);
            return Collections.emptyList();
        }
    }

    public List<HotelDetailsDTO> getHotelsByOwnerId(Integer ownerId) {
        try {
            logger.info("Fetching hotels for owner ID: {}", ownerId);
            List<Hotel> hotels = hotelRepo.findByOwner_Id(ownerId);
            logger.info("Found {} hotels for owner ID: {}", hotels.size(), ownerId);
            return hotels.stream().map(this::getHotelDTO).toList();
        } catch (Exception e) {
            logger.error("Error fetching hotels for owner ID {}: {}", ownerId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Hotel addHotel(Integer ownerId, String name, String location, String description, Float rating) {
        logger.info("Attempting to add hotel - Owner: {}, Name: {}", ownerId, name);

        // Verify owner exists
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + ownerId + " not found"));

        if(!owner.getRole().equals(User.UserRole.OWNER)) {
            throw new IllegalArgumentException("User with ID " + ownerId + " is not an owner");
        }

        Hotel newHotel = Hotel.builder()
                .name(name)
                .location(location)
                .description(description)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        logger.info("Saving hotel: {}", newHotel);
        Hotel savedHotel = hotelRepo.save(newHotel);
        logger.info("Hotel saved with ID: {}", savedHotel.getId());

        return savedHotel;
    }


    private HotelDetailsDTO getHotelDTO(Hotel hotel) {
        try {
            if (hotel == null) {
                logger.error("Hotel is null");
                throw new IllegalArgumentException("Hotel cannot be null");
            }

            logger.info("Converting hotel to DTO: {}", hotel.getId());

            // Log hotel details
            logger.info("Hotel details - Name: {}, Location: {}, Rating: {}",
                hotel.getName(), hotel.getLocation(), hotel.getRating());

            List<RoomType> roomTypes = roomTypeRepo.findByHotel(hotel);
            logger.info("Found {} room types for hotel {}", roomTypes.size(), hotel.getId());

            // Log room types
            roomTypes.forEach(roomType ->
                logger.info("Room type: id={}, label={}, price={}",
                    roomType.getId(), roomType.getLabel(), roomType.getPrice())
            );

            Set<String> amenities = new HashSet<>();
            for (RoomType roomType : roomTypes) {
                try {
                    var offers = offerRepo.findByRoomType(roomType);
                    logger.info("Found {} offers for room type {}", offers.size(), roomType.getId());

                    for (var offer : offers) {
                        try {
                            var service = offer.getRoomService();
                            if (service != null) {
                                String amenity = service.getLabel() + ":" +
                                        service.getFontawsome_icon_class() + ":" +
                                        service.getId();
                                amenities.add(amenity);
                                logger.info("Added amenity: {}", amenity);
                            } else {
                                logger.warn("Room service is null for offer {}", offer.getRoomType().getId());
                            }
                        } catch (Exception e) {
                            logger.error("Error processing offer {}: {}", offer.getRoomType().getId(), e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error fetching offers for room type {}: {}", roomType.getId(), e.getMessage());
                }
            }

            var photo = hotelPhotoRepo.findByHotel(hotel).orElse(null);
            logger.info("Hotel photo found: {}", photo != null);

            String imageUrl;
            if (photo != null && photo.getFilename() != null) {
                imageUrl = "/" + hotelphotoDir + "/" + photo.getFilename();
                logger.info("Using photo: {}", imageUrl);
            } else {
                imageUrl = "/" + hotelphotoDir + "/placeholder.png";
                logger.info("Using placeholder image: {}", imageUrl);
            }

            Double lowestPrice = roomTypes.stream()
                    .map(RoomType::getPrice)
                    .filter(Objects::nonNull)
                    .min(Double::compare)
                    .orElse(100.0);
            logger.info("Lowest price: {}", lowestPrice);
            String ownerInfo = "Unknown";
            if (hotel.getOwner() != null) {
                ownerInfo = hotel.getOwner().getEmail(); // or getName() if you have that field
            }

            HotelDetailsDTO dto = HotelDetailsDTO.builder()
                    .id(hotel.getId())
                    .name(hotel.getName() != null ? hotel.getName() : "Unnamed Hotel")
                    .image(imageUrl)
                    .location(hotel.getLocation() != null ? hotel.getLocation() : "Location not specified")
                    .lowestPrice(lowestPrice)
                    .description(hotel.getDescription() != null ? hotel.getDescription() : "")
                    .rating(hotel.getRating() != null ? hotel.getRating() : 0.0f)
                    .services(amenities)
                    .ownerEmail(hotel.getOwner() != null ? hotel.getOwner().getEmail() : null)
                    .build();

            logger.info("Successfully created DTO for hotel {}", hotel.getId());
            return dto;

        } catch (Exception e) {
            logger.error("Error converting hotel {} to DTO: {}", hotel != null ? hotel.getId() : "null", e.getMessage(), e);
            throw e;
        }
    }

    public HotelDetailsDTO getHotel(Integer hotelId) {
        return getHotelDTO(hotelRepo.findById(hotelId).orElseThrow());
    }
}
