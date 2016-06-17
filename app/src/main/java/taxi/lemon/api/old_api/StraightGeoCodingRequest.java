package taxi.lemon.api.old_api;

/**
 * Created by Takeitez on 07.03.2016.
 */
public class StraightGeoCodingRequest extends GeoCodingRequest {
//    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?language=ru&address=%s&components=country:UA&%s";
//    private static final String API_KEY = "key=" + App.getContext().getString(R.string.geocoding_key);
    private static final String PARAM_ADDRESS = "address=%s";
    private static final String PARAM_COMPONENTS = "components=country:UA";

    /**
     * Coordinates of way point
     */
    private String address;

    public StraightGeoCodingRequest(String address) {
        this.address = address;
    }

    public String getUrlRequest() {
        return String.format(BASE_URL, String.format(PARAM_ADDRESS, address) + "&" + PARAM_COMPONENTS);
    }
}
