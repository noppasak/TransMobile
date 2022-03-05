package com.thnopp.it.trans.count;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.thnopp.it.trans.Config;
import com.thnopp.it.trans.DatabaseHelper;
import com.thnopp.it.trans.Global;
import com.thnopp.it.trans.MainActivity;
import com.thnopp.it.trans.MenuActivity;
import com.thnopp.it.trans.R;
import com.thnopp.it.trans.ScannedBarcodeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CountDetailActivity extends Activity {

    TextView lblcount, lbluser;
    Button scan, next, back;
    EditText txtvin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdetail);


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        String countat = prefs.getString("countat","");
        Global.user = username;

        lbluser = (TextView) findViewById(R.id.lbluser);
        lblcount = (TextView) findViewById(R.id.lblcountat);

        if (lbluser.getText() != null)
            lbluser.setText(Global.user);

        if (Global.user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        lblcount.setText("Count at " + countat);

        txtvin = (EditText) findViewById(R.id.txtvin);

        scan = (Button) findViewById(R.id.buttonScan);
        next = (Button) findViewById(R.id.buttonNext);
        back = (Button) findViewById(R.id.buttonBack);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((txtvin.length() == 17) || (txtvin.length() == 6)) {
                    txtvin.setText(txtvin.getText().toString().toUpperCase());
                    getDate_ChkVIN(txtvin.getText().toString());
                }else{
                    txtvin.setText(null);
                    Toast.makeText(getBaseContext(), "VIN ไม่ถูกต้อง !!!",Toast.LENGTH_LONG).show();
                }



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CountHeaderActivity.class);
                startActivity(intent);
            }
        });


    }



    public void getDate_ChkVIN(final String vin){



        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");
        final String countat = prefs.getString("countat","");
        final String hdid = prefs.getString("hdid","");

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
                                editor.putString("vin", vin);

                                editor.commit();

                                startActivity(new Intent(CountDetailActivity.this, ChkVINCountResultActivity.class).putExtra("data", vin));



                            }else if (ref.equals("scaned")){
                                txtvin.setText(null);
                                Toast.makeText(getBaseContext(), rlocation,Toast.LENGTH_LONG).show();
                            }else if (ref.equals("notfound")){
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("ref", ref);

                                editor.putString("rlocation", "ไม่มี VIN ในระบบต้องการ Confirm Count หรือไม่");
                                editor.putString("location", location);
                                editor.putString("vin", vin);
                                startActivity(new Intent(CountDetailActivity.this, ChkVINCountNewResultActivity.class).putExtra("data", vin));
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

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




    @Override
    public void onBackPressed() {
    }
}
