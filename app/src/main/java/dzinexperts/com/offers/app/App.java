package dzinexperts.com.offers.app;

import android.app.Application;
import com.crashlytics.android.Crashlytics;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.TypefaceUtil;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by LENOVO on 3/15/2018.
 */

public class App extends Application {

    //Variables
    private static App mInstance;

    //Methods
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/hallo_sans_black.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Fabric.with(this, new Crashlytics());
        mInstance = this;
    }

    public static App getInstance() {
        return mInstance;
    }
}
