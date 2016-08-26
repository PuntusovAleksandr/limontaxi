package taxi.lemon.models;

// TODO: 03.12.2015 add doc comments

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Hold order history item info
 */
public class HistoryItem {

    @SerializedName("dispatching_order_uid")
    private String orderId;

    @SerializedName("required_time")
    private String requiredTime;

    @SerializedName("order_cost")
    private String orderCost;

    @SerializedName("user_full_name")
    private String userFullName;

    @SerializedName("user_phone")
    private String userPhone;

    @SerializedName("route")
    ArrayList<RouteItem> route;

    @SerializedName("close_reason")
    private int closeReason;

    @SerializedName("execution_status")
    private String executionStatus;

    private boolean showHistory;

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

    public void setUserFullName(String mUserFullName) {
        userFullName = mUserFullName;
    }

    public void setUserPhone(String mUserPhone) {
        userPhone = mUserPhone;
    }

    public void setCloseReason(int mCloseReason) {
        closeReason = mCloseReason;
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

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return requiredTime;
    }

    public String getCost() {
        return orderCost;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public ArrayList<RouteItem> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<RouteItem> route) {
        this.route = route;
    }

    public int getCloseReason() {
        return closeReason;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    //    @SerializedName("status")
//    private String executionStatus;
//
//    private String cost;
//
//    private String date;
//
//    private ArrayList<RouteItem> route;
//
//    public HistoryItem() {
//    }
//
//    public String getExecutionStatus() {
//        return executionStatus;
//    }
//
//    public void setExecutionStatus(String executionStatus) {
//        this.executionStatus = executionStatus;
//    }
//
//    public String getCost() {
//        return cost;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public ArrayList<RouteItem> getRoute() {
//        return route;
//    }
}
