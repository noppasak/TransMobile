package com.thnopp.it.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TripPendingActivity extends Activity {


    /**
     * Created by THLT88 on 10/20/2017.
     */



        ArrayList<HashMap<String, String>> MyArrList;
        String t_dealer,t_trailer;
        String sXML;
        String arrive, dispatch;
        ProgressBar bar1;
        ListView lisView1;
        DatabaseHelper db;
        List<Vinmaster> lst_pod, lst_pod_user;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_trip);
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            String username = prefs.getString("username","");
            Integer client = prefs.getInt("client",0);
            String conf = prefs.getString("conf","");


            t_trailer = username;

            db = DatabaseHelper.getInstance(getApplicationContext());

            Vinmaster v = db.findAvaialble_Order();
            if (v != null){
                t_dealer = v.getId() + "/"+ v.getVin();
            }else{
                t_dealer = "";
            }
            lisView1 = (ListView) findViewById(android.R.id.list);

            MyArrList = new ArrayList<HashMap<String, String>>();
            //get avaialble id

            bar1 = (ProgressBar) findViewById(R.id.progressBar1);

            TextView cap = (TextView) findViewById(R.id.textView1);
            cap.setText("Order ID :" + t_dealer);
            //lisView1.setAdapter(new CountryAdapter(TripActivity.this));


            bar1.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new read_data1pd().execute();

            Button btnReload = (Button) findViewById(R.id.btnReload);
            btnReload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    bar1.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new  read_data1pd().execute();
                    // lisView1.setAdapter(new CountryAdapter(TripActivity.this));
                }
            });

            Button btnBack = (Button) findViewById(R.id.btnGetItem);
            btnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);

                }
            });
        }

        public class CountryAdapter extends BaseAdapter
        {
            private Context context;

            public CountryAdapter(Context c)
            {
                //super( c, R.layout.activity_column, R.id.rowTextView, );
                // TODO Auto-generated method stub
                context = c;
            }

            public int getCount() {
                // TODO Auto-generated method stub
                return MyArrList.size();
            }

            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }
            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.activity_trip_column, null);

                }

                // ColLT
                TextView txtID = (TextView) convertView.findViewById(R.id.ColLT);
                txtID.setText(MyArrList.get(position).get("LT"));


                if (!txtID.getText().equals("LT")){
                    Button cmdA = (Button) convertView.findViewById(R.id.CmdArrive);//stop
                    Button cmdD = (Button) convertView.findViewById(R.id.CmdDepart);//start

                    if (!MyArrList.get(position).get("Arrive").equals("")){
                        cmdA.setVisibility(View.INVISIBLE);
                    }else{
                        cmdA.setVisibility(View.VISIBLE);
                        cmdA.setText("ถึงที่หมาย");
                        //cmdShared.setBackgroundColor(Color.TRANSPARENT);
                        cmdA.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //update and hide
                                final AlertDialog.Builder adb = new AlertDialog.Builder(TripPendingActivity.this);
                                adb.setTitle("Confirm?");
                                adb.setMessage("Plese Confirm เวลาถึง Order " + MyArrList.get(position).get("LT"));
                                adb.setNegativeButton("Cancel", null);
                                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // TODO Auto-generated method stub
                                        db.updateArrival(Long.parseLong(MyArrList.get(position).get("ID")));
                                        lst_pod = db.getPOD();
                                        if (!lst_pod.isEmpty()){
                                            new TripPendingActivity.update_pod().execute();
                                            //    startService(new Intent(getApplicationContext(), LocationService.class));

                                        }
                                        new TripPendingActivity.read_data1pd().execute();
                                    }
                                });
                                adb.show();


                            }
                        });
                    }

                    if (!MyArrList.get(position).get("Depart").equals("")){
                        cmdD.setVisibility(View.INVISIBLE);
                    }else{
                        cmdD.setVisibility(View.VISIBLE);
                        cmdD.setText("ออกจากต้นทาง");
                        //cmdShared.setBackgroundColor(Color.TRANSPARENT);
                        cmdD.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final AlertDialog.Builder adbd = new AlertDialog.Builder(TripPendingActivity.this);
                                adbd.setTitle("Confirm?");
                                adbd.setMessage("Plese Confirm เวลาออก Order " + MyArrList.get(position).get("LT"));
                                adbd.setNegativeButton("Cancel", null);
                                adbd.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        //update and hide
                                        db.updateDepart(Long.parseLong(MyArrList.get(position).get("ID")));
                                        lst_pod = db.getPOD();
                                        if (!lst_pod.isEmpty()){
                                            new TripPendingActivity.update_pod().execute();
                                            //    startService(new Intent(getApplicationContext(), LocationService.class));

                                        }
                                        new TripPendingActivity.read_data1pd().execute();
                                    }
                                });
                                adbd.show();

                            }
                        });
                    }

                }else{
                    Button cmdA = (Button) convertView.findViewById(R.id.CmdArrive);
                    cmdA.setVisibility(View.INVISIBLE);
                    Button cmdD = (Button) convertView.findViewById(R.id.CmdDepart);
                    cmdD.setVisibility(View.INVISIBLE);

                }
                return convertView;
            }

        }


        private class read_data1pd extends AsyncTask<Void,Void, Void> {
            JSONArray result;
            @Override
            protected  void onPreExecute(){

                super.onPreExecute();

            }
            @Override
            protected Void doInBackground(Void... arg0) {
                try{
                    result = db.SelectLT(t_trailer);

                }catch (Exception ex){
                    bar1.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                return null;

            }


            @Override
            protected void onPostExecute(Void result1) {
                super.onPostExecute(result1);

                if (result== null){
                    // not found any order
                }else {

                    try {
                        MyArrList.clear();
                        JSONArray jsonarray = result;

                        HashMap<String, String> map;
                        map = new HashMap<String, String>();

                        map.put("ID", "ID");
                        map.put("Ref", "Ref");
                        map.put("Arrive", "Arrive");
                        map.put("Depart", "Depart");
                        /*map.put("", "");*/
                        MyArrList.add(map);



                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);

                            map = new HashMap<String, String>();
                            map.put("ID", jsonobject.getString("detid"));
                            map.put("LT", jsonobject.getString("ltid"));
                            map.put("Arrive", jsonobject.getString("arrivaldt"));
                            map.put("Depart", jsonobject.getString("departdt"));
                            MyArrList.add(map);

                        }

                        lisView1.setAdapter(new TripPendingActivity.CountryAdapter(TripPendingActivity.this));
                        bar1.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        bar1.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    // Toast.makeText(getBaseContext(), "Asset Load Complete", Toast.LENGTH_SHORT).show();

                }


            }

        }

    public class update_pod extends AsyncTask<Void, Void, Void> {
        RestService rs;
        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rs = new RestService(getBaseContext(),"","",Global.user,Global.conf);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try{
                //  rs.Connect();
                //result = rs.GetJsonString("FixedAsset");
                result = rs.PostPOD_F(lst_pod);

            }catch (Exception ex){
//                Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result2) {
            super.onPostExecute(result2);
            Toast.makeText(getBaseContext(),result, Toast.LENGTH_SHORT).show();
          /*  if (result== null){
              //  Toast.makeText(getBaseContext(),"Can't post the POD Data",Toast.LENGTH_LONG).show();
            }*/
        }
    }


    @Override
        public void onBackPressed() {
        }
    }

