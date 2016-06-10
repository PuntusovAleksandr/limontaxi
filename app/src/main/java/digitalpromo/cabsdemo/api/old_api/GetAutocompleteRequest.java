package digitalpromo.cabsdemo.api.old_api;

/**
 * Request to get autocomplete suggestions
 */
public class GetAutocompleteRequest extends BaseRequest {
    private static final String SERVICE_NAME = GOOGLE + "getautocomplete";

    /**
     * Code of city
     */
    private int city;

    /**
     * String for which we need to get autocomplete
     */
    private String string;

    public GetAutocompleteRequest(int city, String string) {
        super(SERVICE_NAME);
        this.city = city;
        this.string = string;
    }
}
