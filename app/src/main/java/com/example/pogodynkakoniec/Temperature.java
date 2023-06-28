package com.example.pogodynkakoniec;

import java.io.Serializable;

public class Temperature implements Serializable {
    private double temperature;
    private String date;
    public double getTemperature() {
        return temperature;
    }
    public String getDate() {
        return date;
    }
    public Temperature (){}
    public Temperature(double temperature, String date) {
        this.temperature = temperature;
        this.date = date;
    }
}
