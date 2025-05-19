-- Hotel Reservation System Mock Data
-- Generated on May 19, 2025

-- Disable foreign key checks temporarily to allow for easier inserts
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data (if any)
TRUNCATE TABLE `booking`;
TRUNCATE TABLE `faq`;
TRUNCATE TABLE `hotel_photo`;
TRUNCATE TABLE `offer`;
TRUNCATE TABLE `review`;
TRUNCATE TABLE `room_photo`;
TRUNCATE TABLE `room_service`;
TRUNCATE TABLE `room_type`;
TRUNCATE TABLE `hotel`;
TRUNCATE TABLE `user`;

-- Insert user data
INSERT INTO `user` (`id`, `active`, `created_at`, `email`, `first_name`, `last_name`, `password`, `role`, `updated_at`) VALUES
(1, b'1', '2024-12-15 10:30:00', 'john.doe@example.com', 'John', 'Doe', '$2a$10$xJhFhUAa8IEeuFnqU1XnEO4lP5PY2XJ9wv.cLfpxnxiJk6W8fseCK', 'USER', '2025-05-15 14:25:00'),
(2, b'1', '2025-01-05 09:45:00', 'jane.smith@example.com', 'Jane', 'Smith', '$2a$10$j7Lg8sfPW.SxLaVB9Ioz1uc78RkVbcVR6xw7jU57Gd4BjOieWnBaS', 'USER', NULL),
(3, b'1', '2025-01-20 11:15:00', 'bob.johnson@example.com', 'Bob', 'Johnson', '$2a$10$dX2GITj6xGzVb7qx8Vz3/e/tRlJOQdgVbZmP06SrX7vL1.C5LfJHa', 'USER', '2025-05-01 16:10:00'),
(4, b'1', '2025-02-10 14:20:00', 'alice.williams@example.com', 'Alice', 'Williams', '$2a$10$yIX5dPz0sMx0f.Gb3Q5vTefD4XnxGPnDk6QlIqEgvVrM0x8N5b4fi', 'USER', NULL),
(5, b'1', '2025-02-25 16:30:00', 'michael.brown@example.com', 'Michael', 'Brown', '$2a$10$4O2NYzQbOz9hqXtQWuDmDeE3.jxeJZ7xGDcbTKrJwMuRiKlPVMOlq', 'USER', '2025-04-15 10:45:00'),
(6, b'1', '2025-03-10 08:15:00', 'sarah.miller@luxhotels.com', 'Sarah', 'Miller', '$2a$10$BO7RP6HRTzOsxuI4A9Bq0eaItVQ8TnRJtKg4qVKtV2g8uY7RTQysu', 'HOTEL_MANAGER', '2025-05-10 11:30:00'),
(7, b'1', '2025-03-12 10:00:00', 'david.garcia@seasideresorts.com', 'David', 'Garcia', '$2a$10$zzFR.c3GsqGFTfWDkC9Qce5H1sPTF4rBLzXhD6.NRQJ/EGOVRoTxC', 'HOTEL_MANAGER', NULL),
(8, b'1', '2025-03-15 13:45:00', 'emma.wilson@mountainretreats.com', 'Emma', 'Wilson', '$2a$10$WQp1KI5AH3g6iH0jLgCB/.NSWPMlgr3R4Y5IfGM5fMV.qXMW7eIgS', 'HOTEL_MANAGER', '2025-04-20 09:15:00'),
(9, b'1', '2025-03-20 15:30:00', 'james.taylor@citystays.com', 'James', 'Taylor', '$2a$10$zADnK1oKpQf/fjnSrBnMV.ZICGEkVnUC1R3R3f3gB9t2pWs3sHPra', 'HOTEL_MANAGER', NULL),
(10, b'1', '2025-01-02 11:00:00', 'admin@hotelsystem.com', 'System', 'Admin', '$2a$10$8sJgq5EBcZrXXBpDvOiM2e5VHt3DAiX9UWMpD9jz8Dj6Z7NzUWW0G', 'ADMIN', '2025-05-01 08:30:00');

-- Insert hotel data
INSERT INTO `hotel` (`id`, `created_at`, `description`, `location`, `name`, `owner`, `rating`, `updated_at`) VALUES
(1, '2025-01-10 09:00:00', 'Luxury Hotel offers an unparalleled experience with panoramic city views. Our elegant rooms are designed for comfort and style, featuring premium amenities and modern conveniences. The hotel boasts multiple fine dining options, a world-class spa, fitness center, and an infinity rooftop pool. Located in the heart of downtown, you'll find yourself just steps away from shopping, entertainment, and cultural attractions. Our dedicated staff ensures personalized service that exceeds expectations.', 'New York, NY', 'Luxury Grand Hotel', 'Sarah Miller', 4.8, '2025-05-10 14:30:00'),
(2, '2025-01-15 10:30:00', 'Seaside Resort & Spa is a beachfront paradise where relaxation meets luxury. Wake up to stunning ocean views from your private balcony. Our resort features spacious suites with coastal-inspired décor, multiple swimming pools, and direct beach access. Indulge in our award-winning seafood restaurant or enjoy a tropical cocktail at the beach bar. Our world-renowned spa offers rejuvenating treatments inspired by the sea. Water sports, beach yoga, and guided excursions are available for adventure seekers.', 'Miami Beach, FL', 'Seaside Resort & Spa', 'David Garcia', 4.7, '2025-04-25 11:15:00'),
(3, '2025-02-01 08:45:00', 'Mountain Retreat Lodge is nestled among towering pines with breathtaking views of snow-capped peaks. Our rustic-chic accommodations blend natural elements with modern luxury. Each cabin features a fireplace, premium bedding, and a private hot tub on the deck. The property includes hiking trails, a serene lake for fishing, and a cozy restaurant serving farm-to-table cuisine. In winter, enjoy ski-in/ski-out access to nearby slopes. Our activities center offers guided nature tours, mountain biking, and stargazing experiences.', 'Aspen, CO', 'Mountain Retreat Lodge', 'Emma Wilson', 4.9, '2025-05-05 16:45:00'),
(4, '2025-02-10 13:15:00', 'City Center Suites offers sophisticated accommodations for business and leisure travelers alike. Our spacious suite-style rooms include fully equipped kitchenettes, dedicated work areas, and premium entertainment systems. Located in the bustling business district, we provide convenient access to corporate offices, convention centers, and public transportation. Amenities include a 24-hour business center, fitness facility, and a trendy rooftop lounge perfect for unwinding after a productive day. Weekly networking events and complimentary breakfast make us a favorite among frequent travelers.', 'Chicago, IL', 'City Center Suites', 'James Taylor', 4.6, '2025-04-20 09:30:00'),
(5, '2025-03-01 11:00:00', 'Vintage Boutique Inn is housed in a meticulously restored 19th-century mansion, offering a charming blend of historic character and modern comforts. Each uniquely decorated room tells a story of the building\'s rich past while providing all contemporary amenities. Enjoy afternoon tea in our lush gardens, browse our extensive library, or relax in the antique-filled common areas. Our gourmet breakfast features locally sourced ingredients and heirloom recipes. Located in a picturesque historic district, you\'ll be within walking distance of museums, galleries, and boutique shops.', 'Charleston, SC', 'Vintage Boutique Inn', 'Sarah Miller', 4.9, '2025-05-15 10:30:00');

-- Insert room_service data (amenities)
INSERT INTO `room_service` (`label`) VALUES
('WiFi'),
('Breakfast'),
('Spa Access'),
('Fitness Center'),
('Swimming Pool'),
('Room Service'),
('Concierge'),
('Parking'),
('Business Center'),
('Airport Shuttle'),
('Laundry Service'),
('Mini Bar'),
('Pet Friendly'),
('Child Care'),
('Restaurant Discount');

-- Insert room_type data
INSERT INTO `room_type` (`id`, `label`, `number_available`, `price`, `total_number`, `fk_hotel`) VALUES
(1, 'Standard Room', 15, 150.00, 20, 1),
(2, 'Deluxe Room', 8, 250.00, 15, 1),
(3, 'Executive Suite', 3, 450.00, 5, 1),
(4, 'Penthouse', 1, 1200.00, 1, 1),
(5, 'Ocean View Room', 12, 300.00, 20, 2),
(6, 'Beach Front Suite', 5, 500.00, 8, 2),
(7, 'Family Villa', 2, 800.00, 4, 2),
(8, 'Mountain View Room', 10, 280.00, 15, 3),
(9, 'Luxury Cabin', 6, 450.00, 12, 3),
(10, 'Premium Lodge', 2, 650.00, 5, 3),
(11, 'Standard Suite', 15, 200.00, 20, 4),
(12, 'Business Suite', 10, 300.00, 15, 4),
(13, 'Executive Apartment', 4, 500.00, 5, 4),
(14, 'Historic Room', 6, 220.00, 8, 5),
(15, 'Garden Suite', 3, 350.00, 5, 5),
(16, 'Heritage Suite', 1, 600.00, 2, 5);

-- Insert offer data (room types with their services/amenities)
INSERT INTO `offer` (`fk_room_service`, `fk_room_type`) VALUES
-- Luxury Grand Hotel
('WiFi', 1), ('Breakfast', 1),
('WiFi', 2), ('Breakfast', 2), ('Mini Bar', 2), ('Room Service', 2),
('WiFi', 3), ('Breakfast', 3), ('Mini Bar', 3), ('Room Service', 3), ('Spa Access', 3), ('Concierge', 3),
('WiFi', 4), ('Breakfast', 4), ('Mini Bar', 4), ('Room Service', 4), ('Spa Access', 4), ('Concierge', 4), ('Airport Shuttle', 4), ('Restaurant Discount', 4),

-- Seaside Resort & Spa
('WiFi', 5), ('Breakfast', 5), ('Swimming Pool', 5),
('WiFi', 6), ('Breakfast', 6), ('Swimming Pool', 6), ('Spa Access', 6), ('Room Service', 6),
('WiFi', 7), ('Breakfast', 7), ('Swimming Pool', 7), ('Spa Access', 7), ('Room Service', 7), ('Child Care', 7), ('Restaurant Discount', 7),

-- Mountain Retreat Lodge
('WiFi', 8), ('Breakfast', 8), ('Parking', 8),
('WiFi', 9), ('Breakfast', 9), ('Parking', 9), ('Fitness Center', 9), ('Restaurant Discount', 9),
('WiFi', 10), ('Breakfast', 10), ('Parking', 10), ('Fitness Center', 10), ('Room Service', 10), ('Restaurant Discount', 10), ('Pet Friendly', 10),

-- City Center Suites
('WiFi', 11), ('Breakfast', 11), ('Business Center', 11),
('WiFi', 12), ('Breakfast', 12), ('Business Center', 12), ('Fitness Center', 12), ('Laundry Service', 12),
('WiFi', 13), ('Breakfast', 13), ('Business Center', 13), ('Fitness Center', 13), ('Laundry Service', 13), ('Room Service', 13), ('Parking', 13),

-- Vintage Boutique Inn
('WiFi', 14), ('Breakfast', 14),
('WiFi', 15), ('Breakfast', 15), ('Room Service', 15), ('Restaurant Discount', 15),
('WiFi', 16), ('Breakfast', 16), ('Room Service', 16), ('Concierge', 16), ('Restaurant Discount', 16);

-- Insert booking data (recent dates)
INSERT INTO `booking` (`fk_client_id`, `fk_room_type_id`, `date`) VALUES
(1, 3, '2025-05-10 12:30:00'),
(2, 5, '2025-05-12 14:00:00'),
(3, 9, '2025-05-15 11:45:00'),
(4, 12, '2025-05-18 13:15:00'),
(5, 14, '2025-05-20 10:00:00'),
(1, 7, '2025-05-25 15:30:00'),
(2, 10, '2025-05-27 16:45:00'),
(3, 4, '2025-06-02 09:30:00'),
(4, 6, '2025-06-05 14:15:00'),
(5, 13, '2025-06-10 11:00:00');

-- Insert review data
INSERT INTO `review` (`fk_client_id`, `fk_hotel_id`, `rating`, `review_date`, `review_text`) VALUES
(1, 1, 4.5, '2025-05-11 18:30:00', 'Beautiful hotel with excellent service. The room was spacious and comfortable. Will definitely return!'),
(2, 2, 4.8, '2025-05-13 19:15:00', 'Amazing beachfront property. The ocean view from our room was breathtaking. Staff was extremely helpful.'),
(3, 3, 5.0, '2025-05-16 20:00:00', 'Perfect mountain getaway! The cabin was cozy and the scenery was incredible. Great hiking trails nearby.'),
(4, 4, 4.3, '2025-05-19 17:45:00', 'Very convenient location for business travelers. Well-equipped rooms and good breakfast options.'),
(5, 5, 4.7, '2025-05-21 16:30:00', 'Charming historic inn with wonderful atmosphere. The garden suite was beautiful and the breakfast was delicious.'),
(1, 2, 4.6, '2025-05-26 18:00:00', 'Lovely resort with great amenities. The family villa exceeded our expectations. Kids loved the pool!'),
(2, 3, 4.9, '2025-05-28 19:30:00', 'The premium lodge was worth every penny. Spectacular views and excellent dining options on-site.'),
(3, 1, 4.7, '2025-06-03 17:15:00', 'The penthouse suite was luxurious and spacious. Outstanding concierge service and amenities.'),
(4, 2, 4.5, '2025-06-06 18:45:00', 'Beautiful beachfront suite with amazing sunrise views. The spa services were exceptional.'),
(5, 4, 4.4, '2025-06-11 16:00:00', 'Excellent business accommodations. The executive apartment had everything needed for a productive stay.');

-- Insert FAQ data
INSERT INTO `faq` (`fk_client_id`, `fk_hotel_id`, `faq_answer`, `faq_question`) VALUES
(1, 1, 'Check-in time is 3:00 PM and check-out time is 11:00 AM.', 'What are the check-in and check-out times?'),
(1, 1, 'Yes, we offer valet parking for $30 per day.', 'Is parking available at the hotel?'),
(2, 2, 'Yes, all our restaurants can accommodate dietary restrictions. Please inform us in advance.', 'Can you accommodate dietary restrictions?'),
(2, 2, 'Our resort is located 15 miles from Miami International Airport, approximately 25 minutes by car.', 'How far is the resort from the airport?'),
(3, 3, 'Yes, we are pet-friendly. There is a $50 fee per stay for pets under 25 pounds.', 'Are pets allowed at the lodge?'),
(3, 3, 'The nearest ski slopes are just 5 minutes away via our complimentary shuttle service.', 'How close are the ski slopes?'),
(4, 4, 'Yes, we offer complimentary high-speed WiFi throughout the property.', 'Is WiFi available?'),
(4, 4, 'Our business center is open 24/7 and includes printing, scanning, and videoconferencing facilities.', 'What business services do you provide?'),
(5, 5, 'Yes, all historic rooms feature modern air conditioning and heating systems.', 'Do the historic rooms have air conditioning?'),
(5, 5, 'Yes, we can arrange private tours of the historic district with advance notice.', 'Can you help arrange local tours?');

-- Insert hotel_photo data
INSERT INTO `hotel_photo` (`id`, `fk_hotel_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insert room_photo data
INSERT INTO `room_photo` (`id`, `fk_room_type_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15),
(16, 16);

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;