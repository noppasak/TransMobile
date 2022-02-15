package com.thnopp.it.trans;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.androidnetworking.error.ANError;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.List;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;


public class ScanRetrofitActivity extends Activity {

    DatabaseHelper db;
    List<Scanvin> lst;

    TextView lbltype, lbluser,lbldealer,lbldealer_name;
    Button scan, back, ok,ng;
    EditText dealer;
    ProgressBar bar1;
    String vin,status, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        //view.setFocusableInTouchMode(true);
        bar1 = (ProgressBar) findViewById(R.id.progressBar1);


        lbldealer = (TextView) findViewById(R.id.msg);
        lbldealer.setText("");

        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }




        dealer = (EditText) findViewById(R.id.txtvin);
        dealer.setText("");
        dealer.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (dealer.getText().equals("")){
                                Toast.makeText(ScanRetrofitActivity.this, "VIN ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                            }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                                Toast.makeText(ScanRetrofitActivity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                            }else{
                                vin = dealer.getText().toString().toUpperCase();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        dealer.setFocusableInTouchMode(true);
        dealer.requestFocus();

        scan =  (Button)findViewById(R.id.buttonScan);
        back =  (Button)findViewById(R.id.buttonBack);
        ng =  (Button)findViewById(R.id.buttonNG);
        ok =  (Button)findViewById(R.id.buttonOK);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.scantyp = 0;
                IntentIntegrator scanIntegrator = new IntentIntegrator(ScanRetrofitActivity.this);
                scanIntegrator.initiateScan();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dealer.getText().equals("")){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ไม่มีข้อมุล", Toast.LENGTH_LONG).show();
                }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                }else{
                    status="OK";
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    getData();
                }

            }
        });

        ng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dealer.getText().equals("")){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ไม่มีข้อมุล", Toast.LENGTH_LONG).show();
                }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                }else{
                    status="NG";
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    getData();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            IntentResult scaningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scaningResult != null) {
                String scanContent = scaningResult.getContents();
                dealer.setText(scanContent.toUpperCase());

                if (dealer.getText().equals("")){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ไม่มีข้อมุล", Toast.LENGTH_LONG).show();
                }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                    Toast.makeText(ScanRetrofitActivity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e){
            Toast.makeText(ScanRetrofitActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void getData(){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        Global.user = username;

        AndroidNetworking.post(Config.UPDATE_RETROFIT)
                .addBodyParameter("vin",dealer.getText().toString().toUpperCase())
                .addBodyParameter("result",status)
                .addBodyParameter("user",Global.user)
                .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("My success", "" + response);

                        try {
                            bar1.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (response.equals("ok")){
                                dealer.setText("");
                                lbldealer.setText("Scan Complete");
                            }else{
                                dealer.setText("");
                                lbldealer.setText("Error:" + response);
                            }

                        } catch (Exception e) {
                            dealer.setText("");
                            lbldealer.setText("Error:" + response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScanRetrofitActivity.this, "my error :" + anError.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class PostMobileCount extends AsyncTask<Void, Void, Void> {
        RestService rs;
        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rs = new RestService(getBaseContext(),"","","","");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try{
                result = rs.postRetrofit(vin.toUpperCase(), status,
                        String.valueOf(Global.user).toString());
                if (result.equals("ok")){
                    dealer.setText("");
                    lbldealer.setText("Scan Complete");
                }else{
                    dealer.setText("");
                    lbldealer.setText("Error:" + result);
                }

            }catch (Exception ex){
                //  Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();

                lbldealer.setText("Error:" + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result2) {
            super.onPostExecute(result2);

            bar1.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        }

    }
    @Override
    public void onBackPressed() {
    }
}

