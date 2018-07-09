package dzinexperts.com.offers.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LENOVO on 3/18/2018.
 */

public class LoginApi {
    private static String baseUrl = "http://dzinexperts.com/";
//    private static String baseUrl = "http://192.168.0.114:82/offers/";

    private String Client_Service = "frontend-client";
    private String Auth_key = "simplerestapi";
    private String Content_type = "application/x-www-form-urlencoded";
    public static Apis apis = null;

    public static Apis login(){
        if(apis == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apis = retrofit.create(Apis.class);
        }
        return apis;
    }


}
