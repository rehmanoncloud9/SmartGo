package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.Hotel;

import java.util.ArrayList;
import java.util.List;

// HotelService handles everything related to hotels.
// It lets users browse available hotels and their ratings.
// It also lets admins add new hotels to a destination.

public class HotelService {

    // This counter ensures every new hotel gets a unique identification number
    private static int nextHotelId = 1;

    // This method prepares the hotel database when the application launches
    public static void init() {
        List<Hotel> hotels = DataStore.loadHotels();
        if (!hotels.isEmpty()) {
            nextHotelId = hotels.get(hotels.size() - 1).getId() + 1;
        } else {
            // We only add sample hotels if the system is completely empty
            addSampleData();
        }
    }

    // This adds a few well known hotels so the app is ready for testing immediately
    private static void addSampleData() {
        addHotel(1, "Multan Continental Hotel", 4, "+92-61-111-2233", "Nawan Shehar, Multan", 35.00);
        addHotel(2, "Pearl Continental Lahore", 5, "+92-42-111-5050", "Shahrah-e-Quaid-e-Azam, Lahore", 85.00);
        addHotel(3, "Shangrila Resort Skardu", 5, "+92-58-145-0101", "Lower Kachura Lake, Skardu", 70.00);
        System.out.println("Sample hotels loaded.");
    }

    // This displays a full list of every hotel currently in the system
    public static void showAllHotels() {
        List<Hotel> hotels = DataStore.loadHotels();

        if (hotels.isEmpty()) {
            System.out.println("No hotels available right now.");
            return;
        }

        System.out.println("\nAll Available Hotels:");
        System.out.println("======================");
        for (Hotel h : hotels) {
            System.out.println(h);
        }
        System.out.println();
    }

    // This allows users to find a specific hotel by typing any part of its name
    public static List<Hotel> searchByName(String query) {
        List<Hotel> results = new ArrayList<>();
        for (Hotel h : DataStore.loadHotels()) {
            // We search for partial matches so 'pearl' would find 'Pearl Continental'
            if (h.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(h);
            }
        }
        return results;
    }

    // This finds a specific hotel record using its unique ID number
    public static Hotel getHotelById(int id) throws SmartGoException {
        for (Hotel h : DataStore.loadHotels()) {
            if (h.getId() == id) return h;
        }
        throw new SmartGoException("No hotel found with ID " + id + ".");
    }

    // This method allows an administrator to add a new hotel to a specific destination
    public static void addHotel(int destinationId, String name, int rating,
                                String managerContact, String address, double pricePerNight) {
        Hotel hotel = new Hotel(nextHotelId++, destinationId, name, rating, managerContact, address, pricePerNight);
        DataStore.saveHotel(hotel);
        System.out.println("Hotel added: " + name);
    }

    // This simply returns the entire list of hotels for other services to use
    public static List<Hotel> getAllHotels() {
        return DataStore.loadHotels();
    }
}
