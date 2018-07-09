package dzinexperts.com.offers.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.CrashReportingLogging;
import dzinexperts.com.offers.Utils.GetSetToken;
import dzinexperts.com.offers.Utils.ProgressRequestBody;
import dzinexperts.com.offers.Utils.Utility;
import dzinexperts.com.offers.apis.ImageOffersPost;
import dzinexperts.com.offers.apis.LoginApi;
import dzinexperts.com.offers.fragments.ImageOffers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageOffersActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ProgressRequestBody.UploadCallbacks, View.OnLongClickListener  {
    private byte[] imgByte;
    private ProgressDialog progressDialog;
    private String id, token;
    private int numOfImages = 0;
    private Uri[] uris;
    private String dirName = "Offers ImageOffers";
    private ProgressBar progressBar;
    private String TAG = "Image Offers";
    private EditText from_date;
    private EditText to_date;
    private Calendar myCalendar1,myCalendar2;
    private FragmentActivity myContext;
    private DatePickerDialog datePickerDialog;
    private int minDate,minYear,minMonth;
    private int dateBox = 1; //1 means from date 2 means to date
    private CrashReportingLogging crashReportingLogging;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button cam_button,upload_img_offer;
    boolean result;
    private Context context;
    private ImageView ivImage,ivImage1,ivImage2,ivImage3;
    private String fromDate,toDate,uri_img;
    private GetSetToken getSetToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_offers);
        uris = new Uri[4];
        uris[0] = Uri.parse("");
        uris[1] = Uri.parse("");
        uris[2] = Uri.parse("");
        uris[3] = Uri.parse("");
        context = ImageOffersActivity.this;

        getSetToken = new GetSetToken(context);
        token = getSetToken.getAuthToken();
        id = getSetToken.getUserID();

        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        result= Utility.checkPermission(context);

        //        Dates Initialization
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);

        //        ImageViews Initialization
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);
//        ivImage.setOnLongClickListener(this);

        //        Camera Buttons Initialization
//        cam_button = (Button) findViewById(R.id.cam_button);
        upload_img_offer = (Button) findViewById(R.id.upload_imgoffer);
//        cam_button.setOnClickListener(this);
        upload_img_offer.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        ivImage1.setOnClickListener(this);
        ivImage2.setOnClickListener(this);
        ivImage3.setOnClickListener(this);


        String fomDates = myCalendar1.getTime().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String datse = format.format(Date.parse(fomDates));
        from_date.setText(datse);
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        minDate = myCalendar1.get(Calendar.DAY_OF_MONTH);
        minMonth = myCalendar1.get(Calendar.MONTH);
        minYear = myCalendar1.get(Calendar.YEAR);

        //Set Starting Date
        datePickerDialog = new DatePickerDialog(
                ImageOffersActivity.this,ImageOffersActivity.this, minYear, minMonth, minDate);
    }

    private void selectImage() {
        if(numOfImages<=3)
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
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
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
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            imgByte = bytes.toByteArray();
        } catch (IOException ex) {
            crashReportingLogging.reporting(TAG,ex);

        }
        File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/"+dirName+"/",
                "offer"+System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Toast.makeText(context,destination.getPath().toString(),Toast.LENGTH_LONG).show();
            setImagetoImageView(thumbnail,destination);
        } catch (FileNotFoundException ex) {
            crashReportingLogging.reporting(TAG,ex);
        } catch (IOException ex) {
            crashReportingLogging.reporting(TAG,ex);
        }

    }

    private void setImagetoImageView(Bitmap thumbnail,File destination) {
        uris[numOfImages] = Uri.parse(destination.getPath());
        if (numOfImages == 0)
            loadImagetoIVwithBitmap(thumbnail,ivImage);
        else if(numOfImages == 1)
            loadImagetoIVwithBitmap(thumbnail,ivImage1);
        else if(numOfImages == 2)
            loadImagetoIVwithBitmap(thumbnail,ivImage2);
        else if(numOfImages == 3)
            loadImagetoIVwithBitmap(thumbnail,ivImage3);

        if(numOfImages<3)
            numOfImages++;
    }

    private void loadImagetoIVwithBitmap(Bitmap thumbnail, ImageView iv){
        Glide.with(context)
                .load(thumbnail)
                .into(iv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivImage:
                numOfImages = 0;
                selectImage();
                break;

            case R.id.ivImage1:
                numOfImages = 1;
                selectImage();
                break;

            case R.id.ivImage2:
                numOfImages = 2;
                selectImage();
                break;

            case R.id.ivImage3:
                numOfImages = 3;
                selectImage();
                break;

            case R.id.from_date:
                dateBox = 1;
                datePickerDialog.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
                selectDate();
                break;

            case R.id.to_date:
                dateBox = 2;
                datePickerDialog.getDatePicker().setMinDate(myCalendar2.getTimeInMillis());
                selectDate();
                break;

            case R.id.upload_imgoffer:
                uploadImgOffer();
                break;
        }
    }

    private void uploadImgOffer() {
        if(!validate()){
            onUploadingImgOfferFailed();
            return;
        }

        uploadImageOffer();
    }

    private void uploadImageOffer() {
        File imgFile = new File(uri_img);
        ProgressRequestBody fileBody = new ProgressRequestBody(imgFile, this);
        CreateProgressDialog();
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                imgFile);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("filename", imgFile.getName(), RequestBody.create(MediaType.parse("image/*"), imgFile));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("filename", imgFile.getName(), fileBody);
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), imgFile.getName());
        RequestBody fromDates = RequestBody.create(MediaType.parse("text/plain"), fromDate);
        RequestBody toDates = RequestBody.create(MediaType.parse("text/plain"), toDate);
        RequestBody campid = RequestBody.create(MediaType.parse("text/plain"), id);

        retrofit2.Call<ImageOffersPost> postImageOffer = LoginApi.login().uploadImagesTest(
                token,
                Integer.parseInt(id),
                filePart,
                titlePart,
                fromDates,
                toDates,
                campid
        );
        postImageOffer.enqueue(new Callback<ImageOffersPost>() {
            @Override
            public void onResponse(Call<ImageOffersPost> call, Response<ImageOffersPost> response) {
                ImageOffersPost imageOffersPost = response.body();
                if (imageOffersPost.getMessage().contains("Data has been created") && imageOffersPost.getStatus()==200){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Uploaded the Image offer Successfully",Toast.LENGTH_LONG).show();
                    LogMessage(String.valueOf(imageOffersPost.getStatus()));
                    LogMessage((imageOffersPost.getMessage()));
                    Intent returntoActivity = new Intent(ImageOffersActivity.this,MainActivity.class);
                    startActivity(returntoActivity);
                    finish();
                } else
                    Toast.makeText(context,"Server Response: "+imageOffersPost.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ImageOffersPost> call, Throwable t) {
                Toast.makeText(context,"Failed: Unexpected Response from Server\n" +t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void LogMessage(String messages) {
        Log.d(TAG,messages);
    }


    private boolean validate(){
        boolean valid = true;
        fromDate = from_date.getText().toString();
        toDate = to_date.getText().toString();
        uri_img = uris[0].getPath().toString();
        if(fromDate.isEmpty()){
            valid = false;
            from_date.setError("This Field can't be empty");
        } else{
            from_date.setError(null);
        }

        if(toDate.isEmpty()){
            valid = false;
            to_date.setError("This Field can't be empty");
        } else {
            to_date.setError(null);
        }

        if (uri_img.isEmpty()){
            valid = false;
            Toast.makeText(context,"Please select the Brochure Image",Toast.LENGTH_LONG).show();
        }
        return valid;
    }

    private void onUploadingImgOfferFailed() {
    }

    private void selectDate() {
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int tempMonth = month +1;
        String setDate = String.format("%02d", dayOfMonth) + "-" + String.format("%02d",tempMonth) + "-" + year;
        if(dateBox == 2){
            to_date.setText(setDate);
        } else {
            minYear = year;
            minMonth = month;
            minDate = dayOfMonth;
            from_date.setText(setDate);

            myCalendar2.set(Calendar.YEAR,year);
            myCalendar2.set(Calendar.MONTH,month);
            myCalendar2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
        progressDialog.setProgress(100);
    }

    @Override
    public boolean onLongClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        switch (v.getId()){
            case R.id.ivImage:
                removeImageBitmap();
            case R.id.ivImage1:
                removeImageBitmap();
        }
        return false;
    }

    private void removeImageBitmap() {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                context);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void CreateProgressDialog() {
        progressDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading....");
        progressDialog.setMessage("Uploading Image Offer\nPlease Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }
}
