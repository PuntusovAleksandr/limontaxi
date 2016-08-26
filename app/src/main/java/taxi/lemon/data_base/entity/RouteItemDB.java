package taxi.lemon.data_base.entity;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by AleksandrP on 25.08.2016.
 */
public class RouteItemDB extends RealmObject {


    public boolean isObject;

    public String address;
    public double lat;
    public double lng;
    public String street;
    public String number;
    public RealmList<HouseDB> houses;

    public String orderId;

    public RouteItemDB() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String mOrderId) {
        orderId = mOrderId;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean mObject) {
        isObject = mObject;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String mAddress) {
        address = mAddress;
    }

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String mStreet) {
        street = mStreet;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String mNumber) {
        number = mNumber;
    }

    public RealmList<HouseDB> getHouses() {
        return houses;
    }

    public void setHouses(RealmList<HouseDB> mHouses) {
        houses = mHouses;
    }
}
