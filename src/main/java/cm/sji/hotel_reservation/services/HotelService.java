package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepo hotelRepository;
    private final ReviewService reviewService;

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> findById(Integer id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> findByOwner(String owner) {
        return hotelRepository.findByOwner(owner);
    }

    public List<Hotel> findByName(String name) {
        return hotelRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Hotel> findByLocation(String location) {
        return hotelRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Hotel> findByMinRating(Double minRating) {
        return hotelRepository.findByRatingGreaterThanEqual(minRating);
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    public Hotel updateHotel(Hotel hotel) {
        // Update hotel rating based on reviews
        Double avgRating = reviewService.getAverageRatingForHotel(hotel.getId());
        if (avgRating != null) {
            hotel.setRating(avgRating.floatValue());
        }
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Integer id) {
        hotelRepository.deleteById(id);
    }

    public boolean isHotelOwner(Integer hotelId,Integer userId) {
        return hotelRepository.findById(hotelId)
                .map(hotel -> hotel.getOwner().getId().equals(userId))
                .orElse(false);
    }
}