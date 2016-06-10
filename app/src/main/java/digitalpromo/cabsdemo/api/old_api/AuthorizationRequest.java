package digitalpromo.cabsdemo.api.old_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for user authorization
 */
public class AuthorizationRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "auth";

    /**
     * User's login (same as user's phone number)
     */
    @SerializedName("user_login")
    private String login;

    /**
     * User's password
     */
    @SerializedName("user_password")
    private String password;

    /**
     * Construct instance of the object
     * @param login user's login (same as user's phone number)
     * @param password user's password
     */
    public AuthorizationRequest(@NonNull String login, @NonNull String password) {
        super(SERVICE_NAME);
        this.login = login;
        this.password = password;
    }
}
