package digitalpromo.cabsdemo.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for registering a new user
 */
public class RegisterUserRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "adduser";
    /**
     * Name of the user
     */
    @SerializedName("user_name")
    private String userName;

    /**
     * Phone number of the user (same as user's account login)
     */
    @SerializedName("user_phone")
    private String userPhone;

    /**
     * Password for the user account
     */
    @SerializedName("user_password")
    private String userPassword;

    /**
     * Construct the instance of the object
     * @param userName user's name
     * @param userPhone user's phone
     * @param userPassword user's password
     */
    public RegisterUserRequest(@NonNull String userName, @NonNull String userPhone, @NonNull String userPassword) {
        super(SERVICE_NAME);
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }
}
