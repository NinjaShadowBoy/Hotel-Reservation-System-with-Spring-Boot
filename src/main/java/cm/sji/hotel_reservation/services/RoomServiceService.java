package cm.sji.hotel_reservation.services;


import cm.sji.hotel_reservation.entities.RoomService;
import cm.sji.hotel_reservation.repositories.RoomServiceRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomServiceRepo roomServiceRepo;

    public List<RoomService> getAllServices() {
        return roomServiceRepo.findAll();
    }
}
