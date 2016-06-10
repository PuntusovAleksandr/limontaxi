package digitalpromo.cabsdemo.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.GetUserProfileResponse;
import digitalpromo.cabsdemo.events.MessageEvent;
import digitalpromo.cabsdemo.fragments.FragmentsInteractionListener;
import digitalpromo.cabsdemo.fragments.MyLocationFragment;
import digitalpromo.cabsdemo.fragments.OrderFragment;
import digitalpromo.cabsdemo.fragments.OrdersHistoryFragment;
import digitalpromo.cabsdemo.fragments.ProfileFragment;
import digitalpromo.cabsdemo.models.Order;
import digitalpromo.cabsdemo.models.UserProfile;
import digitalpromo.cabsdemo.utils.SharedPreferencesManager;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentsInteractionListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_IS_USER_LOGGED_IN = "EXTRA_IS_USER_LOGGED_IN";

    private TextView tvNotifyPhone;
    private TextView tvUserName;

    private boolean isUserLoggedIn;

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event) {
        if (event.getEvent() == MessageEvent.EVENT_MAKE_ORDER_FROM_HISTORY) {
            onNavigationItemSelected(((NavigationView) findViewById(R.id.nav_view)).getMenu().getItem(0).setChecked(true));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isUserLoggedIn = getIntent().getBooleanExtra(EXTRA_IS_USER_LOGGED_IN, false);

        Order.initInstance();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isUserLoggedIn) {
            getUserProfile();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_call_taxi) {
            setContainerPadding(false);
            ft.replace(R.id.content_container, new OrderFragment()).commit();
//            showNotifyPhoneItem(true);
        } else if (id == R.id.nav_my_location) {
            setContainerPadding(false);
            ft.replace(R.id.content_container, new MyLocationFragment()).commit();
//            showNotifyPhoneItem(false);
        } else if (id == R.id.nav_history) {
            setContainerPadding(false);
            ft.replace(R.id.content_container, new OrdersHistoryFragment()).commit();
//            showNotifyPhoneItem(false);
        } else if (id == R.id.nav_share) {
            share();
        } else if (id == R.id.nav_comment) {
            rateUpApp();
        } else if (id == R.id.nav_logout) {
            SharedPreferencesManager.getInstance().saveAutoLoginFlag(false);
            SharedPreferencesManager.getInstance().saveUserLoggedInFlag(false);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            setContainerPadding(true);
            ft.replace(R.id.content_container, new ProfileFragment()).commit();
//            showNotifyPhoneItem(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Set container's padding
     * @param add if true then add padding, else - remove
     */
    private void setContainerPadding(boolean add) {
        if (add) {
            Resources res = getResources();
            findViewById(R.id.content_container).setPadding(
                    (int) res.getDimension(R.dimen.activity_horizontal_margin),
                    (int) res.getDimension(R.dimen.activity_vertical_margin),
                    (int) res.getDimension(R.dimen.activity_horizontal_margin),
                    (int) res.getDimension(R.dimen.activity_vertical_margin)
            );
        } else {
            findViewById(R.id.content_container).setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Set up navigation drawer
     */
    private void setUpDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);

        if (!isUserLoggedIn) {
            setUserName(getString(R.string.guest));
            navigationView.getHeaderView(0).findViewById(R.id.tv_name).setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }
    }

    private void setUserName(String name) {
        tvUserName.setText(name);
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void changeTitle(String title) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null && title != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void showBackButton(View.OnClickListener listener) {

    }

    @Override
    public void displayProgress(boolean display) {
        View v = findViewById(R.id.pb_progress);

        if (display) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * Init all views
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpDrawer(toolbar);

        onNavigationItemSelected(((NavigationView) findViewById(R.id.nav_view)).getMenu().getItem(0).setChecked(true));

//        tvNotifyPhone = (TextView) findViewById(R.id.tv_notify_phone);
//
//        ImageButton btnEditNotifyPhone = (ImageButton) findViewById(R.id.ib_edit_notify_phone);
//        btnEditNotifyPhone.setOnClickListener(this);
    }

//    /**
//     * Show or hide item with notify phone number
//     * @param show if true show, hide - otherwise
//     */
//    private void showNotifyPhoneItem(boolean show) {
//        View v = findViewById(R.id.ll_notify_phone);
//
//        if (show) {
//            v.setVisibility(View.VISIBLE);
//        } else {
//            v.setVisibility(View.GONE);
//        }
//    }

    /**
     * Create an image file from assets folder, get it URI and share it with text via installed apps
     */
    private void share() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        File imageToShare = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/demo.png");

        try {
            inputStream = assetManager.open("demo.png");

            outputStream = new FileOutputStream(imageToShare);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();

                    Uri theUri = Uri.fromFile(imageToShare);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("image/*");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, theUri);
                    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getText(R.string.shared_text));
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_via)));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Launch the Play Store with your App page already opened
     */
    private void rateUpApp() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationInfo().packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        goToMarket.addFlags(flags);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationInfo().packageName)));
        }
    }

    /**
     * Get user's profile
     */
    private void getUserProfile() {
        displayProgress(true);
        ApiClient.getInstance().getUserProfile(new ApiClient.ApiCallback<GetUserProfileResponse>() {
            @Override
            public void response(GetUserProfileResponse response) {
                displayProgress(false);

                if (response.isOK()) {
                    Log.d(TAG, "response: success");

                    if (UserProfile.getInstance() == null) {
                        UserProfile.initInstance(response);
                    } else {
                        UserProfile.getInstance().updateData(response);
                    }

                    setUserName(UserProfile.getInstance().getName().trim());
                    Order.getInstance().setPhone(UserProfile.getInstance().getPhone());
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_SET_USER_PHONE));
                } else {
                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error() {
                Log.d(TAG, "error: ");
                displayProgress(false);
            }

            @Override
            public void noInternetConnection() {
                displayProgress(false);
            }
        });
    }
}
