package digitalpromo.cabsdemo.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Response for geo coding request
 */
public class GeoCodingResponse {
    private static final String STATUS_OK = "OK";

    /**
     * List of results
     */
    private List<GeoCodingResult> results;

    /**
     * Request status
     */
    private String status;

    public String getAddress() {
        if (results != null && results.size() > 0 ) {
            return results.get(0).getAddress();
        }
        return "";
    }

    public LatLng getLatLng() {
        if (results != null && results.size() > 0) {
            return results.get(0).getGeometry().getLocation().getLatLng();
        } else {
            return null;
        }
    }

    public boolean isOK() {
        return status.equals(STATUS_OK);
    }

    /**
     * Hold geo coding result
     */
    private class GeoCodingResult {
        /**
         * List of components
         */
        @SerializedName("address_components")
        private List<Component> components;

        /**
         * Full address string
         */
        @SerializedName("formatted_address")
        private String address;

        private Geometry geometry;

        public String getAddress() {
            if (!components.isEmpty()) {
                if (components.size() > 1) {
                    return components.get(1).getLongName() + ", " + components.get(0).getLongName();
                }
            }

            return address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        private class Geometry {
            private MyLocation location;

            public MyLocation getLocation() {
                return location;
            }

            private class MyLocation {
                private double lat;
                private double lng;

                public LatLng getLatLng() {return new LatLng(lat, lng);}
            }
        }

        /**
         * Hold component of geo coding result
         */
        private class Component {
            /**
             * Long name of a place
             */
            @SerializedName("long_name")
            private String longName;

            /**
             * Short name of a place
             */
            @SerializedName("short_name")
            private String shortName;

            public String getLongName() {
                return longName;
            }

            public String getShortName() {
                return shortName;
            }
        }
    }


}
