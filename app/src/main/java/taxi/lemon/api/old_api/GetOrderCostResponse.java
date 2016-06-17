package taxi.lemon.api.old_api;

/**
 * Response for get order cost request
 */
public class GetOrderCostResponse extends BaseResponse {
    /**
     * Order's cost
     */
    private String cost;

    public String getCost() {
        return cost;
    }
}
