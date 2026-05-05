package models;

// Interface for everything that can be reviewed by a user.
// This satisfies the "Interfaces" requirement of the project.
public interface Reviewable {
    int getId();
    String getDisplayName();
    String getReviewType();
}
