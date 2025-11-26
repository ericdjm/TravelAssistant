package service;

import domain.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Real implementation of Transit Service.
 * Calculates ETAs based on distance and transport mode.
 *
 * GRASP: Protected Variations - Isolates external API dependency
 *
 * @author CPS731 Team 20
 */
public class TransitService implements ITransitService {

    // Average speeds for different transport modes (meters per minute)
    private static final double WALKING_SPEED_M_PER_MIN = 83.3;    // ~5 km/h
    private static final double DRIVING_SPEED_M_PER_MIN = 666.7;   // ~40 km/h
    private static final double TRANSIT_SPEED_M_PER_MIN = 333.3;   // ~20 km/h

    public TransitService() {
        System.out.println("✓ TransitService initialized");
    }

    /**
     * Get ETAs from origin to multiple destinations.
     * Calculates ETAs based on:
     * - Distance (Haversine formula)
     * - Transport mode (walking/driving/transit speed)
     *
     * @param req Route request with origin, destinations, mode
     * @return List of ETAs (one per destination)
     */
    @Override
    public List<ETA> getTransitETAs(RouteRequest req) {
        if (req == null || req.getOrigin() == null || req.getDestinations() == null) {
            return new ArrayList<>();
        }

        List<ETA> etas = new ArrayList<>();
        LatLng origin = req.getOrigin();
        String mode = req.getMode() != null ? req.getMode() : "walking";

        System.out.println("Calculating ETAs from " + origin);
        System.out.println("  Mode: " + mode);
        System.out.println("  Destinations: " + req.getDestinations().size());

        // Get speed based on mode
        double speedMPerMin = getSpeedForMode(mode);

        for (LatLng destination : req.getDestinations()) {
            // Calculate distance
            double distanceMeters = calculateDistance(origin, destination);

            // Calculate duration based on speed
            int durationMinutes = (int) Math.ceil(distanceMeters / speedMPerMin);

            // Create ETA object
            ETA eta = new ETA(durationMinutes, distanceMeters, mode);
            etas.add(eta);

            System.out.println("  -> " + destination + ": " + durationMinutes + " min (" +
                             String.format("%.0f", distanceMeters) + "m)");
        }

        return etas;
    }

    /**
     * Get speed for transport mode.
     */
    private double getSpeedForMode(String mode) {
        switch (mode.toLowerCase()) {
            case "walking":
                return WALKING_SPEED_M_PER_MIN;
            case "driving":
                return DRIVING_SPEED_M_PER_MIN;
            case "transit":
                return TRANSIT_SPEED_M_PER_MIN;
            default:
                return WALKING_SPEED_M_PER_MIN; // Default to walking
        }
    }

    /**
     * Calculate distance between two coordinates using Haversine formula.
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
}
