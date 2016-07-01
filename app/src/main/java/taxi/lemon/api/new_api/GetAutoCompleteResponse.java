package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 14.06.2016.
 */
public class GetAutoCompleteResponse {
    @SerializedName("geo_streets")
    private ArrayList<GeoStreet> geoStreets;

    @SerializedName("geo_objects")
    private ArrayList<GeoObject> geoObjects;

    private ArrayList<RouteItem> items = new ArrayList<>();

    public ArrayList<RouteItem> getAutocomplete() {
        return null;
    }

    protected class GeoStreet {
        @SerializedName("geo_street")
        protected ArrayList<RouteItem> autocomplete1;
    }

    protected class GeoObject {
        @SerializedName("geo_object")
        protected ArrayList<RouteItem> autocomplete2;
    }
}
