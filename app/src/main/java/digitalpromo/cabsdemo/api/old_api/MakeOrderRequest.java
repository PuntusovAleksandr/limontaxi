package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import digitalpromo.cabsdemo.models.Order;
import digitalpromo.cabsdemo.models.RouteItem;

/**
 * Request for creating an order
 */
public class MakeOrderRequest extends BaseRequest {
    private static final String SERVICE_NAME = ORDER + "add";

    /**
     * Add parameter conditioner
     */
    @SerializedName("dop_air_conditioning")
    private boolean conditioner;

    /**
     * Add parameter animals
     */
    @SerializedName("dop_animals")
    private boolean animals;

    /**
     * Add parameter express delivery
     */
    @SerializedName("dop_express_delivery")
    private boolean delivery;

    /**
     * Add parameter baggage
     */
    @SerializedName("dop_loading_salon")
    private boolean baggage;

    /**
     * Add parameter meet with table
     */
    @SerializedName("dop_plate_tablet")
    private boolean meetWithTable;

    /**
     * Add parameter meet with table
     */
    @SerializedName("dop_empty_trunk")
    private boolean emptyTrunk;

    /**
     * Rate type
     */
    @SerializedName("type_rate")
    private int rate;

    /**
     * Phone for feedback
     */
    private String phone;

    /**
     * Order cost
     */
    @SerializedName("price")
    private String cost;

    /**
     * Add cost for order
     */
    @SerializedName("add_cost")
    private transient String addCost;

    /**
     * Comment for driver
     */
    private String comment;

    /**
     * Pre order time
     */
    @SerializedName("order_time")
    private String orderTime;

    /**
     * Encoded route
     */
    @SerializedName("path_routes")
    private String encodedRoute;

    /**
     * Route
     */
    private List<RouteItem> route;

    /**
     * City's id
     */
    @SerializedName("city_id")
    private int cityId;

    public MakeOrderRequest() {
        super(SERVICE_NAME);

        Order order = Order.getInstance();

        conditioner = order.isConditioner();
        animals = order.isAnimals();
        delivery = order.isDelivery();
        baggage = order.isBaggage();
//        meetWithTable = order.isMeetWithTable();
        emptyTrunk = order.isEmptyTrunk();
        rate = order.getRate();
        phone = order.getPhone();
        cost = String.valueOf(order.getCost());
        addCost = String.valueOf(order.getAddCost());
        comment = order.getComment();
        orderTime = order.getPreOrderTime();
        route = order.getRoute();
        encodedRoute = order.getEncodedRoute();
//        cityId = order.getCityId();
    }

    @Override
    public String toString() {
        return "MakeOrderRequest{" +
                "conditioner=" + conditioner +
                ", animals=" + animals +
                ", delivery=" + delivery +
                ", baggage=" + baggage +
                ", meetWithTable=" + meetWithTable +
                ", emptyTrunk=" + emptyTrunk +
                ", rate=" + rate +
                ", phone='" + phone + '\'' +
                ", cost='" + cost + '\'' +
                ", addCost='" + addCost + '\'' +
                ", comment='" + comment + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", encodedRoute='" + encodedRoute + '\'' +
                ", route=" + route +
                ", cityId=" + cityId +
                '}';
    }
}
