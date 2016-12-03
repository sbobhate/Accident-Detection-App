package com.lifeline;

public class Distance {

    // 地球的半径
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(Point point1, Point point2) {
        double long1 = point1.getLon();
        double lat1 = point1.getLat();
        double long2 = point2.getLon();
        double lat2 = point2.getLat();

        double a, b, d, sa2, sb2;
        lat1 = rad(lat1);
        lat2 = rad(lat2);
        a = lat1 - lat2;
        b = rad(long1 - long2);

        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * EARTH_RADIUS
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));

        return d;
    }

}