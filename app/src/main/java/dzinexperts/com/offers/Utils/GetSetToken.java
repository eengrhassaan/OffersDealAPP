package dzinexperts.com.offers.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by LENOVO on 3/19/2018.
 */

public class GetSetToken {
    String userIDTag = "User ID";
    String userID = "";
    String authTag = "Authentication Token";
    String authToken = "";
    Context context;
    SharedPreferences sharedPreferences;

    public GetSetToken(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getAuthToken() {
        if(sharedPreferences.getString(authTag,"").isEmpty()){
            return "";
        } else{
            authToken = sharedPreferences.getString(authTag,"");
            return authToken;
        }
    }

    public String getUserID() {
        if(sharedPreferences.getString(userIDTag,"").isEmpty()){
            return "0";
        } else{
            userID = sharedPreferences.getString(userIDTag,"");
            return userID;
        }
    }
    public void setAuthToken(String authToken, String userID) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(authTag,authToken);
        editor.putString(userIDTag,userID);
        editor.apply();
        this.authToken = authToken;
        this.userID = userID;
    }
}
