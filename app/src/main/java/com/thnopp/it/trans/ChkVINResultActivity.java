package com.thnopp.it.trans;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;


public class ChkVINResultActivity extends Activity {

    DatabaseHelper db;
    List<Scanvin> lst;

    TextView lbltype, lbluser,lbldealer,lbldealer_name;
    Button scan, next, back,upload, inst;
    EditText dealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chkvin_result);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String vin = prefs.getString("vin","");
        String msg = prefs.getString("msg","");

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

        //view.setFocusableInTouchMode(true);

        lbldealer = (TextView) findViewById(R.id.msg);
        lbldealer_name = (TextView)findViewById(R.id.lbldealer_name);
        lbldealer.setText(msg);
        lbldealer_name.setText(vin);
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

                            }else{
                                Global.pickslip = dealer.getText().toString();
                                dealer.setText("");
                                if(String.valueOf(Global.tdealer).toString().toUpperCase().equals(Global.pickslip.toUpperCase())) {
                                    Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                                    startActivity(intent);
                                }else
                                new ChkVINResultActivity.PostMobileCount().execute();
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



        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.scantyp = 0;
                IntentIntegrator scanIntegrator = new IntentIntegrator(ChkVINResultActivity.this);
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

                }else{
                    Global.pickslip = dealer.getText().toString();
                    if(String.valueOf(Global.tdealer).toString().toUpperCase().equals(Global.pickslip.toUpperCase())) {
                        Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                        startActivity(intent);
                    }else
                    new ChkVINResultActivity.PostMobileCount().execute();
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
                dealer.setText(scanContent);
                Global.pickslip = dealer.getText().toString();
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("pickslip",Global.pickslip);
                editor.commit();
                if(String.valueOf(Global.tdealer).toString().toUpperCase().equals(Global.pickslip.toUpperCase())) {
                    Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                    startActivity(intent);
                }else
                new ChkVINResultActivity.PostMobileCount().execute();
                // check data and return result


            }

        } catch (Exception e){

        }


    }


    private class PostMobileCount extends AsyncTask<Void, Void, Void> {
        RestService rs;
        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //rs = new RestService(getBaseContext(),"http://54.254.134.225:92","MSBS","admin","123");
            rs = new RestService(getBaseContext(),"","","","");
            // Toast.makeText(getBaseContext(),"Sending...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // android.os.Debug.waitForDebugger();

            try{

                result = rs.postDispatch_rem( String.valueOf(Global.tdealer).toString().toUpperCase(),String.valueOf(Global.user).toString(),"Pickslip incorrect");
                if (result.equals("ok")){
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("vin",dealer.getText().toString().toUpperCase() );
                    editor.putString("msg", "Pick Slip ?????????");
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ChkVINResult1Activity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                    startActivity(intent);
                }
               /* if (result.equals("ok")){
                    dealer.setText("");

                    Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                    startActivity(intent);
                }


                else{

                    Intent intent = new Intent(getApplicationContext(), ChkDealerActivity.class);
                    startActivity(intent);

                 *//*   SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("vin",dealer.getText().toString().toUpperCase() );
                    editor.putString("msg", result);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ChkVINResult1Activity.class);
                    startActivity(intent);*//*


                }*/


            }catch (Exception ex){
                //  Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                result =ex.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result2) {
            super.onPostExecute(result2);



        }

    }
    @Override
    public void onBackPressed() {
    }
}

