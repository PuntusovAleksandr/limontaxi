package taxi.lemon.fragments;

import android.view.View;

/**
 * Should be implemented by activity to interact with fragments
 */
public interface FragmentsInteractionListener {
    /**
     * Change toolbar's title
     * @param title
     */
    void changeTitle(String title);

    /**
     * Show back button
     * @param listener listener for click events
     */
    void showBackButton(View.OnClickListener listener);

    /**
     * Change visibility of progress
     * @param display true - show progress, false - otherwise
     */
    void displayProgress(boolean display);
}
