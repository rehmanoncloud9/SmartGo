package models;

// Tour Plan: A pre-bundled travel package made by an Admin
//
// Example: "3 Days in Lahore" package includes:
//   - Transport (flight from Karachi)
//   - Hotel (Pearl Continental)
//   - Meal Plan (Full Board)
//   - All managed by one Admin

public class TourPlan {

    private int    id;
    private int    destinationId; // where this tour goes
    private int    adminId;       // who created this package
    private String title;         // e.g. "Weekend in Murree"
    private int    durationDays;  // how many days the tour lasts
    private String status;        // "ACTIVE" or "INACTIVE"
    private double basePlanPrice; // base price before adding meals etc.

    public TourPlan(int id, int destinationId, int adminId,
                    String title, int durationDays,
                    String status, double basePlanPrice) {
        this.id            = id;
        this.destinationId = destinationId;
        this.adminId       = adminId;
        this.title         = title;
        this.durationDays  = durationDays;
        this.status        = status;
        this.basePlanPrice = basePlanPrice;
    }

    public void displayInfo() {
        System.out.println("  🗺  " + title);
        System.out.println("  Duration   : " + durationDays + " days");
        System.out.println("  Base Price : Rs. " + basePlanPrice);
        System.out.println("  Status     : " + status);
    }

    // Getters
    public int    getId()            { return id;            }
    public int    getDestinationId() { return destinationId; }
    public int    getAdminId()       { return adminId;       }
    public String getTitle()         { return title;         }
    public int    getDurationDays()  { return durationDays;  }
    public String getStatus()        { return status;        }
    public double getBasePlanPrice() { return basePlanPrice; }

    // Setters
    public void setStatus(String status)           { this.status        = status;        }
    public void setBasePlanPrice(double price)     { this.basePlanPrice = price;         }

    public String toFileString() {
        return id + "," + destinationId + "," + adminId + "," + title + ","
             + durationDays + "," + status + "," + basePlanPrice;
    }
}
