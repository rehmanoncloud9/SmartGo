-- 1. Create User Table
CREATE TABLE User (
    UserID INT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 2. Create Packages Table
CREATE TABLE Packages (
    Package_Id INT PRIMARY KEY,
    Duration INT,
    TotalCost FLOAT,
    Type VARCHAR(100)
);

-- 3. Create Flights Table (Many Flights belong to one Package)
CREATE TABLE Flights (
    FlightId INT PRIMARY KEY,
    Source VARCHAR(100),
    Destination VARCHAR(100),
    Time DATETIME,
    Company VARCHAR(100),
    Ticket_Cost DOUBLE
 
   
);

create table flight_has_packages
(
    flight_package_id int primary key,
    FlightId int,
    Package_Id int,
    
    constraint flight_fk foreign key (FlightId)
    references Flights(FlightId)
    on delete cascade
    constraint package_fk foreign key (Package_Id)
    references Packages(Package_Id)
    on delete cascade 
)

-- 4. Create Hotels Table
CREATE TABLE Hotels (
    Hotel_ID INT PRIMARY KEY,
    Hotel_Name VARCHAR(255),
    address VARCHAR(255),
    No_of_rooms INT
);

-- 5. Create Rooms Table (Many Rooms belong to one Hotel)
CREATE TABLE Rooms (
    Room_no INT PRIMARY KEY,
    Room_Type VARCHAR(100),
    Bed_type VARCHAR(100),
    Rent DOUBLE,
    Hotel_ID INT,
    FOREIGN KEY (Hotel_ID) REFERENCES Hotels(Hotel_ID)
);

-- 6. Create Bookings Table (Links User and Package)
CREATE TABLE Bookings (
    BkID INT PRIMARY KEY,
    PackageID INT,
    CustomerID INT,
    FOREIGN KEY (PackageID) REFERENCES Packages(Package_Id),
    FOREIGN KEY (CustomerID) REFERENCES User(UserID)
);

create table package_hotel (
    package_hotel_id int primary key,
    Package_Id int,
    Hotel_Id int,
    constraint hotel_fk foreign key (Hotel_Id)
    references Hotels(Hotel_Id)
    on delete cascade
    constraint package_fk foreign key (Package_Id)
    references Packages(Package_Id)
    on delete cascade 
)