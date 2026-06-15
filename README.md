# SmartGo

SmartGo is a Java-based travel booking desktop application with a Swing GUI.  
It lets users register, log in, browse travel options, book trips, manage bookings, make payments, and leave reviews.

## Features

- User registration and login
- Browse and search flights
- Browse tour plans and meal plans
- Browse and search hotels
- Book flights, tours, and hotels
- View/cancel bookings and pay bills
- Submit and view reviews

## Tech Stack

- Java (Swing for UI)
- File-based persistence for application data (`src/data/*.txt`)
- SQL schema for relational modeling (`src/db.sql`)

## Project Structure

- `/src/Main.java` – application entry point and GUI flows
- `/src/services` – business logic (auth, booking, flight, hotel, tour plan, review)
- `/src/models` – domain models
- `/src/data/DataStore.java` – read/write persistence for text-based data files
- `/src/db.sql` – database schema and sample seed data

## How to Run

From the repository root:

1. Compile:
   ```bash
   mkdir -p /tmp/smartgo-build
   javac -d /tmp/smartgo-build $(find src -name '*.java')
   ```
2. Run:
   ```bash
   java -cp /tmp/smartgo-build Main
   ```

## Data Notes

- Runtime data is stored in the `data/` folder (created automatically on startup).
- The app loads and saves records using plain text files through `DataStore`.

## Database Schema

`src/db.sql` contains a full relational schema for SmartGo entities and sample insert statements for testing/demo purposes.
