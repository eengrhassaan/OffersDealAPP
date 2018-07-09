package dzinexperts.com.offers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.adapter.DashboardAdapter;
import dzinexperts.com.offers.apis.DashboardData;
import dzinexperts.com.offers.apis.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends android.support.v4.app.Fragment {
    //Crashlytics Reporting
    private CrashReportingLogging crashReportingLogging;
    private String token;
    private ProgressBar progressBar;
    private int id=0,currentItems,totalitems,scrolledOutItems,currentpage=0,totalpage=0;
    private Context context;
    private String TAG = "Dashboard Fragment";
    private String dirName = "Offers ImageOffers";
    private GetSetToken getSetToken;
    private boolean isScrolling = false;
    List datumList = new ArrayList<>();
    ArrayList list;
    private FloatingActionButton fab_brochure;
    private GridLayoutManager lLayout;
    RecyclerView recyclerView;
    DashboardAdapter dashboardAdapter;
    private Boolean isDataLoaded = false;

    public static Dashboard newInstance() {
        Dashboard fragment = new Dashboard();
        return fragment;
    }

    //    Default Constructor
    public Dashboard(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_layout, container, false);
        context = getActivity();
        fab_brochure = (FloatingActionButton) getActivity().findViewById(R.id.fab_image_offer);
        getSetToken = new GetSetToken(context);
        token = getSetToken.getAuthToken();
        id = Integer.parseInt(getSetToken.getUserID());
        progressBar = (ProgressBar) rootView.findViewById(R.id.dash_progress);

        lLayout = new GridLayoutManager(context, 2);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.dash_recycler);
        dashboardAdapter = null;
        dashboardAdapter = new DashboardAdapter(datumList,context);
        recyclerView.setAdapter(dashboardAdapter);
        recyclerView.setLayoutManager(lLayout);
        if(!isDataLoaded)
            getData();
        return rootView;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        retrofit2.Call<DashboardData> dashboardDataCall = LoginApi.login().getCoorporateData(token,id);
        dashboardDataCall.enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                DashboardData dashboardData = response.body();
                progressBar.setVisibility(View.INVISIBLE);
                if(dashboardData.getStatus()==200){
                    if(dashboardData.getBrochures()<1)
                        fab_brochure.setEnabled(true);
                    else
                        fab_brochure.setEnabled(false);
                    datumList.add(dashboardData);
                    datumList.add(dashboardData);
                    datumList.add(dashboardData);
                    isDataLoaded = true;
                    dashboardAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(context,"Error from Server: "+dashboardData.getStatus(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"Error while Getting Data from Server: "+t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
