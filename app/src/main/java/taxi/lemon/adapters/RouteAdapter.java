package taxi.lemon.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import taxi.lemon.R;
import taxi.lemon.helper.ItemTouchHelperAdapter;
import taxi.lemon.helper.ItemTouchHelperViewHolder;
import taxi.lemon.models.RouteItem;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = RouteAdapter.class.getSimpleName();
    private ArrayList<RouteItem> route;
    private OnEditButtonClicked mEditButtonClickListener;
    private OnDataChanged mDataChangedListener;
    private Handler mHandler;

    public interface OnEditButtonClicked {
        void clicked(int position);
    }

    public RouteAdapter(ArrayList<RouteItem> route, OnEditButtonClicked  mEditButtonClickListener) {
        this.route = route;
        this.mEditButtonClickListener = mEditButtonClickListener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText((position < 1) ? R.string.ead_title_start_address : R.string.ead_title_end_address);
        if(route.get(position).getNumber() == null) {
            holder.tvAddress.setText(route.get(position).getStreet());
        } else {
            holder.tvAddress.setText(route.get(position).getAddress());
        }
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditButtonClickListener.clicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return route.size();
    }

    /**
     * Set listener of data changing state
     * @param onDataChanged - data change listener
     */
    public void setDataChangedListener(OnDataChanged onDataChanged) {
        mDataChangedListener = onDataChanged;
    }

    /**
     * Add an item to data array and notify that data has been changed
     * @param newItem - new route item which will be added to data array
     */
    public void addItem(RouteItem newItem) {
        Log.d("ADAPTER", "addItem");
        route.add(newItem);
        notifyItemInserted(route.size() - 1);
        mDataChangedListener.dataChanged();
    }

    /**
     * Update item
     * @param newItem - new route item which will be updated in data array
     * @param position - position of updated item
     */
    public void updateItem(RouteItem newItem, int position) {
        route.set(position, newItem);
        notifyItemChanged(position);
        mDataChangedListener.dataChanged();
    }

    /**
     * Get route
     * @return route list
     */
    public ArrayList<RouteItem> getRoute() {
        return route;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, "onItemMove() called with: " + "fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "]");
        Collections.swap(route, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDelete(int position) {
        route.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, route.size());
        mDataChangedListener.dataChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public Context context;
        private CardView card;
        public TextView tvTitle;
        public TextView tvAddress;
        public Button btnEdit;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            card = (CardView) v.findViewById(R.id.card);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            btnEdit = (Button) v.findViewById(R.id.btn_edit_route_item);
        }

        @Override
        public void onItemSelected() {
            card.setCardBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                    mDataChangedListener.dataChanged();
                    card.setCardBackgroundColor(Color.WHITE);
                }
            });
        }
    }
}
