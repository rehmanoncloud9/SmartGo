SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';


CREATE TABLE PERSON (
    id            CHAR(36)     NOT NULL,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    phone         VARCHAR(20)  NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_person_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE USER (
    id          CHAR(36)     NOT NULL,
    person_id   CHAR(36)     NOT NULL,
    address     VARCHAR(255) NOT NULL,
    last_login  TIMESTAMP    NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_user_person (person_id),
    CONSTRAINT fk_user_person
        FOREIGN KEY (person_id) REFERENCES PERSON(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE ADMIN (
    id          CHAR(36)     NOT NULL,
    person_id   CHAR(36)     NOT NULL,
    role        VARCHAR(100) NOT NULL,
    last_login  TIMESTAMP    NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_admin_person (person_id),
    CONSTRAINT fk_admin_person
        FOREIGN KEY (person_id) REFERENCES PERSON(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE DESTINATION (
    id          CHAR(36)     NOT NULL,
    city        VARCHAR(255) NOT NULL,
    region      VARCHAR(255) NOT NULL,
    country     VARCHAR(255) NOT NULL,
    attractions TEXT         NOT NULL,
    image_url   VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_dest_country (country),
    INDEX idx_dest_city    (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE HOTEL (
    id              CHAR(36)       NOT NULL,
    destination_id  CHAR(36)       NOT NULL,
    name            VARCHAR(255)   NOT NULL,
    rating          FLOAT          NOT NULL,
    manager_contact VARCHAR(255)   NOT NULL,
    address         VARCHAR(255)   NOT NULL,
    base_price      DECIMAL(10,2)  NOT NULL,
    price_per_night DECIMAL(10,2)  NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_hotel_dest (destination_id),
    CONSTRAINT fk_hotel_dest
        FOREIGN KEY (destination_id) REFERENCES DESTINATION(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE TRANSPORT (
    id             CHAR(36)      NOT NULL,
    destination_id CHAR(36)      NOT NULL,
    type           VARCHAR(20)   NOT NULL,
    price          DECIMAL(10,2) NOT NULL,
    capacity       INT           NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_transport_dest (destination_id),
    CONSTRAINT fk_transport_dest
        FOREIGN KEY (destination_id) REFERENCES DESTINATION(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE TRANSPORT (
    id             CHAR(36)      NOT NULL,
    destination_id CHAR(36)      NOT NULL,
    type           VARCHAR(20)   NOT NULL,
    price          DECIMAL(10,2) NOT NULL,
    capacity       INT           NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_transport_dest (destination_id),
    INDEX idx_transport_type (type),
    CONSTRAINT chk_transport_type
        CHECK (type IN ('FLIGHT', 'BUS', 'TRAIN')),
    CONSTRAINT fk_transport_dest
        FOREIGN KEY (destination_id) REFERENCES DESTINATION(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE FLIGHT (
    id             CHAR(36)     NOT NULL,
    transport_id   CHAR(36)     NOT NULL,
    airline        VARCHAR(255) NOT NULL,
    flight_number  VARCHAR(20)  NOT NULL,
    class          VARCHAR(50)  NOT NULL,
    departure_time DATETIME     NOT NULL,
    return_time    DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_flight_transport (transport_id),
    INDEX idx_flight_number (flight_number),
    CONSTRAINT chk_flight_class
        CHECK (class IN ('ECONOMY', 'BUSINESS', 'FIRST')),
    CONSTRAINT fk_flight_transport
        FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE BUS (
    id             CHAR(36)     NOT NULL,
    transport_id   CHAR(36)     NOT NULL,
    operator       VARCHAR(255) NOT NULL,
    bus_type       VARCHAR(100) NOT NULL,
    departure_time DATETIME     NOT NULL,
    arrival_time   DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_bus_transport (transport_id),
    CONSTRAINT fk_bus_transport
        FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE TRAIN (
    id             CHAR(36)     NOT NULL,
    transport_id   CHAR(36)     NOT NULL,
    operator       VARCHAR(255) NOT NULL,
    train_number   VARCHAR(20)  NOT NULL,
    departure_time DATETIME     NOT NULL,
    arrival_time   DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_train_transport (transport_id),
    CONSTRAINT fk_train_transport
        FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE TOUR_PLAN (
    id             CHAR(36)      NOT NULL,
    destination_id CHAR(36)      NOT NULL,
    admin_id       CHAR(36)      NOT NULL,
    title          VARCHAR(255)  NOT NULL,
    duration_days  INT           NOT NULL,
    base_price     DECIMAL(10,2) NOT NULL,
    status         VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (id),
    INDEX idx_tp_dest  (destination_id),
    INDEX idx_tp_admin (admin_id),
    INDEX idx_tp_status(status),
    CONSTRAINT chk_tp_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'DRAFT', 'ARCHIVED')),
    CONSTRAINT fk_tp_dest
        FOREIGN KEY (destination_id) REFERENCES DESTINATION(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_tp_admin
        FOREIGN KEY (admin_id) REFERENCES ADMIN(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE TOUR_PLAN_TRANSPORT (
    id           CHAR(36) NOT NULL,
    tour_plan_id CHAR(36) NOT NULL,
    transport_id CHAR(36) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_tpt (tour_plan_id, transport_id),
    INDEX idx_tpt_tp   (tour_plan_id),
    INDEX idx_tpt_tr   (transport_id),
    CONSTRAINT fk_tpt_tp
        FOREIGN KEY (tour_plan_id) REFERENCES TOUR_PLAN(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tpt_tr
        FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE TOUR_PLAN_HOTEL (
    id           CHAR(36)    NOT NULL,
    tour_plan_id CHAR(36)    NOT NULL,
    hotel_id     CHAR(36)    NOT NULL,
    is_default   TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE INDEX uq_tph (tour_plan_id, hotel_id),
    INDEX idx_tph_tp (tour_plan_id),
    INDEX idx_tph_h  (hotel_id),
    CONSTRAINT fk_tph_tp
        FOREIGN KEY (tour_plan_id) REFERENCES TOUR_PLAN(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tph_h
        FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE MEAL_PLAN (
    id           CHAR(36)      NOT NULL,
    tour_plan_id CHAR(36)      NOT NULL,
    name         VARCHAR(255)  NOT NULL,
    description  TEXT          NOT NULL,
    price        DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_mp_tp (tour_plan_id),
    CONSTRAINT fk_mp_tp
        FOREIGN KEY (tour_plan_id) REFERENCES TOUR_PLAN(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE BOOKING (
    id           CHAR(36)    NOT NULL,
    user_id      CHAR(36)    NOT NULL,
    booking_type VARCHAR(20) NOT NULL,
    tour_plan_id CHAR(36)    DEFAULT NULL,
    transport_id CHAR(36)    DEFAULT NULL,
    booked_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    PRIMARY KEY (id),
    INDEX idx_bk_user    (user_id),
    INDEX idx_bk_tp      (tour_plan_id),
    INDEX idx_bk_tr      (transport_id),
    INDEX idx_bk_status  (status),
    INDEX idx_bk_booked  (booked_at),
    CONSTRAINT chk_bk_type
        CHECK (booking_type IN ('STANDALONE', 'TOUR_PLAN')),
    CONSTRAINT chk_bk_status
        CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
    CONSTRAINT fk_bk_user
        FOREIGN KEY (user_id) REFERENCES USER(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_bk_tp
        FOREIGN KEY (tour_plan_id) REFERENCES TOUR_PLAN(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_bk_tr
        FOREIGN KEY (transport_id) REFERENCES TRANSPORT(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE HOTEL_BOOKING (
    id         CHAR(36) NOT NULL,
    booking_id CHAR(36) NOT NULL,
    hotel_id   CHAR(36) NOT NULL,
    check_in   DATE     NOT NULL,
    check_out  DATE     NOT NULL,
    num_guests INT      NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_hb_booking (booking_id),
    INDEX idx_hb_hotel   (hotel_id),
    CONSTRAINT fk_hb_booking
        FOREIGN KEY (booking_id) REFERENCES BOOKING(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_hb_hotel
        FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE MEAL_BOOKING (
    id           CHAR(36)   NOT NULL,
    booking_id   CHAR(36)   NOT NULL,
    meal_plan_id CHAR(36)   NOT NULL,
    is_cancelled TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_mb_booking (booking_id),
    INDEX idx_mb_meal    (meal_plan_id),
    CONSTRAINT fk_mb_booking
        FOREIGN KEY (booking_id) REFERENCES BOOKING(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mb_meal
        FOREIGN KEY (meal_plan_id) REFERENCES MEAL_PLAN(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE REVIEW (
    id              CHAR(36)    NOT NULL,
    user_id         CHAR(36)    NOT NULL,
    reviewable_type VARCHAR(20) NOT NULL,
    reviewable_id   CHAR(36)    NOT NULL,
    rating          INT         NOT NULL,
    comment         TEXT        NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_rv_user       (user_id),
    INDEX idx_rv_target     (reviewable_type, reviewable_id),
    CONSTRAINT chk_rv_type
        CHECK (reviewable_type IN ('HOTEL', 'FLIGHT', 'TOUR_PLAN')),
    CONSTRAINT chk_rv_rating
        CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT fk_rv_user
        FOREIGN KEY (user_id) REFERENCES USER(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE BILL (
    id           CHAR(36)      NOT NULL,
    booking_id   CHAR(36)      NOT NULL,
    base_amount  DECIMAL(10,2) NOT NULL,
    platform_fee DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'UNPAID',
    PRIMARY KEY (id),
    UNIQUE INDEX uq_bill_booking (booking_id),
    INDEX idx_bill_status (status),
    CONSTRAINT chk_bill_status
        CHECK (status IN ('UNPAID', 'PARTIALLY_PAID', 'PAID', 'REFUNDED')),
    CONSTRAINT fk_bill_booking
        FOREIGN KEY (booking_id) REFERENCES BOOKING(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE PAYMENT (
    id           CHAR(36)      NOT NULL,
    bill_id      CHAR(36)      NOT NULL,
    amount       DECIMAL(10,2) NOT NULL,
    payment_type VARCHAR(20)   NOT NULL,
    method       VARCHAR(20)   NOT NULL,
    paid_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(20)   NOT NULL DEFAULT 'SUCCESS',
    PRIMARY KEY (id),
    INDEX idx_pay_bill   (bill_id),
    INDEX idx_pay_status (status),
    INDEX idx_pay_paid   (paid_at),
    CONSTRAINT chk_pay_type
        CHECK (payment_type IN ('ADVANCE', 'FINAL', 'FULL', 'REFUND')),
    CONSTRAINT chk_pay_method
        CHECK (method IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'CASH', 'WALLET')),
    CONSTRAINT chk_pay_status
        CHECK (status IN ('SUCCESS', 'FAILED', 'PENDING', 'REFUNDED')),
    CONSTRAINT fk_pay_bill
        FOREIGN KEY (bill_id) REFERENCES BILL(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO PERSON (id, name, email, phone, password_hash, created_at) VALUES
('p-0001-0000-0000-000000000001', 'Abdul Rehman', 'abdul@smartgo.pk',   '+92-300-1234567', '$2b$12$hashUserAbdulRehman',  '2025-01-10 09:00:00'),
('p-0001-0000-0000-000000000002', 'Fakiha Noor',  'faki@smartgo.pk',    '+92-301-2345678', '$2b$12$hashUserFakihaNoor',   '2025-01-11 10:30:00'),
('p-0001-0000-0000-000000000003', 'Sara Ahmed',   'sara@smartgo.pk',    '+92-302-3456789', '$2b$12$hashUserSaraAhmed',   '2025-01-12 08:15:00'),
('p-0001-0000-0000-000000000004', 'Admin Ali',    'ali.admin@smartgo.pk','+92-321-9999999', '$2b$12$hashAdminAli',        '2025-01-01 08:00:00'),
('p-0001-0000-0000-000000000005', 'Admin Zara',   'zara.admin@smartgo.pk','+92-322-8888888','$2b$12$hashAdminZara',       '2025-01-01 08:30:00');

INSERT INTO USER (id, person_id, address, last_login) VALUES
('u-0002-0000-0000-000000000001', 'p-0001-0000-0000-000000000001', 'Hostel-6, NUST, H-12, Islamabad',  '2025-04-18 14:22:00'),
('u-0002-0000-0000-000000000002', 'p-0001-0000-0000-000000000002', 'Block-B, Bahria Town, Lahore',      '2025-04-17 09:05:00'),
('u-0002-0000-0000-000000000003', 'p-0001-0000-0000-000000000003', '12 Garden Town, Faisalabad',        '2025-04-15 20:11:00');

INSERT INTO ADMIN (id, person_id, role, last_login) VALUES
('a-0003-0000-0000-000000000001', 'p-0001-0000-0000-000000000004', 'SUPER_ADMIN',    '2025-04-19 08:00:00'),
('a-0003-0000-0000-000000000002', 'p-0001-0000-0000-000000000005', 'CONTENT_ADMIN',  '2025-04-18 17:45:00');

INSERT INTO DESTINATION (id, city, region, country, attractions, image_url) VALUES
('d-0004-0000-0000-000000000001', 'Lahore',    'Punjab',          'Pakistan', 'Badshahi Mosque, Lahore Fort, Shalimar Gardens',   'https://cdn.smartgo.pk/lahore.jpg'),
('d-0004-0000-0000-000000000002', 'Murree',    'Punjab Hills',    'Pakistan', 'Mall Road, Patriata Chairlift, Pindi Point',        'https://cdn.smartgo.pk/murree.jpg'),
('d-0004-0000-0000-000000000003', 'Hunza',     'Gilgit-Baltistan','Pakistan', 'Karimabad Bazaar, Rakaposhi View, Attabad Lake',    'https://cdn.smartgo.pk/hunza.jpg'),
('d-0004-0000-0000-000000000004', 'Karachi',   'Sindh',           'Pakistan', 'Clifton Beach, Mohatta Palace, Port Grand',         'https://cdn.smartgo.pk/karachi.jpg'),
('d-0004-0000-0000-000000000005', 'Islamabad', 'Federal Capital', 'Pakistan', 'Faisal Mosque, Margalla Hills, Lok Virsa Museum',   'https://cdn.smartgo.pk/islamabad.jpg');

INSERT INTO HOTEL (id, destination_id, name, rating, manager_contact, address, base_price, price_per_night) VALUES
('h-0005-0000-0000-000000000001', 'd-0004-0000-0000-000000000001', 'Pearl Continental Lahore',  5.0, '+92-42-111505505', '99-A Shah Rah-e-Quaid-e-Azam, Lahore',  12000.00, 12000.00),
('h-0005-0000-0000-000000000002', 'd-0004-0000-0000-000000000002', 'Hotel Lockwood Murree',     4.0, '+92-51-3410010',  'The Mall, Murree',                       6500.00,  6500.00),
('h-0005-0000-0000-000000000003', 'd-0004-0000-0000-000000000003', 'Hunza Serena Inn',          4.5, '+92-5811-457079', 'Karimabad, Hunza Valley',                9000.00,  9000.00),
('h-0005-0000-0000-000000000004', 'd-0004-0000-0000-000000000004', 'Movenpick Hotel Karachi',   5.0, '+92-21-35630000', 'Club Road, Karachi',                     15000.00, 15000.00),
('h-0005-0000-0000-000000000005', 'd-0004-0000-0000-000000000005', 'Marriott Islamabad',        5.0, '+92-51-2826121',  'Aga Khan Rd, Islamabad',                 18000.00, 18000.00);

INSERT INTO TRANSPORT (id, destination_id, type, price, capacity) VALUES
('tr-0006-000-0000-000000000001', 'd-0004-0000-0000-000000000001', 'FLIGHT', 15000.00, 180),
('tr-0006-000-0000-000000000002', 'd-0004-0000-0000-000000000002', 'FLIGHT', 8500.00,  150),
('tr-0006-000-0000-000000000003', 'd-0004-0000-0000-000000000003', 'FLIGHT', 22000.00, 80),
('tr-0006-000-0000-000000000004', 'd-0004-0000-0000-000000000002', 'BUS',    2500.00,  45),
('tr-0006-000-0000-000000000005', 'd-0004-0000-0000-000000000001', 'TRAIN',  3000.00,  200);

INSERT INTO FLIGHT (id, transport_id, airline, flight_number, class, departure_time, return_time) VALUES
('fl-0007-000-0000-000000000001', 'tr-0006-000-0000-000000000001', 'PIA',          'PK-300', 'ECONOMY',  '2025-06-10 07:30:00', '2025-06-17 15:00:00'),
('fl-0007-000-0000-000000000002', 'tr-0006-000-0000-000000000002', 'AirSial',      'PF-101', 'ECONOMY',  '2025-07-01 09:00:00', '2025-07-05 18:00:00'),
('fl-0007-000-0000-000000000003', 'tr-0006-000-0000-000000000003', 'Serene Air',   'ER-505', 'BUSINESS', '2025-08-15 06:00:00', '2025-08-22 14:30:00');

INSERT INTO BUS (id, transport_id, operator, bus_type, departure_time, arrival_time) VALUES
('bs-0008-000-0000-000000000001', 'tr-0006-000-0000-000000000004', 'Daewoo Express', 'LUXURY',    '2025-06-10 22:00:00', '2025-06-11 05:00:00'),
('bs-0008-000-0000-000000000002', 'tr-0006-000-0000-000000000004', 'Faisal Movers',  'STANDARD',  '2025-07-01 21:00:00', '2025-07-02 04:30:00'),
('bs-0008-000-0000-000000000003', 'tr-0006-000-0000-000000000004', 'Skyways',        'EXECUTIVE', '2025-08-15 23:00:00', '2025-08-16 06:00:00');

INSERT INTO TRAIN (id, transport_id, operator, train_number, departure_time, arrival_time) VALUES
('tn-0009-000-0000-000000000001', 'tr-0006-000-0000-000000000005', 'Pakistan Railways', 'PR-001-Tezgam',  '2025-06-10 08:00:00', '2025-06-10 14:00:00'),
('tn-0009-000-0000-000000000002', 'tr-0006-000-0000-000000000005', 'Pakistan Railways', 'PR-005-Khyber',  '2025-07-01 06:30:00', '2025-07-01 12:30:00'),
('tn-0009-000-0000-000000000003', 'tr-0006-000-0000-000000000005', 'Pakistan Railways', 'PR-007-Fareed',  '2025-08-15 07:00:00', '2025-08-15 13:00:00');

INSERT INTO TOUR_PLAN (id, destination_id, admin_id, title, duration_days, base_price, status) VALUES
('tp-0010-000-0000-000000000001', 'd-0004-0000-0000-000000000002', 'a-0003-0000-0000-000000000001', 'Murree Winter Escape',   4,  28000.00, 'ACTIVE'),
('tp-0010-000-0000-000000000002', 'd-0004-0000-0000-000000000003', 'a-0003-0000-0000-000000000001', 'Hunza Valley Explorer',  7,  65000.00, 'ACTIVE'),
('tp-0010-000-0000-000000000003', 'd-0004-0000-0000-000000000001', 'a-0003-0000-0000-000000000002', 'Lahore Heritage Tour',   3,  18000.00, 'ACTIVE'),
('tp-0010-000-0000-000000000004', 'd-0004-0000-0000-000000000004', 'a-0003-0000-0000-000000000002', 'Karachi City Getaway',   2,  22000.00, 'DRAFT');

INSERT INTO TOUR_PLAN_TRANSPORT (id, tour_plan_id, transport_id) VALUES
('tpt-001-0000-0000-000000000001', 'tp-0010-000-0000-000000000001', 'tr-0006-000-0000-000000000002'),
('tpt-001-0000-0000-000000000002', 'tp-0010-000-0000-000000000002', 'tr-0006-000-0000-000000000003'),
('tpt-001-0000-0000-000000000003', 'tp-0010-000-0000-000000000003', 'tr-0006-000-0000-000000000001');

INSERT INTO TOUR_PLAN_HOTEL (id, tour_plan_id, hotel_id, is_default) VALUES
('tph-001-0000-0000-000000000001', 'tp-0010-000-0000-000000000001', 'h-0005-0000-0000-000000000002', 1),
('tph-001-0000-0000-000000000002', 'tp-0010-000-0000-000000000002', 'h-0005-0000-0000-000000000003', 1),
('tph-001-0000-0000-000000000003', 'tp-0010-000-0000-000000000003', 'h-0005-0000-0000-000000000001', 1),
('tph-001-0000-0000-000000000004', 'tp-0010-000-0000-000000000003', 'h-0005-0000-0000-000000000005', 0);

INSERT INTO MEAL_PLAN (id, tour_plan_id, name, description, price) VALUES
('mp-0011-000-0000-000000000001', 'tp-0010-000-0000-000000000001', 'Standard Breakfast', 'Daily continental breakfast at hotel', 1200.00),
('mp-0011-000-0000-000000000002', 'tp-0010-000-0000-000000000001', 'Full Board',          'Breakfast, lunch and dinner included', 3500.00),
('mp-0011-000-0000-000000000003', 'tp-0010-000-0000-000000000002', 'Adventure Pack',      'High-calorie meals for trekking days',  4200.00),
('mp-0011-000-0000-000000000004', 'tp-0010-000-0000-000000000003', 'Lahori Desi Khana',   'Authentic Lahori cuisine — biryani, nihari, falooda', 2000.00);

INSERT INTO BOOKING (id, user_id, booking_type, tour_plan_id, transport_id, booked_at, status) VALUES
('bk-0012-000-0000-000000000001', 'u-0002-0000-0000-000000000001', 'TOUR_PLAN',  'tp-0010-000-0000-000000000001', NULL,                            '2025-04-01 10:00:00', 'CONFIRMED'),
('bk-0012-000-0000-000000000002', 'u-0002-0000-0000-000000000002', 'STANDALONE', NULL,                            'tr-0006-000-0000-000000000001', '2025-04-05 14:30:00', 'CONFIRMED'),
('bk-0012-000-0000-000000000003', 'u-0002-0000-0000-000000000003', 'TOUR_PLAN',  'tp-0010-000-0000-000000000002', NULL,                            '2025-04-10 08:00:00', 'PENDING'),
('bk-0012-000-0000-000000000004', 'u-0002-0000-0000-000000000001', 'STANDALONE', NULL,                            'tr-0006-000-0000-000000000002', '2025-04-15 11:00:00', 'CANCELLED');

INSERT INTO HOTEL_BOOKING (id, booking_id, hotel_id, check_in, check_out, num_guests) VALUES
('hb-0013-000-0000-000000000001', 'bk-0012-000-0000-000000000001', 'h-0005-0000-0000-000000000002', '2025-06-10', '2025-06-14', 2),
('hb-0013-000-0000-000000000002', 'bk-0012-000-0000-000000000002', 'h-0005-0000-0000-000000000001', '2025-06-10', '2025-06-13', 1),
('hb-0013-000-0000-000000000003', 'bk-0012-000-0000-000000000003', 'h-0005-0000-0000-000000000003', '2025-08-15', '2025-08-22', 3);

INSERT INTO MEAL_BOOKING (id, booking_id, meal_plan_id, is_cancelled) VALUES
('mb-0014-000-0000-000000000001', 'bk-0012-000-0000-000000000001', 'mp-0011-000-0000-000000000002', 0),
('mb-0014-000-0000-000000000002', 'bk-0012-000-0000-000000000003', 'mp-0011-000-0000-000000000003', 0),
('mb-0014-000-0000-000000000003', 'bk-0012-000-0000-000000000001', 'mp-0011-000-0000-000000000001', 1);

INSERT INTO REVIEW (id, user_id, reviewable_type, reviewable_id, rating, comment, created_at) VALUES
('rv-0015-000-0000-000000000001', 'u-0002-0000-0000-000000000001', 'HOTEL',     'h-0005-0000-0000-000000000002', 5, 'Amazing views and excellent service at Lockwood!',           '2025-04-15 16:00:00'),
('rv-0015-000-0000-000000000002', 'u-0002-0000-0000-000000000002', 'FLIGHT',    'fl-0007-000-0000-000000000001', 4, 'PIA was on time for once. Comfortable economy seats.',        '2025-04-16 09:00:00'),
('rv-0015-000-0000-000000000003', 'u-0002-0000-0000-000000000003', 'TOUR_PLAN', 'tp-0010-000-0000-000000000002', 5, 'Hunza tour exceeded all expectations. Highly recommended!',   '2025-04-18 11:30:00');

INSERT INTO BILL (id, booking_id, base_amount, platform_fee, total_amount, status) VALUES
('bl-0016-000-0000-000000000001', 'bk-0012-000-0000-000000000001', 28000.00, 840.00,  28840.00, 'PARTIALLY_PAID'),
('bl-0016-000-0000-000000000002', 'bk-0012-000-0000-000000000002', 15000.00, 450.00,  15450.00, 'PAID'),
('bl-0016-000-0000-000000000003', 'bk-0012-000-0000-000000000003', 65000.00, 1950.00, 66950.00, 'UNPAID'),
('bl-0016-000-0000-000000000004', 'bk-0012-000-0000-000000000004', 8500.00,  255.00,  8755.00,  'REFUNDED');

INSERT INTO PAYMENT (id, bill_id, amount, payment_type, method, paid_at, status) VALUES
('py-0017-000-0000-000000000001', 'bl-0016-000-0000-000000000001', 11536.00, 'ADVANCE', 'CREDIT_CARD',   '2025-04-01 10:05:00', 'SUCCESS'),
('py-0017-000-0000-000000000002', 'bl-0016-000-0000-000000000002', 15450.00, 'FULL',    'BANK_TRANSFER', '2025-04-05 14:35:00', 'SUCCESS'),
('py-0017-000-0000-000000000003', 'bl-0016-000-0000-000000000004', 8755.00,  'REFUND',  'BANK_TRANSFER', '2025-04-16 12:00:00', 'REFUNDED');

