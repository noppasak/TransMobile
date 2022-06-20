package com.thnopp.it.trans.count;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.thnopp.it.trans.ChkDealerActivity;
import com.thnopp.it.trans.ChkVINResult1Activity;
import com.thnopp.it.trans.Config;
import com.thnopp.it.trans.DatabaseHelper;
import com.thnopp.it.trans.Global;
import com.thnopp.it.trans.MainActivity;
import com.thnopp.it.trans.R;
import com.thnopp.it.trans.RestService;
import com.thnopp.it.trans.ScannedBarcodeActivity;
import com.thnopp.it.trans.Scanvin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;


public class ChkVINCountResultActivity extends Activity {

    String form_scan;
    TextView lblcount, lblmsg, lblparking, lbluser;

    Button back, next;
    EditText nparking, remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chkvin_count_result);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        String username = prefs.getString("username","");
        String countat = prefs.getString("countat","");

        String ref = prefs.getString("ref","");
        String detid = prefs.getString("detid","");
        String rlocation = prefs.getString("rlocation","");
        String locatoin = prefs.getString("location","");
        String vin = prefs.getString("vin","");
        form_scan = prefs.getString("form_scan","");
        Global.user = username;

        lblcount  = (TextView) findViewById(R.id.lblcountat);
        lbluser  = (TextView) findViewById(R.id.lbluser);
        lblparking  = (TextView) findViewById(R.id.lblparking);
        lblmsg  = (TextView) findViewById(R.id.msg);
        lblmsg.setVisibility(View.INVISIBLE);

        nparking = (EditText) findViewById(R.id.txtparking);
        remark = (EditText) findViewById(R.id.txtremark);
        back = (Button) findViewById(R.id.buttonBack);
        next = (Button) findViewById(R.id.buttonNext);

        lbluser.setText("นับโดย " + username);
        lblcount.setText("นับที่ " + countat);
        lblparking.setText(locatoin);
        lblmsg.setText(rlocation);
        lblmsg.setVisibility(View.GONE);

        nparking.setText(null);
        remark.setText(null);

        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CountDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post data
                if (nparking.getText().toString().isEmpty()){

                    Toast.makeText(getBaseContext(),"ต้องใส่ Parking ด้วย",Toast.LENGTH_LONG).show();
                }else{
                    if (nparking.getText().toString().length()!=5){
                        Toast.makeText(getBaseContext(),"Parking ต้องมี 5 digit เท่านั้น",Toast.LENGTH_LONG).show();
                    }else{
                        getDate_Count(nparking.getText().toString(), remark.getText().toString());
                    }

                }

            }
        });



    }

    public void getDate_Count(String newparking, String rem){



        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");
        final String hdid = prefs.getString("hdid","");
        final String vin = prefs.getString("vin","");
        final String countat = prefs.getString("countat","");

        final String form = prefs.getString("countat","");

        AndroidNetworking.post(Config.UPDATE_COUNT_VIN_NEW_URL)
                .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .addBodyParameter("hdid",hdid)
                .addBodyParameter("vin",vin)
                .addBodyParameter("location",countat)
                .addBodyParameter("nparking",newparking)
                .addBodyParameter("remark",rem)
                .addBodyParameter("user",username)
                .setTag("post loaded data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("ok")){
                            Toast.makeText(getBaseContext(),"Update Count Complete..." + response,Toast.LENGTH_SHORT).show();
                            if (form_scan.equals("barcode")){
                                Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(getApplicationContext(), CaptureVINActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }else{
                            Toast.makeText(getBaseContext(),"Update Count Error..." + response,Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getBaseContext(),"Error " + anError.getErrorDetail() ,Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void getDate_Countbk(String newparking, String rem){



        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");
        final String detid = prefs.getString("detid","");

        AndroidNetworking.post(Config.UPDATE_COUNT_VIN_URL)
                .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .addBodyParameter("detid",detid)
                .addBodyParameter("nparking",newparking)
                .addBodyParameter("remark",rem)
                .addBodyParameter("user",username)
                .setTag("post loaded data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("ok")){
                            Toast.makeText(getBaseContext(),"Update Count Complete..." + response,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), CountDetailActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getBaseContext(),"Update Count Error..." + response,Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getBaseContext(),"Error " + anError.getErrorDetail() ,Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
    }
}

