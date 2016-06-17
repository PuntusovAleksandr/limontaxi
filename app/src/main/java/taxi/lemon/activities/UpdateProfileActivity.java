package taxi.lemon.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import taxi.lemon.R;
import taxi.lemon.fragments.BaseFragment;
import taxi.lemon.fragments.FragmentsInteractionListener;
import taxi.lemon.fragments.OnBackPressedListener;
import taxi.lemon.fragments.UpdateAddressFragment;
import taxi.lemon.fragments.UpdateNameFragment;
import taxi.lemon.fragments.UpdatePasswordFragment;
import taxi.lemon.fragments.UpdatePhoneFragment;

public class UpdateProfileActivity extends AppCompatActivity implements FragmentsInteractionListener {
    public static final String TAG = UpdateProfileActivity.class.getSimpleName();

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public static final int MODE_UPDATE_NAME = 0x1;
    public static final int MODE_UPDATE_ADDRESS = 0x2;
    public static final int MODE_UPDATE_PHONE = 0x3;
    public static final int MODE_CHANGE_PASSWORD = 0x4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int mode = getIntent().getIntExtra(EXTRA_MODE, 0x1);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (mode) {
            case MODE_UPDATE_NAME:
                ft.replace(R.id.content_container, new UpdateNameFragment(), UpdateNameFragment.TAG).commit();
                break;
            case MODE_UPDATE_ADDRESS:
                ft.replace(R.id.content_container, new UpdateAddressFragment(), UpdateAddressFragment.TAG).commit();
                break;
            case MODE_UPDATE_PHONE:
                ft.replace(R.id.content_container, new UpdatePhoneFragment(), UpdatePhoneFragment.TAG).commit();
                break;
            case MODE_CHANGE_PASSWORD:
                ft.replace(R.id.content_container, new UpdatePasswordFragment(), UpdatePasswordFragment.TAG).commit();
                break;
        }
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
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    @Override
    public void displayProgress(boolean display) {
        if (display) {
            findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.pb_progress).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        OnBackPressedListener listener = getOnBackPressedListener();

        if (listener != null) {
            listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private OnBackPressedListener getOnBackPressedListener() {
        FragmentManager fm  = getSupportFragmentManager();

        List<Fragment> fragments = fm.getFragments();

        Fragment fragment = fragments.get(0);

        if (fragment != null && fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        } else {
            return null;
        }
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }
}
