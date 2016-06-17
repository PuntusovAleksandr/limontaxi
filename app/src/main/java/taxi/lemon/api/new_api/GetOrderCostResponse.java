package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 15.06.2016.
 */
public class GetOrderCostResponse {
    @SerializedName("dispatching_order_uid")
    private String uid;

    @SerializedName("order_cost")
    private Double orderCost;

    @SerializedName("currency")
    private String currency;

    @SerializedName("discount_trip")
    private Boolean discountTrip;

    @SerializedName("can_pay_bonuses")
    private Boolean canPayBonuses;

    public String getUid() {
        return uid;
    }

    public Double getOrderCost() {
        return orderCost;
    }

    public String getCurrency() {
        return currency;
    }

    public Boolean getDiscountTrip() {
        return discountTrip;
    }

    public Boolean getCanPayBonuses() {
        return canPayBonuses;
    }
}