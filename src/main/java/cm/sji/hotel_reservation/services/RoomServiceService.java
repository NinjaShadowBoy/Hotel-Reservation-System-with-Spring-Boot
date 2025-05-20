package cm.sji.hotel_reservation.services;


import cm.sji.hotel_reservation.entities.RoomService;
import cm.sji.hotel_reservation.repositories.RoomServiceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceService {

    private final RoomServiceRepo roomServiceRepo;

    public List<RoomService> getAllServices() {
        return roomServiceRepo.findAll();
    }
}
