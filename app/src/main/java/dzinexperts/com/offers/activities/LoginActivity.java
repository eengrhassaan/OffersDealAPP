package dzinexperts.com.offers.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

import dzinexperts.com.offers.R;

/**
 * Created by LENOVO on 3/16/2018.
 */

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.BindView;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.Utils.TypefaceUtil;
import dzinexperts.com.offers.Utils.Utility;
import dzinexperts.com.offers.apis.Apis;
import dzinexperts.com.offers.apis.LoginApi;
import dzinexperts.com.offers.apis.LoginApiGetSet;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    //Text and Flags Variables
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    CrashReportingLogging crashReportingLogging;
    //Control Variables
    private String email;
    private String password;
    private ProgressDialog progressDialog;
    private GetSetToken getSetToken;
    private Context context;
    private String dirName = "Offers ImageOffers";

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getBaseContext();

//        Crashlytics.getInstance().crash();
        getSetToken = new GetSetToken(context);
        if(Utility.checkPermission(this)){
            createFolder();
        }

        if (!getSetToken.getAuthToken().isEmpty()){
            loggedin(getSetToken.getAuthToken());
        }

        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void createFolder() {
        try{
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + dirName);
            boolean success = true;
            if(!folder.exists()){
                if(folder.mkdir()) {
                    System.out.println("Folder created");
                } else {
                    System.out.println("Folder is not created");
                }
            } else
                System.out.print("Already Existed");

        } catch (Exception ex){
            Log.d(TAG,ex.getMessage());
        }
    }

    public void login() {
        Log.d(TAG, "Login");
        if(Utility.checkPermission(this)){
            createFolder();
            if (!validate()) {
                onLoginFailed();
                return;
            }
            _loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            email = _emailText.getText().toString();
            password = _passwordText.getText().toString();

            // TODO: Implement your own authentication logic here.
            RequestBody body = new FormBody.Builder()
                    .add("username", email)
                    .add("password", password)
                    .build();

            retrofit2.Call<LoginApiGetSet> token = LoginApi.login().getToken(body);
            token.enqueue(new Callback<LoginApiGetSet>() {
                @Override
                public void onResponse(retrofit2.Call<LoginApiGetSet> call, Response<LoginApiGetSet> response) {
                    LoginApiGetSet loginApiGetSet = response.body();
                    if (loginApiGetSet.getMessage().toString().equalsIgnoreCase("Successfully login.")) {
                        progressDialog.dismiss();
                        getSetToken.setAuthToken(loginApiGetSet.getToken().toString(),loginApiGetSet.getId().toString());
                        loggedin(loginApiGetSet.getId().toString());
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error: " + loginApiGetSet.getMessage(), Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LoginApiGetSet> call, Throwable t) {
                    try {
                        Toast.makeText(getBaseContext(),"Failed:"+t.getMessage().toString(),Toast.LENGTH_LONG).show();
                    } catch (Exception ex){
                        crashReportingLogging.reporting(TAG,ex);
                        Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_LONG).show();
                    };
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                }
            });
        } else
            Toast.makeText(context,"Please first Give Permission to access the Camera and Gallery",Toast.LENGTH_LONG).show();
    }

    private void loggedin(String authToken) {
        Intent t = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(t);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
//        _loginButton.setEnabled(true);
//        finish();

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        if (email.isEmpty()){
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
