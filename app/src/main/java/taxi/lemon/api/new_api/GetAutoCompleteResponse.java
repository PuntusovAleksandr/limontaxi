package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 14.06.2016.
 */
public class GetAutoCompleteResponse {
    @SerializedName("geo_street")
    private ArrayList<RouteItem> autocomplete;

    public ArrayList<RouteItem> getAutocomplete() {
        return autocomplete;
    }
}
