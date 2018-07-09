
package dzinexperts.com.offers.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageOffersPost {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("DB_response")
    @Expose
    private DBResponse dBResponse;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DBResponse getDBResponse() {
        return dBResponse;
    }

    public void setDBResponse(DBResponse dBResponse) {
        this.dBResponse = dBResponse;
    }

}
