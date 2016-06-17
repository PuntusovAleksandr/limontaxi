package taxi.lemon.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 14.06.2016.
 */
public class House {
    @SerializedName("house")
    private String house;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getHouse() {
        return house;
    }
}
