package digitalpromo.cabsdemo.api.old_api;

import com.google.android.gms.maps.model.LatLng;

/**
 * Request for reverse geo coding latlang -> address
 */
public class ReverseGeoCodingRequest extends GeoCodingRequest {
    private static final String PARAM_LAT_LNG = "latlng=%s";

    /**
     * Coordinates of way point
     */
    private LatLng latLng;

    public ReverseGeoCodingRequest(LatLng latLng) {
        this.latLng = latLng;
    }

    private String getLatLngParam() {
        return latLng.latitude + "," + latLng.longitude;
    }

    public String getUrlRequest() {
        return String.format(BASE_URL, String.format(PARAM_LAT_LNG, getLatLngParam()));
    }
}
