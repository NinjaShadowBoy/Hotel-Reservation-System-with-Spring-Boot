-- Mock data for hotel_reservation database
-- Generated on May 19, 2025

-- Disable foreign key checks temporarily for easier data insertion
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data
TRUNCATE TABLE booking;
TRUNCATE TABLE faq;
TRUNCATE TABLE hotel_photo;
TRUNCATE TABLE hotel;
TRUNCATE TABLE offer;
TRUNCATE TABLE review;
TRUNCATE TABLE room_photo;
TRUNCATE TABLE room_service;
TRUNCATE TABLE room_type;
TRUNCATE TABLE user;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insert users with bcrypt-encoded passwords
-- Note: The password for each user is the first word in their email
INSERT INTO user (id, active, created_at, email, first_name, last_name, password, role, updated_at) VALUES
(1, 1, '2025-04-10 09:30:00', 'john.doe@example.com', 'John', 'Doe', '$2a$10$cV8oSGFn2Yd.vKfvgzx8ReYLX9sY4zSZD47I7aEWOcEzj1lOCiE.2', 'CLIENT', '2025-05-15 11:20:00'),
(2, 1, '2025-04-11 14:20:00', 'jane.smith@example.com', 'Jane', 'Smith', '$2a$10$ZnrCsDmrIqBDsJbEV9bUUeOK.SXyqcUKW2uo.gvXdcRrzNlYdCvUO', 'CLIENT', NULL),
(3, 1, '2025-04-12 10:15:00', 'robert.johnson@example.com', 'Robert', 'Johnson', '$2a$10$Xt5J4vHVPMB9aJrvn8QcWOxVGHIRNzK/3E1gBCFtqVy7NyVjd5pKG', 'CLIENT', NULL),
(4, 1, '2025-04-13 08:45:00', 'sarah.williams@example.com', 'Sarah', 'Williams', '$2a$10$mZqP5JDQq7dQ7dGEcObp5O7/DqGvNzKXfEiY1bEQGlBmM8fDyZCae', 'CLIENT', NULL),
(5, 1, '2025-04-14 16:30:00', 'david.brown@example.com', 'David', 'Brown', '$2a$10$PzRz1.y.IEkAXvEDCZrqnOuWvkq/i6h7n29G74RwQUxR9zTUFWI3G', 'CLIENT', NULL),
(6, 1, '2025-04-15 11:25:00', 'luxury.resorts@hotelgroup.com', 'Michael', 'Anderson', '$2a$10$gQWCwCc9uITcjBfNn6nVveStUa4vLn5KRDWj2CouQsVR1KGkNOXX.', 'OWNER', '2025-05-10 09:15:00'),
(7, 1, '2025-04-16 13:40:00', 'cozy.inns@hotelgroup.com', 'Jennifer', 'Wilson', '$2a$10$jM4rVaLxQZjUfH4SCcx4DO5VQd82EcXFPf3ZtGaOlw11OQcaVQsRK', 'OWNER', NULL),
(8, 1, '2025-04-17 09:55:00', 'urban.suites@hotelgroup.com', 'Thomas', 'Taylor', '$2a$10$qv4fscAg8HnfC.nXxQV26O4oeZMADNfGlAuEaxyfM/mHWOoTFI3ru', 'OWNER', NULL),
(9, 1, '2025-04-18 15:10:00', 'admin@reservationsystem.com', 'Admin', 'User', '$2a$10$BDjHVoSrFkQXmWg/w8RRfe0v2LGZhwn/KH9fFy5oLo6y.UD2RFrz.', 'ADMIN', '2025-05-12 14:30:00');

-- Insert hotels
INSERT INTO hotel (id, created_at, description, location, name, owner, rating, updated_at) VALUES
(1, '2025-01-15 08:00:00', 'Luxury hotel with stunning ocean views, featuring elegant rooms, gourmet restaurants, a world-class spa, and infinity pool. Perfect for an unforgettable vacation experience with exceptional service and amenities.', 'Malibu, California', 'Ocean Paradise Resort', 'Luxury Resorts Group', 4.8, '2025-05-01 10:15:00'),
(2, '2025-02-10 09:30:00', 'Charming boutique hotel in the heart of the historic district. Each room is uniquely decorated with local crafts and artwork. Enjoy our complimentary breakfast featuring local specialties.', 'Savannah, Georgia', 'Heritage Inn & Suites', 'Cozy Inns Collection', 4.6, '2025-04-25 14:20:00'),
(3, '2025-03-05 11:45:00', 'Modern, tech-forward hotel in the center of downtown. Features smart rooms with voice-controlled amenities, coworking spaces, and a rooftop bar with panoramic city views.', 'Chicago, Illinois', 'Urban Skyline Hotel', 'Urban Suites International', 4.7, '2025-05-10 16:30:00'),
(4, '2025-03-20 10:00:00', 'Rustic mountain lodge surrounded by natural beauty. Offering cozy fireplaces in every room, hiking trails, and an award-winning restaurant serving farm-to-table cuisine.', 'Aspen, Colorado', 'Alpine Lodge Retreat', 'Luxury Resorts Group', 4.9, '2025-04-30 09:45:00'),
(5, '2025-04-01 13:15:00', 'Family-friendly beachfront resort with spacious suites, multiple pools with water slides, kids club, and a variety of dining options to please every palate.', 'Miami Beach, Florida', 'Sunshine Beach Resort', 'Urban Suites International', 4.5, '2025-05-05 11:30:00');

-- Insert hotel photos
INSERT INTO hotel_photo (id, fk_hotel_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insert room services with FontAwesome 6 free icons
INSERT INTO room_service (label, fontawsome_icon_class) VALUES
('Wi-Fi', 'fa-solid fa-wifi'),
('Room Service', 'fa-solid fa-bell-concierge'),
('Air Conditioning', 'fa-solid fa-snowflake'),
('Television', 'fa-solid fa-tv'),
('Mini Bar', 'fa-solid fa-martini-glass'),
('Coffee Maker', 'fa-solid fa-mug-hot'),
('Swimming Pool', 'fa-solid fa-person-swimming'),
('Fitness Center', 'fa-solid fa-dumbbell'),
('Spa', 'fa-solid fa-spa'),
('Pet Friendly', 'fa-solid fa-paw'),
('Parking', 'fa-solid fa-square-parking'),
('Restaurant', 'fa-solid fa-utensils'),
('Breakfast', 'fa-solid fa-mug-saucer'),
('Laundry', 'fa-solid fa-shirt'),
('Safe', 'fa-solid fa-vault');

-- Insert room types
INSERT INTO room_type (id, label, number_available, price, total_number, fk_hotel) VALUES
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

-- Insert room photos
INSERT INTO room_photo (id, fk_room_type_id) VALUES
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
(14, 14);

-- Insert offers (room services for room types)
INSERT INTO offer (fk_room_service, fk_room_type) VALUES
('Wi-Fi', 1), ('Television', 1), ('Air Conditioning', 1),
('Wi-Fi', 2), ('Television', 2), ('Air Conditioning', 2), ('Mini Bar', 2), ('Coffee Maker', 2),
('Wi-Fi', 3), ('Television', 3), ('Air Conditioning', 3), ('Mini Bar', 3), ('Coffee Maker', 3), ('Room Service', 3),
('Wi-Fi', 4), ('Television', 4), ('Air Conditioning', 4), ('Coffee Maker', 4),
('Wi-Fi', 5), ('Television', 5), ('Air Conditioning', 5), ('Coffee Maker', 5), ('Mini Bar', 5),
('Wi-Fi', 6), ('Television', 6), ('Air Conditioning', 6), ('Mini Bar', 6), ('Coffee Maker', 6), ('Room Service', 6),
('Wi-Fi', 7), ('Television', 7), ('Air Conditioning', 7), ('Mini Bar', 7), ('Coffee Maker', 7),
('Wi-Fi', 8), ('Television', 8), ('Air Conditioning', 8), ('Mini Bar', 8), ('Coffee Maker', 8), ('Room Service', 8),
('Wi-Fi', 9), ('Television', 9), ('Air Conditioning', 9), ('Coffee Maker', 9),
('Wi-Fi', 10), ('Television', 10), ('Air Conditioning', 10), ('Mini Bar', 10), ('Coffee Maker', 10), ('Room Service', 10),
('Wi-Fi', 11), ('Television', 11), ('Air Conditioning', 11), ('Mini Bar', 11), ('Coffee Maker', 11), ('Room Service', 11), ('Spa', 11),
('Wi-Fi', 12), ('Television', 12), ('Air Conditioning', 12), ('Coffee Maker', 12),
('Wi-Fi', 13), ('Television', 13), ('Air Conditioning', 13), ('Mini Bar', 13), ('Coffee Maker', 13), ('Room Service', 13),
('Wi-Fi', 14), ('Television', 14), ('Air Conditioning', 14), ('Mini Bar', 14), ('Coffee Maker', 14), ('Room Service', 14), ('Spa', 14);

-- Insert bookings
INSERT INTO booking (fk_client_id, fk_room_type_id, date) VALUES
(1, 3, '2025-06-15 14:00:00'),
(2, 6, '2025-06-20 15:00:00'),
(3, 8, '2025-07-05 13:00:00'),
(4, 10, '2025-06-25 12:00:00'),
(5, 13, '2025-07-10 16:00:00'),
(1, 11, '2025-08-01 14:00:00'),
(2, 14, '2025-07-15 15:00:00'),
(3, 5, '2025-06-18 13:00:00'),
(4, 9, '2025-08-05 12:00:00'),
(5, 2, '2025-06-30 16:00:00');

-- Insert reviews
INSERT INTO review (fk_client_id, fk_hotel_id, rating, review_date, review_text) VALUES
(1, 1, 5.0, '2025-05-10 09:30:00', 'Absolutely stunning resort with impeccable service. The ocean views were breathtaking, and the staff went above and beyond to make our stay memorable.'),
(2, 2, 4.5, '2025-05-05 14:20:00', 'Charming hotel with so much character and history. The breakfast was delicious, and the location was perfect for exploring the historic district.'),
(3, 3, 4.7, '2025-05-12 11:15:00', 'Loved the smart room features! The rooftop bar has the best views of the city skyline, especially at sunset. Will definitely return.'),
(4, 4, 5.0, '2025-05-08 16:45:00', 'The perfect mountain getaway. Sitting by the fireplace after a day of hiking was magical. The restaurant served the best local cuisine we''ve ever had.'),
(5, 5, 4.3, '2025-05-15 10:30:00', 'Great family vacation spot! The kids loved the pools and water slides. Rooms were spacious and clean. Would recommend for families with children of all ages.'),
(1, 4, 4.8, '2025-05-18 13:20:00', 'Our second stay at Alpine Lodge and it was even better than the first. The peaceful setting and attentive staff make this place special.'),
(2, 5, 4.6, '2025-05-14 15:10:00', 'The beachfront location is unbeatable. Enjoyed watching the sunrise from our balcony every morning. Great amenities for the whole family.');

-- Insert FAQs
INSERT INTO faq (fk_client_id, fk_hotel_id, faq_question, faq_answer) VALUES
(1, 1, 'What time is check-in and check-out?', 'Check-in time is 3:00 PM and check-out time is 11:00 AM. Early check-in and late check-out may be available upon request, subject to availability.'),
(2, 1, 'Is breakfast included in the room rate?', 'Breakfast is included with certain room packages. Please check your specific reservation details or contact our front desk for more information.'),
(3, 2, 'Do you offer airport shuttle service?', 'Yes, we offer complimentary airport shuttle service for all guests. Please provide your flight details at least 24 hours prior to arrival.'),
(4, 3, 'Is parking available at the hotel?', 'Yes, we offer both valet parking ($35/day) and self-parking ($25/day) options for our guests.'),
(5, 4, 'Are pets allowed at the resort?', 'Yes, we are a pet-friendly resort. There is a $50 pet fee per stay, and we welcome pets under 50 pounds. Please notify us in advance if you plan to bring a pet.'),
(1, 5, 'What activities are available for children?', 'We offer a Kids Club for ages 4-12, daily poolside activities, beach games, and special evening entertainment for the whole family.');
