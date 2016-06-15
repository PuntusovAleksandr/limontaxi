package digitalpromo.cabsdemo.models;

// TODO: 04.12.2015 add doc comments

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Hold route item info
 */
public class RouteItem {
    /**
     * Address
     */
    private transient String address;

    /**
     * Address's lat and lng
     */
    private transient LatLng latLng;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    /**
     * Street and adress for single item
     */
    @SerializedName("name")
    private String street;

    /**
     * Houses
     */
    @SerializedName("houses")
    private ArrayList<House> houses;

    public ArrayList<House> getHouses() {
        return houses;
    }

//    public RouteItem() {
//        this.address = "";
//        this.latLng = new LatLng(0, 0);
//    }

    public RouteItem() {
        this.street = "";
        this.latLng = new LatLng(0, 0);
    }

//    public String getAddress() {
//        if (address != null && !address.isEmpty()) {
//            return address;
//        } else {
//            return street + ((house == null) ? "" : ", " + house);
//        }
//    }

    public String getAddress() {
        return address;
    }

    public RouteItem(String address, LatLng latLng) {
        this();
        setAddress(address);
        setLatLng(latLng);
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
        this.street = address;
    }

//    public String getStreet() {
//        if (address != null && !address.isEmpty()) {
//            return address;
//        } else {
//            return street + ", " + house;
//        }
//    }

    public String getStreet() {
        return street;
    }

//    public void setAddress(String address) {
//        this.address = address;
//
//        String[] parts = address.split(", ");
//
//        if (parts.length > 1) {
//            street = parts[0];
//            house = parts[1];
//        } else if (parts.length > 0) {
//            street = parts[0];
//        }
//    }

//    public void setAddress(String address) {
//        this.street = address;
//
//        String[] parts = address.split(", ");
//
//        if (parts.length > 1) {
//            street = parts[0];
//        } else if (parts.length > 0) {
//            street = parts[0];
//        }
//    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        if(latLng == null) return new LatLng(0, 0);
        return latLng;
    }

//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//
//        lat = latLng.latitude;
//        lng = latLng.longitude;
//    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getStringLatLng() {
        return latLng.latitude + "," + latLng.longitude;
    }

    @Override
    public String toString() {
        return "RouteItem{" +
                "address='" + address + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
