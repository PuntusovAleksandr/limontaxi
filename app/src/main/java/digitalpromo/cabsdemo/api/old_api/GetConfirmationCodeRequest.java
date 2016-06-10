package digitalpromo.cabsdemo.api.old_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for checking confirmation code
 */
public class GetConfirmationCodeRequest extends BaseRequest {
    public static final String REGISTRATION = "register/sendConfirmCode";
    public static final String PHONE_CHANGE = "checkcodephone";

    /**
     * Phone for confirmation
     */
    @SerializedName("phone")
    private String phone;

    /**
     * Construct the instance of the object
     * @param serviceName one of {@link #REGISTRATION}, {@link #PHONE_CHANGE}
     * @param phone phone confirmation code
     */
//    public GetConfirmationCodeRequest(@NonNull String serviceName, @NonNull Integer code) {
//        super(USER + serviceName);
//        this.code = code;
//    }

    public GetConfirmationCodeRequest(@NonNull String serviceName, @NonNull String phone) {
        super(USER + serviceName);
        this.phone = phone;
    }
}
