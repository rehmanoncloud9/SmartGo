package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// ReviewService handles everything related to reviews.
// Users can leave a review for a flight, hotel, or tour plan.
// Anyone can read the reviews for a specific item.
// This shows polymorphism because one service works for all three types.

public class ReviewService {

    // This counter ensures that every review in the system has its own unique ID
    private static int nextReviewId = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // We initialize the service by checking the last saved review to set the correct starting ID
    public static void init() {
        List<Review> existing = DataStore.loadReviews();
        if (!existing.isEmpty()) {
            nextReviewId = existing.get(existing.size() - 1).getId() + 1;
        }
    }

    // This method allows users to share their feedback on any travel service they have used
    public static void addReview(int userId, String reviewableType, int reviewableId,
                                  int rating, String comment) throws SmartGoException {

        // Step 1: We make sure the rating is within the logical range of one to five stars
        if (rating < 1 || rating > 5) {
            throw new SmartGoException("Rating must be between 1 and 5.");
        }

        // Step 2: We also check that the user didn't leave the comment section empty
        if (comment == null || comment.trim().isEmpty()) {
            throw new SmartGoException("Please write a comment for your review.");
        }

        String createdAt = LocalDateTime.now().format(formatter);

        // Step 3: This is critical logic because we use a generic 'reviewableType' string to identify if this is for a flight or a hotel
        // This allows one single service to handle reviews for completely different parts of the system
        Review review = new Review(
                nextReviewId++, userId, reviewableType,
                reviewableId, rating, comment.trim(), createdAt
        );

        // Step 4: Record the user's feedback permanently into our text database
        DataStore.saveReview(review);

        System.out.println("Thank you! Your review has been saved.");
        System.out.println("Rating: " + rating + "/5 for " + reviewableType + " #" + reviewableId);
    }

    // This displays all feedback left for a specific flight, hotel, or tour plan
    public static void showReviews(String reviewableType, int reviewableId) {
        // Step 1: Gather every single review ever written from our storage files
        List<Review> reviews = DataStore.loadReviews();
        boolean found = false;

        System.out.println("\nReviews for " + reviewableType + " #" + reviewableId + ":");
        System.out.println("================================");

        // Step 2: We filter the giant list to only show reviews that match the specific type and ID we want
        for (Review r : reviews) {
            // This cross-reference is what lets us display only hotel reviews when you're looking at a hotel
            if (r.getReviewableType().equalsIgnoreCase(reviewableType)
                    && r.getReviewableId() == reviewableId) {
                System.out.println(r);
                System.out.println();
                found = true;
            }
        }

        // Step 3: If we didn't find any matching reviews we let the user know instead of showing an empty screen
        if (!found) {
            System.out.println("No reviews yet for this item.");
        }
    }

    // This lets a logged in user see every single review they have ever written
    public static void showMyReviews(int userId) {
        List<Review> reviews = DataStore.loadReviews();
        boolean found = false;

        System.out.println("\nYour Reviews:");
        System.out.println("==============");

        for (Review r : reviews) {
            if (r.getUserId() == userId) {
                System.out.println(r);
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("You have not written any reviews yet.");
        }
    }
}
