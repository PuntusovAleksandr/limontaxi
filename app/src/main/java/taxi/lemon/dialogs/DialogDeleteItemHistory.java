package taxi.lemon.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import taxi.lemon.R;
import taxi.lemon.models.HistoryItem;

/**
 * Created by AleksandrP on 17.08.2016.
 */
public class DialogDeleteItemHistory extends AlertDialog {

    private Context mContext;
    private HistoryItem item;
    private ListenerDeleteHistoryItem mHistoryItem;


    public DialogDeleteItemHistory(Context mContext, HistoryItem mItem, ListenerDeleteHistoryItem mHistoryItem1) {
        super(mContext);
        // Init dialog view
        LayoutInflater inflater = getLayoutInflater();
        this.mContext = mContext;
        this.item = mItem;
        this.mHistoryItem = mHistoryItem1;

        View view = inflater.inflate(R.layout.context_make_in_out, null);

        initUi(view);
    }

    private void initUi(View view) {

        view.findViewById(R.id.bt_ok_count).setOnClickListener(listener);
        view.findViewById(R.id.bt_cancel_count).setOnClickListener(listener);

        this.setView(view);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.bt_ok_count:
                    mHistoryItem.deleteFromList(item);
                    cancel();
                    break;

                case R.id.bt_cancel_count:
                    cancel();
                    break;
            }
        }
    };

    public interface ListenerDeleteHistoryItem {
        void deleteFromList(HistoryItem mItem);
    }

}
