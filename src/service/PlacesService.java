package service;

import domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Real implementation of Places Service using MySQL database.
 * Loads POI data from database instead of calling external API.
 *
 * GRASP: Protected Variations - Isolates external API dependency
 *
 * @author CPS731 Team 20
 */
public class PlacesService implements IPlacesService {

    private List<POI> allPOIs;
    private DatabaseConnection dbConnection;

    public PlacesService() {
        this.dbConnection = DatabaseConnection.getInstance();

        // Load POIs from database
        this.allPOIs = loadPOIsFromDatabase();
        System.out.println("✓ PlacesService initialized with " + allPOIs.size() + " POIs from database");
    }

    /**
     * Load all POIs from the database.
     *
     * @return List of POIs from database
     */
    private List<POI> loadPOIsFromDatabase() {
        List<POI> pois = new ArrayList<>();

        String sql = "SELECT poi_id, name, category, latitude, longitude, rating, " +
                     "price_level, tags, open_now, address FROM pois";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                POI poi = new POI();
                poi.setId(rs.getString("poi_id"));
                poi.setName(rs.getString("name"));
                poi.setCategory(rs.getString("category"));

                // Create LatLng
                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                poi.setLocation(new LatLng(lat, lng));

                poi.setRating(rs.getFloat("rating"));
                poi.setPriceLevel(rs.getString("price_level"));
                poi.setOpenNow(rs.getBoolean("open_now"));
                poi.setAddress(rs.getString("address"));

                // Parse tags JSON array
                String tagsJson = rs.getString("tags");
                if (tagsJson != null) {
                    List<String> tags = parseJsonArray(tagsJson);
                    poi.setTags(tags);
                }

                pois.add(poi);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading POIs from database: " + e.getMessage());
            e.printStackTrace();
        }

        return pois;
    }

    /**
     * Parse JSON array string to List<String>.
     * Simple parser for format: ["tag1", "tag2", "tag3"]
     */
    private List<String> parseJsonArray(String json) {
        List<String> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return list;
        }

        // Remove brackets and quotes, split by comma
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (!cleaned.isEmpty()) {
            String[] items = cleaned.split(",\\s*");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    list.add(item.trim());
                }
            }
        }

        return list;
    }

    /**
     * Search for places near coordinates matching user preferences.
     * Filters by:
     * - Interests (category matching)
     * - Radius (distance from user location)
     *
     * @param coords User location
     * @param prefs User preferences (interests, radius)
     * @return Filtered list of POIs
     */
    @Override
    public List<POI> searchPlaces(LatLng coords, Preferences prefs) {
        if (coords == null || prefs == null) {
            return new ArrayList<>();
        }

        List<POI> results = new ArrayList<>();

        // Get user interests (e.g., ["restaurants", "museums"])
        List<String> interests = prefs.getInterests();
        int radiusMeters = prefs.getRadius();
        String budget = prefs.getBudget(); // "low", "medium", "high"

        System.out.println("Searching for POIs near " + coords);
        System.out.println("  Interests: " + interests);
        System.out.println("  Budget: " + budget);
        System.out.println("  Radius: " + radiusMeters + "m");

        for (POI poi : allPOIs) {
            // Filter 1: Check if POI category matches any user interest
            boolean matchesInterest = false;
            if (interests != null && !interests.isEmpty()) {
                for (String interest : interests) {
                    // Normalize: "restaurants" -> "restaurant", "museums" -> "museum"
                    String normalized = interest.toLowerCase().replaceAll("s$", "");
                    String poiCategory = poi.getCategory().toLowerCase();

                    if (poiCategory.contains(normalized) || normalized.contains(poiCategory)) {
                        matchesInterest = true;
                        break;
                    }

                    // Also check tags
                    if (poi.getTags() != null) {
                        for (String tag : poi.getTags()) {
                            if (tag.toLowerCase().contains(normalized) ||
                                normalized.contains(tag.toLowerCase())) {
                                matchesInterest = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                // No interests specified - include all
                matchesInterest = true;
            }

            if (!matchesInterest) {
                continue; // Skip this POI
            }

            // Filter 2: Check if POI is within radius
            double distance = calculateDistance(coords, poi.getLocation());
            if (distance > radiusMeters) {
                continue; // Too far away
            }

            // Filter 3: Check budget match (if specified)
            if (budget != null && !budget.isEmpty() && poi.getPriceLevel() != null) {
                boolean matchesBudget = false;

                switch (budget.toLowerCase()) {
                    case "low":
                        // Low budget: $ only
                        matchesBudget = poi.getPriceLevel().equals("$");
                        break;
                    case "medium":
                        // Medium budget: $ or $$
                        matchesBudget = poi.getPriceLevel().equals("$") ||
                                       poi.getPriceLevel().equals("$$");
                        break;
                    case "high":
                        // High budget: any price level ($, $$, $$$, $$$$)
                        matchesBudget = true;
                        break;
                    default:
                        matchesBudget = true; // Unknown budget, don't filter
                }

                if (!matchesBudget) {
                    continue; // Doesn't match budget
                }
            }

            // POI matches all filters - add to results
            results.add(poi);
        }

        System.out.println("  Found " + results.size() + " matching POIs");
        return results;
    }

    /**
     * Geocode an address to coordinates.
     * For demo purposes, always returns Toronto downtown coordinates.
     *
     * @param address Address string (ignored in demo)
     * @return Toronto downtown coordinates
     */
    @Override
    public LatLng geocode(String address) {
        System.out.println("Geocoding address: " + address);
        // Toronto downtown
        LatLng coords = new LatLng(43.6532, -79.3832);
        System.out.println("  Returning Toronto downtown: " + coords);
        return coords;
    }

    /**
     * Calculate distance between two coordinates using Haversine formula.
     * Returns distance in meters.
     */
    private double calculateDistance(LatLng coord1, LatLng coord2) {
        final int EARTH_RADIUS_KM = 6371;

        double lat1 = Math.toRadians(coord1.getLatitude());
        double lat2 = Math.toRadians(coord2.getLatitude());
        double lon1 = Math.toRadians(coord1.getLongitude());
        double lon2 = Math.toRadians(coord2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceKm = EARTH_RADIUS_KM * c;
        return distanceKm * 1000; // Convert to meters
    }

    /**
     * Reload POIs from database.
     * Call this to refresh data if database is updated.
     */
    public void refreshPOIs() {
        this.allPOIs = loadPOIsFromDatabase();
        System.out.println("✓ POIs refreshed from database: " + allPOIs.size() + " total");
    }
}
