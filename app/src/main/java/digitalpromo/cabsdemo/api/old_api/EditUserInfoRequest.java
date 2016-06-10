package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

/**
 * Request for editing user's profile information
 */
public class EditUserInfoRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "edituser";

    /**
     * User's first name
     */
    @SerializedName("first_name")
    private String firstName;

    /**
     * User's middle name
     */
    @SerializedName("patronymic")
    private String middleName;

    /**
     * User's last name
     */
    @SerializedName("last_name")
    private String lastName;

    /**
     * User's address
     */
    private String address;

    /**
     * User's entrance
     */
    @SerializedName("driveway")
    private String entrance;

    /**
     * Construct the instance of the object
     * @param firstName new user's first name
     * @param middleName new user's middle name
     * @param lastName new user's last name
     * @param address new user's address
     * @param entrance new user's entrance
     */
    public EditUserInfoRequest(String firstName, String middleName, String lastName, String address, String entrance) {
        super(SERVICE_NAME);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.entrance = entrance;
    }
}
