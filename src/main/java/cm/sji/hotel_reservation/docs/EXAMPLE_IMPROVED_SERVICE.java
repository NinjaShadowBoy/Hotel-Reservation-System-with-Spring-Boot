//package cm.sji.hotel_reservation.docs;
//
//import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
//import cm.sji.hotel_reservation.entities.Hotel;
//import cm.sji.hotel_reservation.entities.HotelPhoto;
//import cm.sji.hotel_reservation.entities.RoomType;
//import cm.sji.hotel_reservation.exceptions.ResourceNotFoundException;
//import cm.sji.hotel_reservation.repositories.HotelPhotoRepo;
//import cm.sji.hotel_reservation.repositories.HotelRepo;
//import cm.sji.hotel_reservation.repositories.OfferRepo;
//import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * This is an example of an improved service implementation that follows best practices.
// * It is not meant to be used in production, but rather to demonstrate how to implement
// * the best practices outlined in the BEST_PRACTICES.md document.
// */
//@Service
//@RequiredArgsConstructor // Lombok annotation to generate constructor with required fields
//public class EXAMPLE_IMPROVED_SERVICE {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    // Using constructor injection (via @RequiredArgsConstructor) for required dependencies
//    private final HotelRepo hotelRepo;
//    private final OfferRepo offerRepo;
//    private final RoomTypeRepo roomTypeRepo;
//    private final HotelPhotoRepo hotelPhotoRepo;
//
//    @Value("${hotelphoto.upload.dir}")
//    private String hotelphotoDir;
//
//    @Value("${roomphoto.upload.dir}")
//    private String roomphotoDir;
//
//    /**
//     * Get all hotels.
//     * This method demonstrates:
//     * - Proper logging
//     * - Caching for performance
//     * - Transactional annotation
//     * - Functional programming with streams
//     *
//     * @return List of hotel DTOs
//     */
//    @Transactional(readOnly = true) // Read-only transaction for better performance
//    @Cacheable("hotels") // Cache the result to improve performance
//    public List<HotelDetailsDTO> getAllHotels() {
//        logger.info("Fetching all hotels");
//        List<Hotel> hotels = hotelRepo.findAll();
//        logger.debug("Found {} hotels", hotels.size());
//
//        return hotels.stream()
//                .map(this::getHotelDTO)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Get a hotel by ID.
//     * This method demonstrates:
//     * - Proper exception handling with custom exceptions
//     * - Transactional annotation
//     * - Proper logging
//     *
//     * @param hotelId The ID of the hotel to retrieve
//     * @return Hotel DTO
//     * @throws ResourceNotFoundException if hotel not found
//     */
//    @Transactional(readOnly = true)
//    @Cacheable(value = "hotel", key = "#hotelId")
//    public HotelDetailsDTO getHotel(Integer hotelId) {
//        logger.info("Fetching hotel with ID: {}", hotelId);
//
//        // Using orElseThrow with a custom exception
//        Hotel hotel = hotelRepo.findById(hotelId)
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));
//
//        logger.debug("Found hotel: {}", hotel.getName());
//        return getHotelDTO(hotel);
//    }
//
//    /**
//     * Convert a Hotel entity to a HotelDetailsDTO.
//     * This method demonstrates:
//     * - Proper error handling
//     * - Null checks
//     * - Functional programming with streams
//     * - Clean code practices
//     *
//     * @param hotel The hotel entity to convert
//     * @return Hotel DTO
//     */
//    private HotelDetailsDTO getHotelDTO(Hotel hotel) {
//        if (hotel == null) {
//            logger.warn("Attempted to convert null hotel to DTO");
//            throw new IllegalArgumentException("Hotel cannot be null");
//        }
//
//        // Fetch room types for the hotel
//        List<RoomType> roomTypes = roomTypeRepo.findByHotel(hotel);
//        logger.debug("Found {} room types for hotel: {}", roomTypes.size(), hotel.getName());
//
//        // Extract amenities from room types and offers
//        Set<String> amenities = extractAmenities(roomTypes);
//
//        // Get hotel photo
//        String imageUrl = getHotelImageUrl(hotel);
//
//        // Calculate lowest price
//        Double lowestPrice = calculateLowestPrice(roomTypes);
//
//        // Build and return the DTO
//        return HotelDetailsDTO.builder()
//                .id(hotel.getId())
//                .name(hotel.getName())
//                .image(imageUrl)
//                .location(hotel.getLocation())
//                .lowestPrice(lowestPrice)
//                .desc(hotel.getDescription())
//                .rating(hotel.getRating())
//                .services(amenities)
//                .build();
//    }
//
//    /**
//     * Extract amenities from room types.
//     * This method demonstrates:
//     * - Method extraction for better readability
//     * - Functional programming with streams
//     *
//     * @param roomTypes List of room types
//     * @return Set of amenities
//     */
//    private Set<String> extractAmenities(List<RoomType> roomTypes) {
//        return roomTypes.stream()
//                .flatMap(roomType -> offerRepo.findByRoomType(roomType).stream()
//                        .map(offer -> offer.getRoomService().getLabel() + ":" +
//                                offer.getRoomService().getFontawsome_icon_class()))
//                .collect(Collectors.toSet());
//    }
//
//    /**
//     * Get the hotel image URL.
//     * This method demonstrates:
//     * - Method extraction for better readability
//     * - Proper null handling
//     *
//     * @param hotel The hotel entity
//     * @return Image URL
//     */
//    private String getHotelImageUrl(Hotel hotel) {
//        var photo = hotelPhotoRepo.findByHotel(hotel).orElse(null);
//
//        if (photo != null) {
//            return "/" + hotelphotoDir + "/" + photo.getFilename();
//        } else {
//            return "/" + hotelphotoDir + "/placeholder.png";
//        }
//    }
//
//    /**
//     * Calculate the lowest price among room types.
//     * This method demonstrates:
//     * - Method extraction for better readability
//     * - Functional programming with streams
//     * - Proper default value handling
//     *
//     * @param roomTypes List of room types
//     * @return Lowest price
//     */
//    private Double calculateLowestPrice(List<RoomType> roomTypes) {
//        return roomTypes.stream()
//                .map(RoomType::getPrice)
//                .min(Double::compare)
//                .orElse(100.0); // Default price if no room types found
//    }
//}