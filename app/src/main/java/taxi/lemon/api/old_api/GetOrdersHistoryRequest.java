package taxi.lemon.api.old_api;

/**
 * Request for retrieving orders history
 */
public class GetOrdersHistoryRequest extends BaseRequest {
    private static final String SERVICE_NAME = ORDER + "history";

    /**
     * Date according to which we wish to get history
     */
    private String date;

    public GetOrdersHistoryRequest(String date) {
        super(SERVICE_NAME);
        this.date = date;
    }
}
