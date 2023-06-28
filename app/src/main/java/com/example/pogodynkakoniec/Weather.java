package com.example.pogodynkakoniec;
import java.io.Serializable;
public class Weather implements Serializable {
    private String cityAndCountry;
    private String iconNumber;
    private String main;
    private  String description;
    private String pressure;
    private String wind;
    private String humidity;
    private int temperature;
    private int feelsTemperature;
    public Weather() {
    }
    public Weather(String cityAndCountry, String iconNumber, String main, String description, String pressure, String wind, String humidity, int temperature, int feelsTemperature) {
        this.cityAndCountry = cityAndCountry;
        this.iconNumber = iconNumber;
        this.main = main;
        this.description = description;
        this.pressure = pressure;
        this.wind = wind;
        this.humidity = humidity;
        this.temperature = temperature;
        this.feelsTemperature = feelsTemperature;
    }
    public String getCityAndCountry() {
        return cityAndCountry;
    }
    public String getMain() {
        return main;
    }
    public String getDescription() {
        return description;
    }
    public String getPressure() {
        return pressure;
    }
    public String getWind() {
        return wind;
    }
    public String getHumidity() {
        return humidity;
    }
    public int getTemperature() {
        return temperature;
    }
    public int getFeelsTemperature() {
        return feelsTemperature;
    }
}
