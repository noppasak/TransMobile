package com.thnopp.it.trans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiveNewActivity extends Activity {

    TextView lbltype, lbluser;
    Button scan, create, back,reload;
    EditText location, qty;
    ListView lisView1;
    ArrayList<HashMap<String, String>> MyArrList;
    ProgressBar bar1;
    DatabaseHelper db;
    String param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_new);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        String conf = prefs.getString("password","");
        Global.user = username;

        param = username;
        bar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lisView1 = (ListView) findViewById(android.R.id.list);

        db = DatabaseHelper.getInstance(getApplicationContext());


        if (Global.user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }



        // ArrayList<Dua> arrayList = new ArrayList<>();
        MyArrList = new ArrayList<HashMap<String, String>>();
        bar1.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new GetVINMaster().execute();




        Button btnReload = (Button) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bar1.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new  GetVINMaster().execute();
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
                convertView = inflater.inflate(R.layout.activity_receive_new_column, null);

            }



            if (MyArrList.get(position).get("ID").equals("ID")){
               /* TableRow r1 = (TableRow) convertView.findViewById(R.id.tableRow0);r1.la
                TableRow r2 = (TableRow) convertView.findViewById(R.id.tableRow1);r2.setVisibility(View.INVISIBLE);
                TableRow r3 = (TableRow) convertView.findViewById(R.id.tableRow3);r3.setVisibility(View.INVISIBLE);
                TableRow r4 = (TableRow) convertView.findViewById(R.id.tableRow12);r4.setVisibility(View.INVISIBLE);
                TableRow r5 = (TableRow) convertView.findViewById(R.id.tableRow2);r5.setVisibility(View.INVISIBLE);
                TableRow r6 = (TableRow) convertView.findViewById(R.id.tableRow21);r6.setVisibility(View.INVISIBLE);
*/


             /*   Button cmdShared = (Button) convertView.findViewById(R.id.CmdReceive);
                cmdShared.setVisibility(View.INVISIBLE);
                TextView txtID = (TextView) convertView.findViewById(R.id.ColID);txtID.setVisibility(View.INVISIBLE);
                TextView txtD = (TextView) convertView.findViewById(R.id.ColDate);txtD.setVisibility(View.INVISIBLE);
                TextView txtR = (TextView) convertView.findViewById(R.id.ColRef);txtR.setVisibility(View.INVISIBLE);
                TextView txtSource = (TextView) convertView.findViewById(R.id.ColSource);txtSource.setVisibility(View.INVISIBLE);
                TextView txtDest = (TextView) convertView.findViewById(R.id.ColDest);txtDest.setVisibility(View.INVISIBLE);*/
            }else{
                // ColLT
                TextView txtID = (TextView) convertView.findViewById(R.id.ColID);
                txtID.setText("ID: " + MyArrList.get(position).get("ID"));

                TextView txtD = (TextView) convertView.findViewById(R.id.ColDate);
                txtD.setText("Date: " +MyArrList.get(position).get("Date"));

                TextView txtR = (TextView) convertView.findViewById(R.id.ColRef);
                txtR.setText("Chassis: "+MyArrList.get(position).get("Chassis"));

                TextView txtSource = (TextView) convertView.findViewById(R.id.ColSource);
                txtSource.setText("รับที่: "+MyArrList.get(position).get("Source"));

                TextView txtDest = (TextView) convertView.findViewById(R.id.ColDest);
                txtDest.setText("ส่งที่: "+MyArrList.get(position).get("Dest"));

                TextView txtRem = (TextView) convertView.findViewById(R.id.ColRem);
                txtRem.setText("หมายเหตุ: "+MyArrList.get(position).get("Remark"));

                if (!txtID.getText().equals("ID")){
                    Button cmdShared = (Button) convertView.findViewById(R.id.CmdReceive);
                    cmdShared.setVisibility(View.VISIBLE);
                    cmdShared.setText("Receive");
                    //cmdShared.setBackgroundColor(Color.TRANSPARENT);
                    cmdShared.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                        /*add trip to local
                        redirect to main menu
                        */
                            Vinmaster newv = new Vinmaster();
                            newv.setId(Long.parseLong(MyArrList.get(position).get("ID")));
                            newv.setVin(MyArrList.get(position).get("Chassis"));
                            newv.setLtcode(MyArrList.get(position).get("Ref"));
                            newv.setStatus("A");
                            newv.setTrailer(Global.user);
                            newv.setDealer_name(MyArrList.get(position).get("Dest"));
                            newv.setDealer(MyArrList.get(position).get("Date"));
                            newv.setEngine(MyArrList.get(position).get("Fuel"));
                            newv.setRef(MyArrList.get(position).get("Distance"));
                            newv.setSender(MyArrList.get(position).get("Sender"));
                            newv.setReceiver(MyArrList.get(position).get("Receiver"));
                            newv.setRemark(MyArrList.get(position).get("Remark"));
                            newv.setCarrier(MyArrList.get(position).get("Carrier"));
                            newv.setSource(MyArrList.get(position).get("Source"));
                            db.InsertDelivery(newv);

                            Intent newActivity = new Intent(ReceiveNewActivity.this,MenuActivity.class);
                            startActivity(newActivity);
                        }
                    });
                }else{
                    Button cmdShared = (Button) convertView.findViewById(R.id.CmdReceive);
                    cmdShared.setVisibility(View.INVISIBLE);
                }
            }


            return convertView;
        }

    }

    public class GetVINMaster extends AsyncTask<Void, Void, Void> {
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
                //  rs.Connect();
                //result = rs.GetJsonString("FixedAsset");
                result = rs.GetJsonString(param,"VIN");
            }catch (Exception ex){
//                Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                bar1.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result2) {
            super.onPostExecute(result2);
            //       Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();
            if (result== null){
                // not found any order
                // Toast.makeText(getBaseContext(),"Can't load the Master Data",Toast.LENGTH_LONG).show();
            }else {
                try {
                    MyArrList.clear();
                    JSONArray jsonarray = new JSONArray(result);

                    HashMap<String, String> map;
                    map = new HashMap<String, String>();

                /*    map.put("ID", "ID");
                    map.put("Date", "Date");
                    map.put("Ref", "Ref");
                    map.put("Dest", "Dest");
                    map.put("Chassis","Chassis");
                    map.put("VIN","VIN");
                    map.put("Distance","Distance");
                    map.put("Fuel","Fuel");
                    map.put("Sender","Sender");
                    map.put("Receiver","Receiver");
                    map.put("Remark","Remark");
                    map.put("Carrier","Carrier");
                    map.put("Source","Source");
                    MyArrList.add(map);*/

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        map = new HashMap<String, String>();

                        map.put("ID", jsonobject.getString("delid"));
                        map.put("Date", jsonobject.getString("transdt"));
                        map.put("Ref", jsonobject.getString("ltcode"));
                        map.put("Dest", jsonobject.getString("dealercd") + "/" + jsonobject.getString("dealername"));
                        map.put("Chassis",jsonobject.getString("engine"));
                        map.put("VIN",jsonobject.getString("vin"));
                        map.put("Distance",jsonobject.getString("inst1"));
                        map.put("Fuel",jsonobject.getString("widealer"));
                        map.put("Sender",jsonobject.getString("sender"));
                        map.put("Receiver",jsonobject.getString("receiver"));
                        map.put("Remark",jsonobject.getString("remark"));
                        map.put("Carrier",jsonobject.getString("carrier"));
                        map.put("Source",jsonobject.getString("source"));

                        MyArrList.add(map);

                    }
                    bar1.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    lisView1.setAdapter(new ReceiveNewActivity.CountryAdapter(ReceiveNewActivity.this));

                } catch (JSONException e) {
                    Log.e("", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                    bar1.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }
    }



    @Override
    public void onBackPressed() {
    }
}//end class
