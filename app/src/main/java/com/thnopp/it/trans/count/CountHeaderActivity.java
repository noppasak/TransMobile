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
import com.thnopp.it.trans.Vinmaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CountHeaderActivity extends Activity {

    TextView lbltype, lbluser;
    Button scan, next, back,reload;
    EditText location, qty;
    DatabaseHelper db;
    Long HID ;
    String Bal, ID,HRef, Pickupdt;
    String t_detid;
    String t_location, t_qty;
    RadioGroup rg;
    ArrayList<HashMap<String, String>> MyArrList;
    ListView lisView1;
    RadioButton c1, c2,c3,c4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorder);


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        Integer client = prefs.getInt("client",0);
        String conf = prefs.getString("password","");
        Global.user = username;
        Global.client = client;
        Global.conf = conf;

        lisView1 = (ListView) findViewById(android.R.id.list);

        db = DatabaseHelper.getInstance(getApplicationContext());

        // ArrayList<Dua> arrayList = new ArrayList<>();
        MyArrList = new ArrayList<HashMap<String, String>>();


        //assign value into label

        lbluser = (TextView) findViewById(R.id.lbluser);

        if (lbluser.getText() != null)
            lbluser.setText(Global.user);


        if (Global.user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }




        scan = (Button) findViewById(R.id.buttonScan);
        next = (Button) findViewById(R.id.buttonNext);
        back = (Button) findViewById(R.id.buttonBack);
        reload = (Button) findViewById(R.id.buttonReload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new read_item_bal().execute();
                getData_HD();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });


        //new read_item_bal().execute();
        getData_HD();

    }



    public class CountryAdapter extends BaseAdapter
    {
        private Context context;
        public LayoutInflater mInflater;

        public CountryAdapter(Context c)
        {
            //super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArrList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_trip_column1, null);
                holder = new ViewHolder();
                holder.txtID =(TextView) convertView.findViewById(R.id.ColRef);
                holder.cmdShared = (Button) convertView.findViewById(R.id.cmdGW);
                holder.cmdMin = (Button) convertView.findViewById(R.id.cmdMIN);

                convertView.setTag(holder);
            } else{
                holder  = (ViewHolder) convertView.getTag();
            }

            // ColLT

            holder.txtID.setText(MyArrList.get(position).get("REF"));

            if (!holder.txtID.getText().equals("เลขที่อ้างอิง")){

                holder.cmdShared.setVisibility(View.VISIBLE);
                holder.cmdShared.setText("GW");
                //holder.cmdShared.setBackgroundColor(Color.BLUE);
                //cmdShared.setBackgroundColor(Color.TRANSPARENT);
                holder.cmdShared.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final AlertDialog.Builder adbd = new AlertDialog.Builder(CountHeaderActivity.this);
                        adbd.setTitle("ต้องการยืนยันหรือไม่?");
                        adbd.setMessage("ยืนยันการนับรถที่ เกตเวย์ " + MyArrList.get(position).get("ID") + "\n" +
                                MyArrList.get(position).get("REF") );
                        adbd.setNegativeButton("Cancel", null);
                        adbd.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                //update and hide
                                //check order
                                HRef=MyArrList.get(position).get("ID");

                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("countat", "GV10");
                                editor.putString("form", "count");
                                editor.putString("hdid",  MyArrList.get(position).get("ID") );
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), CountDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //getData();

                            }
                        });
                        adbd.show();

                    }

                });

                holder.cmdMin.setVisibility(View.VISIBLE);
                holder.cmdMin.setText("MINBURI");
                //holder.cmdMin.setBackgroundColor(Color.BLUE);
                //cmdShared.setBackgroundColor(Color.TRANSPARENT);
                holder.cmdMin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final AlertDialog.Builder adbd = new AlertDialog.Builder(CountHeaderActivity.this);
                        adbd.setTitle("ต้องการยืนยันหรือไม่?");
                        adbd.setMessage("ยืนยันการนับรถที่ มีนบุรี " + MyArrList.get(position).get("ID") + "\n" +
                                MyArrList.get(position).get("REF") );
                        adbd.setNegativeButton("Cancel", null);
                        adbd.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                //update and hide
                                //check order
                                HRef=MyArrList.get(position).get("ID");

                                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("countat", "MV10");
                                editor.putString("form", "count");
                                editor.putString("hdid",  MyArrList.get(position).get("ID") );

                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), CountDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });
                        adbd.show();

                    }

                });
            }else{
                holder.txtID.setLines(1);
                holder.cmdShared.setVisibility(View.INVISIBLE);
                holder.cmdMin.setVisibility(View.INVISIBLE);


            }

            return convertView;

        }

    }

    public class ViewHolder{
        TextView txtID;
        Button cmdShared;
        Button cmdMin;

    }

    public void getData_HD(){


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");

        AndroidNetworking.post(Config.GET_COUNTHD_URL)
                .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .addBodyParameter("user",username)

                .setTag("conifrm job")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        String trip, ref,vin,pickdt,shipfr,shipto,shipfr_name,shipto_name,delid,lat,lon;
                        double d_lat=0,d_lon=0;
                        JSONArray jsonarray = response;
                        try {
                            MyArrList.clear();

                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            String username = prefs.getString("username","");

                            HashMap<String, String> map;
                            map = new HashMap<String, String>();

                            map.put("ID", "ID");
                            map.put("REF", "เลขที่อ้างอิง");
                            map.put("CountDate", "วันที่");



                            MyArrList.add(map);

                            String id,transstatus,recperson,recphone,arrperson,arrphone,ackdt,assigndt,drivers;
                            String item1, item2, item3, ritem1, ritem2,ritem3;
                            Long qty1 =null, qty2=null,qty3=null;

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                map = new HashMap<String, String>();

                                map.put("ID", jsonobject.getString("hdid"));
                                map.put("REF", jsonobject.getString("ref"));
                                map.put("CountDate", jsonobject.getString("countdt"));


                                MyArrList.add(map);
                            }

                            lisView1.setAdapter(new CountryAdapter(CountHeaderActivity.this));

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


/*

    public void getData(){


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        final String username = prefs.getString("username","");
        Global.user = username;

       AndroidNetworking.post(Config.ACCEPT_JOB_URL)
            .addHeaders(Config.HEAD_KEY, Config.HEAD_VALUE)
                .addBodyParameter("id",HRef)
                .addBodyParameter("user",username)
                .setTag("conifrm job")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
        @Override
        public void onResponse(JSONArray response) {
            // do anything with response
            String vin;
            String engine;
            String id;
            String dealer, dealer_name;
            String ltcode="";
            Date scandt;
            String trailer,status;
            String widealer,etadt, inst1,inst2,transdt;
            JSONArray jsonarray = response;
            Vinmaster d;
            Eta e;
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = null;
                try {
                    jsonobject = jsonarray.getJSONObject(i);

                    id = jsonobject.getString("delid");
                    vin = jsonobject.getString("vin");
                    engine = jsonobject.getString("engine");
                    trailer = jsonobject.getString("email");
                    dealer = jsonobject.getString("dealercd");
                    dealer_name = jsonobject.getString("dealername");
                    ltcode = jsonobject.getString("ltcode");
                    etadt = jsonobject.getString("etadt");
                    widealer = jsonobject.getString("widealer");
                    inst1 = jsonobject.getString("inst1");
                    inst2 = jsonobject.getString("inst2");
                    transdt = jsonobject.getString("transdt");
                    status = jsonobject.getString("status");

                    d = new Vinmaster();
                    d.setId(id);
                    d.setDealer(dealer);
                    d.setDealer_name(dealer_name);
                    d.setEngine(engine);
                    d.setTrailer(trailer);
                    d.setVin(vin);
                    d.setLtcode(ltcode);
                    d.setStatus(status);
                    d.setInst1(inst1);
                    d.setInst2(inst2);
                    d.setWidealer(widealer);
                    d.setTransdt(transdt);
                    db.InsertMaster(d);

                    e = new Eta();
                    e.setLtcode(ltcode);
                    e.setEtadt(etadt);
                    db.InsertETA(e);

                    db.updateDealerInstfromMaster();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }

            db.delVIN_Not_Match(ltcode);
            String st_des ="";
            if (ltcode.equals("")){
                st_des="Waiting Job";
            }else{
                st_des ="Receive Job " + ltcode;
            }
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("status", st_des);
            editor.putString("ltcode", ltcode);
            editor.commit();


            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);


        }
        @Override
        public void onError(ANError error) {
            // handle error
            Toast.makeText(getBaseContext(),error.getErrorDetail(),Toast.LENGTH_LONG).show();
        }
    });
}
*/

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
