package com.thnopp.it.trans;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ChkVINResult1Activity extends Activity {

    TextView lbltype, lbluser,lbldealer, lbldealer_name;
    Button scan, next, back, tripd;
    EditText vin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chkvin_result1);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String vin = prefs.getString("vin","");
        String msg = prefs.getString("msg","");



        lbldealer = (TextView) findViewById(R.id.msg);
        lbldealer_name = (TextView)findViewById(R.id.lbldealer_name);
        lbldealer.setText(msg);
        lbldealer_name.setText(vin);




        back =  (Button)findViewById(R.id.buttonBack);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                startActivity(intent);

            }
        });


        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }else{



        }


    }


    @Override
    public void onBackPressed() {
    }
}

