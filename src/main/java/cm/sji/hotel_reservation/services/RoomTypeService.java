package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepo roomTypeRepository;

    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public Optional<RoomType> findById(Integer id) {
        return roomTypeRepository.findById(id);
    }

    public List<RoomType> findByHotelId(Integer hotelId) {
        return roomTypeRepository.findByHotelId(hotelId);
    }

    public List<RoomType> findByHotel(Hotel hotel) {
        return roomTypeRepository.findByHotel(hotel);
    }

    public List<RoomType> findAvailableRoomsByHotelId(Integer hotelId) {
        return roomTypeRepository.findAvailableRoomsByHotelId(hotelId);
    }

    public List<RoomType> findByPriceLessThanEqual(Double maxPrice) {
        return roomTypeRepository.findByPriceLessThanEqual(maxPrice);
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public RoomType updateRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public void deleteRoomType(Integer id) {
        roomTypeRepository.deleteById(id);
    }

    public boolean isRoomAvailable(Integer roomTypeId, int numberOfRooms) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(roomTypeId);
        return roomTypeOpt.filter(roomType -> roomType.getNumberAvailable() >= numberOfRooms).isPresent();
    }

    public void updateRoomAvailability(Integer roomTypeId, int change) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(roomTypeId);
        if (roomTypeOpt.isPresent()) {
            RoomType roomType = roomTypeOpt.get();
            roomType.setNumberAvailable(roomType.getNumberAvailable() + change);
            roomTypeRepository.save(roomType);
        }
    }
}
