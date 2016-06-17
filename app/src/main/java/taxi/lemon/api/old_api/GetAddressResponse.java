package taxi.lemon.api.old_api;

/**
 * Response for get address request
 */
public class GetAddressResponse extends BaseResponse {

    /**
     * Street name
     */
    private String street;

    /**
     * House
     */
    private String house;

    /**
     * Get street name
     * @return street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * Get house
     * @return house
     */
    public String getHouse() {
        return house;
    }

    /**
     * Get display name of street
     * @return display name
     */
    public String getAddress() {
        return street + ", " + house;
    }
}
