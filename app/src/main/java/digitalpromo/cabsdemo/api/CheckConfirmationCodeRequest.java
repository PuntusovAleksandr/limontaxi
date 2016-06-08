package digitalpromo.cabsdemo.api;

import android.support.annotation.NonNull;

/**
 * Request for checking confirmation code
 */
public class CheckConfirmationCodeRequest extends BaseRequest {
    public static final String REGISTRATION = "checkcode";
    public static final String PHONE_CHANGE = "checkcodephone";

    /**
     * Confirmation code
     */
    private Integer code;

    /**
     * Construct the instance of the object
     * @param serviceName one of {@link #REGISTRATION}, {@link #PHONE_CHANGE}
     * @param code confirmation code
     */
    public CheckConfirmationCodeRequest(@NonNull String serviceName, @NonNull Integer code) {
        super(USER + serviceName);
        this.code = code;
    }
}
