package taxi.lemon.api.old_api;

import taxi.lemon.App;
import taxi.lemon.R;

/**
 * Created by Takeitez on 07.03.2016.
 */
public abstract class GeoCodingRequest {
    private static final String API_KEY = "&key=" + App.getContext().getString(R.string.geocoding_key);
    protected static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?language=ru&%s" + API_KEY;

    public abstract String getUrlRequest();
}
