package taxi.lemon.api.new_api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.House;
import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 15.06.2016.
 */
public class GetAddressResponse {
    
    @SerializedName("geo_streets")
    private GeoStreets streets;

    public ArrayList<RouteItem> getAddress() {
        if(streets.address.size() > 0) {
            RouteItem singleStreetRouteItem = streets.address.get(0);
            ArrayList<RouteItem> items = new ArrayList<>();
            for (House house : singleStreetRouteItem.getHouses()) {
                items.add(new RouteItem(singleStreetRouteItem.getStreet() + ", " + house.getHouse(), new LatLng(house.getLat(), house.getLng())));
            }
            return items;
        }
        return null;
    }

    protected class GeoStreets {
        @SerializedName("geo_street")
        protected ArrayList<RouteItem> address;
    }
}