package service;

import domain.*;

/**
 * Real implementation of Weather Service.
 * Returns pleasant weather for all locations (for demo).
 *
 * GRASP: Protected Variations - Isolates external API dependency
 *
 * @author CPS731 Team 20
 */
public class WeatherService implements IWeatherService {

    public WeatherService() {
        System.out.println("✓ WeatherService initialized");
    }

    /**
     * Get weather for a location.
     * For demo purposes, always returns pleasant weather.
     *
     * @param loc Location to get weather for
     * @return Weather object with pleasant conditions
     */
    @Override
    public Weather getWeather(LatLng loc) {
        System.out.println("Getting weather for " + loc);

        // Create pleasant weather object
        Weather weather = new Weather();
        weather.setTemperature(22.0);        // 22°C (nice spring/fall day)
        weather.setCondition("sunny");       // Sunny
        weather.setHumidity(60);             // 60% humidity
        weather.setWindSpeed(10.0);          // 10 km/h light breeze

        System.out.println("  Weather: " + weather);

        return weather;
    }
}
