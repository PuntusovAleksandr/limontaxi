package taxi.lemon.api.old_api;

/**
 * Request to get address from latitude and longitude
 */
public class GetAddressRequest extends BaseRequest {
    private static final String SERVICE_NAME = GOOGLE + "getaddress";

    /**
     * Latitude
     */
    private double lat;

    /**
     * Longitude
     */
    private double lng;

    public GetAddressRequest(double lat, double lng) {
        super(SERVICE_NAME);
        this.lat = lat;
        this.lng = lng;
    }
}
