package dzinexperts.com.offers.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
import com.github.clans.fab.FloatingActionButton;

import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.BackgroundToForegroundTransformer;
import com.eftimoff.viewpagertransformers.DepthPageTransformer;
import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.eftimoff.viewpagertransformers.ParallaxPageTransformer;
import com.eftimoff.viewpagertransformers.RotateDownTransformer;
import com.eftimoff.viewpagertransformers.ZoomInTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutTranformer;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.apis.LoggedOut;
import dzinexperts.com.offers.apis.LoginApi;
import dzinexperts.com.offers.fragments.Dashboard;
import dzinexperts.com.offers.fragments.DiscountOffers;
import dzinexperts.com.offers.fragments.ImageOffers;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Context context;
    CrashReportingLogging crashReportingLogging;
    //    ServiceFactory serviceFactory;
    private boolean statsfab = false;
    //Variables
    private String dirName = "Offers ImageOffers";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FloatingActionButton fab_imageoffer,fab_discountoffer;
    private FloatingActionMenu floatingActionMenu;
    private GetSetToken getSetToken;
    private String authToken;
    private String userId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    ;
    private ProgressDialog progressDialog;
    //ViewPager Variable
    private ViewPager mViewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //Methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        getSetToken = new GetSetToken(context);
        authToken = getSetToken.getAuthToken();
        userId = getSetToken.getUserID();
        setContentView(R.layout.activity_main);
        //Check if Drawer Present
        if (ifDrawerPresent()) {
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
//            toolbar.setTitleTextAppearance(getApplicationContext(),R.style.textbold);
            //            Setup the viewpager with section adapter
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setPageTransformer(true,new ZoomOutTranformer());

            tabLayout.setupWithViewPager(mViewPager);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            fab_imageoffer = (FloatingActionButton) findViewById(R.id.fab_image_offer);
            fab_discountoffer = (FloatingActionButton) findViewById(R.id.fab_discount_offer);
            floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu);
            floatingActionMenu.setClosedOnTouchOutside(true);
            floatingActionMenu.setOnClickListener(this);
            fab_imageoffer.setOnClickListener(this);
            fab_discountoffer.setOnClickListener(this);
        }
    }


    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            loggingout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    Logging out function
    private void loggingout() {
        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging Out. Please Wait....");
        progressDialog.show();
        retrofit2.Call<LoggedOut> loggedOutCall = LoginApi.login().logOut(authToken,Integer.parseInt(userId));
        loggedOutCall.enqueue(new Callback<LoggedOut>() {
            @Override
            public void onResponse(retrofit2.Call<LoggedOut> call, Response<LoggedOut> response) {
                LoggedOut loggedOut = response.body();
                if(loggedOut.getStatus() == 200){
                    progressDialog.dismiss();
                    Toast.makeText(context,loggedOut.getMessage().toString(),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    getSetToken.setAuthToken("","");
                    startActivity(i);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, loggedOut.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoggedOut> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.dashboard_drawer) {
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.brochurelist_drawer) {
            mViewPager.setCurrentItem(2);
        } else if (id == R.id.brochureupload_drawer) {
            uploadBrochureActivity();
        } else if (id == R.id.dealgrid_drawer) {
            mViewPager.setCurrentItem(1);
        } else if (id == R.id.uploaddeals_drawer) {
            uploadDiscountDealsActivity();
        } else if (id == R.id.settings_drawer){

        } else if (id == R.id.aboutus_drawer) {

        } else if (id == R.id.signout_drawer) {
            loggingout();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean ifDrawerPresent() {
        return findViewById(R.id.drawer_layout) != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_image_offer:
                uploadBrochureActivity();
                break;

            case R.id.fab_discount_offer:
                uploadDiscountDealsActivity();
                break;

            case R.id.menu:
                if(statsfab){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        floatingActionMenu.setForeground(getApplication().getDrawable(R.mipmap.ic_uploads_main_close_56));
                    }
                } else
                    floatingActionMenu.setForeground(getApplication().getDrawable(R.mipmap.ic_uploads_main_open_56));
                statsfab = !statsfab;
                break;
        }
    }

    private void uploadDiscountDealsActivity() {
        Intent discoffer = new Intent(MainActivity.this,DiscountOffersActivity.class);
        startActivity(discoffer);
    }

    private void uploadBrochureActivity() {
        Intent imgoffer = new Intent(MainActivity.this,ImageOffersActivity.class);
        startActivity(imgoffer);
    }

    //    View Pager Class
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return Dashboard.newInstance();
                case 1:
                    return DiscountOffers.newInstance();
                case 2:
                    return ImageOffers.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Dashboard";
                case 1:
                    return "Deals";
                case 2:
                    return "Brochures";
                default:
                    return "";
            }
        }
    }
}
