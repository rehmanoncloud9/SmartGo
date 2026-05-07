package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.MealPlan;
import models.TourPlan;

import java.util.ArrayList;
import java.util.List;

// TourPlanService handles everything related to tour plans.
// It lets users browse available tours and their meal options.
// It also lets admins add new tour plans and meal plans.

public class TourPlanService {

    // These variables help us assign unique identification numbers to new plans and meals
    private static int nextPlanId = 1;
    private static int nextMealId = 1;

    // We prepare the tour system by loading existing files and setting the next available IDs
    public static void init() {
        List<TourPlan> existing = DataStore.loadTourPlans();
        if (!existing.isEmpty()) {
            nextPlanId = existing.get(existing.size() - 1).getId() + 1;
        }

        List<MealPlan> meals = DataStore.loadMealPlans();
        if (!meals.isEmpty()) {
            nextMealId = meals.get(meals.size() - 1).getId() + 1;
        }

        // If the tour database is empty we add some pre defined packages
        if (existing.isEmpty()) {
            addSampleData();
        }
    }

    // This creates a few exciting tour packages to give users options on their first visit
    private static void addSampleData() {
        // Step 1: Define the primary tour packages for different destinations
        TourPlan t1 = new TourPlan(nextPlanId++, 1, 1, "Multan Heritage Tour", 4, 180.00, "ACTIVE");
        TourPlan t2 = new TourPlan(nextPlanId++, 2, 1, "Lahore Cultural Experience", 5, 220.00, "ACTIVE");
        TourPlan t3 = new TourPlan(nextPlanId++, 3, 1, "Skardu Mountain Adventure", 7, 350.00, "ACTIVE");

        // Step 2: Save the new plans to our permanent text database
        DataStore.saveTourPlan(t1);
        DataStore.saveTourPlan(t2);
        DataStore.saveTourPlan(t3);

        // Step 3: This is critical logic because we attach different dining options to each tour package using the tour ID
        // This creates a parent child relationship between the tour and its food options
        MealPlan m1 = new MealPlan(nextMealId++, t1.getId(), "Full Board", "All meals included", 40.00);
        MealPlan m2 = new MealPlan(nextMealId++, t2.getId(), "Breakfast and Dinner", "Breakfast and dinner included", 35.00);
        MealPlan m3 = new MealPlan(nextMealId++, t3.getId(), "Standard Meals", "Breakfast and dinner included", 50.00);
        MealPlan m4 = new MealPlan(nextMealId++, t3.getId(), "Premium Meals", "All meals plus room service", 90.00);

        // Step 4: Finalize by saving the individual meal choices to their own file
        DataStore.saveMealPlan(m1);
        DataStore.saveMealPlan(m2);
        DataStore.saveMealPlan(m3);
        DataStore.saveMealPlan(m4);

        System.out.println("Sample tour plans and meal plans loaded.");
    }

    // This displays every active tour package that a user can book
    public static void showAllTourPlans() {
        // Step 1: Fetch the full list of packages from our text file
        List<TourPlan> plans = DataStore.loadTourPlans();

        if (plans.isEmpty()) {
            System.out.println("No tour plans available right now.");
            return;
        }

        System.out.println("\nAll Available Tour Plans:");
        System.out.println("==========================");
        // Step 2: Loop through each plan and check if it is still available for booking
        for (TourPlan p : plans) {
            // We only show plans that are currently marked as active to the user
            if (p.getStatus().equals("ACTIVE")) {
                System.out.println(p);
            }
        }
        System.out.println();
    }

    // This prints out all the dining options available for a specific tour
    public static void showMealPlansForTour(int tourPlanId) {
        // Step 1: Load every meal plan from our database to start the filtering process
        List<MealPlan> meals = DataStore.loadMealPlans();
        boolean found = false;

        System.out.println("\nMeal Plans for Tour Plan #" + tourPlanId + ":");
        // Step 2: This critical filter ensures we only show food that belongs to the selected vacation package
        for (MealPlan m : meals) {
            // This cross reference ensures the user doesn't see meal options for the wrong city
            if (m.getTourPlanId() == tourPlanId) {
                System.out.println(m);
                found = true;
            }
        }

        // Step 3: If no specific meals were found for this ID we tell the user clearly
        if (!found) {
            System.out.println("No meal plans available for this tour.");
        }
        System.out.println();
    }

    // This finds a specific tour package using its ID number
    public static TourPlan getTourPlanById(int id) throws SmartGoException {
        for (TourPlan p : DataStore.loadTourPlans()) {
            if (p.getId() == id) return p;
        }
        throw new SmartGoException("No tour plan found with ID " + id + ". Please check and try again.");
    }

    // This finds a specific dining option using its ID number
    public static MealPlan getMealPlanById(int id) throws SmartGoException {
        for (MealPlan m : DataStore.loadMealPlans()) {
            if (m.getId() == id) return m;
        }
        throw new SmartGoException("No meal plan found with ID " + id + ". Please check and try again.");
    }

    // This gives us a list of all meal plans that belong to a certain tour
    public static List<MealPlan> getMealPlansForTour(int tourPlanId) {
        List<MealPlan> result = new ArrayList<>();
        for (MealPlan m : DataStore.loadMealPlans()) {
            if (m.getTourPlanId() == tourPlanId) result.add(m);
        }
        return result;
    }

    // This method lets an administrator add a new tour package to a destination
    public static void addTourPlan(int destinationId, int adminId, String title,
                                    int durationDays, double basePrice) {
        TourPlan plan = new TourPlan(nextPlanId++, destinationId, adminId, title, durationDays, basePrice, "ACTIVE");
        DataStore.saveTourPlan(plan);
        System.out.println("Tour plan added: " + title);
    }

    // This allows an administrator to add new dining options to an existing tour
    public static void addMealPlan(int tourPlanId, String name, String description, double price) {
        MealPlan meal = new MealPlan(nextMealId++, tourPlanId, name, description, price);
        DataStore.saveMealPlan(meal);
        System.out.println("Meal plan added: " + name);
    }

    // This returns the complete list of all tour plans for other services to use
    public static List<TourPlan> getAllTourPlans() {
        return DataStore.loadTourPlans();
    }
}

