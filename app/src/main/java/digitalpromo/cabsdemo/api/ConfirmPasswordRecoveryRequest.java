package digitalpromo.cabsdemo.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for confirmation of password recovery request
 */
public class ConfirmPasswordRecoveryRequest extends CheckConfirmationCodeRequest {
    public static final String SERVICE_NAME = "checkcodepass";

    /**
     * New user's password
     */
    @SerializedName("new_pass")
    private String newPassword;

    /**
     * Construct the instance of the object
     * @param code confirmation code
     * @param newPassword new user's password
     */
    public ConfirmPasswordRecoveryRequest(@NonNull Integer code, String newPassword) {
        super(SERVICE_NAME, code);
        this.newPassword = newPassword;
    }
}
