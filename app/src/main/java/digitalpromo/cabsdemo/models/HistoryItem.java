package digitalpromo.cabsdemo.models;

// TODO: 03.12.2015 add doc comments

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Hold order history item info
 */
public class HistoryItem {
    @SerializedName("status")
    private String executionStatus;

    private String cost;

    private String date;

    private ArrayList<RouteItem> route;

    public HistoryItem() {
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<RouteItem> getRoute() {
        return route;
    }
}
