package models;

// Interface for everything that can be reviewed by a user.
// This satisfies the "Interfaces" requirement of the project.
public interface Reviewable {
    // This allows us to get the unique ID of any item being reviewed
    int getId();

    // This provides a friendly name for the item to show in the review section
    String getDisplayName();

    // This tells the review service exactly what category of item this is
    String getReviewType();
}
