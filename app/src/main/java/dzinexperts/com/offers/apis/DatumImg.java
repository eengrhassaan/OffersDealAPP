
package dzinexperts.com.offers.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumImg {

    @SerializedName("camp_id")
    @Expose
    private String campId;
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("offer_img")
    @Expose
    private String offerImg;
    @SerializedName("offer_fdate")
    @Expose
    private String offerFdate;
    @SerializedName("offer_tdate")
    @Expose
    private String offerTdate;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }

    public String getOfferFdate() {
        return offerFdate;
    }

    public void setOfferFdate(String offerFdate) {
        this.offerFdate = offerFdate;
    }

    public String getOfferTdate() {
        return offerTdate;
    }

    public void setOfferTdate(String offerTdate) {
        this.offerTdate = offerTdate;
    }

}
