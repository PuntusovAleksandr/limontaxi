package digitalpromo.cabsdemo.api.old_api;

/**
 * Request for getting user's profile
 */
public class GetUserProfileRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "getprofile";

    /**
     * Construct the instance of the object
     */
    public GetUserProfileRequest() {
        super(SERVICE_NAME);
    }
}
