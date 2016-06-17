package taxi.lemon.api.old_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for password recovery
 */
public class RecoverPasswordRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "recoveryclientpass";

    /**
     * User's phone
     */
    @SerializedName("user_phone")
    private String userPhone;

    /**
     * Construct the instance of the object
     * @param userPhone user's phone
     */
    public RecoverPasswordRequest(@NonNull String userPhone) {
        super(SERVICE_NAME);
        this.userPhone = userPhone;
    }
}
