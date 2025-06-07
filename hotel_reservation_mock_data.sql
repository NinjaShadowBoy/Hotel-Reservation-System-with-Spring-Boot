-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hotel_reservation
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking`
--

create database hotel_reservation;
use hotel_reservation;

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `checkin_date` datetime(6) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `fk_client_id` int DEFAULT NULL,
  `fk_room_type_id` int DEFAULT NULL,
  `cancellation_date` datetime(6) DEFAULT NULL,
  `commission_amount` double DEFAULT NULL,
  `payment_intent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `refund_amount` double DEFAULT NULL,
  `refunded` bit(1) DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `refund_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `charge_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ns9240iigl4s8nw8o938enjp` (`fk_client_id`),
  KEY `FKmhyw2j6qklc56li7otporbwop` (`fk_room_type_id`),
  CONSTRAINT `FK3ns9240iigl4s8nw8o938enjp` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmhyw2j6qklc56li7otporbwop` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` (`id`, `checkin_date`, `date`, `fk_client_id`, `fk_room_type_id`, `cancellation_date`, `commission_amount`, `payment_intent_id`, `refund_amount`, `refunded`, `status`, `total_amount`, `refund_id`, `charge_id`) VALUES (12,'2025-06-10 03:20:00.000000','2025-06-07 04:21:34.000112',12,3,'2025-06-07 04:22:15.236252',24.9995,'pi_3RXEG7QW2O1tpdCC3wKnu16m',474.99,_binary '','CANCELLED',499.99,'re_3RXEG7QW2O1tpdCC3Cw23f5H',NULL);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `faq_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `faq_question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKo8rthg6u0of181svqb6113pwb` (`fk_client_id`),
  KEY `FKjwd2c3tu3o6unhpuj59620p0o` (`fk_hotel_id`),
  CONSTRAINT `FKjwd2c3tu3o6unhpuj59620p0o` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  CONSTRAINT `FKo8rthg6u0of181svqb6113pwb` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` (`fk_client_id`, `fk_hotel_id`, `faq_answer`, `faq_question`, `id`) VALUES (1,1,'Check-in time is 3:00 PM and check-out time is 11:00 AM. Early check-in and late check-out may be available upon request, subject to availability.','What time is check-in and check-out?',1),(1,5,'We offer a Kids Club for ages 4-12, daily poolside activities, beach games, and special evening entertainment for the whole family.','What activities are available for children?',2),(2,1,'Breakfast is included with certain room packages. Please check your specific reservation details or contact our front desk for more information.','Is breakfast included in the room rate?',3),(3,2,'Yes, we offer complimentary airport shuttle service for all guests. Please provide your flight details at least 24 hours prior to arrival.','Do you offer airport shuttle service?',4),(4,3,'Yes, we offer both valet parking ($35/day) and self-parking ($25/day) options for our guests.','Is parking available at the hotel?',5),(5,4,'Yes, we are a pet-friendly resort. There is a $50 pet fee per stay, and we welcome pets under 50 pounds. Please notify us in advance if you plan to bring a pet.','Are pets allowed at the resort?',6),(10,2,'','Can I have more food?',7);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `rating` float NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `owner_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpof3nsvux6qey3yxom4yoat4o` (`owner_id`),
  CONSTRAINT `FKpof3nsvux6qey3yxom4yoat4o` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` (`id`, `created_at`, `description`, `location`, `name`, `rating`, `updated_at`, `owner_id`) VALUES (1,'2025-01-15 08:00:00.000000','Luxury hotel with stunning ocean views, featuring elegant rooms, gourmet restaurants, a world-class spa, and infinity pool. Perfect for an unforgettable vacation experience with exceptional service and amenities.','Malibu, California','Ocean Paradise Resort',4.8,'2025-05-01 10:15:00.000000',6),(2,'2025-02-10 09:30:00.000000','Charming boutique hotel in the heart of the historic district. Each room is uniquely decorated with local crafts and artwork. Enjoy our complimentary breakfast featuring local specialties.','Savannah, Georgia','Heritage Inn & Suites',4.6,'2025-04-25 14:20:00.000000',6),(3,'2025-03-05 11:45:00.000000','Modern, tech-forward hotel in the center of downtown. Features smart rooms with voice-controlled amenities, coworking spaces, and a rooftop bar with panoramic city views.','Chicago, Illinois','Urban Skyline Hotel',4.7,'2025-05-10 16:30:00.000000',6),(4,'2025-03-20 10:00:00.000000','Rustic mountain lodge surrounded by natural beauty. Offering cozy fireplaces in every room, hiking trails, and an award-winning restaurant serving farm-to-table cuisine.','Aspen, Colorado','Alpine Lodge Retreat',4.9,'2025-04-30 09:45:00.000000',6),(5,'2025-04-01 13:15:00.000000','Family-friendly beachfront resort with spacious suites, multiple pools with water slides, kids club, and a variety of dining options to please every palate.','Miami Beach, Florida','Sunshine Beach Resort',4.5,'2025-05-05 11:30:00.000000',6);
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel_photo`
--

DROP TABLE IF EXISTS `hotel_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel_photo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_hotel_id` int DEFAULT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pw4wbcrvarlbhwcdtvrcyn9y1` (`fk_hotel_id`),
  CONSTRAINT `FK1gcwgym1d6091ue7a3wwu9bxm` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel_photo`
--

LOCK TABLES `hotel_photo` WRITE;
/*!40000 ALTER TABLE `hotel_photo` DISABLE KEYS */;
INSERT INTO `hotel_photo` (`id`, `fk_hotel_id`, `filename`) VALUES (1,1,'1 - Ocean Paradise Resor.jpg'),(2,2,'2 - Heritage Inn & Suite.jpg'),(3,3,'3 - Urban Skyline Hotel.jpg'),(4,4,'4 - Alpine Lodge Retreat.jpg'),(5,5,'5 - Sunshine Beach Resor.jpg');
/*!40000 ALTER TABLE `hotel_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `fk_room_service` int NOT NULL,
  `fk_room_type` int NOT NULL,
  PRIMARY KEY (`fk_room_service`,`fk_room_type`),
  KEY `FK1yy3qqu24s4cfkcekcliehlw8` (`fk_room_type`),
  CONSTRAINT `FK1yy3qqu24s4cfkcekcliehlw8` FOREIGN KEY (`fk_room_type`) REFERENCES `room_type` (`id`),
  CONSTRAINT `FK6iyti4bjop066kei96fvsxkoo` FOREIGN KEY (`fk_room_service`) REFERENCES `room_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` (`fk_room_service`, `fk_room_type`) VALUES (1,1),(1,2),(1,3),(7,3),(1,4),(7,4),(1,5),(6,5),(7,5),(1,6),(6,6),(7,6),(8,6),(1,7),(6,7),(7,7),(8,7),(14,7),(1,8),(2,8),(6,8),(7,8),(8,8),(14,8),(1,9),(2,9),(6,9),(7,9),(8,9),(14,9),(15,9),(1,10),(2,10),(3,10),(6,10),(7,10),(8,10),(9,10),(14,10),(15,10),(1,11),(2,11),(3,11),(6,11),(7,11),(8,11),(9,11),(10,11),(14,11),(15,11),(1,12),(2,12),(3,12),(4,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(14,12),(15,12),(1,13),(2,13),(3,13),(4,13),(5,13),(6,13),(7,13),(8,13),(9,13),(10,13),(11,13),(12,13),(14,13),(15,13),(1,14),(2,14),(3,14),(4,14),(5,14),(6,14),(7,14),(8,14),(9,14),(10,14),(11,14),(12,14),(13,14),(14,14),(15,14);
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `fk_client_id` int NOT NULL,
  `fk_hotel_id` int NOT NULL,
  `rating` float DEFAULT NULL,
  `review_date` datetime(6) DEFAULT NULL,
  `review_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKm79g92hhhqcg8unu390fenvpw` (`fk_client_id`),
  KEY `FKfu5fjm7x69jdrlurkyeui4bgi` (`fk_hotel_id`),
  CONSTRAINT `FKfu5fjm7x69jdrlurkyeui4bgi` FOREIGN KEY (`fk_hotel_id`) REFERENCES `hotel` (`id`),
  CONSTRAINT `FKm79g92hhhqcg8unu390fenvpw` FOREIGN KEY (`fk_client_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` (`fk_client_id`, `fk_hotel_id`, `rating`, `review_date`, `review_text`, `id`) VALUES (1,1,5,'2025-05-10 09:30:00.000000','Absolutely stunning resort with impeccable service. The ocean views were breathtaking, and the staff went above and beyond to make our stay memorable.',1),(1,4,4.8,'2025-05-18 13:20:00.000000','Our second stay at Alpine Lodge and it was even better than the first. The peaceful setting and attentive staff make this place special.',2),(2,2,4.5,'2025-05-05 14:20:00.000000','Charming hotel with so much character and history. The breakfast was delicious, and the location was perfect for exploring the historic district.',3),(2,5,4.6,'2025-05-14 15:10:00.000000','The beachfront location is unbeatable. Enjoyed watching the sunrise from our balcony every morning. Great amenities for the whole family.',4),(3,3,4.7,'2025-05-12 11:15:00.000000','Loved the smart room features! The rooftop bar has the best views of the city skyline, especially at sunset. Will definitely return.',5),(4,4,5,'2025-05-08 16:45:00.000000','The perfect mountain getaway. Sitting by the fireplace after a day of hiking was magical. The restaurant served the best local cuisine we\'ve ever had.',6),(5,5,4.3,'2025-05-15 10:30:00.000000','Great family vacation spot! The kids loved the pools and water slides. Rooms were spacious and clean. Would recommend for families with children of all ages.',7),(10,1,4,'2025-05-31 21:21:29.453129','Haha yoyo',8),(10,2,5,'2025-05-28 15:05:12.667114','I loved the breakfast. The personnel was so nice.',9);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_photo`
--

DROP TABLE IF EXISTS `room_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_photo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_room_type_id` int DEFAULT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1tsnn8sa4nm1cv0eu3581orya` (`fk_room_type_id`),
  CONSTRAINT `FKhfl2vmo85qepxghph5mq8wruf` FOREIGN KEY (`fk_room_type_id`) REFERENCES `room_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_photo`
--

LOCK TABLES `room_photo` WRITE;
/*!40000 ALTER TABLE `room_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_service`
--

DROP TABLE IF EXISTS `room_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_service` (
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fontawsome_icon_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_service`
--

LOCK TABLES `room_service` WRITE;
/*!40000 ALTER TABLE `room_service` DISABLE KEYS */;
INSERT INTO `room_service` (`label`, `fontawsome_icon_class`, `id`) VALUES ('Air Conditioning','fa-solid fa-snowflake',1),('Breakfast','fa-solid fa-mug-saucer',2),('Coffee Maker','fa-solid fa-mug-hot',3),('Fitness Center','fa-solid fa-dumbbell',4),('Laundry','fa-solid fa-shirt',5),('Mini Bar','fa-solid fa-martini-glass',6),('Parking','fa-solid fa-square-parking',7),('Pet Friendly','fa-solid fa-paw',8),('Restaurant','fa-solid fa-utensils',9),('Room Service','fa-solid fa-bell-concierge',10),('Safe','fa-solid fa-vault',11),('Spa','fa-solid fa-spa',12),('Swimming Pool','fa-solid fa-person-swimming',13),('Television','fa-solid fa-tv',14),('Wi-Fi','fa-solid fa-wifi',15);
/*!40000 ALTER TABLE `room_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `number_available` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `total_number` int DEFAULT NULL,
  `fk_hotel` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcsorjnt57y2gdfqxp9vwyjgti` (`fk_hotel`),
  CONSTRAINT `FKcsorjnt57y2gdfqxp9vwyjgti` FOREIGN KEY (`fk_hotel`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` (`id`, `label`, `number_available`, `price`, `total_number`, `fk_hotel`) VALUES (1,'Standard Room',15,199.99,20,1),(2,'Deluxe Room',8,299.99,10,1),(3,'Ocean View Suite',3,499.99,5,1),(4,'Historic Queen Room',12,179.99,15,2),(5,'Historic King Room',7,219.99,10,2),(6,'Heritage Suite',2,349.99,3,2),(7,'Smart City Room',18,249.99,25,3),(8,'Urban Executive Suite',6,399.99,8,3),(9,'Mountain View Room',10,299.99,12,4),(10,'Fireplace Suite',5,449.99,7,4),(11,'Presidential Cabin',1,899.99,1,4),(12,'Family Beach Room',20,259.99,25,5),(13,'Oceanfront Suite',8,459.99,10,5),(14,'Premium Penthouse',1,999.99,1,5);
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('CLIENT','OWNER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `active`, `created_at`, `email`, `first_name`, `last_name`, `password`, `role`, `updated_at`) VALUES (1,_binary '','2025-04-10 09:30:00.000000','john@mail.com','John','Doe','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT','2025-05-15 11:20:00.000000'),(2,_binary '','2025-04-11 14:20:00.000000','jane@mail.com','Jane','Smith','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT',NULL),(3,_binary '','2025-04-12 10:15:00.000000','robert@mail.com','Robert','Johnson','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT',NULL),(4,_binary '','2025-04-13 08:45:00.000000','sarah@mail.com','Sarah','Williams','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT',NULL),(5,_binary '','2025-04-14 16:30:00.000000','david@mail.com','David','Brown','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT',NULL),(6,_binary '','2025-04-15 11:25:00.000000','luxury@hotel.com','Michael','Anderson','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','OWNER','2025-05-10 09:15:00.000000'),(7,_binary '','2025-04-16 13:40:00.000000','inns@hotel.com','Jennifer','Wilson','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','OWNER',NULL),(8,_binary '','2025-04-17 09:55:00.000000','urban@hotel.com','Thomas','Taylor','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','OWNER',NULL),(9,_binary '','2025-04-18 15:10:00.000000','admin@system.com','Admin','User','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','ADMIN','2025-05-12 14:30:00.000000'),(10,_binary '','2025-05-28 14:20:52.253006','alex@mail.com','Alex','Nelson','$2a$10$mUZBYpZnG6Pd0ANrfDYzH.XPwPPm.HqLChlgs212DUe41A.hVgWfu','CLIENT',NULL),(11,_binary '','2025-06-01 13:27:34.170516','nelson@mail.com','Statham','Nelson','$2a$10$EXvhN.OxcHee0oLh/8MumuEwp4r5yJZ9hJ1TSqQtVSfpY062.Yy/K','CLIENT',NULL),(12,_binary '','2025-06-06 07:26:05.700493','alex.nelson.bryan@gmail.com','Abena','Alex','$2a$10$0N4KhlwKgfn.1jB9FAQCM.XD10t1ISBi9vFuwxEXstaUpWQ2gK1y2','CLIENT',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07  5:47:20
