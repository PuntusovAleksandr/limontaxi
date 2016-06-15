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

    @SerializedName("required_time")
    private String requiredTime;

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

    @SerializedName("route_undefined")
    private Boolean routeUndefined;

    @SerializedName("terminal")
    private Boolean terminal;

    @SerializedName("receipt")
    private Boolean receipt;

    public GetOrderCostRequest(String userFullName, String phone, Boolean reservation, String requiredTime,
                               String comment, Boolean minibus, Boolean wagon, Boolean premium,
                               Boolean baggage, Boolean animal, Boolean conditioner, Boolean courier,
                               Boolean routeUndefined, Boolean terminal, Boolean receipt, ArrayList<RouteItem> route, Integer addCost) {
        this.userFullName = userFullName;
        this.phone = phone;
        this.reservation = reservation;
        this.requiredTime = requiredTime;
        this.comment = comment;
        this.minibus = minibus;
        this.wagon = wagon;
        this.premium = premium;
        this.baggage = baggage;
        this.animal = animal;
        this.conditioner = conditioner;
        this.courier = courier;
        this.routeUndefined = routeUndefined;
        this.terminal = terminal;
        this.receipt = receipt;
        this.route = route;
        this.addCost = addCost;
    }

    @SerializedName("route")
    private ArrayList<RouteItem> route;

    @SerializedName("add_cost")
    private Integer addCost;
}
