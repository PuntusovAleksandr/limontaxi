package digitalpromo.cabsdemo.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Takeitez on 08.02.2016.
 */
public abstract class BasePagerFragment extends BaseFragment {

    protected FragmentsInteractionListener mListener;

    public abstract String getPageTitle();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentsInteractionListener) {
            mListener = (FragmentsInteractionListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }
}
