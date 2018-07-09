
package dzinexperts.com.offers.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountOffersPost {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("DB_response_disc")
    @Expose
    private DBResponseDisc dBResponseDisc;

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

    public DBResponseDisc getDBResponseDisc() {
        return dBResponseDisc;
    }

    public void setDBResponseDisc(DBResponseDisc dBResponseDisc) {
        this.dBResponseDisc = dBResponseDisc;
    }

}
