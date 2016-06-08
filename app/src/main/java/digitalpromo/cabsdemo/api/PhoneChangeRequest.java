package digitalpromo.cabsdemo.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for phone change
 */
public class PhoneChangeRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "changephone";

    /**
     * New user's phone
     */
    @SerializedName("new_user_phone")
    private String newUserPhone;

    /**
     * Construct the instance of the object
     * @param newUserPhone user's new phone number
     */
    public PhoneChangeRequest(@NonNull String newUserPhone) {
        super(SERVICE_NAME);
        this.newUserPhone = newUserPhone;
    }
}
