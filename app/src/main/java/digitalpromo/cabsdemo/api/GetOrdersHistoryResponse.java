package digitalpromo.cabsdemo.api;

import java.util.ArrayList;

import digitalpromo.cabsdemo.models.HistoryItem;

/**
 * Response for get orders history request
 */
public class GetOrdersHistoryResponse extends BaseResponse {
    /**
     * Array with orders history
     */
    private ArrayList<HistoryItem> history;

    /**
     * Get order's history
     * @return array of orders history
     */
    public ArrayList<HistoryItem> getHistory() {
        return history;
    }
}
