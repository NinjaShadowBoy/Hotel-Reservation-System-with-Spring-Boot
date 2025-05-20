SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `hotel_reservation` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `hotel_reservation`;

CREATE TABLE `booking` (
  `fk_client_id` int NOT NULL,
  `fk_room_type_id` int NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `checkin_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `booking` (`fk_client_id`, `fk_room_type_id`, `date`, `checkin_date`) VALUES
(1, 3, '2025-06-15 14:00:00.000000', '2025-05-14 08:58:56.652000'),
(1, 11, '2025-08-01 14:00:00.000000', '2025-05-22 08:58:56.652000'),
(2, 6, '2025-06-20 15:00:00.000000', '2025-05-14 08:58:56.652000'),
(2, 14, '2025-07-15 15:00:00.000000', '2025-05-14 08:58:56.652000'),
(3, 5, '2025-06-18 13:00:00.000000', '2025-05-14 08:58:56.652000'),
(3, 8, '2025-07-05 13:00:00.000000', '2025-05-14 08:58:56.652000'),
(4, 9, '2025-08-05 12:00:00.000000', '2025-05-14 08:58:56.652000'),
(4, 10, '2025-06-25 12:00:00.000000', '2025-05-14 08:00:56.652000'),
(5, 2, '2025-06-30 16:00:00.000000', '2025-05-14 08:00:00.000000'),
(5, 13, '2025-07-10 16:00:00.000000', '2025-05-14 08:00:56.652000');

CREATE TABLE `faq` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `faq_answer` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `faq_question` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `faq` (`fk_client_id`, `fk_hotel_id`, `faq_answer`, `faq_question`) VALUES
(1, 1, 'Check-in time is 3:00 PM and check-out time is 11:00 AM. Early check-in and late check-out may be available upon request, subject to availability.', 'What time is check-in and check-out?'),
(1, 5, 'We offer a Kids Club for ages 4-12, daily poolside activities, beach games, and special evening entertainment for the whole family.', 'What activities are available for children?'),
(2, 1, 'Breakfast is included with certain room packages. Please check your specific reservation details or contact our front desk for more information.', 'Is breakfast included in the room rate?'),
(3, 2, 'Yes, we offer complimentary airport shuttle service for all guests. Please provide your flight details at least 24 hours prior to arrival.', 'Do you offer airport shuttle service?'),
(4, 3, 'Yes, we offer both valet parking ($35/day) and self-parking ($25/day) options for our guests.', 'Is parking available at the hotel?'),
(5, 4, 'Yes, we are a pet-friendly resort. There is a $50 pet fee per stay, and we welcome pets under 50 pounds. Please notify us in advance if you plan to bring a pet.', 'Are pets allowed at the resort?');

CREATE TABLE `hotel` (
  `id` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `owner` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `rating` float NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `hotel` (`id`, `created_at`, `description`, `location`, `name`, `owner`, `rating`, `updated_at`) VALUES
(1, '2025-01-15 08:00:00.000000', 'Luxury hotel with stunning ocean views, featuring elegant rooms, gourmet restaurants, a world-class spa, and infinity pool. Perfect for an unforgettable vacation experience with exceptional service and amenities.', 'Malibu, California', 'Ocean Paradise Resort', 'Luxury Resorts Group', 4.8, '2025-05-01 10:15:00.000000'),
(2, '2025-02-10 09:30:00.000000', 'Charming boutique hotel in the heart of the historic district. Each room is uniquely decorated with local crafts and artwork. Enjoy our complimentary breakfast featuring local specialties.', 'Savannah, Georgia', 'Heritage Inn & Suites', 'Cozy Inns Collection', 4.6, '2025-04-25 14:20:00.000000'),
(3, '2025-03-05 11:45:00.000000', 'Modern, tech-forward hotel in the center of downtown. Features smart rooms with voice-controlled amenities, coworking spaces, and a rooftop bar with panoramic city views.', 'Chicago, Illinois', 'Urban Skyline Hotel', 'Urban Suites International', 4.7, '2025-05-10 16:30:00.000000'),
(4, '2025-03-20 10:00:00.000000', 'Rustic mountain lodge surrounded by natural beauty. Offering cozy fireplaces in every room, hiking trails, and an award-winning restaurant serving farm-to-table cuisine.', 'Aspen, Colorado', 'Alpine Lodge Retreat', 'Luxury Resorts Group', 4.9, '2025-04-30 09:45:00.000000'),
(5, '2025-04-01 13:15:00.000000', 'Family-friendly beachfront resort with spacious suites, multiple pools with water slides, kids club, and a variety of dining options to please every palate.', 'Miami Beach, Florida', 'Sunshine Beach Resort', 'Urban Suites International', 4.5, '2025-05-05 11:30:00.000000');

CREATE TABLE `hotel_photo` (
  `id` int NOT NULL,
  `fk_hotel_id` int DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `hotel_photo` (`id`, `fk_hotel_id`, `filename`) VALUES
(1, 1, '1 - Ocean Paradise Resor.jpg'),
(2, 2, '2 - Heritage Inn & Suite.jpg'),
(3, 3, '3 - Urban Skyline Hotel.jpg'),
(4, 4, '4 - Alpine Lodge Retreat.jpg'),
(5, 5, '5 - Sunshine Beach Resor.jpg');

CREATE TABLE `offer` (
  `fk_room_service` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `fk_room_type` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `offer` (`fk_room_service`, `fk_room_type`) VALUES
('Air Conditioning', 1),
('Television', 1),
('Wi-Fi', 1),
('Air Conditioning', 2),
('Coffee Maker', 2),
('Mini Bar', 2),
('Television', 2),
('Wi-Fi', 2),
('Air Conditioning', 3),
('Coffee Maker', 3),
('Mini Bar', 3),
('Room Service', 3),
('Television', 3),
('Wi-Fi', 3),
('Air Conditioning', 4),
('Coffee Maker', 4),
('Television', 4),
('Wi-Fi', 4),
('Air Conditioning', 5),
('Coffee Maker', 5),
('Mini Bar', 5),
('Television', 5),
('Wi-Fi', 5),
('Air Conditioning', 6),
('Coffee Maker', 6),
('Mini Bar', 6),
('Room Service', 6),
('Television', 6),
('Wi-Fi', 6),
('Air Conditioning', 7),
('Coffee Maker', 7),
('Mini Bar', 7),
('Television', 7),
('Wi-Fi', 7),
('Air Conditioning', 8),
('Coffee Maker', 8),
('Mini Bar', 8),
('Room Service', 8),
('Television', 8),
('Wi-Fi', 8),
('Air Conditioning', 9),
('Coffee Maker', 9),
('Television', 9),
('Wi-Fi', 9),
('Air Conditioning', 10),
('Coffee Maker', 10),
('Mini Bar', 10),
('Room Service', 10),
('Television', 10),
('Wi-Fi', 10),
('Air Conditioning', 11),
('Coffee Maker', 11),
('Mini Bar', 11),
('Room Service', 11),
('Spa', 11),
('Television', 11),
('Wi-Fi', 11),
('Air Conditioning', 12),
('Coffee Maker', 12),
('Television', 12),
('Wi-Fi', 12),
('Air Conditioning', 13),
('Coffee Maker', 13),
('Mini Bar', 13),
('Room Service', 13),
('Television', 13),
('Wi-Fi', 13),
('Air Conditioning', 14),
('Coffee Maker', 14),
('Mini Bar', 14),
('Room Service', 14),
('Spa', 14),
('Television', 14),
('Wi-Fi', 14);

CREATE TABLE `review` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `rating` float DEFAULT NULL,
  `review_date` datetime(6) DEFAULT NULL,
  `review_text` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `review` (`fk_client_id`, `fk_hotel_id`, `rating`, `review_date`, `review_text`) VALUES
(1, 1, 5, '2025-05-10 09:30:00.000000', 'Absolutely stunning resort with impeccable service. The ocean views were breathtaking, and the staff went above and beyond to make our stay memorable.'),
(1, 4, 4.8, '2025-05-18 13:20:00.000000', 'Our second stay at Alpine Lodge and it was even better than the first. The peaceful setting and attentive staff make this place special.'),
(2, 2, 4.5, '2025-05-05 14:20:00.000000', 'Charming hotel with so much character and history. The breakfast was delicious, and the location was perfect for exploring the historic district.'),
(2, 5, 4.6, '2025-05-14 15:10:00.000000', 'The beachfront location is unbeatable. Enjoyed watching the sunrise from our balcony every morning. Great amenities for the whole family.'),
(3, 3, 4.7, '2025-05-12 11:15:00.000000', 'Loved the smart room features! The rooftop bar has the best views of the city skyline, especially at sunset. Will definitely return.'),
(4, 4, 5, '2025-05-08 16:45:00.000000', 'The perfect mountain getaway. Sitting by the fireplace after a day of hiking was magical. The restaurant served the best local cuisine we\'ve ever had.'),
(5, 5, 4.3, '2025-05-15 10:30:00.000000', 'Great family vacation spot! The kids loved the pools and water slides. Rooms were spacious and clean. Would recommend for families with children of all ages.');

CREATE TABLE `room_photo` (
  `id` int NOT NULL,
  `fk_room_type_id` int DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `room_service` (
  `label` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `fontawsome_icon_class` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `room_service` (`label`, `fontawsome_icon_class`) VALUES
('Air Conditioning', 'fa-solid fa-snowflake'),
('Breakfast', 'fa-solid fa-mug-saucer'),
('Coffee Maker', 'fa-solid fa-mug-hot'),
('Fitness Center', 'fa-solid fa-dumbbell'),
('Laundry', 'fa-solid fa-shirt'),
('Mini Bar', 'fa-solid fa-martini-glass'),
('Parking', 'fa-solid fa-square-parking'),
('Pet Friendly', 'fa-solid fa-paw'),
('Restaurant', 'fa-solid fa-utensils'),
('Room Service', 'fa-solid fa-bell-concierge'),
('Safe', 'fa-solid fa-vault'),
('Spa', 'fa-solid fa-spa'),
('Swimming Pool', 'fa-solid fa-person-swimming'),
('Television', 'fa-solid fa-tv'),
('Wi-Fi', 'fa-solid fa-wifi');

CREATE TABLE `room_type` (
  `id` int NOT NULL,
  `label` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `number_available` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `total_number` int DEFAULT NULL,
  `fk_hotel` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

INSERT INTO `user` (`id`, `active`, `created_at`, `email`, `first_name`, `last_name`, `password`, `role`, `updated_at`) VALUES
(1, b'1', '2025-04-10 09:30:00.000000', 'john.doe@example.com', 'John', 'Doe', '$2a$10$cV8oSGFn2Yd.vKfvgzx8ReYLX9sY4zSZD47I7aEWOcEzj1lOCiE.2', 'CLIENT', '2025-05-15 11:20:00.000000'),
(2, b'1', '2025-04-11 14:20:00.000000', 'jane.smith@example.com', 'Jane', 'Smith', '$2a$10$ZnrCsDmrIqBDsJbEV9bUUeOK.SXyqcUKW2uo.gvXdcRrzNlYdCvUO', 'CLIENT', NULL),
(3, b'1', '2025-04-12 10:15:00.000000', 'robert.johnson@example.com', 'Robert', 'Johnson', '$2a$10$Xt5J4vHVPMB9aJrvn8QcWOxVGHIRNzK/3E1gBCFtqVy7NyVjd5pKG', 'CLIENT', NULL),
(4, b'1', '2025-04-13 08:45:00.000000', 'sarah.williams@example.com', 'Sarah', 'Williams', '$2a$10$mZqP5JDQq7dQ7dGEcObp5O7/DqGvNzKXfEiY1bEQGlBmM8fDyZCae', 'CLIENT', NULL),
(5, b'1', '2025-04-14 16:30:00.000000', 'david.brown@example.com', 'David', 'Brown', '$2a$10$PzRz1.y.IEkAXvEDCZrqnOuWvkq/i6h7n29G74RwQUxR9zTUFWI3G', 'CLIENT', NULL),
(6, b'1', '2025-04-15 11:25:00.000000', 'luxury.resorts@hotelgroup.com', 'Michael', 'Anderson', '$2a$10$gQWCwCc9uITcjBfNn6nVveStUa4vLn5KRDWj2CouQsVR1KGkNOXX.', 'OWNER', '2025-05-10 09:15:00.000000'),
(7, b'1', '2025-04-16 13:40:00.000000', 'cozy.inns@hotelgroup.com', 'Jennifer', 'Wilson', '$2a$10$jM4rVaLxQZjUfH4SCcx4DO5VQd82EcXFPf3ZtGaOlw11OQcaVQsRK', 'OWNER', NULL),
(8, b'1', '2025-04-17 09:55:00.000000', 'urban.suites@hotelgroup.com', 'Thomas', 'Taylor', '$2a$10$qv4fscAg8HnfC.nXxQV26O4oeZMADNfGlAuEaxyfM/mHWOoTFI3ru', 'OWNER', NULL),
(9, b'1', '2025-04-18 15:10:00.000000', 'admin@reservationsystem.com', 'Admin', 'User', '$2a$10$BDjHVoSrFkQXmWg/w8RRfe0v2LGZhwn/KH9fFy5oLo6y.UD2RFrz.', 'ADMIN', '2025-05-12 14:30:00.000000');


ALTER TABLE `booking`
  ADD PRIMARY KEY (`fk_client_id`,`fk_room_type_id`),
  ADD KEY `FKmhyw2j6qklc56li7otporbwop` (`fk_room_type_id`);

ALTER TABLE `faq`
  ADD PRIMARY KEY (`fk_client_id`,`fk_hotel_id`),
  ADD KEY `FKjwd2c3tu3o6unhpuj59620p0o` (`fk_hotel_id`);

ALTER TABLE `hotel`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `hotel_photo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_pw4wbcrvarlbhwcdtvrcyn9y1` (`fk_hotel_id`);

ALTER TABLE `offer`
  ADD PRIMARY KEY (`fk_room_service`,`fk_room_type`),
  ADD KEY `FK1yy3qqu24s4cfkcekcliehlw8` (`fk_room_type`);

ALTER TABLE `review`
  ADD PRIMARY KEY (`fk_client_id`,`fk_hotel_id`),
  ADD KEY `FKfu5fjm7x69jdrlurkyeui4bgi` (`fk_hotel_id`);

ALTER TABLE `room_photo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_1tsnn8sa4nm1cv0eu3581orya` (`fk_room_type_id`);

ALTER TABLE `room_service`
  ADD PRIMARY KEY (`label`);

ALTER TABLE `room_type`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcsorjnt57y2gdfqxp9vwyjgti` (`fk_hotel`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`);


ALTER TABLE `hotel`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `hotel_photo`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `room_photo`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

ALTER TABLE `room_type`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;


ALTER TABLE `booking`
  ADD CONSTRAINT `FK3ns9240iigl4s8nw8o938enjp` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKmhyw2j6qklc56li7otporbwop` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`);

ALTER TABLE `faq`
  ADD CONSTRAINT `FKjwd2c3tu3o6unhpuj59620p0o` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  ADD CONSTRAINT `FKo8rthg6u0of181svqb6113pwb` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`);

ALTER TABLE `hotel_photo`
  ADD CONSTRAINT `FK1gcwgym1d6091ue7a3wwu9bxm` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`);

ALTER TABLE `offer`
  ADD CONSTRAINT `FK1yy3qqu24s4cfkcekcliehlw8` FOREIGN KEY (`fk_room_type`) REFERENCES `room_type` (`id`),
  ADD CONSTRAINT `FK6iyti4bjop066kei96fvsxkoo` FOREIGN KEY (`fk_room_service`) REFERENCES `room_service` (`label`);

ALTER TABLE `review`
  ADD CONSTRAINT `FKfu5fjm7x69jdrlurkyeui4bgi` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  ADD CONSTRAINT `FKm79g92hhhqcg8unu390fenvpw` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`);

ALTER TABLE `room_photo`
  ADD CONSTRAINT `FKhfl2vmo85qepxghph5mq8wruf` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`);

ALTER TABLE `room_type`
  ADD CONSTRAINT `FKcsorjnt57y2gdfqxp9vwyjgti` FOREIGN KEY (`fk_hotel`) REFERENCES `hotel` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
