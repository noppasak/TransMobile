package com.thnopp.it.trans;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class CaptureActivity extends Activity {

    private TextView lblvin, lbldealer, lblCap;
    private ImageView img1;

    private Uri mImageCaptureUri;
    ProgressBar bar1;
    String mCurrentPhotoPath;
    DatabaseHelper db;

    private static final int REQUEST_TAKE_PHOTO = 1;
    static final int CAMERA_PIC_REQUEST = 1337;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private final int CAMERA_RESULT = 101;

    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo,btnComplete;
    String caption;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

       // db = DatabaseHelper.getInstance(getApplicationContext());
        db = DatabaseHelper.getInstance(getApplicationContext());

        lblCap = (TextView)findViewById(R.id.txtCap);
        lbldealer = (TextView)findViewById(R.id.lbldealer_name);
        lblvin = (TextView)findViewById(R.id.lblvin);
        btnCapturePicture = (Button) findViewById(R.id.btnCap);

        bar1 = (ProgressBar) findViewById(R.id.progressBar1);

//        btnRecordVideo = (Button) findViewById(R.id.btnVDO);
        btnComplete = (Button) findViewById(R.id.btnBack);

        /*
        load data from local server

         */

        Vinmaster v = db.findAvaialble_Order();
        if (v==null){
            Toast.makeText(getBaseContext(),"ยังไม่มี Trip ในมือถือ",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ReceiveNewActivity.class);
            startActivity(intent);
        }else{
            Global.t_delid = v.getId();

            lbldealer.setText(v.getDealer_name());
            lblvin.setText(v.getVin());
        }


        caption = "ถ่ายรูปรถ trailer";


        //check last scan

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }else{
            btnCapturePicture.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // capture picture

                    captureImage();

                }
            });

            btnComplete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // capture picture

                    Intent i = new Intent(CaptureActivity.this, MenuActivity.class);
                    startActivity(i);

                }
            });


            lblCap.setText(caption);

            btnComplete.setVisibility(View.VISIBLE);




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


                Uri photoURI = FileProvider.getUriForFile(CaptureActivity.this,
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

        File image = File.createTempFile( Global.t_delid +
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
            if(ContextCompat.checkSelfPermission(CaptureActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
            }


        } catch (IOException e) {
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


        Scanvin vin = new Scanvin();
        vin.setPath(fileUri.getPath());
        vin.setId(Global.t_delid); // Long.toString(Global.tdelid));
        db.InsertVIN(vin);



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

    @Override
    public void onBackPressed() {
    }










}
