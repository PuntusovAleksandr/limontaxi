package taxi.lemon.models;

// TODO: 04.12.2015 add doc comments

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Hold route item info
 */
public class RouteItem {

    private boolean isObject;

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

    @SerializedName("number")
    private String number;

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


    public double getLat() {
        return lat;
    }

    public void setLat(double mLat) {
        lat = mLat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double mLng) {
        lng = mLng;
    }

    public void setStreet(String mStreet) {
        street = mStreet;
    }

    public void setHouses(ArrayList<House> mHouses) {
        houses = mHouses;
    }

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
        if (latLng != null) {
            this.lat = latLng.latitude;
            this.lng = latLng.longitude;
        } else {
            this.lat = 0.0;
            this.lng = 0.0;
        }
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

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
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
        if (latLng != null) {
            this.lat = latLng.latitude;
            this.lng = latLng.longitude;
        } else {
            this.lat = 0.0;
            this.lng = 0.0;
        }
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String mNumber) {
        number = mNumber;
    }


}
