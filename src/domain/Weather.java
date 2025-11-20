package domain;

/**
 * Weather information.
 */
public class Weather {
    private double temperature;     // in Celsius
    private String condition;       // e.g., "sunny", "rainy", "cloudy"
    private int humidity;           // percentage
    private double windSpeed;       // km/h

    public Weather() {
    }

    public Weather(double temperature, String condition, int humidity, double windSpeed) {
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return temperature + "°C, " + condition;
    }
}
