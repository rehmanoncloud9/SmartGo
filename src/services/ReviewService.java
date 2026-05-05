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
// This shows polymorphism - one service works for all three types.

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

        // We make sure the rating is within the logical range of one to five stars
        if (rating < 1 || rating > 5) {
            throw new SmartGoException("Rating must be between 1 and 5.");
        }

        // We also check that the user didn't leave the comment section empty
        if (comment == null || comment.trim().isEmpty()) {
            throw new SmartGoException("Please write a comment for your review.");
        }

        String createdAt = LocalDateTime.now().format(formatter);

        // We create a new Review object and save it immediately to our text file
        Review review = new Review(
                nextReviewId++, userId, reviewableType,
                reviewableId, rating, comment.trim(), createdAt
        );

        DataStore.saveReview(review);

        System.out.println("Thank you! Your review has been saved.");
        System.out.println("Rating: " + rating + "/5 for " + reviewableType + " #" + reviewableId);
    }

    // This displays all feedback left for a specific flight, hotel, or tour plan
    public static void showReviews(String reviewableType, int reviewableId) {
        List<Review> reviews = DataStore.loadReviews();
        boolean found = false;

        System.out.println("\nReviews for " + reviewableType + " #" + reviewableId + ":");
        System.out.println("================================");

        for (Review r : reviews) {
            // We check both the type and the ID to find the exact matches
            if (r.getReviewableType().equalsIgnoreCase(reviewableType)
                    && r.getReviewableId() == reviewableId) {
                System.out.println(r);
                System.out.println();
                found = true;
            }
        }

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
