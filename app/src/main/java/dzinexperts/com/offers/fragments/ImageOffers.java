package dzinexperts.com.offers.fragments;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.adapter.ImageOffersAdapter;
import dzinexperts.com.offers.apis.DatumImg;
import dzinexperts.com.offers.apis.ImageOffersData;
import dzinexperts.com.offers.apis.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LENOVO on 3/20/2018.
 */

public class ImageOffers extends android.support.v4.app.Fragment {

    boolean result;
    private Context context;
    private ImageView ivImage;
    private GetSetToken getSetToken;
    private ProgressBar progressBar;
    private int id=0,currentItems,totalitems,scrolledOutItems,currentpage=0,totalpage=0;
    private String TAG = "ImageOffers Fragment";
    private boolean isScrolling = false;
    private String token;
    List<DatumImg> datumList = new ArrayList<>();
    //Additional Variables
    RecyclerView recyclerView;
    Uri uri;
    private GridLayoutManager lLayout;
    ImageOffersAdapter imageOffersAdapter;

    public static ImageOffers newInstance() {
        ImageOffers fragment = new ImageOffers();
        return fragment;
    }

//    Default Constructor
    public ImageOffers(){
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.discountoffers_display, container, false);
        context = getActivity();
        getSetToken = new GetSetToken(context);
        token = getSetToken.getAuthToken();
        id = Integer.parseInt(getSetToken.getUserID());
        progressBar = (ProgressBar) rootView.findViewById(R.id.discoffers_progress);
        uri = Uri.parse("");
        lLayout = new GridLayoutManager(context, 1);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.disc_recycler);

        imageOffersAdapter = new ImageOffersAdapter(datumList,context);

        recyclerView.setAdapter(imageOffersAdapter);
        recyclerView.setLayoutManager(lLayout);
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

                if(isScrolling && ( scrolledOutItems == totalitems )){
                    isScrolling = false;
                    if (currentpage<totalpage-1) {
                        currentpage++;
                        try {
                            getData();
                        } catch (Exception ex){
                            Log.d(TAG,"Exception: "+ex.getMessage().toString());
                        }
                    }
                }
            }
        });
        getData();
        return rootView;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        retrofit2.Call<ImageOffersData> getImageoffersdata = LoginApi.login().getImageOffers(token,id,currentpage);
        getImageoffersdata.enqueue(new Callback<ImageOffersData>() {
            @Override
            public void onResponse(Call<ImageOffersData> call, Response<ImageOffersData> response) {
                ImageOffersData imageOffersData = response.body();
                if(imageOffersData.getStatus()==200){
                    totalpage = imageOffersData.getPages();
                    datumList.addAll(imageOffersData.getData());
                    imageOffersAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(context,"Error from Server"+ imageOffersData.getStatus().toString(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ImageOffersData> call, Throwable t) {
                Toast.makeText(context,"Error From Server",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
