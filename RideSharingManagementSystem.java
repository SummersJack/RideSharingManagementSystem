import java.util.*;

public class RideSharingManagementSystem {
    private Map<String, Rider> riders = new HashMap<>();
    private Map<String, Driver> drivers = new HashMap<>();
    private PriorityQueue<Driver> availableDrivers;
    private Trie riderTrie = new Trie();
    private Trie driverTrie = new Trie();
    private Graph roadNetwork = new Graph();

    public RideSharingManagementSystem() {
        availableDrivers = new PriorityQueue<>(Comparator.comparingDouble((Driver d) -> d.rating).reversed());
    }

    public void registerRider(String name, String id, Location location) {
        if (riders.containsKey(id)) {
            System.out.println("Error: Rider ID already exists.");
            return;
        }
        Rider newRider = new Rider(name, id, location);
        riders.put(id, newRider);
        riderTrie.insert(name);
        System.out.println("Rider registered: " + name);
    }

    public void registerDriver(String name, String id, Location location, double rating) {
        if (drivers.containsKey(id)) {
            System.out.println("Error: Driver ID already exists.");
            return;
        }
        Driver newDriver = new Driver(name, id, location, rating);
        drivers.put(id, newDriver);
        driverTrie.insert(name);
        availableDrivers.add(newDriver);
        System.out.println("Driver registered: " + name);
    }

    public List<String> searchRiderByName(String prefix) {
        return riderTrie.autocomplete(prefix);
    }

    public List<String> searchDriverByName(String prefix) {
        return driverTrie.autocomplete(prefix);
    }

    public String matchDriver(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            return "Error: Rider not found!";
        }
        if (availableDrivers.isEmpty()) {
            return "Error: No drivers available!";
        }

        Driver bestMatch = availableDrivers.stream()
                .filter(driver -> driver.isAvailable)
                .min(Comparator.comparingDouble(driver -> roadNetwork.calculateShortestDistance(driver.currentLocation, rider.currentLocation)))
                .orElse(null);

        if (bestMatch != null) {
            double distance = roadNetwork.calculateShortestDistance(bestMatch.currentLocation, rider.currentLocation);
            bestMatch.isAvailable = false; // Update driver availability
            availableDrivers.remove(bestMatch); // Remove the matched driver from available drivers
            System.out.printf("Driver %s matched with Rider %s at a distance of %.2f km\n", bestMatch.name, rider.name, distance);
            return "Match successful!";
        }
        return "Error: No matching driver found.";
    }

    public void completeRide(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            System.out.println("Error: Driver not found!");
            return;
        }
        driver.isAvailable = true;
        availableDrivers.add(driver); // Re-add driver to the available pool after ride completion
        System.out.println("Driver " + driver.name + " is now available.");
    }

    public static void main(String[] args) {
        RideSharingManagementSystem system = new RideSharingManagementSystem();

        // Register riders
        system.registerRider("Alice", "R1", new Location(1, 1));
        system.registerRider("Bob", "R2", new Location(2, 2));

        // Register drivers
        system.registerDriver("Charlie", "D1", new Location(1.5, 1.5), 4.5);
        system.registerDriver("David", "D2", new Location(5, 5), 4.0);

        // Search riders and drivers by name
        System.out.println("Searching riders with 'A': " + system.searchRiderByName("A"));
        System.out.println("Searching drivers with 'D': " + system.searchDriverByName("D"));

        // Setup road network and add edges
        setupRoadNetwork(system);

        // Match drivers with riders
        System.out.println(system.matchDriver("R1"));
        System.out.println(system.matchDriver("R2"));

        // Complete a ride and update availability
        system.completeRide("D1");
    }

    private static void setupRoadNetwork(RideSharingManagementSystem system) {
        Location loc1 = new Location(1, 1);
        Location loc2 = new Location(2, 2);
        Location loc3 = new Location(1.5, 1.5);
        Location loc4 = new Location(5, 5); // Additional location for extended network

        system.roadNetwork.addEdge(loc1, loc2);
        system.roadNetwork.addEdge(loc1, loc3);
        system.roadNetwork.addEdge(loc2, loc3);
        system.roadNetwork.addEdge(loc2, loc4); // New edge for more realistic route options
        system.roadNetwork.addEdge(loc3, loc4); // Additional edge to improve route variety
    }
}
