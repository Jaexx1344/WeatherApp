package com.example.pogodynkakoniec;

import java.io.Serializable;
import java.util.ArrayList;

public class Forecast implements Serializable {
    private String cityAndCountry;

    public String getCityAndCountry() {
        return cityAndCountry;
    }
    public ArrayList<Cloudiness> getClouds() {
        return clouds;
    }
    public ArrayList<Temperature> getTemperatures() {
        return temperatures;
    }

    public Forecast(String cityAndCountry, ArrayList<Cloudiness> clouds, ArrayList<Temperature> temperatures) {
        this.cityAndCountry = cityAndCountry;
        this.clouds = clouds;
        this.temperatures = temperatures;
    }
    private ArrayList<Cloudiness> clouds;
    public Forecast() {}
    private ArrayList<Temperature> temperatures;
}
