package models;

// Hotel: An accommodation option at a Destination

public class Hotel {

    private int    id;
    private int    destinationId;
    private String name;
    private int    starRating;      // 1 to 5
    private String managerContact;
    private int    availableRooms;
    private double pricePerNight;
    private String imageUrl;        // optional, for future UI use

    public Hotel(int id, int destinationId, String name, int starRating,
                 String managerContact, int availableRooms,
                 double pricePerNight, String imageUrl) {
        this.id              = id;
        this.destinationId   = destinationId;
        this.name            = name;
        this.starRating      = starRating;
        this.managerContact  = managerContact;
        this.availableRooms  = availableRooms;
        this.pricePerNight   = pricePerNight;
        this.imageUrl        = imageUrl;
    }

    // Display hotel info in a nice format
    public void displayInfo() {
        System.out.println("  🏨 " + name);
        System.out.println("  Stars          : " + "⭐".repeat(starRating));
        System.out.println("  Price/Night    : Rs. " + pricePerNight);
        System.out.println("  Rooms Available: " + availableRooms);
        System.out.println("  Contact        : " + managerContact);
    }

    // Calculate total cost for a stay
    public double calculateStayCost(int nights) {
        return pricePerNight * nights;
    }

    // Getters
    public int    getId()              { return id;              }
    public int    getDestinationId()   { return destinationId;   }
    public String getName()            { return name;            }
    public int    getStarRating()      { return starRating;      }
    public String getManagerContact()  { return managerContact;  }
    public int    getAvailableRooms()  { return availableRooms;  }
    public double getPricePerNight()   { return pricePerNight;   }
    public String getImageUrl()        { return imageUrl;        }

    // Setters
    public void setAvailableRooms(int rooms)     { this.availableRooms = rooms;    }
    public void setPricePerNight(double price)   { this.pricePerNight  = price;    }

    public String toFileString() {
        return id + "," + destinationId + "," + name + "," + starRating + ","
             + managerContact + "," + availableRooms + "," + pricePerNight + "," + imageUrl;
    }
}
