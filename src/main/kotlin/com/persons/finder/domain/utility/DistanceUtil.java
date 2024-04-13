package com.persons.finder.domain.utility;

import java.math.BigDecimal;

public class DistanceUtil {

    private static final int EARTH_RADIUS =6371;

    // Based on the Haversine Formula
    public static BigDecimal calculateDistance(final BigDecimal startLat, final BigDecimal startLong, final BigDecimal endLat,
                                         final BigDecimal endLong) {

        final double dLat = Math.toRadians((endLat.doubleValue() - startLat.doubleValue()));
        final double dLong = Math.toRadians((endLong.doubleValue() - startLong.doubleValue()));

        final double startLatDouble = Math.toRadians(startLat.doubleValue());
        final double endLatDouble = Math.toRadians(endLat.doubleValue());

        final double a = haversine(dLat) + Math.cos(startLatDouble) * Math.cos(endLatDouble) * haversine(dLong);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return BigDecimal.valueOf(EARTH_RADIUS * c);
    }

    private static double haversine(final double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

}
