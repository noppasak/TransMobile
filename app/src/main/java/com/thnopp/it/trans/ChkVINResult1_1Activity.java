package com.thnopp.it.trans;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ChkVINResult1_1Activity extends Activity {

    TextView lbltype, lbluser,lbldealer, lbldealer_name;
    Button scan, next, back, tripd;
    EditText tvin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chkvin_result1);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String vin = prefs.getString("vin","");
        String msg = prefs.getString("msg","");
        String pickslip = prefs.getString("pickslip","");

        tvin = (EditText) findViewById(R.id.txtkey);
        tvin.setText(null);

        lbldealer = (TextView) findViewById(R.id.msg);
        lbldealer_name = (TextView)findViewById(R.id.lbldealer_name);
        lbldealer.setText("Pickslip และ DD Sheet ไม่ตรงกัน !!!");
        lbldealer.setTextColor(Color.RED);
        lbldealer_name.setText(vin + " / " + pickslip);


        if (vin.equals(pickslip)){
            Intent intent = new Intent(getApplicationContext(), ChkDealer_1Activity.class);
            startActivity(intent);
        }

        new ChkVINResult1_1Activity.PostMobileCount().execute();

        back =  (Button)findViewById(R.id.buttonBack);


        tvin.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (tvin.getText().toString().equals("78")){

                                Intent intent = new Intent(getApplicationContext(), ChkDealer_1Activity.class);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvin.getText().toString().equals("78")){
                    Intent intent = new Intent(getApplicationContext(), ChkDealer_1Activity.class);
                    startActivity(intent);
                }



            }
        });


        if (Global.user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }else{



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
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                String vin1 = prefs.getString("vin","");
                String pickslip1 = prefs.getString("pickslip","");
                result = rs.postDispatch_1(vin1.toUpperCase(), pickslip1.toUpperCase(),String.valueOf(Global.user).toString());



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

