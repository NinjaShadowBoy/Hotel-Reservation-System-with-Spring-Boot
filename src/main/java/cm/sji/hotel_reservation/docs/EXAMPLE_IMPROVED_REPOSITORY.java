//package cm.sji.hotel_reservation.docs;
//
//import cm.sji.hotel_reservation.entities.Hotel;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * This is an example of an improved repository interface that follows best practices.
// * It is not meant to be used in production, but rather to demonstrate how to implement
// * the best practices outlined in the BEST_PRACTICES.md document.
// */
//@Repository
//public interface EXAMPLE_IMPROVED_REPOSITORY extends JpaRepository<Hotel, Integer>, JpaSpecificationExecutor<Hotel> {
//
//    /**
//     * Find hotels by location.
//     * This method demonstrates:
//     * - Derived query method naming convention
//     *
//     * @param location The location to search for
//     * @return List of hotels in the specified location
//     */
//    List<Hotel> findByLocation(String location);
//
//    /**
//     * Find hotels with a rating greater than or equal to the specified value.
//     * This method demonstrates:
//     * - Derived query method naming convention with comparison operators
//     *
//     * @param rating The minimum rating
//     * @return List of hotels with a rating greater than or equal to the specified value
//     */
//    List<Hotel> findByRatingGreaterThanEqual(Float rating);
//
//    /**
//     * Find hotels by owner.
//     * This method demonstrates:
//     * - Derived query method naming convention
//     *
//     * @param owner The owner to search for
//     * @return List of hotels owned by the specified owner
//     */
//    List<Hotel> findByOwner(String owner);
//
//    /**
//     * Search hotels by name or location.
//     * This method demonstrates:
//     * - Custom JPQL query with @Query annotation
//     * - Named parameters with @Param
//     * - Case-insensitive search
//     *
//     * @param keyword The keyword to search for
//     * @return List of hotels matching the search criteria
//     */
//    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Hotel> searchHotels(@Param("keyword") String keyword);
//
//    /**
//     * Find a hotel by ID with eager loading of related entities.
//     * This method demonstrates:
//     * - @EntityGraph for optimizing fetching of related entities
//     * - Avoiding N+1 query problems
//     *
//     * @param id The hotel ID
//     * @return Optional containing the hotel if found
//     */
//    @EntityGraph(attributePaths = {"roomTypes"})
//    Optional<Hotel> findWithRoomTypesById(Integer id);
//
//    /**
//     * Find hotels with pagination.
//     * This method demonstrates:
//     * - Pagination with Pageable parameter
//     * - Returning Page<T> for paginated results
//     *
//     * @param pageable Pagination information
//     * @return Page of hotels
//     */
//    @Override
//    Page<Hotel> findAll(Pageable pageable);
//
//    /**
//     * Find hotels by location with pagination.
//     * This method demonstrates:
//     * - Combining derived query methods with pagination
//     *
//     * @param location The location to search for
//     * @param pageable Pagination information
//     * @return Page of hotels in the specified location
//     */
//    Page<Hotel> findByLocation(String location, Pageable pageable);
//
//    /**
//     * Count hotels by location.
//     * This method demonstrates:
//     * - Count query method
//     *
//     * @param location The location to count hotels for
//     * @return Number of hotels in the specified location
//     */
//    long countByLocation(String location);
//
//    /**
//     * Find top rated hotels.
//     * This method demonstrates:
//     * - Limiting results with Top keyword
//     * - Ordering results with OrderBy
//     *
//     * @return List of top 5 hotels ordered by rating
//     */
//    List<Hotel> findTop5ByOrderByRatingDesc();
//
//    /**
//     * Find hotels by name and location.
//     * This method demonstrates:
//     * - Combining multiple criteria in a derived query method
//     *
//     * @param name The hotel name
//     * @param location The hotel location
//     * @return List of hotels matching both criteria
//     */
//    List<Hotel> findByNameAndLocation(String name, String location);
//
//    /**
//     * Find hotels by name or location.
//     * This method demonstrates:
//     * - Using OR condition in a derived query method
//     *
//     * @param name The hotel name
//     * @param location The hotel location
//     * @return List of hotels matching either criteria
//     */
//    List<Hotel> findByNameOrLocation(String name, String location);
//
//    /**
//     * Find hotels with a native SQL query.
//     * This method demonstrates:
//     * - Using native SQL queries for complex or performance-critical operations
//     *
//     * @param rating The minimum rating
//     * @return List of hotels with a rating greater than the specified value
//     */
//    @Query(value = "SELECT * FROM hotel h WHERE h.rating > :rating", nativeQuery = true)
//    List<Hotel> findHotelsWithRatingGreaterThan(@Param("rating") Float rating);
//}