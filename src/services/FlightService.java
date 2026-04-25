package services;

import models.Flight;
import exceptions.SmartGoException;
import data.DataStore;

import java.util.ArrayList;
import java.util.List;

// Flight Service: Search and manage flights
//
// This service lets users browse and find flights.
// It loads flights from the file and lets you search by destination.

public class FlightService {

    private List<Flight> flights;

    // Constructor: loads flights from file + adds sample data
    // if no flights exist yet (so the app isn't empty on first run)
    public FlightService() {
        this.flights = DataStore.loadFlights();

        // If no flights loaded, seed some sample data
        if (flights.isEmpty()) {
            seedSampleFlights();
        }
    }

    // Get all available flights
    public List<Flight> getAllFlights() {
        return flights;
    }

    // Search flights by destination ID
    public List<Flight> searchByDestination(int destinationId) throws SmartGoException {
        List<Flight> results = new ArrayList<>();

        for (Flight f : flights) {
            if (f.getDestinationId() == destinationId) {
                results.add(f);
            }
        }

        if (results.isEmpty()) {
            throw new SmartGoException("No flights found for the selected destination.");
        }

        return results;
    }

    // Get a single flight by its ID
    public Flight getFlightById(int flightId) throws SmartGoException {
        for (Flight f : flights) {
            if (f.getId() == flightId) {
                return f;
            }
        }
        throw new SmartGoException("Flight with ID " + flightId + " not found.");
    }

    // Display all flights in a numbered list
    public void displayAllFlights() {
        if (flights.isEmpty()) {
            System.out.println("  No flights available at the moment.");
            return;
        }

        System.out.println("\n  ========== AVAILABLE FLIGHTS ==========");
        int i = 1;
        for (Flight f : flights) {
            System.out.println("\n  [" + i + "]");
            f.displayTransportDetails(); // polymorphism: calls Flight's version
            i++;
        }
    }

    // Search flights by airline name
    public List<Flight> searchByAirline(String airline) throws SmartGoException {
        List<Flight> results = new ArrayList<>();

        for (Flight f : flights) {
            if (f.getAirline().equalsIgnoreCase(airline)) {
                results.add(f);
            }
        }

        if (results.isEmpty()) {
            throw new SmartGoException("No flights found for airline: " + airline);
        }

        return results;
    }

    // Add a new flight (Admin use)
    public void addFlight(Flight flight) {
        flights.add(flight);
        DataStore.saveFlights(flights);
        System.out.println("  ✅ Flight added successfully.");
    }

    // Seed Sample Data: runs only on first launch
    private void seedSampleFlights() {
        System.out.println("  [FlightService] No flights found. Loading sample flights...");

        flights.add(new Flight(1, 1, 15000, 150,
                "2025-06-01 08:00", "2025-06-01 09:30",
                "PIA", "PK-302", "ECONOMY", "Karachi"));

        flights.add(new Flight(2, 2, 45000, 200,
                "2025-06-05 14:00", "2025-06-05 18:00",
                "AirBlue", "PA-101", "BUSINESS", "Lahore"));

        flights.add(new Flight(3, 3, 32000, 180,
                "2025-06-10 06:00", "2025-06-10 12:00",
                "SereneAir", "ER-201", "ECONOMY", "Islamabad"));

        DataStore.saveFlights(flights);
        System.out.println("  [FlightService] Sample flights loaded.");
    }
}
