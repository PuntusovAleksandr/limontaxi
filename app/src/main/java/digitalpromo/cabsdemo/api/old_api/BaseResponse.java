package digitalpromo.cabsdemo.api.old_api;

import com.google.gson.annotations.SerializedName;

/**
 * Base class for responses
 */
public class BaseResponse {
    private static final int RESPONSE_OK = 0;

    @SerializedName("response_status")
    private Integer responseStatus = RESPONSE_OK;

    @SerializedName("error_msg")
    private String errorMessage;

    /**
     * Get status of the response
     * @return status of the response
     */
    public Integer getResponseStatus() {
        return responseStatus;
    }

    /**
     * Check whether response is OK
     * @return true if response is OK and false otherwise
     */
    public boolean isOK() {
        return responseStatus == RESPONSE_OK;
    }

    /**
     * Get error message
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }


}
