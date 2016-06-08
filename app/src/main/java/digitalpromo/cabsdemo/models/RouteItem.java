package digitalpromo.cabsdemo.models;

// TODO: 04.12.2015 add doc comments

import com.google.android.gms.maps.model.LatLng;

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

    /**
     * Street
     */
    private String street;

    /**
     * House
     */
    private String house;

    /**
     * Latitude
     */
    private double lat;

    /**
     * Longitude
     */
    private double lng;


    public RouteItem() {
        this.address = "";
        this.latLng = new LatLng(0, 0);
    }

    public String getAddressLine() {
        if (address != null && !address.isEmpty()) {
            return address;
        } else {
            return street + ((house == null) ? "" : ", " + house);
        }
    }

    public RouteItem(String address, LatLng latLng) {
        this();
        setAddress(address);
        setLatLng(latLng);
    }

    public String getAddress() {
        if (address != null && !address.isEmpty()) {
            return address;
        } else {
            return street + ", " + house;
        }
    }

    public void setAddress(String address) {
        this.address = address;

        String[] parts = address.split(", ");

        if (parts.length > 1) {
            street = parts[0];
            house = parts[1];
        } else if (parts.length > 0) {
            street = parts[0];
        }
    }

    public LatLng getLatLng() {
        if (latLng.latitude == 0 || latLng.longitude == 0) {
            return new LatLng(lat, lng);
        }
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;

        lat = latLng.latitude;
        lng = latLng.longitude;
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
