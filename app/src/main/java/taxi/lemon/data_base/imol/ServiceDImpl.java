package taxi.lemon.data_base.imol;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import taxi.lemon.data_base.entity.HistoryItemDB;
import taxi.lemon.data_base.entity.HouseDB;
import taxi.lemon.data_base.entity.RouteItemDB;
import taxi.lemon.models.HistoryItem;
import taxi.lemon.models.House;
import taxi.lemon.models.RouteItem;

/**
 * Created by AleksandrP on 25.08.2016.
 */
public class ServiceDImpl {


    private static final long VERSION_DB = 1;

    private Context mContext;
    private Realm mRealm;
    private static ServiceDImpl sServiceD;

    public static ServiceDImpl getServise(Context mContext) {
        if (sServiceD == null) {
            sServiceD = new ServiceDImpl(mContext);
        }
        return sServiceD;
    }

    public ServiceDImpl(Context mContext) {
        this.mContext = mContext;
    }

    private Realm openRealm(Context mContext) {
        if (mRealm == null) {
            mRealm = createRealm(mContext);
        }
        return mRealm;
    }

    public void StopRealm() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public boolean addFromServer(ArrayList<HistoryItem> historyItems) {
        openRealm(mContext);

        for (final HistoryItem history : historyItems) {
            String orderId = history.getOrderId();
            HistoryItemDB orderIdExist = mRealm.where(HistoryItemDB.class).equalTo("orderId", orderId).findFirst();
            if (orderIdExist == null) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        HistoryItemDB item = realm.createObject(HistoryItemDB.class);
                        item.orderId = (history.getOrderId());
                        item.requiredTime = (history.getDate());
                        item.orderCost = (history.getCost());
                        item.userFullName = (history.getUserFullName());
                        item.userPhone = (history.getUserPhone());
                        item.closeReason = (history.getCloseReason());
                        item.executionStatus = (history.getExecutionStatus());

                        for (RouteItem routeItem : history.getRoute()) {
                            RouteItemDB itemDB = realm.createObject(RouteItemDB.class);
                            itemDB.isObject = routeItem.isObject();
                            itemDB.address = routeItem.getAddress();
                            itemDB.lat = routeItem.getLatLng().latitude;
                            itemDB.lng = routeItem.getLatLng().longitude;
                            itemDB.street = routeItem.getStreet();
                            itemDB.number = routeItem.getNumber();
                            itemDB.orderId = history.getOrderId();

                            ArrayList<House> houses = routeItem.getHouses();
                            if (houses != null) {
                                for (House house : houses) {
                                    HouseDB houseDB = realm.createObject(HouseDB.class);
                                    houseDB.house = house.getHouse();
                                    houseDB.lat = house.getLat();
                                    houseDB.lng = house.getLng();

                                    itemDB.houses.add(houseDB);
                                }
                            }

                            item.route.add(itemDB);
                        }
                    }
                });
            }
        }
        return true;
    }


    public ArrayList<HistoryItem> getHistoryItems() {
        ArrayList<HistoryItem> historyItems = new ArrayList<>();
        openRealm(mContext);
        RealmResults<HistoryItemDB> historyItemDBs =
                mRealm.where(HistoryItemDB.class).equalTo("showHistory", false).findAll();

        for (HistoryItemDB itemDB : historyItemDBs) {
            HistoryItem item = new HistoryItem();
            item.setOrderId(itemDB.getOrderId());
            item.setRequiredTime(itemDB.getRequiredTime());
            item.setOrderCost(itemDB.getOrderCost());
            item.setUserFullName(itemDB.getUserFullName());
            item.setUserPhone(itemDB.getUserPhone());
            item.setCloseReason(itemDB.getCloseReason());
            item.setExecutionStatus(itemDB.getExecutionStatus());

            ArrayList<RouteItem> itemArrayList = new ArrayList<>();
            for (RouteItemDB routeItemDB : itemDB.getRoute()) {
                RouteItem itemRoute = new RouteItem();
                itemRoute.setObject(routeItemDB.isObject());
                itemRoute.setAddress(routeItemDB.getAddress());
                itemRoute.setLat(routeItemDB.getLat());
                itemRoute.setLng(routeItemDB.getLng());
                itemRoute.setStreet(routeItemDB.getStreet());
                itemRoute.setNumber(routeItemDB.getNumber());

                ArrayList<House> houseArrayList = new ArrayList<>();
                RealmList<HouseDB> houses = routeItemDB.getHouses();
                if (houses != null) {
                    for (HouseDB houseDB : houses) {
                        House house = new House();
                        house.setHouse(houseDB.getHouse());
                        house.setLat(houseDB.getLat());
                        house.setLng(houseDB.getLng());

                        houseArrayList.add(house);
                    }
                }
                itemRoute.setHouses(houseArrayList);

                itemArrayList.add(itemRoute);
            }

            item.setRoute(itemArrayList);
            historyItems.add(item);
        }

        return historyItems;
    }


    public void deleteHistoryItem(HistoryItem mItem) {
        openRealm(mContext);
        mRealm.beginTransaction();
        HistoryItemDB orderId =
                mRealm.where(HistoryItemDB.class).equalTo("orderId", mItem.getOrderId()).findFirst();
        orderId.setShowHistory(true);
        mRealm.copyToRealmOrUpdate(orderId);
        mRealm.commitTransaction();
    }

    private static Realm createRealm(Context mContext) {
        // The RealmConfiguration is created using the builder pattern.
        // The Realm file will be located in Context.getFilesDir() with name "myrealm.realm"
        RealmConfiguration config = new RealmConfiguration.Builder(mContext)
                .name("realm")
                .schemaVersion(VERSION_DB)
                .build();
        // Use the config
        return Realm.getInstance(config);
    }


}
