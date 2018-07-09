package dzinexperts.com.offers.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.Utils.ProgressRequestBody;
import dzinexperts.com.offers.Utils.Utility;
import dzinexperts.com.offers.activities.MainActivity;
import dzinexperts.com.offers.adapter.DiscountOffersAdapter;
import dzinexperts.com.offers.apis.Datum;
import dzinexperts.com.offers.apis.DiscountDealsDisplay;
import dzinexperts.com.offers.apis.DiscountOffersPost;
import dzinexperts.com.offers.apis.ImageOffersPost;
import dzinexperts.com.offers.apis.LoginApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LENOVO on 3/20/2018.
 */

public class DiscountOffers extends android.support.v4.app.Fragment {
    private GridLayoutManager lLayout;
    //Crashlytics Reporting
    private CrashReportingLogging crashReportingLogging;
    private String token;
    private ProgressBar progressBar;
    private int id=0,currentItems,totalitems,scrolledOutItems,currentpage=0,totalpage=0;
    private Uri uri;
    private Context context;
    private String TAG = "DiscountOffers Fragment";
    private ProgressDialog progressDialog;
    private GetSetToken getSetToken;
    private boolean isScrolling = false;
    List<Datum> datumList = new ArrayList<>();
    ArrayList list;

    //Additional Variables
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    DiscountOffersAdapter discountOffersAdapter;

    public static DiscountOffers newInstance() {
        DiscountOffers fragment = new DiscountOffers();
        return fragment;
    }

    //    Default Constructor
    public DiscountOffers(){
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.discountoffers_display, container, false);
        context = getActivity();
        getSetToken = new GetSetToken(context);
        token = getSetToken.getAuthToken();
        id = Integer.parseInt(getSetToken.getUserID());
        progressBar = (ProgressBar) rootView.findViewById(R.id.discoffers_progress);
        uri = Uri.parse("");
        lLayout = new GridLayoutManager(context, 2);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.disc_recycler);

        discountOffersAdapter = new DiscountOffersAdapter(datumList,context);
        recyclerView.setAdapter(discountOffersAdapter);
        recyclerView.setLayoutManager(lLayout);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if( newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = lLayout.getChildCount();
                totalitems = lLayout.getItemCount();
                lLayout.findLastCompletelyVisibleItemPosition();
                scrolledOutItems = lLayout.findFirstVisibleItemPosition();

                if(isScrolling && ( currentItems+scrolledOutItems == totalitems )){
                    isScrolling = false;
                    if (currentpage<totalpage-1) {
                        currentpage++;
                        getData();
                    }
                }
            }
        });
        getData();
        return rootView;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        retrofit2.Call<DiscountDealsDisplay> getDiscountDeals = LoginApi.login().getDiscountDeals(token,id,currentpage);
        getDiscountDeals.enqueue(new Callback<DiscountDealsDisplay>() {
            @Override
            public void onResponse(Call<DiscountDealsDisplay> call, Response<DiscountDealsDisplay> response) {
                DiscountDealsDisplay discountDealsDisplay = response.body();

                if(discountDealsDisplay.getStatus()==200){
                    totalpage = discountDealsDisplay.getPages();
                    datumList.addAll(discountDealsDisplay.getData());
                    discountOffersAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(context,"Error from Server"+ discountDealsDisplay.getStatus().toString(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DiscountDealsDisplay> call, Throwable t) {
                Toast.makeText(context,"Error From Server",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}