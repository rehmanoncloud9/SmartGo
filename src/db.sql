-- SmartGo Travel App Database Schema
-- This file creates all tables exactly as shown in the ERD.
-- Run this file in MySQL to set up the full database.


-- Drop tables in reverse order to avoid foreign key issues
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS meal_booking;
DROP TABLE IF EXISTS hotel_booking;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS meal_plan;
DROP TABLE IF EXISTS tour_plan_hotel;
DROP TABLE IF EXISTS tour_plan_transport;
DROP TABLE IF EXISTS tour_plan;
DROP TABLE IF EXISTS hotel;
DROP TABLE IF EXISTS flight;
DROP TABLE IF EXISTS bus;
DROP TABLE IF EXISTS train;
DROP TABLE IF EXISTS transport;
DROP TABLE IF EXISTS destination;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS person;


-- PERSON is the base table for everyone in the system.
-- Both user and admin link back to person using person_id.
CREATE TABLE person (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100)        NOT NULL,
    email         VARCHAR(150)        NOT NULL UNIQUE,
    phone         VARCHAR(20),
    password_hash VARCHAR(255)        NOT NULL,
    created_at    TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);


-- USER is a person who uses the app to book trips.
CREATE TABLE user (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    person_id   INT         NOT NULL,
    address     VARCHAR(255),
    last_login  TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES person(id)
);


-- ADMIN is a person who manages flights, hotels, and tour plans.
CREATE TABLE admin (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    person_id   INT         NOT NULL,
    role        VARCHAR(50),
    last_login  TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES person(id)
);


-- DESTINATION is a city or place that users can travel to.
-- Hotels, transports, and tour plans all belong to a destination.
CREATE TABLE destination (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    city        VARCHAR(100)    NOT NULL,
    region      VARCHAR(100),
    country     VARCHAR(100)    NOT NULL,
    attractions TEXT,
    image_url   VARCHAR(255)
);


-- TRANSPORT is the base table for all travel options.
-- Flight, bus, and train each have their own table that links here.
CREATE TABLE transport (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    destination_id  INT             NOT NULL,
    type            VARCHAR(20)     NOT NULL,
    price           DECIMAL(10, 2)  NOT NULL,
    capacity        INT             NOT NULL,
    FOREIGN KEY (destination_id) REFERENCES destination(id)
);


-- FLIGHT stores airline-specific details for a transport record.
CREATE TABLE flight (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    transport_id    INT         NOT NULL,
    airline         VARCHAR(100),
    flight_number   VARCHAR(20),
    class           VARCHAR(50),
    departure_time  DATETIME,
    return_time     DATETIME,
    FOREIGN KEY (transport_id) REFERENCES transport(id)
);


-- BUS stores bus-specific details for a transport record.
CREATE TABLE bus (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    transport_id    INT         NOT NULL,
    operator        VARCHAR(100),
    bus_type        VARCHAR(50),
    departure_time  DATETIME,
    arrival_time    DATETIME,
    FOREIGN KEY (transport_id) REFERENCES transport(id)
);


-- TRAIN stores train-specific details for a transport record.
CREATE TABLE train (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    transport_id    INT         NOT NULL,
    operator        VARCHAR(100),
    train_number    VARCHAR(20),
    departure_time  DATETIME,
    arrival_time    DATETIME,
    FOREIGN KEY (transport_id) REFERENCES transport(id)
);


-- HOTEL belongs to a destination and can be booked separately or as part of a tour plan.
CREATE TABLE hotel (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    destination_id    INT             NOT NULL,
    name              VARCHAR(150)    NOT NULL,
    rating            FLOAT,
    manager_contact   VARCHAR(100),
    address           VARCHAR(255),
    price_per_night   DECIMAL(10, 2),
    FOREIGN KEY (destination_id) REFERENCES destination(id)
);


-- TOUR_PLAN is a travel package created by an admin.
-- It is linked to a destination and managed by a specific admin.
CREATE TABLE tour_plan (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    destination_id  INT             NOT NULL,
    admin_id        INT             NOT NULL,
    title           VARCHAR(200)    NOT NULL,
    duration_days   INT,
    base_price      DECIMAL(10, 2),
    status          VARCHAR(20)     DEFAULT 'ACTIVE',
    FOREIGN KEY (destination_id) REFERENCES destination(id),
    FOREIGN KEY (admin_id)       REFERENCES admin(id)
);


-- TOUR_PLAN_TRANSPORT links a tour plan to one or more transport options.
CREATE TABLE tour_plan_transport (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    tour_plan_id    INT NOT NULL,
    transport_id    INT NOT NULL,
    FOREIGN KEY (tour_plan_id)  REFERENCES tour_plan(id),
    FOREIGN KEY (transport_id)  REFERENCES transport(id)
);


-- TOUR_PLAN_HOTEL links a tour plan to one or more hotels.
-- is_default marks the main recommended hotel for that tour.
CREATE TABLE tour_plan_hotel (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    tour_plan_id    INT         NOT NULL,
    hotel_id        INT         NOT NULL,
    is_default      BOOLEAN     DEFAULT FALSE,
    FOREIGN KEY (tour_plan_id)  REFERENCES tour_plan(id),
    FOREIGN KEY (hotel_id)      REFERENCES hotel(id)
);


-- MEAL_PLAN is an optional food package that belongs to a tour plan.
CREATE TABLE meal_plan (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    tour_plan_id    INT             NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    description     TEXT,
    price           DECIMAL(10, 2),
    FOREIGN KEY (tour_plan_id) REFERENCES tour_plan(id)
);


-- BOOKING is created when a user books a flight or a tour plan.
CREATE TABLE booking (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT         NOT NULL,
    booking_type    VARCHAR(20) NOT NULL,
    tour_plan_id    INT,
    transport_id    INT,
    booked_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (user_id)       REFERENCES user(id),
    FOREIGN KEY (tour_plan_id)  REFERENCES tour_plan(id),
    FOREIGN KEY (transport_id)  REFERENCES transport(id)
);


-- HOTEL_BOOKING stores the hotel details for a specific booking.
CREATE TABLE hotel_booking (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    booking_id  INT     NOT NULL,
    hotel_id    INT     NOT NULL,
    check_in    DATE,
    check_out   DATE,
    num_guests  INT,
    FOREIGN KEY (booking_id)    REFERENCES booking(id),
    FOREIGN KEY (hotel_id)      REFERENCES hotel(id)
);


-- MEAL_BOOKING links a booking to the meal plan the user chose.
-- is_cancelled lets us cancel just the meal without cancelling the whole booking.
CREATE TABLE meal_booking (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    booking_id      INT         NOT NULL,
    meal_plan_id    INT         NOT NULL,
    is_cancelled    BOOLEAN     DEFAULT FALSE,
    FOREIGN KEY (booking_id)    REFERENCES booking(id),
    FOREIGN KEY (meal_plan_id)  REFERENCES meal_plan(id)
);


-- REVIEW can be left for a hotel, a flight, or a tour plan.
-- reviewable_type says what kind of thing is being reviewed.
-- reviewable_id is the ID of that specific thing.
CREATE TABLE review (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT         NOT NULL,
    reviewable_type   VARCHAR(20) NOT NULL,
    reviewable_id     INT         NOT NULL,
    rating            INT         CHECK (rating BETWEEN 1 AND 5),
    comment           TEXT,
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);


-- BILL is automatically generated when a booking is made.
-- It includes the base amount, a platform fee, and the total.
CREATE TABLE bill (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    booking_id      INT             NOT NULL,
    base_amount     DECIMAL(10, 2)  NOT NULL,
    platform_fee    DECIMAL(10, 2)  NOT NULL,
    total_amount    DECIMAL(10, 2)  NOT NULL,
    status          VARCHAR(20)     DEFAULT 'UNPAID',
    FOREIGN KEY (booking_id) REFERENCES booking(id)
);


-- PAYMENT records each payment made against a bill.
-- payment_type can be ADVANCE or FULL.
CREATE TABLE payment (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    bill_id         INT             NOT NULL,
    amount          DECIMAL(10, 2)  NOT NULL,
    payment_type    VARCHAR(20),
    method          VARCHAR(20),
    paid_at         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20)     DEFAULT 'COMPLETED',
    FOREIGN KEY (bill_id) REFERENCES bill(id)
);


-- Sample data to test the database

INSERT INTO person (name, email, phone, password_hash) VALUES
('Rafay Ahmed',   'rafay@smartgo.com',  '03001234567', 'pass123'),
('Mani Admin',    'mani@smartgo.com',   '03009876543', 'admin123'),
('Sara Khan',     'sara@smartgo.com',   '03111234567', 'sara123');

INSERT INTO user (person_id, address) VALUES
(1, 'House 5, Street 3, Islamabad'),
(3, 'Flat 12, Block B, Lahore');

INSERT INTO admin (person_id, role) VALUES
(2, 'Super Admin');

INSERT INTO destination (city, region, country, attractions, image_url) VALUES
('Dubai',    'Gulf',         'UAE',    'Burj Khalifa, Dubai Mall, Desert Safari', 'dubai.jpg'),
('Paris',    'Ile-de-France','France', 'Eiffel Tower, Louvre Museum',             'paris.jpg'),
('Istanbul', 'Marmara',      'Turkey', 'Blue Mosque, Grand Bazaar, Bosphorus',    'istanbul.jpg');

INSERT INTO transport (destination_id, type, price, capacity) VALUES
(1, 'Flight', 450.00, 180),
(1, 'Flight', 850.00, 40),
(2, 'Flight', 620.00, 200),
(3, 'Flight', 380.00, 150);

INSERT INTO flight (transport_id, airline, flight_number, class, departure_time, return_time) VALUES
(1, 'Emirates',         'EK-611', 'Economy',  '2025-06-10 08:00:00', '2025-06-20 14:00:00'),
(2, 'Emirates',         'EK-612', 'Business', '2025-06-12 10:00:00', '2025-06-22 16:00:00'),
(3, 'Air France',       'AF-201', 'Economy',  '2025-07-01 06:00:00', '2025-07-10 18:00:00'),
(4, 'Turkish Airlines', 'TK-708', 'Economy',  '2025-06-25 09:00:00', '2025-07-05 21:00:00');

INSERT INTO hotel (destination_id, name, rating, manager_contact, address, price_per_night) VALUES
(1, 'Burj Al Arab',    5, '+971-4-301-7777', 'Jumeirah St, Dubai',      800.00),
(1, 'Dubai Inn',       3, '+971-4-222-1111', 'Deira, Dubai',             120.00),
(2, 'Hotel de Paris',  4, '+33-1-4444-5555', 'Rue de Rivoli, Paris',    350.00),
(3, 'Istanbul Palace', 4, '+90-212-333-4444','Sultanahmet, Istanbul',   200.00);

INSERT INTO tour_plan (destination_id, admin_id, title, duration_days, base_price, status) VALUES
(1, 1, 'Dubai Explorer',           7, 1200.00, 'ACTIVE'),
(2, 1, 'Paris Romantic Getaway',   5, 1500.00, 'ACTIVE'),
(3, 1, 'Istanbul Heritage Tour',   6,  950.00, 'ACTIVE');

INSERT INTO tour_plan_transport (tour_plan_id, transport_id) VALUES
(1, 1), (2, 3), (3, 4);

INSERT INTO tour_plan_hotel (tour_plan_id, hotel_id, is_default) VALUES
(1, 1, TRUE), (1, 2, FALSE),
(2, 3, TRUE),
(3, 4, TRUE);

INSERT INTO meal_plan (tour_plan_id, name, description, price) VALUES
(1, 'Standard Meals',   'Breakfast and dinner included',  80.00),
(1, 'Premium Meals',    'All meals plus room service',   150.00),
(2, 'Breakfast Only',   'Daily breakfast included',       50.00),
(3, 'Full Board',       'All meals included',            120.00);
