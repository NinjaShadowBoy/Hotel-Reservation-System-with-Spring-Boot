//package cm.sji.hotel_reservation.docs;
//
//import cm.sji.hotel_reservation.dtos.HotelDetailsDTO;
//import cm.sji.hotel_reservation.exceptions.ResourceNotFoundException;
//import cm.sji.hotel_reservation.services.HotelService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * This is an example of an improved REST controller implementation that follows best practices.
// * It is not meant to be used in production, but rather to demonstrate how to implement
// * the best practices outlined in the BEST_PRACTICES.md document.
// */
//@RestController
//@RequestMapping("/api/hotels")
//@Tag(name = "Hotel", description = "Hotel management APIs")
//@RequiredArgsConstructor
//public class EXAMPLE_IMPROVED_CONTROLLER {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final HotelService hotelService;
//
//    /**
//     * Get all hotels.
//     * This endpoint demonstrates:
//     * - Proper API documentation with Swagger/OpenAPI annotations
//     * - Proper response handling with ResponseEntity
//     * - No try-catch blocks (exceptions are handled by GlobalExceptionHandler)
//     *
//     * @return List of hotel DTOs
//     */
//    @GetMapping
//    @Operation(summary = "Get all hotels", description = "Returns a list of all hotels in the system")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotels",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = HotelDetailsDTO.class))),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<List<HotelDetailsDTO>> getAllHotels() {
//        logger.info("REST request to get all hotels");
//        List<HotelDetailsDTO> hotels = hotelService.getAllHotels();
//        return ResponseEntity.ok(hotels);
//    }
//
//    /**
//     * Get a hotel by ID.
//     * This endpoint demonstrates:
//     * - Path variable validation
//     * - Proper API documentation with Swagger/OpenAPI annotations
//     * - Proper response handling with ResponseEntity
//     * - No try-catch blocks (exceptions are handled by GlobalExceptionHandler)
//     *
//     * @param hotelId The ID of the hotel to retrieve
//     * @return Hotel DTO
//     */
//    @GetMapping("/{hotelId}")
//    @Operation(summary = "Get hotel by ID", description = "Returns a hotel based on its ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = HotelDetailsDTO.class))),
//            @ApiResponse(responseCode = "404", description = "Hotel not found"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<HotelDetailsDTO> getHotel(
//            @Parameter(description = "ID of the hotel to retrieve", required = true)
//            @PathVariable Integer hotelId) {
//        logger.info("REST request to get hotel with ID: {}", hotelId);
//        HotelDetailsDTO hotel = hotelService.getHotel(hotelId);
//        return ResponseEntity.ok(hotel);
//    }
//
//    /**
//     * Search hotels by location.
//     * This endpoint demonstrates:
//     * - Request parameter handling
//     * - Proper API documentation with Swagger/OpenAPI annotations
//     * - Proper response handling with ResponseEntity
//     *
//     * @param location The location to search for
//     * @return List of hotel DTOs matching the location
//     */
//    @GetMapping("/search")
//    @Operation(summary = "Search hotels by location", description = "Returns hotels matching the specified location")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotels",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = HotelDetailsDTO.class))),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<List<HotelDetailsDTO>> searchHotelsByLocation(
//            @Parameter(description = "Location to search for", required = true)
//            @RequestParam String location) {
//        logger.info("REST request to search hotels by location: {}", location);
//
//        // This is just an example - in a real implementation, you would call a service method
//        // that searches hotels by location
//        List<HotelDetailsDTO> hotels = hotelService.getAllHotels().stream()
//                .filter(hotel -> hotel.getLocation().toLowerCase().contains(location.toLowerCase()))
//                .toList();
//
//        return ResponseEntity.ok(hotels);
//    }
//
//    /**
//     * Example of a POST endpoint to create a hotel.
//     * This endpoint demonstrates:
//     * - Request body validation
//     * - Proper API documentation with Swagger/OpenAPI annotations
//     * - Proper response handling with ResponseEntity
//     * - Returning appropriate HTTP status code (201 Created)
//     *
//     * @param hotelDTO The hotel to create
//     * @return Created hotel DTO
//     */
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Create a new hotel", description = "Creates a new hotel in the system")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Hotel created successfully",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = HotelDetailsDTO.class))),
//            @ApiResponse(responseCode = "400", description = "Invalid input data"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<HotelDetailsDTO> createHotel(
//            @Parameter(description = "Hotel to create", required = true)
//            @RequestBody HotelDetailsDTO hotelDTO) {
//        logger.info("REST request to create a new hotel: {}", hotelDTO.getName());
//
//        // This is just an example - in a real implementation, you would call a service method
//        // that creates a new hotel
//
//        // Return a 201 Created status with the created hotel
//        return ResponseEntity.status(HttpStatus.CREATED).body(hotelDTO);
//    }
//}