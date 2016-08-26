package taxi.lemon.data_base.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AleksandrP on 25.08.2016.
 */
public class HistoryItemDB  extends RealmObject {

    @PrimaryKey
    public  String orderId;
    public  String requiredTime;
    public  String orderCost;
    public  String userFullName;
    public  String userPhone;
    public RealmList <RouteItemDB> route;
    public  int closeReason;
    public  String executionStatus;

    public  boolean showHistory;


    public HistoryItemDB() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String mOrderId) {
        orderId = mOrderId;
    }

    public String getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(String mRequiredTime) {
        requiredTime = mRequiredTime;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String mOrderCost) {
        orderCost = mOrderCost;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String mUserFullName) {
        userFullName = mUserFullName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String mUserPhone) {
        userPhone = mUserPhone;
    }

    public RealmList<RouteItemDB> getRoute() {
        return route;
    }

    public void setRoute(RealmList<RouteItemDB> mRoute) {
        route = mRoute;
    }

    public int getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(int mCloseReason) {
        closeReason = mCloseReason;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String mExecutionStatus) {
        executionStatus = mExecutionStatus;
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean mShowHistory) {
        showHistory = mShowHistory;
    }
}
