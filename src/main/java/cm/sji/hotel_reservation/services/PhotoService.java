package cm.sji.hotel_reservation.services;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.HotelPhoto;
import cm.sji.hotel_reservation.entities.RoomPhoto;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.repositories.HotelPhotoRepo;
import cm.sji.hotel_reservation.repositories.HotelRepo;
import cm.sji.hotel_reservation.repositories.RoomPhotoRepo;
import cm.sji.hotel_reservation.repositories.RoomTypeRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PhotoService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HotelPhotoRepo hotelPhotoRepo;

    private final RoomPhotoRepo roomPhotoRepo;

    private final HotelRepo hotelRepo;

    private final RoomTypeRepo roomTypeRepo;

    @Value("${hotelphoto.upload.dir}")
    private String hoteluploadDir;

    @Value("${roomphoto.upload.dir}")
    private String roomuploadDir;

    public PhotoService(HotelPhotoRepo photoRepository,
                        RoomPhotoRepo roomPhotoRepo, HotelRepo hotelRepo, RoomTypeRepo roomTypeRepo) {
        this.hotelPhotoRepo = photoRepository;
        this.roomPhotoRepo = roomPhotoRepo;
        this.hotelRepo = hotelRepo;
        this.roomTypeRepo = roomTypeRepo;
    }

    public HotelPhoto saveHotelPhoto(MultipartFile file, Integer hotelId) throws IOException {

        Hotel hotel = hotelRepo.findById(hotelId).orElse(null);


        assert hotel != null;
        // Step 1: Save photo to get ID
        HotelPhoto photo = hotelPhotoRepo.findByHotel(hotel).orElse(null);

        if (photo != null) {
            // Delete hotel photo if already existing
            Files.deleteIfExists(Paths.get(photo.getFilename()));
        } else {
            photo = new HotelPhoto();
            photo.setHotel(hotel);
            photo = hotelPhotoRepo.save(photo);
        }

        // Step 2: Generate filename from ID
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = photo.getId() + " - " + hotel.getName().substring(0, Math.min(20, hotel.getName().length())) +
                (extension.isEmpty() ? "" : "." + extension);

        // Step 3: Save file to disk
        Path filePath = Paths.get(hoteluploadDir, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        photo.setFilename(filename);

        return hotelPhotoRepo.save(photo);
    }

    public RoomPhoto saveRoomPhoto(MultipartFile file, Integer roomTypeId) throws IOException {

        RoomType roomType = roomTypeRepo.findById(roomTypeId).orElse(null);

        assert roomType != null;
        // Step 1: Save photo to get ID
        RoomPhoto photo = roomPhotoRepo.findByRoomType(roomType).orElse(null);

        if (photo != null) {
            // Delete hotel photo if already existing
            Files.deleteIfExists(Paths.get(photo.getFilename()));
        } else {
            photo = new RoomPhoto();
            photo.setRoomType(roomType);
            photo = roomPhotoRepo.save(photo);
        }

        // Step 2: Generate filename from ID
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = photo.getId() + " - " + roomType.getHotel().getName().substring(0, Math.min(20, roomType.getHotel().getName().length())) + " - " +
                roomType.getLabel().substring(0, Math.min(20, roomType.getLabel().length())) + (extension.isEmpty() ? "" : "." + extension);

        // Step 3: Save file to disk
        Path filePath = Paths.get(hoteluploadDir, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        photo.setFilename(filename);

        return roomPhotoRepo.save(photo);
    }

    public List<HotelPhoto> getAllHotelPhotos() {
        return hotelPhotoRepo.findAll();
    }

    public List<HotelPhoto> getAllRoomPhotos() {
        return hotelPhotoRepo.findAll();
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
