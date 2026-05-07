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

    // These counters keep track of the next unique IDs for flights and destinations
    private static int nextFlightId = 1;
    private static int nextDestinationId = 1;

    // This method sets up the initial flight data when the app starts up
    public static void init() {
        // Step 1: This is critical logic because we look at the existing flights to find the highest ID used
        List<Flight> existing = DataStore.loadFlights();
        if (!existing.isEmpty()) {
            // Step 2: We set our counter to the next number so we never reuse an ID, even after restarting the app
            nextFlightId = existing.get(existing.size() - 1).getId() + 1;
        }

        // Step 3: We do the same check for destinations to keep those IDs unique as well
        List<Destination> destinations = DataStore.loadDestinations();
        if (!destinations.isEmpty()) {
            nextDestinationId = destinations.get(destinations.size() - 1).getId() + 1;
        }

        // Step 4: If this is the very first time the app is running we populate it with sample data
        if (existing.isEmpty()) {
            addSampleData();
        }
    }

    // This creates a few default flights so the user has something to browse immediately
    private static void addSampleData() {
        // Step 1: Create the destinations first so the flights have a place to go
        Destination d1 = new Destination(nextDestinationId++, "Multan", "Punjab", "Pakistan",
                "Shah Rukn-e-Alam Shrine, Multan Fort, Ghanta Ghar", "multan.jpg");
        Destination d2 = new Destination(nextDestinationId++, "Lahore", "Punjab", "Pakistan",
                "Badshahi Mosque, Lahore Fort, Shalimar Gardens", "lahore.jpg");
        Destination d3 = new Destination(nextDestinationId++, "Skardu", "Gilgit-Baltistan", "Pakistan",
                "Shangrila Resort, Deosai Plains, Upper Kachura Lake", "skardu.jpg");

        // Step 2: Save these destinations to the file
        DataStore.saveDestination(d1);
        DataStore.saveDestination(d2);
        DataStore.saveDestination(d3);

        // Step 3: Create the flight records and link them to the destination IDs we just created
        Flight f1 = new Flight(nextFlightId++, d1.getId(), 55.00, 180,
                "PIA", "PK-341", "Economy", "2025-06-10 08:00", "2025-06-14 14:00");
        Flight f2 = new Flight(nextFlightId++, d2.getId(), 45.00, 200,
                "AirBlue", "PA-210", "Economy", "2025-06-15 07:30", "2025-06-20 18:00");
        Flight f3 = new Flight(nextFlightId++, d2.getId(), 120.00, 40,
                "AirBlue", "PA-211", "Business", "2025-06-18 09:00", "2025-06-23 16:00");
        Flight f4 = new Flight(nextFlightId++, d3.getId(), 95.00, 150,
                "PIA", "PK-451", "Economy", "2025-07-01 06:00", "2025-07-08 21:00");

        // Step 4: Finalize the setup by saving all the sample flights
        DataStore.saveFlight(f1);
        DataStore.saveFlight(f2);
        DataStore.saveFlight(f3);
        DataStore.saveFlight(f4);

        System.out.println("Sample flights and destinations loaded.");
    }

    // This prints every available flight along with its destination details
    public static void showAllFlights() {
        // Step 1: Load the full list of flights and destinations from the database
        List<Flight> flights = DataStore.loadFlights();
        List<Destination> destinations = DataStore.loadDestinations();

        if (flights.isEmpty()) {
            System.out.println("No flights available right now.");
            return;
        }

        System.out.println("\nAll Available Flights:");
        System.out.println("========================");
        // Step 2: Loop through each flight and find its matching city name to show the user
        for (Flight f : flights) {
            String destName = getDestinationName(destinations, f.getDestinationId());
            System.out.println(f);
            System.out.println("  Destination: " + destName);
            System.out.println();
        }
    }

    // This lets users filter flights by typing in a city name
    public static List<Flight> searchByDestination(String city) {
        // Step 1: Get all the data needed for the cross-reference search
        List<Flight> flights = DataStore.loadFlights();
        List<Destination> destinations = DataStore.loadDestinations();
        List<Flight> results = new ArrayList<>();

        // Step 2: First find the destination ID that matches the city name entered
        for (Destination d : destinations) {
            // We ignore case so 'multan' and 'Multan' both work correctly
            if (d.getCity().equalsIgnoreCase(city)) {
                // Step 3: Once we have the ID we find all flights headed to that specific destination
                for (Flight f : flights) {
                    if (f.getDestinationId() == d.getId()) {
                        results.add(f);
                    }
                }
            }
        }
        return results;
    }

    // This filters flights based on the airline name provided
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

    // This finds a specific flight using its ID number
    public static Flight getFlightById(int id) throws SmartGoException {
        for (Flight f : DataStore.loadFlights()) {
            if (f.getId() == id) return f;
        }
        throw new SmartGoException("No flight found with ID " + id + ". Please check the ID and try again.");
    }

    // This method allows an administrator to add a brand new flight to the system
    public static void addFlight(int destinationId, double price, int capacity,
                                  String airline, String flightNumber, String flightClass,
                                  String departureTime, String returnTime) {
        Flight newFlight = new Flight(nextFlightId++, destinationId, price, capacity,
                airline, flightNumber, flightClass, departureTime, returnTime);
        DataStore.saveFlight(newFlight);
        System.out.println("Flight added successfully: " + newFlight.getFlightNumber());
    }

    // Returns a complete list of all destinations currently in the database
    public static List<Destination> getAllDestinations() {
        return DataStore.loadDestinations();
    }

    // This is a private helper to turn a destination ID into a readable city name
    private static String getDestinationName(List<Destination> destinations, int id) {
        for (Destination d : destinations) {
            if (d.getId() == id) return d.getCity() + ", " + d.getCountry();
        }
        return "Unknown Destination";
    }
}

