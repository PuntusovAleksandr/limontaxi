/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package taxi.lemon.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import taxi.lemon.R;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.GetAutoCompleteResponse;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.models.House;
import taxi.lemon.models.RouteItem;
import taxi.lemon.utils.SharedPreferencesManager;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 * <p/>
 * Note that this adapter requires a valid {@link com.google.android.gms.common.api.GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 */
public class PlaceAutocompleteAdapter
        extends ArrayAdapter<RouteItem> implements Filterable {
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<RouteItem> mResultList;
    private boolean searchHome;
    private RouteItem searchItem;

    public LatLng getLLatLngFromAddress(String mAddress, String mHouse) {
        LatLng latLng = new LatLng(0, 0);
        searchHome = true;
        ArrayList<RouteItem> routeItems = getAutocomplete(mAddress);
        if (routeItems.size() > 0) {
            for (RouteItem item : routeItems) {
                ArrayList<House> houses = item.getHouses();
                for (House house : houses) {
                    if (house.getHouse().equalsIgnoreCase(mHouse)) {
                        latLng = new LatLng(house.getLat(), house.getLng());
                    }
                }
            }
        }
        return latLng;
    }

//    private int city;

    public interface NetworkError {
        void noInternet();
    }

    private NetworkError callback;

    public PlaceAutocompleteAdapter(Context context/*, int city*/) {
        super(context, R.layout.autocomplete_item, R.id.tv_address);
//        this.city = city;
    }

    public void setNetworkErrorListener(NetworkError callback) {
        this.callback = callback;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return mResultList == null ? 0 : mResultList.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public RouteItem getItem(int position) {
        return mResultList.get(position);
    }

    public void searchHomes(boolean searchHome, RouteItem mItem) {
        this.searchHome = searchHome;
        this.searchItem = mItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        RouteItem item = getItem(position);

        TextView textView1 = (TextView) row.findViewById(R.id.tv_address);
//        textView1.setText(item.getAddress());
        if (item.getHouses() != null) {
            textView1.setText(item.getStreet());
        } else if (!item.isObject()) {
            textView1.setText(item.getAddress());
        } else {
            textView1.setText(item.getStreet());
        }
        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint.toString());
                    if (mResultList != null) {
                        // The API successfully returned result
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {

                    if (mResultList.size() == 1 && !mResultList.get(0).isObject()) {
                        RouteItem singleStreetRouteItem = mResultList.get(0);
                        ArrayList<RouteItem> items = new ArrayList<>();
                        for (House house : singleStreetRouteItem.getHouses()) {
                            RouteItem object = new RouteItem(singleStreetRouteItem.getStreet() + ", " + house.getHouse(), new LatLng(house.getLat(), house.getLng()));
                            object.setObject(true);
                            object.setNumber(house.getHouse());
                            items.add(object);
                        }
                        mResultList.clear();
                        mResultList.addAll(items);
                        notifyDataSetInvalidated();
                    }
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof RouteItem) {
                    return ((RouteItem) resultValue).getStreet();
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    private ArrayList<RouteItem> getAutocomplete(String string) {
//        ArrayList<RouteItem> results = ApiClient.getInstance().getAutocomplete(city, string);

        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class,
                SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        try {
//            Response<ArrayList<RouteItem>> results = client.getAutocompleteRequest(string).execute();
//            return client.getAutocompleteRequest(string, "houses").execute().body().getAutocomplete();
//            GetAutoCompleteResponse res = client.getAutocompleteRequest(string).execute().body();
            ArrayList<RouteItem> items = new ArrayList<>();
            if (!searchHome) {
                Call<GetAutoCompleteResponse> autocompleteRequest = client.getAutocompleteRequest(string, 1000);
                Response<GetAutoCompleteResponse> execute = autocompleteRequest.execute();
                GetAutoCompleteResponse body = execute.body();
                items = body.getAutocomplete();
            } else {
                if (!string.equalsIgnoreCase(searchItem.getStreet())) {
                    searchHome = false;
                }
                if (items.size() > 0) {
                    items.clear();
                }
                items.add(searchItem);
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (results == null) {
//            callback.noInternet();
//        }
//
//        return results;

        return null;
    }
}
