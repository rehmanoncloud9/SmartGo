package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.Destination;
import models.Flight;

import java.util.ArrayList;
import java.util.List;

// FlightService handles everything related to flights.
// It loads sample flights on first run, lets users browse and search,
// and lets admins add new flights.

public class FlightService {

    private static int nextFlightId = 1;
    private static int nextDestinationId = 1;

    // Load existing data and add sample flights if none exist
    public static void init() {
        List<Flight> existing = DataStore.loadFlights();
        if (!existing.isEmpty()) {
            nextFlightId = existing.get(existing.size() - 1).getId() + 1;
        }

        List<Destination> destinations = DataStore.loadDestinations();
        if (!destinations.isEmpty()) {
            nextDestinationId = destinations.get(destinations.size() - 1).getId() + 1;
        }

        // Add sample data only if no flights exist yet
        if (existing.isEmpty()) {
            addSampleData();
        }
    }

    // Add some starting flights and destinations so the app is not empty on first run
    private static void addSampleData() {
        Destination d1 = new Destination(nextDestinationId++, "Dubai", "Gulf", "UAE",
                "Burj Khalifa, Dubai Mall, Desert Safari", "dubai.jpg");
        Destination d2 = new Destination(nextDestinationId++, "Paris", "Ile-de-France", "France",
                "Eiffel Tower, Louvre Museum, Notre Dame", "paris.jpg");
        Destination d3 = new Destination(nextDestinationId++, "Istanbul", "Marmara", "Turkey",
                "Blue Mosque, Grand Bazaar, Bosphorus", "istanbul.jpg");

        DataStore.saveDestination(d1);
        DataStore.saveDestination(d2);
        DataStore.saveDestination(d3);

        Flight f1 = new Flight(nextFlightId++, d1.getId(), 450.00, 180,
                "Emirates", "EK-611", "Economy", "2025-06-10 08:00", "2025-06-20 14:00");
        Flight f2 = new Flight(nextFlightId++, d1.getId(), 850.00, 40,
                "Emirates", "EK-612", "Business", "2025-06-12 10:00", "2025-06-22 16:00");
        Flight f3 = new Flight(nextFlightId++, d2.getId(), 620.00, 200,
                "Air France", "AF-201", "Economy", "2025-07-01 06:00", "2025-07-10 18:00");
        Flight f4 = new Flight(nextFlightId++, d3.getId(), 380.00, 150,
                "Turkish Airlines", "TK-708", "Economy", "2025-06-25 09:00", "2025-07-05 21:00");

        DataStore.saveFlight(f1);
        DataStore.saveFlight(f2);
        DataStore.saveFlight(f3);
        DataStore.saveFlight(f4);

        System.out.println("Sample flights and destinations loaded.");
    }

    // Show all available flights
    public static void showAllFlights() {
        List<Flight> flights = DataStore.loadFlights();
        List<Destination> destinations = DataStore.loadDestinations();

        if (flights.isEmpty()) {
            System.out.println("No flights available right now.");
            return;
        }

        System.out.println("\nAll Available Flights:");
        System.out.println("========================");
        for (Flight f : flights) {
            String destName = getDestinationName(destinations, f.getDestinationId());
            System.out.println(f);
            System.out.println("  Destination: " + destName);
            System.out.println();
        }
    }

    // Search flights by destination city name
    public static List<Flight> searchByDestination(String city) {
        List<Flight> flights = DataStore.loadFlights();
        List<Destination> destinations = DataStore.loadDestinations();
        List<Flight> results = new ArrayList<>();

        for (Destination d : destinations) {
            if (d.getCity().equalsIgnoreCase(city)) {
                for (Flight f : flights) {
                    if (f.getDestinationId() == d.getId()) {
                        results.add(f);
                    }
                }
            }
        }
        return results;
    }

    // Search flights by airline name
    public static List<Flight> searchByAirline(String airline) {
        List<Flight> flights = DataStore.loadFlights();
        List<Flight> results = new ArrayList<>();

        for (Flight f : flights) {
            if (f.getAirline().equalsIgnoreCase(airline)) {
                results.add(f);
            }
        }
        return results;
    }

    // Get a single flight by its ID
    public static Flight getFlightById(int id) throws SmartGoException {
        for (Flight f : DataStore.loadFlights()) {
            if (f.getId() == id) return f;
        }
        throw new SmartGoException("No flight found with ID " + id + ". Please check the ID and try again.");
    }

    // Add a new flight (admin only)
    public static void addFlight(int destinationId, double price, int capacity,
                                  String airline, String flightNumber, String flightClass,
                                  String departureTime, String returnTime) {
        Flight newFlight = new Flight(nextFlightId++, destinationId, price, capacity,
                airline, flightNumber, flightClass, departureTime, returnTime);
        DataStore.saveFlight(newFlight);
        System.out.println("Flight added successfully: " + newFlight.getFlightNumber());
    }

    // Get all available destinations
    public static List<Destination> getAllDestinations() {
        return DataStore.loadDestinations();
    }

    // Helper to get destination name by ID
    private static String getDestinationName(List<Destination> destinations, int id) {
        for (Destination d : destinations) {
            if (d.getId() == id) return d.getCity() + ", " + d.getCountry();
        }
        return "Unknown Destination";
    }
}
