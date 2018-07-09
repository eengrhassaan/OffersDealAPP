
package dzinexperts.com.offers.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("camp_id")
    @Expose
    private String campId;
    @SerializedName("disc_id")
    @Expose
    private String discId;
    @SerializedName("disc_img")
    @Expose
    private String discImg;
    @SerializedName("disc_item")
    @Expose
    private String discItem;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("disc_rate")
    @Expose
    private String discRate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("disc_fdate")
    @Expose
    private String discFdate;
    @SerializedName("disc_tdate")
    @Expose
    private String discTdate;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getDiscId() {
        return discId;
    }

    public void setDiscId(String discId) {
        this.discId = discId;
    }

    public String getDiscImg() {
        return discImg;
    }

    public void setDiscImg(String discImg) {
        this.discImg = discImg;
    }

    public String getDiscItem() {
        return discItem;
    }

    public void setDiscItem(String discItem) {
        this.discItem = discItem;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscRate() {
        return discRate;
    }

    public void setDiscRate(String discRate) {
        this.discRate = discRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscFdate() {
        return discFdate;
    }

    public void setDiscFdate(String discFdate) {
        this.discFdate = discFdate;
    }

    public String getDiscTdate() {
        return discTdate;
    }

    public void setDiscTdate(String discTdate) {
        this.discTdate = discTdate;
    }

}
