package taxi.lemon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import taxi.lemon.R;
import taxi.lemon.models.ProfileItem;

/**
 *
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private static final String TAG = ProfileAdapter.class.getSimpleName();

    private ArrayList<ProfileItem> profile;

    public ProfileAdapter(ArrayList<ProfileItem> profile) {
        this.profile = profile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfileItem item = profile.get(position);

        switch (item.getTitle()) {
            case LOGIN:
                holder.title.setText(R.string.pf_login);
                break;
            case NAME:
                holder.title.setText(R.string.pf_fio);
                break;
            case PHONE:
                holder.title.setText(R.string.pf_phone);
                break;
            case BALANCE:
                holder.title.setText(R.string.pf_balance);
                break;
            case ADDRESS:
                holder.title.setText(R.string.pf_address);
                break;
            case ORDERS_COUNT:
                holder.title.setText(R.string.pf_orders_count);
                break;
            case DISCOUNT:
                holder.title.setText(R.string.pf_discount);
                break;
            case PAYMENT_TYPE:
                holder.title.setText(R.string.pf_payment_type);
                break;
            case BONUSES:
                holder.title.setText(R.string.pf_bonuses);
                break;
            case ENTRANCE:
                holder.title.setText(R.string.pf_entrance);
        }

        holder.content.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return profile.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    /**
     * Add profile item to array
     * @param profile new profile items list
     */
    public void updateData(ArrayList<ProfileItem> profile) {
        this.profile = profile;

        notifyDataSetChanged();
    }
}
