package com.example.faizrehman.geofence_example;

/**
 * Created by faizrehman on 1/6/17.
 */

public class Model {
    private double Long;
    private double Lat;
    private String Place;
    private String dateTime;

    public Model() {
    }

    public Model(double aLong, double lat, String place, String dateTime) {
        Long = aLong;
        Lat = lat;
        Place = place;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
