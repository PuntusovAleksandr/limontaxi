package taxi.lemon.data_base.entity;

import io.realm.RealmObject;

/**
 * Created by AleksandrP on 25.08.2016.
 */
public class HouseDB extends RealmObject{

    public  String house;
    public  double lat;
    public  double lng;

    private String orderId;

    public HouseDB() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String mOrderId) {
        orderId = mOrderId;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String mHouse) {
        house = mHouse;
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
}
