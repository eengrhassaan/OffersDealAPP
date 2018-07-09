
package dzinexperts.com.offers.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("total_offers")
    @Expose
    private Integer totalOffers;
    @SerializedName("brochures")
    @Expose
    private Integer brochures;
    @SerializedName("deals")
    @Expose
    private Integer deals;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(Integer totalOffers) {
        this.totalOffers = totalOffers;
    }

    public Integer getBrochures() {
        return brochures;
    }

    public void setBrochures(Integer brochures) {
        this.brochures = brochures;
    }

    public Integer getDeals() {
        return deals;
    }

    public void setDeals(Integer deals) {
        this.deals = deals;
    }

}
