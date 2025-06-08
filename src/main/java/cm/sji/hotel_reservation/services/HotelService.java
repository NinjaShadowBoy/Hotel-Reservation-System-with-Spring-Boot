package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.HotelDTO;
import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.HotelPhotoRepo;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.OfferRepo;
import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HotelRepo hotelRepo;

    private final OfferRepo offerRepo;

    private final RoomTypeRepo roomTypeRepo;

    private final HotelPhotoRepo hotelPhotoRepo;

    @Value("${hotelphoto.upload.dir}")
    private String hotelphotoDir;

    @Value("${roomphoto.upload.dir}")
    private String roomphotoDir;

    public HotelService(HotelRepo hotelRepo, OfferRepo offerRepo, RoomTypeRepo roomTypeRepo, HotelPhotoRepo hotelPhotoRepo) {
        this.hotelRepo = hotelRepo;
        this.offerRepo = offerRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.hotelPhotoRepo = hotelPhotoRepo;
    }

    public List<HotelDetailsDTO> getAllHotels() {
        return hotelRepo.findAll().stream().map(this::getHotelDTO).toList();
    }

    private HotelDetailsDTO getHotelDTO(Hotel hotel) {
        if (hotel == null) {
            return HotelDetailsDTO.builder()
                    .name("Unknown Hotel")
                    .location("Location not specified")
                    .rating(0F)
                    .desc("No description available")
                    .build();
        }
        List<RoomType> roomTypes = roomTypeRepo.findByHotel(hotel);

        Set<String> amenities = roomTypes.stream().flatMap(roomType ->
                offerRepo.findByRoomType(roomType).stream().map(
                        offer -> offer.getRoomService().getLabel() + ":" +
                                offer.getRoomService().getFontawsome_icon_class() + ":" +
                                offer.getRoomService().getId()
                )
        ).collect(Collectors.toSet());

        var photo = hotelPhotoRepo.findByHotel(hotel).orElse(null);

        String imageUrl = null;
        if (photo != null) {
            imageUrl = "/" + hotelphotoDir + "/" + photo.getFilename();
        } else {
            imageUrl = "/" + hotelphotoDir + "/placeholder.png";
        }

        Double lowestPrice = roomTypes.stream()
                .map(RoomType::getPrice)
                .min(Double::compare)
                .orElse(100.0);

        return HotelDetailsDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .image(imageUrl)
                .location(hotel.getLocation())
                .lowestPrice(lowestPrice)
                .desc(hotel.getDescription())
                .rating(hotel.getRating())
                .services(amenities)
                .build();
    }
    public Hotel createHotel(HotelDTO hotelDTO) {
        if (hotelDTO == null ||
                hotelDTO.getName() == null || hotelDTO.getName().isBlank() ||
                hotelDTO.getLocation() == null || hotelDTO.getLocation().isBlank()) {
            throw new IllegalArgumentException("Invalid hotel data provided");
        }
        Hotel hotel = Hotel.builder()
                .name(hotelDTO.getName())
                .location(hotelDTO.getLocation())
                .rating(hotelDTO.getRating())
                .description(hotelDTO.getDescription())
                // Set owner if needed: .owner(userRepository.findById(hotelDTO.getOwnerId()).orElse(null))
                .build();


        return hotelRepo.save(hotel);
    }

    public HotelDetailsDTO getHotel(Integer hotelId) {
        return getHotelDTO(hotelRepo.findById(hotelId).orElseThrow());
    }
}
