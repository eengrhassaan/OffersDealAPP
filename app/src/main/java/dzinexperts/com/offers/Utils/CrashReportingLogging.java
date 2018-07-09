package dzinexperts.com.offers.Utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by LENOVO on 3/22/2018.
 */

public class CrashReportingLogging {

    public void reporting (String TAG,Exception ex){
        Log.d(TAG,ex.getMessage().toString());
        Crashlytics.logException(ex);
    }

}
