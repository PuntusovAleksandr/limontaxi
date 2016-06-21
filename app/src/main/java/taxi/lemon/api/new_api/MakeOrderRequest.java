package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import taxi.lemon.models.Order;
import taxi.lemon.models.RouteItem;

/**
 * Created by Администратор on 16.06.2016.
 */
public class MakeOrderRequest {
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

    @SerializedName("route")
    private ArrayList<RouteItem> route;

    @SerializedName("add_cost")
    private long addCost;

    @SerializedName("app_registration_token")
    private String appRegistrationToken;

    public MakeOrderRequest(Order order, String userFullName, String appRegistrationToken) {
        this.userFullName = userFullName;
        this.phone = order.getPhone();
        this.reservation = order.isPreOrder();
        this.requiredTime = order.getPreOrderTime();
        this.comment = order.getComment();
        this.minibus = order.getRate() == 5;
        this.wagon = order.getRate() == 4;
        this.premium = order.getRate() == 2;
        this.baggage = order.isBaggage();
        this.animal = order.isAnimals();
        this.conditioner = order.isConditioner();
        this.courier = order.isDelivery();
        this.routeUndefined = order.isRouteUndefined();
        this.terminal = order.isTerminalPay();
        this.receipt = order.isReceiptNeed();
        this.route = order.getRoute();
        this.appRegistrationToken = appRegistrationToken;
    }
}
