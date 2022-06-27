package com.thnopp.it.trans.count;

/**
 * Created by CEVAUser on 5/27/2017.
 */


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import com.thnopp.it.trans.Config;
import com.thnopp.it.trans.Global;
import com.thnopp.it.trans.MainActivity;
import com.thnopp.it.trans.R;
import com.thnopp.it.trans.BuildConfig;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.thnopp.it.trans.ScannedBarcodeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class CaptureVINActivity extends Activity {

    private TextView lblvin, lbldealer, lblCap;

    private Uri mImageCaptureUri;
    ProgressBar bar1;
    String mCurrentPhotoPath;

    private Bitmap bitmap;


    int last_scan;
    private static final int REQUEST_TAKE_PHOTO = 1;
    static final int CAMERA_PIC_REQUEST = 1337;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private final int CAMERA_RESULT = 101;

    private Uri fileUri; // file url to store image/video

    EditText txtMile;

    private Button btnCapturePicture, btnBack,btnComplete;
    String Hdelid,HRef;
    String frmdo, frmtype,frmmsg,form,mile_type;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_mile);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        frmtype = prefs.getString("frmtype","");
        form =  prefs.getString("form","");
        frmmsg = prefs.getString("msg","");
        mile_type = prefs.getString("mile_type","");
        HRef= prefs.getString("hdid","");
        Hdelid = prefs.getString("delid","");

        String countat = prefs.getString("countat","");
        String rlocation = prefs.getString("rlocation","");
        String locatoin = prefs.getString("location","");

        lblCap = (TextView)findViewById(R.id.txtCap);
        lbldealer = (TextView)findViewById(R.id.lbldealer_name);
        lblvin = (TextView)findViewById(R.id.lblvin);
        btnCapturePicture = (Button) findViewById(R.id.btnCap);
        btnBack = (Button) findViewById(R.id.btnBack);

        lblvin.setVisibility(View.GONE);
        lbldealer.setText("นับที่ " + countat);
        bar1 = (ProgressBar) findViewById(R.id.progressBar1);

        img = (ImageView) findViewById(R.id.img1);

        txtMile = (EditText)findViewById(R.id.txtmile);



        lblCap.setText("ถ่ายรุป Plate VIN");
        img.setVisibility(View.INVISIBLE);
        txtMile.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.filePath!=null){
                    File file = new File(Global.filePath);
                    boolean deleted = file.delete();
                }

                Intent intent = new Intent(getApplicationContext(), CountDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else{
            btnCapturePicture.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Global.filePath!=null){
                        File file = new File(Global.filePath);
                        boolean deleted = file.delete();
                    }


                    // capture picture
                    captureImage();





                }
            });


            // Checking camera availability
            if (!isDeviceSupportCamera()) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Your device doesn't support camera",
                        Toast.LENGTH_LONG).show();
                // will close the app if the device does't have camera
                finish();
            }
        }



    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {


                Uri photoURI = FileProvider.getUriForFile(CaptureVINActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } catch (IOException ex) {


                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
       /* File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
*/
        File externalStorageDir =  Environment.getExternalStorageDirectory(); // = Environment.getExternalStorageDirectory();

        File playNumbersDir = new File(externalStorageDir, "Picture");


        if (!playNumbersDir.exists()) {
            if (playNumbersDir.mkdirs()) {
                Log.d(TAG, "Successfully created the parent dir:" + playNumbersDir.getName());
            } else {
                Log.d(TAG, "Failed to create the parent dir:" + playNumbersDir.getName());
            }
        }

        File image = File.createTempFile( HRef + mile_type+
                        imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                playNumbersDir      /* directory */

        /*File image = File.createTempFile( Global.tdelid +
                        imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mCurrentPhotoPath", mCurrentPhotoPath);
        editor.commit();
        return image;
    }


    /**
     * Launching camera app to capture image
     */
    private void captureImage() {

        try {
            if(ContextCompat.checkSelfPermission(CaptureVINActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
            }


        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            mCurrentPhotoPath = prefs.getString("mCurrentPhotoPath","");

            if (mCurrentPhotoPath==null){
                Toast.makeText(getApplicationContext(), "ถ่ายภาพมีปัญหา รบกวนถ่ายใหม่", Toast.LENGTH_LONG).show();
                bar1.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else{
                fileUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(fileUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);
                    //ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }
                resizeImage(fileUri.getPath(), fileUri.getPath());

                launchUploadActivity(true);
            }


        }


    }

    public boolean resizeImage(String originalFilePath, String compressedFilePath) {
        InputStream in = null;
        try {
            in = new FileInputStream(originalFilePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG","originalFilePath is not valid", e);
        }

        if (in == null) {
            return false;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap preview_bitmap = BitmapFactory.decodeStream(in, null, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        preview_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] byteArray = stream.toByteArray();

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(compressedFilePath);
            outStream.write(byteArray);
            outStream.close();
        } catch (Exception e) {
            Log.e("TAG","could not save", e);
        }

        return true;
    }

    private void launchUploadActivity(boolean isImage){

        Global.filePath = fileUri.getPath();
        String res = OCR_Data(Global.filePath);



        if (!res.equals("")){
            File file = new File(Global.filePath);
            boolean deleted = file.delete();

            getDate_ChkVIN(res);
        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                try{
                    dispatchTakePictureIntent();
                }catch (Exception e){

                }

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





    public String OCR_Data(String filepath){
        String res = "";
        try {
            String stringFileName = filepath;
            bitmap = BitmapFactory.decodeFile(stringFileName);
            img.setImageBitmap(bitmap);
            img.setVisibility(View.VISIBLE);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
            Frame frameImage = new Frame.Builder().setBitmap(bitmap).build();



            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frameImage);
            String stringImageText = "";
            for (int i = 0; i<textBlockSparseArray.size();i++){
                TextBlock textBlock = textBlockSparseArray.get(textBlockSparseArray.keyAt(i));

                stringImageText = stringImageText + " " + textBlock.getValue();
            }
            //get data
            String[] data  = stringImageText.split(" ");
            for (String s: data){
                if (s.length()==17){

                    if (s.substring(0,2).equals("MP")){
                        s = s.replace("O","0");
                        res = s;
                        break;
                    }else if (s.substring(0,2).equals("NP")){
                        s = "MP" + s.substring(2,17);
                        res = s;
                        break;
                    }
                }
            }

        }
        catch (Exception e){
            Toast.makeText(getBaseContext(),"Error " + e.getMessage() ,Toast.LENGTH_LONG).show();
        }

        return  res;
    }



    public void getDate_ChkVIN(final String vin){



        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");
        final String countat = prefs.getString("countat","");
        final String hdid = prefs.getString("hdid","");
        final String t_vin = vin;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        final String t_time = sdf.format(new Date());

        AndroidNetworking.post(Config.CHK_COUNT_VIN_URL)
                .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .addBodyParameter("user",username)
                .addBodyParameter("vin",vin)
                .addBodyParameter("sloc",countat)
                .addBodyParameter("hdid",hdid)

                .setTag("conifrm job")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        JSONArray jsonarray = response;
                        try {

                            String id="",ref="",rlocation="",location="";
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                id =  jsonobject.getString("hdid");
                                ref =  jsonobject.getString("ref");
                                rlocation =  jsonobject.getString("rlocation");
                                location =  jsonobject.getString("location");
                            }

                            if ((ref.equals("ok")) || (ref.equals("misloc"))){
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("ref", ref);
                                editor.putString("detid", id);
                                editor.putString("rlocation", rlocation);
                                editor.putString("location", location);
                                editor.putString("vin", t_vin);
                                editor.commit();

                                startActivity(new Intent(CaptureVINActivity.this, ChkVINCountResultActivity.class).putExtra("data", t_vin));



                            }else if (ref.equals("scaned")){
                                Toast.makeText(getBaseContext(), rlocation,Toast.LENGTH_LONG).show();
                            }else if (ref.equals("notfound")){
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("ref", ref);

                                editor.putString("rlocation", "ไม่มี VIN ในระบบต้องการ Confirm Count หรือไม่");
                                editor.putString("location", location);
                                editor.putString("vin", t_vin);
                                startActivity(new Intent(CaptureVINActivity.this, ChkVINCountResultActivity.class).putExtra("data", t_vin));
                                editor.commit();
                            }

                        } catch (JSONException e) {
                            Log.e("", "unexpected JSON exception", e);
                            // Do something to recover ... or kill the app.
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getBaseContext(),error.getErrorDetail(),Toast.LENGTH_LONG).show();
                    }
                });

    }



}
