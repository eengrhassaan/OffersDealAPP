package dzinexperts.com.offers.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.Utils.ProgressRequestBody;
import dzinexperts.com.offers.Utils.Utility;
import dzinexperts.com.offers.apis.DiscountOffersPost;
import dzinexperts.com.offers.apis.LoginApi;
import dzinexperts.com.offers.fragments.DiscountOffers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscountOffersActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        ProgressRequestBody.UploadCallbacks {
    //Crashlytics Reporting
    private CrashReportingLogging crashReportingLogging;
    private EditText disc_fromdate,disc_todate,disc_item,disc_description,disc_rate,disc_currentRate;
    private String discfromdate,disctodate,discitem,discdescription,discrate,disccurrentRate,token;
    private int id=0;
    private ImageView imageView;
    private Button uploadDiscountOffer, discountOfferImage;
    private Uri uri;
    private Context context;
    private String TAG = "DiscountOffers Fragment";
    private String dirName = "Offers ImageOffers";
    private DatePickerDialog datePickerDialog;
    private int minDate,minYear,minMonth;
    private int dateBox = 1; //1 means from date 2 means to date
    private Calendar myCalendar1,myCalendar2;
    private boolean result;
    private ProgressDialog progressDialog;
    private GetSetToken getSetToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_offers);
        context = DiscountOffersActivity.this;

        //Uri Initialization
        uri = Uri.parse("");
        getSetToken = new GetSetToken(context);
        token = getSetToken.getAuthToken();
        id = Integer.parseInt(getSetToken.getUserID());
        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        result= Utility.checkPermission(context);
//        Edit Text Initialization
        disc_currentRate = (EditText) findViewById(R.id.disc_marketrate);
        disc_rate = (EditText) findViewById(R.id.disc_rate);
        disc_description = (EditText) findViewById(R.id.disc_description);
        disc_fromdate = (EditText) findViewById(R.id.disc_from_date);
        disc_todate = (EditText) findViewById(R.id.disc_to_date);
        disc_item = (EditText) findViewById(R.id.disc_item);

        String fomDates = myCalendar1.getTime().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String datse = format.format(Date.parse(fomDates));
        disc_fromdate.setText(datse);
//        ImageView Initialization
        imageView = (ImageView) findViewById(R.id.disc_img);

//        Buttons Initialization
        uploadDiscountOffer = (Button) findViewById(R.id.upload_disc_offer);
        discountOfferImage = (Button) findViewById(R.id.disc_img_button);

//        Listeners on Buttons
        uploadDiscountOffer.setOnClickListener(this);
        discountOfferImage.setOnClickListener(this);
        disc_fromdate.setOnClickListener(this);
        disc_todate.setOnClickListener(this);
        minDate = myCalendar1.get(Calendar.DAY_OF_MONTH);
        minMonth = myCalendar1.get(Calendar.MONTH);
        minYear = myCalendar1.get(Calendar.YEAR);

        //Set Starting Date
        datePickerDialog = new DatePickerDialog(
                context,DiscountOffersActivity.this, minYear, minMonth, minDate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.disc_img_button:
                selectImage();
                break;
            case R.id.upload_disc_offer:
                uploadDiscountedOffer();
                break;
            case R.id.disc_from_date:
                dateBox = 1;
                datePickerDialog.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
                selectDate();
                break;
            case R.id.disc_to_date:
                dateBox = 2;
                datePickerDialog.getDatePicker().setMinDate(myCalendar2.getTimeInMillis());
                selectDate();
                break;
        }
    }

    private void selectDate() {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int tempMonth = month+1;
        String setDate = dayOfMonth + "/" + tempMonth + "/" + year;
        if(dateBox == 2){
            disc_todate.setText(setDate);
        } else {
            minYear = year;
            minMonth = month;
            minDate = dayOfMonth;
            disc_fromdate.setText(setDate);
            myCalendar2.set(Calendar.YEAR,year);
            myCalendar2.set(Calendar.MONTH,month);
            myCalendar2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        }
    }

    private void uploadDiscountedOffer() {
        if (!validate()){
            onUploadingFailed();
            return;
        }
        uploadingDiscountedOffer();
    }

    private void uploadingDiscountedOffer() {
        File imgFile = new File(uri.getPath());
        ProgressRequestBody fileBody = new ProgressRequestBody(imgFile, this);
        CreateProgressDialog();
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                imgFile);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("filename", imgFile.getName(), RequestBody.create(MediaType.parse("image/*"), imgFile));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("filename", imgFile.getName(), fileBody);
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), imgFile.getName());
        RequestBody fromDates = RequestBody.create(MediaType.parse("text/plain"), discfromdate);
        RequestBody toDates = RequestBody.create(MediaType.parse("text/plain"), disctodate);
        RequestBody itemss = RequestBody.create(MediaType.parse("text/plain"), discitem);
        RequestBody cratess = RequestBody.create(MediaType.parse("text/plain"), disccurrentRate);
        RequestBody dratess = RequestBody.create(MediaType.parse("text/plain"), discrate);
        RequestBody ddescription = RequestBody.create(MediaType.parse("text/plain"), discdescription);


        retrofit2.Call<DiscountOffersPost> postImageOffer = LoginApi.login().uploadDiscountOffers(
                token,
                id,
                filePart,
                titlePart,
                fromDates,
                toDates,
                ddescription,
                cratess,
                dratess,
                itemss
        );
        postImageOffer.enqueue(new Callback<DiscountOffersPost>() {
            @Override
            public void onResponse(Call<DiscountOffersPost> call, Response<DiscountOffersPost> response) {
                DiscountOffersPost discountOffers = response.body();
                if(discountOffers.getMessage().contains("Data has been created.") && discountOffers.getStatus() == 200){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Uploaded the Discount offer Successfully",Toast.LENGTH_LONG).show();
                    Intent returntoActivity = new Intent(DiscountOffersActivity.this,MainActivity.class);
                    startActivity(returntoActivity);
                    finish();
                } else
                    Toast.makeText(context,"Server Response: "+discountOffers.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<DiscountOffersPost> call, Throwable t) {
                Toast.makeText(context,"Failed: Unexpected Response from Server: \n" +t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void CreateProgressDialog() {
        progressDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading....");
        progressDialog.setMessage("Uploading Discount Offer\nPlease Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    private void onUploadingFailed() {
        Toast.makeText(context,"Unable to Upload Discount Offer",Toast.LENGTH_LONG).show();
        uploadDiscountOffer.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        discitem = disc_item.getText().toString();
        disctodate = disc_todate.getText().toString();
        discfromdate = disc_fromdate.getText().toString();
        discdescription = disc_description.getText().toString();
        discrate = disc_rate.getText().toString();
        disccurrentRate = disc_currentRate.getText().toString();

        if(uri.getPath().isEmpty()){
            valid =false;
            Toast.makeText(context,"Please Select an Image for the Discount Offer",Toast.LENGTH_LONG).show();
        }

        if(discitem.isEmpty()){
            disc_item.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_item.setError(null);
        }

        if(disctodate.isEmpty()){
            disc_todate.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_todate.setError(null);
        }

        if(discfromdate.isEmpty()){
            disc_fromdate.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_fromdate.setError(null);
        }

        if(discdescription.isEmpty()){
            disc_description.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_description.setError(null);
        }

        if(discrate.isEmpty()){
            disc_rate.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_rate.setError(null);
        }

        if(disccurrentRate.isEmpty()){
            disc_currentRate.setError("This Field Can't be Empty");
            valid = false;
        } else {
            disc_currentRate.setError(null);
        }
        return valid;
    }

    private void selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                onCaptureImageResult(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void onCaptureImageResult(Uri data) {
        Bitmap thumbnail = null;
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
        try {
            thumbnail = (Bitmap) MediaStore.Images.Media.getBitmap(context.getContentResolver(),data);
        } catch (IOException ex) {
            crashReportingLogging.reporting(TAG,ex);

        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/"+dirName+"/",
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Toast.makeText(context,destination.getPath().toString(),Toast.LENGTH_LONG).show();
            setImagetoImageView(thumbnail,destination);
            //            ivImage.setImageBitmap(thumbnail);
        } catch (FileNotFoundException ex) {
            crashReportingLogging.reporting(TAG,ex);
        } catch (IOException ex) {
            crashReportingLogging.reporting(TAG,ex);
        }

    }

    private void setImagetoImageView(Bitmap thumbnail, File destination) {
        uri = Uri.parse(destination.getPath());
//        imageView.setImageBitmap(thumbnail);
        Glide.with(context)
                .load(thumbnail)
                .into(imageView);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        progressDialog.setTitle("Error");
        progressDialog.setMessage("Unable to Upload Discount Offer");
    }

    @Override
    public void onFinish() {
        progressDialog.setProgress(100);
        progressDialog.dismiss();
    }
}
