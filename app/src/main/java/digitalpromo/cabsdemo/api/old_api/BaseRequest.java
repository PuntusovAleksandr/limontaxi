package digitalpromo.cabsdemo.api.old_api;

/**
 * Base class for requests
 */
public class BaseRequest {
    /**
     * Prefix for client requests
     */
    public static final String USER = "account/";

    /**
     * Prefix for order requests
     */
    public static final String ORDER = "order/";

    /**
     * Prefix for cities request
     */
    public static final String CITIES = "city/";

    /**
     * Prefix for google requests
     */
    public static final String GOOGLE = "googleapi/";

    /**
     * Name of the service
     */
    private transient String serviceName;

    /**
     * Construct the instance of the object
     * @param serviceName name of the service
     */
    public BaseRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Get name of the service
     * @return service name
     */
    public String getServiceName() {
        return serviceName;
    }
}
