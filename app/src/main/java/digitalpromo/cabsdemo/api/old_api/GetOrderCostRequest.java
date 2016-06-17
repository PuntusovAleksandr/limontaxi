package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import digitalpromo.cabsdemo.models.Order;
import digitalpromo.cabsdemo.models.RouteItem;

/**
 * Request for order cost
 */
public class GetOrderCostRequest extends BaseRequest {
    private static final String SERVICE_NAME = GOOGLE + "price";

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
     * Route
     */
    private List<RouteItem> route;

    public GetOrderCostRequest() {
        super(SERVICE_NAME);

        Order order = Order.getInstance();

        conditioner = order.isConditioner();
        animals = order.isAnimals();
        delivery = order.isDelivery();
        baggage = order.isBaggage();
//        meetWithTable = order.isMeetWithTable();
        emptyTrunk = order.isEmptyTrunk();
        rate = order.getRate();
        route = order.getRoute();
    }
}
