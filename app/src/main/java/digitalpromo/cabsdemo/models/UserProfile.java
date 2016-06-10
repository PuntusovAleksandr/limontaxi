package digitalpromo.cabsdemo.models;

import java.util.ArrayList;

import digitalpromo.cabsdemo.api.old_api.GetUserProfileResponse;

/**
 * User's profile
 */
public class UserProfile {
    private static final String TAG = UserProfile.class.getSimpleName();

    private static UserProfile ourInstance;

    private ArrayList<DataChanged> subscribers = new ArrayList<>();

    /**
     * User's first name
     */
    private String firstName;

    /**
     * User's middle name
     */
    private String middleName;

    /**
     * User's last name
     */
    private String lastName;

    /**
     * User's phone
     */
    private String phone;

    /**
     * User's address
     */
    private String address;

    /**
     * User's entrance
     */
    private String entrance;

    /**
     * Interface for data changing notifications
     */
    public interface DataChanged {
        void onDataChanged();
    }

    /**
     * Get instance of the object
     * @return instance
     */
    public static UserProfile getInstance() {
        return ourInstance;
    }

    /**
     * Init instance
     * @param userProfile profile info holder
     */
    public static void initInstance(GetUserProfileResponse userProfile) {
        ourInstance = new UserProfile(
                userProfile.getFirstName(),
                userProfile.getMiddleName(),
                userProfile.getLastName(),
                userProfile.getPhone(),
                userProfile.getAddress(),
                userProfile.getEntrance()
        );
    }

    /**
     * Update profile data
     * @param userProfile profile info holder
     */
    public void updateData(GetUserProfileResponse userProfile) {
        firstName = userProfile.getFirstName();
        middleName = userProfile.getMiddleName();
        lastName = userProfile.getLastName();
        phone = userProfile.getPhone();
        address = userProfile.getAddress();
        entrance = userProfile.getEntrance();

        for (DataChanged subscriber : subscribers) {
            subscriber.onDataChanged();
        }
    }

    /**
     * Construct the instance of the object
     * @param firstName user's first name
     * @param middleName user's middle name
     * @param lastName user's last name
     * @param phone user's phone
     * @param address user's address
     * @param entrance user's entrance
     */
    public UserProfile(String firstName, String middleName, String lastName, String phone, String address, String entrance) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.entrance = entrance;
    }

    /**
     * Subscribe fragment to notify about instance changing
     * @param subscriber subscriber
     */
    public void subscribe(DataChanged subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribe fragment from notifications
     * @param subscriber victim
     */
    public void unsubscirbe(DataChanged subscriber) {

        subscribers.remove(subscribers.indexOf(subscriber));
    }

    public String getName() {
        return lastName + " " + firstName + " " + middleName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }
}
