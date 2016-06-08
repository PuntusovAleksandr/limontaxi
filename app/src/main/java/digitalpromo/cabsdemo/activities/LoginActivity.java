package digitalpromo.cabsdemo.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.fragments.BaseFragment;
import digitalpromo.cabsdemo.fragments.BasePagerFragment;
import digitalpromo.cabsdemo.fragments.FragmentsInteractionListener;
import digitalpromo.cabsdemo.fragments.LoginFragment;
import digitalpromo.cabsdemo.fragments.OnBackPressedListener;
import digitalpromo.cabsdemo.fragments.RegisterFragment;

public class LoginActivity extends BaseActivity implements RegisterFragment.RegistrationSuccessListener, FragmentsInteractionListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.vp_pager);
        mViewPager.setAdapter(new AuthViewPagerAdapter(getSupportFragmentManager()));

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tb_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() > 0) {
            OnBackPressedListener listener = getOnBackPressedListener();
            if (listener != null) {
                listener.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private OnBackPressedListener getOnBackPressedListener() {
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) mViewPager.getAdapter();
        Fragment fragment = adapter.getItem(mViewPager.getCurrentItem());

        if (fragment != null && fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        } else {
            logger("wrong instance");
            return null;
        }
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void onRegSuccess() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void changeTitle(String title) {
        // empty
    }

    @Override
    public void showBackButton(View.OnClickListener listener) {
        // empty
    }

    @Override
    public void displayProgress(boolean display) {
        if (display) {
            findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.pb_progress).setVisibility(View.GONE);
        }
    }

    /**
     * Pager adapter for view pager
     */
    private class AuthViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        public AuthViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new LoginFragment());
            fragments.add(new RegisterFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = fragments.get(position);

            if (fragment instanceof BasePagerFragment) {
                return ((BasePagerFragment) fragment).getPageTitle();
            }

            return super.getPageTitle(position);
        }
    }
}
