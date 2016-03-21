package com.naroh.nfctimecontrol.models;

import org.joda.time.DateTime;

public class Check {
    private String placeName;
    private DateTime checkIn;
    private DateTime checkOut;
    private int hours;

    public Check(String placeName, DateTime checkIn, DateTime checkOut, int hours) {
        this.placeName = placeName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hours = hours;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public DateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(DateTime checkIn) {
        this.checkIn = checkIn;
    }

    public DateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(DateTime checkOut) {
        this.checkOut = checkOut;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
