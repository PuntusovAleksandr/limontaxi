package digitalpromo.cabsdemo.api;

/**
 * Request to get cities list
 */
public class GetCitiesRequest extends BaseRequest {
    private static final String SERVICE_NAME = CITIES + "getallcity";

    public GetCitiesRequest() {
        super(SERVICE_NAME);
    }
}
