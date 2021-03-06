package taxi.lemon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import taxi.lemon.R;
import taxi.lemon.models.HistoryItem;
import taxi.lemon.models.RouteItem;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<HistoryItem> history;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void clicked(HistoryItem item);

        void deleteItemHistory(HistoryItem mItem);
    }

    public HistoryAdapter(ArrayList<HistoryItem> history, OnItemClickListener listener) {
        this.history = history;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final HistoryItem historyItem = history.get(position);

        holder.tvStatus.setText(historyItem.getExecutionStatus());
        holder.tvCost.setText(historyItem.getCost());
        String route = "";
        for (RouteItem item : historyItem.getRoute()) {
            if (item.getNumber() != null) {
                route += item.getStreet() + " " + item.getNumber() + "\n";
            } else {
                route += item.getStreet() + "\n";
            }
        }
        holder.tvRoute.setText(route);
        holder.tvDate.setText(historyItem.getDate());
        holder.btnRemovOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteItemHistory(historyItem);
            }
        });
        holder.btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clicked(historyItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public HistoryItemView mHistoryItem;
//        public ViewHolder(HistoryItemView v) {
//            super(v);
//            mHistoryItem = v;
//        }

        public TextView tvStatus;
        public TextView tvCost;
        public TextView tvRoute;
        public TextView tvDate;
        public Button btnMakeOrder,
                btnRemovOrder;

        public ViewHolder(View v) {
            super(v);

            tvStatus = (TextView) v.findViewById(R.id.tv_status);
            tvCost = (TextView) v.findViewById(R.id.tv_cost);
            tvRoute = (TextView) v.findViewById(R.id.tv_route);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            btnMakeOrder = (Button) v.findViewById(R.id.btn_make_order);
            btnRemovOrder = (Button) v.findViewById(R.id.btn_remove_order);
        }
    }

    public ArrayList<HistoryItem> getHistory() {
        return history;
    }
}
