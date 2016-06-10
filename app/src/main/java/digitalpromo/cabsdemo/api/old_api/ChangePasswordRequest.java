package digitalpromo.cabsdemo.api.old_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for password recovery
 */
public class ChangePasswordRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "passwordrecovery";

    /**
     * User's old password
     */
    @SerializedName("old_password")
    private String oldPassword;

    /**
     * User's new password
     */
    @SerializedName("new_password")
    private String newPassword;

    /**
     * Construct the instance of the object
     * @param oldPassword user's old password
     * @param newPassword user's new password
     */
    public ChangePasswordRequest(@NonNull String oldPassword, @NonNull String newPassword) {
        super(SERVICE_NAME);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
