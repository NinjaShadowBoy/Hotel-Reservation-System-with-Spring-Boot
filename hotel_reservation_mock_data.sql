-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 06, 2025 at 09:09 AM
-- Server version: 8.0.36
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hotel_reservation`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE DATABASE hotel_reservation;

USE hotel_reservation;

CREATE TABLE `booking` (
  `id` int NOT NULL,
  `checkin_date` datetime(6) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `fk_client_id` int DEFAULT NULL,
  `fk_room_type_id` int DEFAULT NULL,
  `cancellation_date` datetime(6) DEFAULT NULL,
  `commission_amount` double DEFAULT NULL,
  `payment_intent_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `refund_amount` double DEFAULT NULL,
  `refunded` bit(1) DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total_amount` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id`, `checkin_date`, `date`, `fk_client_id`, `fk_room_type_id`, `cancellation_date`, `commission_amount`, `payment_intent_id`, `refund_amount`, `refunded`, `status`, `total_amount`) VALUES
(4, '2025-05-28 06:54:00.000000', '2025-05-26 07:55:29.475394', 1, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, '2025-06-03 11:28:00.000000', '2025-06-01 12:29:03.570285', 10, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `faq`
--

CREATE TABLE `faq` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `faq_answer` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `faq_question` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `faq`
--

INSERT INTO `faq` (`fk_client_id`, `fk_hotel_id`, `faq_answer`, `faq_question`, `id`) VALUES
(1, 1, 'Check-in time is 3:00 PM and check-out time is 11:00 AM. Early check-in and late check-out may be available upon request, subject to availability.', 'What time is check-in and check-out?', 1),
(1, 5, 'We offer a Kids Club for ages 4-12, daily poolside activities, beach games, and special evening entertainment for the whole family.', 'What activities are available for children?', 2),
(2, 1, 'Breakfast is included with certain room packages. Please check your specific reservation details or contact our front desk for more information.', 'Is breakfast included in the room rate?', 3),
(3, 2, 'Yes, we offer complimentary airport shuttle service for all guests. Please provide your flight details at least 24 hours prior to arrival.', 'Do you offer airport shuttle service?', 4),
(4, 3, 'Yes, we offer both valet parking ($35/day) and self-parking ($25/day) options for our guests.', 'Is parking available at the hotel?', 5),
(5, 4, 'Yes, we are a pet-friendly resort. There is a $50 pet fee per stay, and we welcome pets under 50 pounds. Please notify us in advance if you plan to bring a pet.', 'Are pets allowed at the resort?', 6),
(10, 2, '', 'Can I have more food?', 7);

-- --------------------------------------------------------

--
-- Table structure for table `hotel`
--

CREATE TABLE `hotel` (
  `id` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `rating` float NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `owner_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotel`
--

INSERT INTO `hotel` (`id`, `created_at`, `description`, `location`, `name`, `rating`, `updated_at`, `owner_id`) VALUES
(1, '2025-01-15 08:00:00.000000', 'Luxury hotel with stunning ocean views, featuring elegant rooms, gourmet restaurants, a world-class spa, and infinity pool. Perfect for an unforgettable vacation experience with exceptional service and amenities.', 'Malibu, California', 'Ocean Paradise Resort', 4.8, '2025-05-01 10:15:00.000000', 6),
(2, '2025-02-10 09:30:00.000000', 'Charming boutique hotel in the heart of the historic district. Each room is uniquely decorated with local crafts and artwork. Enjoy our complimentary breakfast featuring local specialties.', 'Savannah, Georgia', 'Heritage Inn & Suites', 4.6, '2025-04-25 14:20:00.000000', 6),
(3, '2025-03-05 11:45:00.000000', 'Modern, tech-forward hotel in the center of downtown. Features smart rooms with voice-controlled amenities, coworking spaces, and a rooftop bar with panoramic city views.', 'Chicago, Illinois', 'Urban Skyline Hotel', 4.7, '2025-05-10 16:30:00.000000', 6),
(4, '2025-03-20 10:00:00.000000', 'Rustic mountain lodge surrounded by natural beauty. Offering cozy fireplaces in every room, hiking trails, and an award-winning restaurant serving farm-to-table cuisine.', 'Aspen, Colorado', 'Alpine Lodge Retreat', 4.9, '2025-04-30 09:45:00.000000', 6),
(5, '2025-04-01 13:15:00.000000', 'Family-friendly beachfront resort with spacious suites, multiple pools with water slides, kids club, and a variety of dining options to please every palate.', 'Miami Beach, Florida', 'Sunshine Beach Resort', 4.5, '2025-05-05 11:30:00.000000', 6);

-- --------------------------------------------------------

--
-- Table structure for table `hotel_photo`
--

CREATE TABLE `hotel_photo` (
  `id` int NOT NULL,
  `fk_hotel_id` int DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotel_photo`
--

INSERT INTO `hotel_photo` (`id`, `fk_hotel_id`, `filename`) VALUES
(1, 1, '1 - Ocean Paradise Resor.jpg'),
(2, 2, '2 - Heritage Inn & Suite.jpg'),
(3, 3, '3 - Urban Skyline Hotel.jpg'),
(4, 4, '4 - Alpine Lodge Retreat.jpg'),
(5, 5, '5 - Sunshine Beach Resor.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `offer`
--

CREATE TABLE `offer` (
  `fk_room_service` int NOT NULL,
  `fk_room_type` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `offer`
--

INSERT INTO `offer` (`fk_room_service`, `fk_room_type`) VALUES
(1, 1),
(1, 2),
(1, 3),
(7, 3),
(1, 4),
(7, 4),
(1, 5),
(6, 5),
(7, 5),
(1, 6),
(6, 6),
(7, 6),
(8, 6),
(1, 7),
(6, 7),
(7, 7),
(8, 7),
(14, 7),
(1, 8),
(2, 8),
(6, 8),
(7, 8),
(8, 8),
(14, 8),
(1, 9),
(2, 9),
(6, 9),
(7, 9),
(8, 9),
(14, 9),
(15, 9),
(1, 10),
(2, 10),
(3, 10),
(6, 10),
(7, 10),
(8, 10),
(9, 10),
(14, 10),
(15, 10),
(1, 11),
(2, 11),
(3, 11),
(6, 11),
(7, 11),
(8, 11),
(9, 11),
(10, 11),
(14, 11),
(15, 11),
(1, 12),
(2, 12),
(3, 12),
(4, 12),
(6, 12),
(7, 12),
(8, 12),
(9, 12),
(10, 12),
(11, 12),
(14, 12),
(15, 12),
(1, 13),
(2, 13),
(3, 13),
(4, 13),
(5, 13),
(6, 13),
(7, 13),
(8, 13),
(9, 13),
(10, 13),
(11, 13),
(12, 13),
(14, 13),
(15, 13),
(1, 14),
(2, 14),
(3, 14),
(4, 14),
(5, 14),
(6, 14),
(7, 14),
(8, 14),
(9, 14),
(10, 14),
(11, 14),
(12, 14),
(13, 14),
(14, 14),
(15, 14);

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `rating` float DEFAULT NULL,
  `review_date` datetime(6) DEFAULT NULL,
  `review_text` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`fk_client_id`, `fk_hotel_id`, `rating`, `review_date`, `review_text`, `id`) VALUES
(1, 1, 5, '2025-05-10 09:30:00.000000', 'Absolutely stunning resort with impeccable service. The ocean views were breathtaking, and the staff went above and beyond to make our stay memorable.', 1),
(1, 4, 4.8, '2025-05-18 13:20:00.000000', 'Our second stay at Alpine Lodge and it was even better than the first. The peaceful setting and attentive staff make this place special.', 2),
(2, 2, 4.5, '2025-05-05 14:20:00.000000', 'Charming hotel with so much character and history. The breakfast was delicious, and the location was perfect for exploring the historic district.', 3),
(2, 5, 4.6, '2025-05-14 15:10:00.000000', 'The beachfront location is unbeatable. Enjoyed watching the sunrise from our balcony every morning. Great amenities for the whole family.', 4),
(3, 3, 4.7, '2025-05-12 11:15:00.000000', 'Loved the smart room features! The rooftop bar has the best views of the city skyline, especially at sunset. Will definitely return.', 5),
(4, 4, 5, '2025-05-08 16:45:00.000000', 'The perfect mountain getaway. Sitting by the fireplace after a day of hiking was magical. The restaurant served the best local cuisine we\'ve ever had.', 6),
(5, 5, 4.3, '2025-05-15 10:30:00.000000', 'Great family vacation spot! The kids loved the pools and water slides. Rooms were spacious and clean. Would recommend for families with children of all ages.', 7),
(10, 1, 4, '2025-05-31 21:21:29.453129', 'Haha yoyo', 8),
(10, 2, 5, '2025-05-28 15:05:12.667114', 'I loved the breakfast. The personnel was so nice.', 9);

-- --------------------------------------------------------

--
-- Table structure for table `room_photo`
--

CREATE TABLE `room_photo` (
  `id` int NOT NULL,
  `fk_room_type_id` int DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `room_service`
--

CREATE TABLE `room_service` (
  `label` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `fontawsome_icon_class` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room_service`
--

INSERT INTO `room_service` (`label`, `fontawsome_icon_class`, `id`) VALUES
('Air Conditioning', 'fa-solid fa-snowflake', 1),
('Breakfast', 'fa-solid fa-mug-saucer', 2),
('Coffee Maker', 'fa-solid fa-mug-hot', 3),
('Fitness Center', 'fa-solid fa-dumbbell', 4),
('Laundry', 'fa-solid fa-shirt', 5),
('Mini Bar', 'fa-solid fa-martini-glass', 6),
('Parking', 'fa-solid fa-square-parking', 7),
('Pet Friendly', 'fa-solid fa-paw', 8),
('Restaurant', 'fa-solid fa-utensils', 9),
('Room Service', 'fa-solid fa-bell-concierge', 10),
('Safe', 'fa-solid fa-vault', 11),
('Spa', 'fa-solid fa-spa', 12),
('Swimming Pool', 'fa-solid fa-person-swimming', 13),
('Television', 'fa-solid fa-tv', 14),
('Wi-Fi', 'fa-solid fa-wifi', 15);

-- --------------------------------------------------------

--
-- Table structure for table `room_type`
--

CREATE TABLE `room_type` (
  `id` int NOT NULL,
  `label` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `number_available` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `total_number` int DEFAULT NULL,
  `fk_hotel` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room_type`
--

INSERT INTO `room_type` (`id`, `label`, `number_available`, `price`, `total_number`, `fk_hotel`) VALUES
(1, 'Standard Room', 15, 199.99, 20, 1),
(2, 'Deluxe Room', 8, 299.99, 10, 1),
(3, 'Ocean View Suite', 3, 499.99, 5, 1),
(4, 'Historic Queen Room', 12, 179.99, 15, 2),
(5, 'Historic King Room', 7, 219.99, 10, 2),
(6, 'Heritage Suite', 2, 349.99, 3, 2),
(7, 'Smart City Room', 18, 249.99, 25, 3),
(8, 'Urban Executive Suite', 6, 399.99, 8, 3),
(9, 'Mountain View Room', 10, 299.99, 12, 4),
(10, 'Fireplace Suite', 5, 449.99, 7, 4),
(11, 'Presidential Cabin', 1, 899.99, 1, 4),
(12, 'Family Beach Room', 20, 259.99, 25, 5),
(13, 'Oceanfront Suite', 8, 459.99, 10, 5),
(14, 'Premium Penthouse', 1, 999.99, 1, 5);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `first_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('CLIENT','OWNER','ADMIN') COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `active`, `created_at`, `email`, `first_name`, `last_name`, `password`, `role`, `updated_at`) VALUES
(1, b'1', '2025-04-10 09:30:00.000000', 'john@mail.com', 'John', 'Doe', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', '2025-05-15 11:20:00.000000'),
(2, b'1', '2025-04-11 14:20:00.000000', 'jane@mail.com', 'Jane', 'Smith', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', NULL),
(3, b'1', '2025-04-12 10:15:00.000000', 'robert@mail.com', 'Robert', 'Johnson', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', NULL),
(4, b'1', '2025-04-13 08:45:00.000000', 'sarah@mail.com', 'Sarah', 'Williams', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', NULL),
(5, b'1', '2025-04-14 16:30:00.000000', 'david@mail.com', 'David', 'Brown', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', NULL),
(6, b'1', '2025-04-15 11:25:00.000000', 'luxury@hotel.com', 'Michael', 'Anderson', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'OWNER', '2025-05-10 09:15:00.000000'),
(7, b'1', '2025-04-16 13:40:00.000000', 'inns@hotel.com', 'Jennifer', 'Wilson', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'OWNER', NULL),
(8, b'1', '2025-04-17 09:55:00.000000', 'urban@hotel.com', 'Thomas', 'Taylor', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'OWNER', NULL),
(9, b'1', '2025-04-18 15:10:00.000000', 'admin@system.com', 'Admin', 'User', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'ADMIN', '2025-05-12 14:30:00.000000'),
(10, b'1', '2025-05-28 14:20:52.253006', 'alex@mail.com', 'Alex', 'Nelson', '$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu', 'CLIENT', NULL),
(11, b'1', '2025-06-01 13:27:34.170516', 'nelson@mail.com', 'Statham', 'Nelson', '$2a$10$EXvhN.OxcHee0oLh/8MumuEwp4r5yJZ9hJ1TSqQtVSfpY062.Yy/K', 'CLIENT', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3ns9240iigl4s8nw8o938enjp` (`fk_client_id`),
  ADD KEY `FKmhyw2j6qklc56li7otporbwop` (`fk_room_type_id`);

--
-- Indexes for table `faq`
--
ALTER TABLE `faq`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKo8rthg6u0of181svqb6113pwb` (`fk_client_id`),
  ADD KEY `FKjwd2c3tu3o6unhpuj59620p0o` (`fk_hotel_id`);

--
-- Indexes for table `hotel`
--
ALTER TABLE `hotel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpof3nsvux6qey3yxom4yoat4o` (`owner_id`);

--
-- Indexes for table `hotel_photo`
--
ALTER TABLE `hotel_photo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_pw4wbcrvarlbhwcdtvrcyn9y1` (`fk_hotel_id`);

--
-- Indexes for table `offer`
--
ALTER TABLE `offer`
  ADD PRIMARY KEY (`fk_room_service`,`fk_room_type`),
  ADD KEY `FK1yy3qqu24s4cfkcekcliehlw8` (`fk_room_type`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKm79g92hhhqcg8unu390fenvpw` (`fk_client_id`),
  ADD KEY `FKfu5fjm7x69jdrlurkyeui4bgi` (`fk_hotel_id`);

--
-- Indexes for table `room_photo`
--
ALTER TABLE `room_photo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_1tsnn8sa4nm1cv0eu3581orya` (`fk_room_type_id`);

--
-- Indexes for table `room_service`
--
ALTER TABLE `room_service`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `room_type`
--
ALTER TABLE `room_type`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcsorjnt57y2gdfqxp9vwyjgti` (`fk_hotel`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `faq`
--
ALTER TABLE `faq`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `hotel`
--
ALTER TABLE `hotel`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `hotel_photo`
--
ALTER TABLE `hotel_photo`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `room_photo`
--
ALTER TABLE `room_photo`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `room_service`
--
ALTER TABLE `room_service`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `room_type`
--
ALTER TABLE `room_type`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `FK3ns9240iigl4s8nw8o938enjp` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKmhyw2j6qklc56li7otporbwop` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`);

--
-- Constraints for table `faq`
--
ALTER TABLE `faq`
  ADD CONSTRAINT `FKjwd2c3tu3o6unhpuj59620p0o` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  ADD CONSTRAINT `FKo8rthg6u0of181svqb6113pwb` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `hotel`
--
ALTER TABLE `hotel`
  ADD CONSTRAINT `FKpof3nsvux6qey3yxom4yoat4o` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `hotel_photo`
--
ALTER TABLE `hotel_photo`
  ADD CONSTRAINT `FK1gcwgym1d6091ue7a3wwu9bxm` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`);

--
-- Constraints for table `offer`
--
ALTER TABLE `offer`
  ADD CONSTRAINT `FK1yy3qqu24s4cfkcekcliehlw8` FOREIGN KEY (`fk_room_type`) REFERENCES `room_type` (`id`),
  ADD CONSTRAINT `FK6iyti4bjop066kei96fvsxkoo` FOREIGN KEY (`fk_room_service`) REFERENCES `room_service` (`id`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `FKfu5fjm7x69jdrlurkyeui4bgi` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  ADD CONSTRAINT `FKm79g92hhhqcg8unu390fenvpw` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `room_photo`
--
ALTER TABLE `room_photo`
  ADD CONSTRAINT `FKhfl2vmo85qepxghph5mq8wruf` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`);

--
-- Constraints for table `room_type`
--
ALTER TABLE `room_type`
  ADD CONSTRAINT `FKcsorjnt57y2gdfqxp9vwyjgti` FOREIGN KEY (`fk_hotel`) REFERENCES `hotel` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
