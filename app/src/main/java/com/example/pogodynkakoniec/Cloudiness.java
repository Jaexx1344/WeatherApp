package com.example.pogodynkakoniec;

import java.io.Serializable;

public class Cloudiness implements Serializable {
    private int clouds;
    private String date;
    public Cloudiness (){}
    public int getClouds() {
        return clouds;}
    public void setClouds(int clouds) {
        this.clouds = clouds;}
    public String getDate() {
        return date;}
    public void setDate(String date) {
        this.date = date;}
    public Cloudiness(int clouds, String date) {
        this.clouds = clouds;
        this.date = date;
    }
}
