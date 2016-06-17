package taxi.lemon.api.old_api;

import java.util.ArrayList;
import java.util.List;

import taxi.lemon.models.RouteItem;

/**
 * Response for get autocomplete request
 */
public class GetAutocompleteResponse extends BaseResponse {

    /**
     * Array with autocomplete suggestions
     */
    private List<RouteItem> autocomplete;

    /**
     * Get autocomplete suggestions
     * @return autocomplete suggestions
     */
    public ArrayList<RouteItem> getAutocomplete() {
        if (autocomplete != null) {
            return new ArrayList<>(autocomplete);
        }
        else return new ArrayList<>();
    }
}
