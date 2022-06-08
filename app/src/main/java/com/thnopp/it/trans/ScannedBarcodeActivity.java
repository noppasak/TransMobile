package com.thnopp.it.trans;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.thnopp.it.trans.count.ChkVINCountNewResultActivity;
import com.thnopp.it.trans.count.ChkVINCountResultActivity;
import com.thnopp.it.trans.count.CountDetailActivity;
import com.thnopp.it.trans.count.CountHeaderActivity;
import com.thnopp.it.trans.retrofit.ScanRetrofitActivity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ScannedBarcodeActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction, btnFlash;
    String intentData = "";
    boolean isEmail = false;

    private Camera camera = null;
    boolean flashmode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnFlash = findViewById(R.id.btnFlash);



        btnFlash.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                            String flash = prefs.getString("flash","");

                                            if (StringUtils.isEmpty(flash)){
                                                //flash on

                                                flashOnButton();

                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.putString("flash","Y");
                                                editor.commit();
                                            } else{
                                                //flash off
                                                flashOnButton();

                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.putString("flash","");
                                                editor.commit();

                                            }

                                        }
                                    }

        );

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                String form = prefs.getString("form","");

                if (form.equals("retrofit")){
                    if ((intentData.length() == 17) || (intentData.length() == 6)) {
                        Toast.makeText(getBaseContext(),"รหัสรถ,เครื่องจะต้องมี 17 , 6 หลัก",Toast.LENGTH_LONG).show();
                    }else{
                        getDate_ChkVIN(intentData);
                        //startActivity(new Intent(ScannedBarcodeActivity.this, ScanRetrofitActivity.class).putExtra("data", intentData));
                    }
                }else   if (form.equals("count")) {
                    if ((intentData.length() == 17) || (intentData.length() == 6)) {
                        getDate_ChkVIN(intentData);
                        //startActivity(new Intent(ScannedBarcodeActivity.this, ChkVINCountResultActivity.class).putExtra("data", intentData));
                    } else {
                        Toast.makeText(getBaseContext(), "รหัสรถ,เครื่องจะต้องมี 17 , 6 หลัก", Toast.LENGTH_LONG).show();
                    }
                }



            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();




        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
               // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                btnAction.setText("SCAN BARCODE");
                            } else {
                                isEmail = false;
                                btnAction.setText("เก็บข้อมูล");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);


                                if (intentData.length() == 17) {
                                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                    String form = prefs.getString("form","");

                                     if (form.equals("count")) {
                                         getDate_ChkVIN(intentData);

                                    }
                                }


                            }
                        }
                    });

                }
            }
        });
    }


    private void flashOnButton() {
        camera=getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
                if(flashmode){
                    Toast.makeText(getApplicationContext(),"Flash Switched ON", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Flash Switched Off", Toast.LENGTH_SHORT).show();{
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }

    public void getDate_ChkVIN(String vin){



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
                                editor.putString("vin", intentData);
                                editor.commit();

                                startActivity(new Intent(ScannedBarcodeActivity.this, ChkVINCountResultActivity.class).putExtra("data", intentData));



                            }else if (ref.equals("scaned")){
                                Toast.makeText(getBaseContext(), rlocation,Toast.LENGTH_LONG).show();
                            }else if (ref.equals("notfound")){
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("ref", ref);

                                editor.putString("rlocation", "ไม่มี VIN ในระบบต้องการ Confirm Count หรือไม่");
                                editor.putString("location", location);
                                editor.putString("vin", intentData);
                                startActivity(new Intent(ScannedBarcodeActivity.this, ChkVINCountResultActivity.class).putExtra("data", intentData));
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
