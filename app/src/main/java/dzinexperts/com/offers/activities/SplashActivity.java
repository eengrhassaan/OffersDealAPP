package dzinexperts.com.offers.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.io.File;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.GetSetToken;

/**
 * Created by LENOVO on 3/15/2018.
 */

public class SplashActivity extends Activity {

    private GetSetToken getSetToken;
    private Context context;
    private String dirName = "Offers ImageOffers";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        context = getBaseContext();
        getSetToken = new GetSetToken(context);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainAtivity = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainAtivity);
                finish();
            }
        }, 1500);
    }
}
