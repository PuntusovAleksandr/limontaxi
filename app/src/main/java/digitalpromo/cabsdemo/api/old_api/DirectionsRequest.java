package digitalpromo.cabsdemo.api.old_api;

import java.util.ArrayList;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.models.RouteItem;

/**
 * Request for goog;e directions api
 */
public class DirectionsRequest {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?language=ru&mode=driving";
    private static final String API_KEY = "&key=" + App.getContext().getString(R.string.directions_key);

    private ArrayList<RouteItem> route;

    public DirectionsRequest(ArrayList<RouteItem> route) {
        this.route = route;
    }

    private String getOrigin() {
        return "&origin=" + route.get(0).getStringLatLng();
    }

    private String getDestination() {
        return "&destination=" + route.get(route.size() - 1).getStringLatLng();
    }

    private String getWayPoints() {
        if (route.size() < 3) {
            return "";
        } else {
            String waypoints = "&waypoints=";
            int lastElementForWayPoints = route.size() - 1;
            for (int i = 1; i < lastElementForWayPoints; i++) {
                waypoints += route.get(i).getStringLatLng();
                if (i < lastElementForWayPoints - 1) {
                    waypoints += "|";
                }
            }
            return waypoints;
        }
    }

    public String getUrlRequest() {
        return BASE_URL + getOrigin() + getWayPoints() + getDestination() + API_KEY;
    }
}
