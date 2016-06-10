package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response for directions request
 */
public class DirectionsResponse {
    private static final String STATUS_OK = "OK";

    /**
     * Status of response
     */
    private String status;

    /**
     * List of routes (from one way point to another)
     */
    private List<Route> routes;

    /**
     * Check status
     * @return true of status is equal to OK
     */
    public boolean isOK() {
        return status.equals(STATUS_OK);
    }

    /**
     * Get route distance
     * @return distance
     */
    public long getDistance() {
        long distance = 0;

        for (Route route : routes) {
            for (Leg leg: route.getLegs()) {
                distance += leg.getDistance().getValue();
            }
        }

        return distance;
    }

    /**
     * Get encoded route points
     * @return encoded route
     */
    public String getPoints() {
        if (!routes.isEmpty()) {
            return routes.get(0).getOverviewPolyline().getPoints();
        }

        return "";
    }

    /**
     * Hold route from one way point to another
     */
    private class Route {
        /**
         * List of legs
         */
        private List<Leg> legs;

        /**
         * Approximated encoded route
         */
        @SerializedName("overview_polyline")
        private OverviewPolyline overviewPolyline;

        /**
         * Get list of legs
         * @return list of legs
         */
        public List<Leg> getLegs() {
            return legs;
        }

        /**
         * Get encoded route
         * @return encoded route
         */
        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }
    }

    /**
     * Hold leg of route
     */
    private class Leg {
        /**
         * Distance of leg
         */
        private Distance distance;

        /**
         * Get distance
         * @return distance
         */
        public Distance getDistance() {
            return distance;
        }

        /**
         * Hold distance info
         */
        private class Distance {
            /**
             * String with distance value (f.e. 14,6 km)
             */
            private String text;

            /**
             * Distance in meters
             */
            private long value;

            /**
             * Get string with distance
             * @return distance
             */
            public String getText() {
                return text;
            }

            /**
             * Get distance in meters
             * @return distance i nmeters
             */
            public long getValue() {
                return value;
            }
        }
    }

    /**
     * Encoded route
     */
    private class OverviewPolyline {
        /**
         * String with encoded route
         */
        private String points;

        /**
         * Get encoded points
         * @return encoded points
         */
        public String getPoints() {
            return points;
        }
    }
}
