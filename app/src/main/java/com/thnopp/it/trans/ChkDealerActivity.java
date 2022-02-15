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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

import static android.content.ContentValues.TAG;


public class ChkDealerActivity extends Activity {

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

                            }else{
                                Global.tdealer = dealer.getText().toString();
                                dealer.setText("");
                                new ChkDealerActivity.PostMobileCount().execute();
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
                IntentIntegrator scanIntegrator = new IntentIntegrator(ChkDealerActivity.this);
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
                    Global.tdealer = dealer.getText().toString();
                    new ChkDealerActivity.PostMobileCount().execute();
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
              Global.tdealer = dealer.getText().toString();
              SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
              SharedPreferences.Editor editor = prefs.edit();
              editor.putString("dealer",Global.delaer);
              editor.commit();

              new ChkDealerActivity.PostMobileCount().execute();
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

                result = rs.postDispatch( String.valueOf(Global.tdealer).toString().toUpperCase(),String.valueOf(Global.user).toString());
                if (result.equals("ok")){
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("vin",dealer.getText().toString().toUpperCase() );
                    editor.putString("msg", "Scan ถูกต้อง");
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ChkVINResultActivity.class);
                    startActivity(intent);
                }


                else{
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("vin",dealer.getText().toString().toUpperCase() );
                    editor.putString("msg", result);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), ChkVINResult1Activity.class);
                    startActivity(intent);


                }


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
