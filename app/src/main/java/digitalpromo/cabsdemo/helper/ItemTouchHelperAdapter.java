package digitalpromo.cabsdemo.helper;

/**
 * Interface to listen for a move or dismissal event from {@link android.support.v7.widget.helper.ItemTouchHelper.Callback}
 */
public interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and <strong>not</strong> at the end of a "drop" event.
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * Called when an item has been deleted by a swipe.
     * @param position The position of the item dismissed.
     */
    void onItemDelete(int position);
}
