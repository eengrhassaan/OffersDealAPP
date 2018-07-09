
package dzinexperts.com.offers.apis;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageOffersData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("totaloffers")
    @Expose
    private Integer totaloffers;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("data")
    @Expose
    private List<DatumImg> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotaloffers() {
        return totaloffers;
    }

    public void setTotaloffers(Integer totaloffers) {
        this.totaloffers = totaloffers;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<DatumImg> getData() {
        return data;
    }

    public void setData(List<DatumImg> data) {
        this.data = data;
    }

}
