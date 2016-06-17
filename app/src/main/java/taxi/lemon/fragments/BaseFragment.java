package taxi.lemon.fragments;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment implements OnBackPressedListener {

    public BaseFragment() {
    }

    /**
     * Initiate all views of the fragment
     */
    protected abstract void initViews();

    /**
     * Do back action
     */
    protected abstract void doBack();

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}
