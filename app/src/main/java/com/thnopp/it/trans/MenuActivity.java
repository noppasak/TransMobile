package com.thnopp.it.trans;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thnopp.it.trans.count.CountHeaderActivity;
import com.thnopp.it.trans.retrofit.ScanRetrofitActivity;

/**
 * Created by THLT88 on 10/20/2017.
 */

public class MenuActivity extends Activity {

    TextView lbltype, lbluser ;

    Button scan, retrofit, logout, count,upload,fuel,wi ;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        Integer client = prefs.getInt("client",0);
        String conf = prefs.getString("conf","");
        Global.user = username;
        Global.client = client;
        Global.conf = conf;

        db = DatabaseHelper.getInstance(getApplicationContext());

       // db.delete_unmatch_user(username);

        //lst = db.getAllVIN();

        //assign value into label
        lbluser = (TextView)findViewById(R.id.lbluser);
        lbluser.setText(Global.user);

        scan = (Button)findViewById(R.id.buttonScan);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChkDealer_1Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        retrofit = (Button)findViewById(R.id.buttonRetrofit);
        retrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanRetrofitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        count = (Button)findViewById(R.id.buttonCount);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CountHeaderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        logout = (Button)findViewById(R.id.buttonLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.user=null;

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });




    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed() {
    }

}
