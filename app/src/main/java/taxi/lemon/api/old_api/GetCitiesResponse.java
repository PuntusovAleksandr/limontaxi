package taxi.lemon.api.old_api;

import java.util.ArrayList;
import java.util.List;

import taxi.lemon.models.City;

/**
 * Created by Takeitez on 10.03.2016.
 */
public class GetCitiesResponse extends BaseResponse {
    private List<City> cities;

    public ArrayList<City> getCities() {
        return new ArrayList<City>(cities);
    }
}
