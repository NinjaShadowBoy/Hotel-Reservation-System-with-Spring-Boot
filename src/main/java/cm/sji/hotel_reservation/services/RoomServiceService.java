package cm.sji.hotel_reservation.services;


import cm.sji.hotel_reservation.entities.RoomService;
import cm.sji.hotel_reservation.repositories.RoomServiceRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomServiceRepo roomServiceRepo;

    private List<RoomService> getAllServices() {
        return roomServiceRepo.findAll();
    }

    public Set<String> allServices() {
        return getAllServices().stream().map(service -> service.getLabel() + ":"
                + service.getFontawsome_icon_class() + ":"
                + service.getId()).collect(Collectors.toSet());
    }
}
