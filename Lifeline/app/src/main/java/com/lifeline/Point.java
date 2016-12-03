package com.lifeline;

/**
 * Created by 0 on 2016/11/11.
 */
public class Point {
    private double lon;
    private double lat;
    private long ltime;

    public Point(double lon, double lat, long ltime) {
        this.lon = lon;
        this.lat = lat;
        this.ltime = ltime;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getLtime() {
        return ltime;
    }

    public void setLtime(long ltime) {
        this.ltime = ltime;
    }

    @Override
    public String toString() {
        return "Point{" + "lon=" + lon + ", lat=" + lat + ", ltime=" + ltime + '}';
    }
}
