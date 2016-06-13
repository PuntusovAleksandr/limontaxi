package digitalpromo.cabsdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.GetAddressResponse;
import digitalpromo.cabsdemo.api.old_api.MyGoogleApiClient;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = MapActivity.class.getSimpleName();

    public static final int REQUEST_ACCESS_FINE_LOCATION = 0x111;

    public static final int REQUEST_ADDRESS = 0x1;
    public static final int RESULT_OK = 0x200;

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_LAT_LNG = "EXTRA_LAT_LNG";
    public static final String EXTRA_INDEX = "EXTRA_INDEX";

    private GoogleMap mMap;
    private Marker marker;

    private String address = "Some address";
    private LatLng latLng;

    private MyGoogleApiClient myGoogleApiClient;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpToolbar();

        index = getIntent().getIntExtra(EXTRA_INDEX, -1);

        myGoogleApiClient = new MyGoogleApiClient(this, this, this);

        logger("index - " + index);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_accept);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ADDRESS, address);
                intent.putExtra(EXTRA_LAT_LNG, latLng);
                intent.putExtra(EXTRA_INDEX, index);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        myGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Set address
     */
    private void setAddress(String address) {
        this.address = address;

        TextView tv = (TextView) findViewById(R.id.tv_address);
        if (!address.isEmpty()) {
            tv.setText(address);
        } else {
            tv.setText(R.string.failed_determine_address);
        }

        showProgress(false);
    }

    /**
     * Set up toolbar
     */
    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.ma_title_pick_address);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
    }

    /**
     * Set up the Map
     */
    private void setUpMap() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        mMap.setOnMapClickListener(this);

        showProgress(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showProgress(true);

        setMarker(latLng);
    }

    /**
     * Remove old marker and set up a new one
     * @param latLng initial position for new marker
     */
    private void setMarker(LatLng latLng) {
        this.latLng = latLng;

        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setDraggable(true);
        mMap.setOnMarkerDragListener(this);

        getAddress(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        showProgress(true);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getAddress(marker.getPosition());
    }

//    /**
//     * Check permissions
//     */
//    private void checkPermissions() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            checkLocationService();
//        } else {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                logger("not granted");
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    logger("shouldn't request permissions");
//                } else {
//                    logger("request permissions");
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MapActivity.REQUEST_ACCESS_FINE_LOCATION);
//                }
//            } else {
//                logger("granted");
//                mMap.setMyLocationEnabled(true);
//                mMap.setOnMyLocationChangeListener(this);
//            }
//        }
//    }
//
//    /**
//     * Check is location service enabled or not
//     */
//    private void checkLocationService() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch(Exception ex) {}
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch(Exception ex) {}
//
//        if (!gps_enabled && !network_enabled) {
//            showDialog();
//        } else {
//            mMap.setMyLocationEnabled(true);
//            mMap.setOnMyLocationChangeListener(this);
//        }
//
//    }

//    /**
//     * Show dialog with settings button
//     */
//    private void showDialog() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog);
//        dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
//        dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                // TODO Auto-generated method stub
//                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(myIntent);
//                //get gps
//            }
//        });
//        dialog.show();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        logger("onRequestPermissionsResult");
//        switch (requestCode) {
//            case MapActivity.REQUEST_ACCESS_FINE_LOCATION:
//                logger("REQUEST_ACCESS_FINE_LOCATION");
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    logger("PERMISSION_GRANTED");
////                    checkLocationService();
//                }
//                break;
//        }
//    }

    /**
     * Show or hide progress bar
     * @param show if true - show, else - hide
     */
    private void showProgress(boolean show) {
        int visibility;

        if (show) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }

        findViewById(R.id.pb_progress).setVisibility(visibility);
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
        setMarker(latLng);

        myGoogleApiClient.stopLocationUpdates(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        myGoogleApiClient.startLocationUpdates(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        myGoogleApiClient.connectionFailed(connectionResult);
    }

    private void getAddress(LatLng latLng) {
        showProgress(true);
        final Activity activity = this;

        ApiClient.getInstance().getAddress(latLng, new ApiClient.ApiCallback<GetAddressResponse>() {
            @Override
            public void response(GetAddressResponse response) {
                showProgress(false);
                if (response.isOK()) {
                    setAddress(response.getAddress());
                }
            }

            @Override
            public void error() {
                showProgress(false);
            }

            @Override
            public void noInternetConnection() {
                showProgress(false);
                ApiClient.getInstance().showAlert(activity);
            }
        });

//        ApiClient.getInstance().getFullAddress(latLng, new ApiClient.ApiCallback<GeoCodingResponse>() {
//            @Override
//            public void response(GeoCodingResponse response) {
//                showProgress(false);
//                if (response.isOK()) {
//                    setAddress(response.getFullAddress());
//                }
//            }
//
//            @Override
//            public void error() {
//                showProgress(false);
//            }
//
//            @Override
//            public void noInternetConnection() {
//                showProgress(false);
//                ApiClient.getInstance().showAlert(activity);
//            }
//        });
    }
}
