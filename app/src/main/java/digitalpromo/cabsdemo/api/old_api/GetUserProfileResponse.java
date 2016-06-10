package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

/**
 * Response for get user profile response
 */
public class GetUserProfileResponse extends BaseResponse {

    /**
     * User's first name
     */
    @SerializedName("first_name")
    private String firstName = "";

    /**
     * User's middle name
     */
    @SerializedName("patronymic")
    private String middleName = "";

    /**
     * User's last name
     */
    @SerializedName("last_name")
    private String lastName = "";

    /**
     * User's phone
     */
    private String phone = "";

    /**
     * User's address
     */
    private String address = "";

    /**
     * User's entrance
     */
    @SerializedName("driveway")
    private String entrance = "";

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEntrance() {
        return entrance;
    }

    @Override
    public String toString() {
        return "GetUserProfileResponse{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", entrance='" + entrance + '\'' +
                '}';
    }


}
