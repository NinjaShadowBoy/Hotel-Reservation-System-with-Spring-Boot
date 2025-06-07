package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.dtos.RoomTypeDTO;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.OfferRepo;
import cm.sji.hotel_reservation.repositories.RoomPhotoRepo;
import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomTypeRepo roomTypeRepo;

    private final RoomPhotoRepo roomPhotoRepo;

    private final OfferRepo offerRepo;

    @Value("${roomphoto.upload.dir}")
    private String roomphotoDir;

    public RoomTypeService(RoomTypeRepo roomTypeRepo, RoomPhotoRepo roomPhotoRepo, OfferRepo offerRepo) {
        this.roomTypeRepo = roomTypeRepo;
        this.roomPhotoRepo = roomPhotoRepo;
        this.offerRepo = offerRepo;
    }

    public List<RoomTypeDTO> getRoomTypes(Integer hotelId) {
        List<RoomType> roomTypes = roomTypeRepo.findByHotel_Id(hotelId);
        return roomTypes.stream().map(this::getRoomTypeDTO).collect(Collectors.toList());
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
                .price(roomType.getPrice())
                .build();
    }
}
