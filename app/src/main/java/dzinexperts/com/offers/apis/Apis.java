package dzinexperts.com.offers.apis;

import java.io.File;
import java.util.Map;

import dzinexperts.com.offers.Utils.GetSetToken;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by LENOVO on 3/18/2018.
 */

public interface Apis {

    //Login API Interface
    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",

    })
    @POST("offersd/index.php/auth/login")
    Call<LoginApiGetSet> getToken(@Body RequestBody requestBody);

    //Logout API Interface
    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @POST("offersd/index.php/auth/logout")
    Call<LoggedOut> logOut(@Header("Authorization") String authToken,@Header("User-ID") int id );

    //Upload Image Interface
    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @Multipart
    @POST("offersd/index.php/imgoffers/create")
//    Call<ImageOffersPost> uploadImageOffers(@Header("Authorization") String authToken, @Header("id") int id , @Body RequestBody requestBody);
//    Call<ImageOffersPost> uploadImageOffers(@Header("Authorization") String authToken, @Header("id") int id , @Part MultipartBody.Part image, @Part("from_date") String fdate, @Part("to_date") String todate, @Part("camp_id") int ids);
    Call<ImageOffersPost> uploadImageOffers (@Header("Authorization") String authToken, @Part("filename") String title , @Part MultipartBody.Part file, @Part("fromdate") String fdate, @Part("todate") String todate, @Part("campid") String ids);
//    Call<ImageOffersPost> uploadImageOffers (
//            @Header("Authorization") String authToken,
//            @Header("id") int id ,
//            @PartMap Map<String, RequestBody> map
//    );

    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @Multipart
    @POST("offersd/index.php/imgoffers/create2/")
    Call<ImageOffersPost> uploadImagesTest (
            @Header("Authorization") String authToken,
            @Header("User-ID") int id,
            @Part MultipartBody.Part filepart,
            @Part("imagename") RequestBody filename,
            @Part("from_date") RequestBody fromDate,
            @Part("to_date") RequestBody toDate,
            @Part("camp_id") RequestBody campID
            );

    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @Multipart
    @POST("offersd/index.php/discountoffers/create2/")
    Call<DiscountOffersPost> uploadDiscountOffers (
            @Header("Authorization") String authToken,
            @Header("User-ID") int id,
            @Part MultipartBody.Part filepart,
            @Part("imagename") RequestBody filename,
            @Part("disc_fdate") RequestBody fromDate,
            @Part("disc_tdate") RequestBody toDate,
            @Part("descriptions") RequestBody description,
            @Part("disc_rate") RequestBody discRates,
            @Part("rates") RequestBody rates,
            @Part("disc_item") RequestBody items
            );

    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @GET("offersd/index.php/discountoffers/getdiscountoffers/{page}")
    Call<DiscountDealsDisplay> getDiscountDeals(
            @Header("Authorization") String authToken,
            @Header("campid") int id,
            @Path("page") int page
    );


    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @GET("offersd/index.php/imgoffers/getimageoffers/{page}")
    Call<ImageOffersData> getImageOffers(
            @Header("Authorization") String authToken,
            @Header("campid") int id,
            @Path("page") int page
    );

    @Headers({
            "Client-Service: frontend-client",
            "Auth-Key: simplerestapi",
    })
    @GET("offersd/index.php/coorporate/details/")
    Call<DashboardData> getCoorporateData(
            @Header("Authorization") String authToken,
            @Header("campid") int id
    );

}
