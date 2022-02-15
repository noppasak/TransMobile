package com.thnopp.it.trans;

/**
 * Created by CEVAUser on 5/27/2017.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TripdetailActivity extends Activity {

    private TextView lbldest,  lbldistance, lblfuel, lblsource;
    private ImageView img1;


    DatabaseHelper db;


    private Button btnComplete;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

       // db = DatabaseHelper.getInstance(getApplicationContext());
        db = DatabaseHelper.getInstance(getApplicationContext());
        lblsource = (TextView)findViewById(R.id.lblsource);
        lbldest = (TextView)findViewById(R.id.lbldest);
        lbldistance = (TextView)findViewById(R.id.lbldestance);
        lblfuel = (TextView)findViewById(R.id.lblfuel);

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
            lblsource.setText(v.getSource());
            lbldest.setText(v.getDealer_name());
            lbldistance.setText(v.getRef());
            lblfuel.setText(String.format("%.2f", Double.parseDouble(v.getEngine())));

        }



        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }else{

            btnComplete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TripdetailActivity.this, MenuActivity.class);
                    startActivity(i);
                }
            });

            btnComplete.setVisibility(View.VISIBLE);

        }



    }

    @Override
    public void onBackPressed() {
    }










}
