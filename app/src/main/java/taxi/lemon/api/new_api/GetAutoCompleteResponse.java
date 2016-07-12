package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 14.06.2016.
 */
public class GetAutoCompleteResponse {

    @SerializedName("geo_streets")
    private GeoStreets geoStreets;

    @SerializedName("geo_objects")
    private GeoObjects geoObjects;

    public ArrayList<RouteItem> getAutocomplete() {
        ArrayList<RouteItem> items = new ArrayList<>();
        for(RouteItem item : geoObjects.objects) {
            item.setObject(true);
        }
        for (RouteItem item : geoStreets.streets) {
            item.setObject(false);
        }
        items.addAll(geoStreets.streets);
        items.addAll(geoObjects.objects);
        return items;
    }

    protected class GeoStreets {
        @SerializedName("geo_street")
        ArrayList<RouteItem> streets;
    }

    protected class GeoObjects {
        @SerializedName("geo_object")
        ArrayList<RouteItem> objects;
    }
}
