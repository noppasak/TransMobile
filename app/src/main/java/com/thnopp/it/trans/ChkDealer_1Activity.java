package com.thnopp.it.trans;

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

import java.util.List;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;


public class ChkDealer_1Activity extends Activity {

    DatabaseHelper db;
    List<Scanvin> lst;

    TextView lbltype, lbluser;
    Button scan, next, back,upload, inst;
    EditText dealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chkdealer);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        Integer client = prefs.getInt("client",0);
        String conf = prefs.getString("conf","");
        String t_dealer = prefs.getString("dealer","");
        String t_dealer_name = prefs.getString("dealer_name","");
        Global.user = username;
        Global.client = client;
        Global.conf = conf;
        Global.delaer = t_dealer;
        Global.dealer_name=t_dealer_name;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        //assign value into label
        lbluser = (TextView) findViewById(R.id.lbluser);

        lbluser.setText(Global.user);

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

                            }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                                Toast.makeText(ChkDealer_1Activity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                            }else{
                                Global.tdealer = dealer.getText().toString();
                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("vin",dealer.getText().toString().toUpperCase() );
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), ChkVINResult_1Activity.class);
                                startActivity(intent);

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
        next =  (Button)findViewById(R.id.buttonNext);
        back =  (Button)findViewById(R.id.buttonBack);

       // db = DatabaseHelper.getInstance(getApplicationContext());
        db = DatabaseHelper.getInstance(getApplicationContext());

        Global.ltcode = null;
       // next.setEnabled(false);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.scantyp = 0;
                IntentIntegrator scanIntegrator = new IntentIntegrator(ChkDealer_1Activity.this);
                scanIntegrator.initiateScan();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                check data in REST service
                return error
                 */
                if (dealer.getText().equals("")){

                }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                    Toast.makeText(ChkDealer_1Activity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
                }else{
                    Global.tdealer = dealer.getText().toString();
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("vin",dealer.getText().toString().toUpperCase() );
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ChkVINResult_1Activity.class);
                    startActivity(intent);

                }



            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.tdealer = null;
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);

            }
        });



        if (lbluser.getText() != null)
                lbluser.setText(Global.user);


        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }else{


        }


        db.closeDB();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      try{
          IntentResult scaningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
          if (scaningResult != null) {
              String scanContent = scaningResult.getContents();
              dealer.setText(scanContent);
              if (dealer.getText().equals("")){

              }else if ((!dealer.getText().equals("")) && (dealer.getText().length()!=17)){
                  Toast.makeText(ChkDealer_1Activity.this, "VIN ต้องมี 17 Digit", Toast.LENGTH_LONG).show();
              }else{
                  Global.tdealer = dealer.getText().toString();
                  SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                  SharedPreferences.Editor editor = prefs.edit();
                  editor.putString("vin",dealer.getText().toString().toUpperCase() );
                  editor.commit();

                  Intent intent = new Intent(getApplicationContext(), ChkVINResult_1Activity.class);
                  startActivity(intent);

              }


          }

      } catch (Exception e){

      }


    }



    @Override
    public void onBackPressed() {
    }
}
