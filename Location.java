// src/Location.java
public class Location {
    double x, y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Calculates the Euclidean distance to another location
    public double calculateDistance(Location other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
