package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 16.06.2016.
 */
public class GetOrderHistoryResponse {
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
}
