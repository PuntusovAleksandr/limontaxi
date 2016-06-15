package digitalpromo.cabsdemo.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import digitalpromo.cabsdemo.models.RouteItem;

/**
 * Created by Администратор on 15.06.2016.
 */
public class GetAddressResponse {
    @SerializedName("geo_street")
    private ArrayList<RouteItem> address;

    public ArrayList<RouteItem> getAddress() {
        return address;
    }
}