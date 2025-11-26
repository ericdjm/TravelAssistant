package model;

import domain.*;
import java.util.List;

/**
 * «entity» micro itinerary details.
 */
public class Itinerary {

    private List<String> steps;
    private int totalETA;

    public Itinerary fromPOIs(List<POI> pois, List<ETA> etas) {
        // TODO: build itinerary instance from POIs and ETAs
        return null;
    }

    public int computeTotalETA() {
        // TODO: compute total ETA from steps/etas
        return totalETA;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public int getTotalETA() {
        return totalETA;
    }

    public void setTotalETA(int totalETA) {
        this.totalETA = totalETA;
    }
}
