package digitalpromo.cabsdemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.activities.UpdateProfileActivity;
import digitalpromo.cabsdemo.adapters.ProfileAdapter;
import digitalpromo.cabsdemo.models.ProfileItem;
import digitalpromo.cabsdemo.models.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends BaseFragment implements UserProfile.DataChanged {
    public static final String TAG = ProfileFragment.class.getSimpleName();

    private FragmentsInteractionListener mListener;

    private ProfileAdapter mAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        if (getView() == null) return;

        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_profile_info);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<ProfileItem> profile = new ArrayList<ProfileItem>();

        // specify an adapter (see also next example)
        mAdapter = new ProfileAdapter(profile);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void doBack() {

    }

    @Override
    public void onResume() {
        super.onResume();

        UserProfile.getInstance().subscribe(this);

        mListener.changeTitle(getString(R.string.pf_title_profile));

        initProfileAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();

        UserProfile.getInstance().unsubscirbe(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
        switch (item.getItemId()) {
            case R.id.pm_update_name:
                intent.putExtra(UpdateProfileActivity.EXTRA_MODE, UpdateProfileActivity.MODE_UPDATE_NAME);
                break;
            case R.id.pm_update_address:
                intent.putExtra(UpdateProfileActivity.EXTRA_MODE, UpdateProfileActivity.MODE_UPDATE_ADDRESS);
                break;
            case R.id.pm_update_phone:
                intent.putExtra(UpdateProfileActivity.EXTRA_MODE, UpdateProfileActivity.MODE_UPDATE_PHONE);
                break;
            case R.id.pm_change_password:
                intent.putExtra(UpdateProfileActivity.EXTRA_MODE, UpdateProfileActivity.MODE_CHANGE_PASSWORD);
                break;
            default:
                break;
        }
        getActivity().startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentsInteractionListener) {
            mListener = (FragmentsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Add profile info to adapter as single profile items
     */
    private void initProfileAdapter() {
        UserProfile profile = UserProfile.getInstance();

        ArrayList<ProfileItem> items = new ArrayList<>();

        items.add(new ProfileItem(ProfileItem.Titles.LOGIN, profile.getPhone()));
        items.add(new ProfileItem(ProfileItem.Titles.NAME, profile.getName()));
        items.add(new ProfileItem(ProfileItem.Titles.PHONE, profile.getPhone()));
        items.add(new ProfileItem(ProfileItem.Titles.ADDRESS, profile.getFullAddress()));

        mAdapter.updateData(items);
    }

    @Override
    public void onDataChanged() {
        initProfileAdapter();
    }
}
