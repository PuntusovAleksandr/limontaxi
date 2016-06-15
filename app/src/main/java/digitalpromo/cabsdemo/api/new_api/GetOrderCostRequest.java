package digitalpromo.cabsdemo.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import digitalpromo.cabsdemo.models.RouteItem;

/**
 * Created by Администратор on 15.06.2016.
 */
public class GetOrderCostRequest {
    @SerializedName("user_full_name")
    private String userFullName;

    @SerializedName("user_phone")
    private String phone;

    @SerializedName("reservation")
    private Boolean reservation;

    @SerializedName("comment")
    private String comment;

    @SerializedName("minibus")
    private Boolean minibus;

    @SerializedName("wagon")
    private Boolean wagon;

    @SerializedName("premium")
    private Boolean premium;

    @SerializedName("baggage")
    private Boolean baggage;

    @SerializedName("animal")
    private Boolean animal;

    @SerializedName("conditioner")
    private Boolean conditioner;

    @SerializedName("courier_delivery")
    private Boolean courier;

    @SerializedName("route")
    private ArrayList<RouteItem> route;

    @SerializedName("add_cost")
    private Integer addCost;
}
