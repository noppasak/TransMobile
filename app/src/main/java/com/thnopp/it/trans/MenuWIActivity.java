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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by THLT88 on 10/20/2017.
 */

public class MenuWIActivity extends Activity {

    ArrayList<HashMap<String, String>> MyArrList;
    ArrayList<HashMap<String, String>> orig;
    ArrayList<HashMap<String, String>> new_array;
    String t_dealer,t_trailer;
    String sXML;
    SearchView sv;

    WiAdapter wi_adapter;
    ArrayList<WiAdapter> wi_arraylist;

    ListView lisView1;
    DatabaseHelper db;
    ProgressBar bar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wi);


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        Integer client = prefs.getInt("client",0);
        String conf = prefs.getString("conf","");
        Global.user = username;
        Global.client = client;
        Global.conf = conf;

        db = DatabaseHelper.getInstance(getApplicationContext());
        bar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lisView1 = (ListView) findViewById(android.R.id.list);

        MyArrList = new ArrayList<HashMap<String, String>>();
        new_array = new ArrayList<HashMap<String, String>>();

        sv = (SearchView) findViewById(R.id.search_view);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                //callSearch(newText);
                new MenuWIActivity.read_data1pd().execute();
//              }
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });


        TextView cap = (TextView) findViewById(R.id.textView1);
        cap.setText("Work Instruction");

        bar1.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new MenuWIActivity.read_data1pd().execute();
        Button btnReload = (Button) findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bar1.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new MenuWIActivity.read_data1pd().execute();
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
                convertView = inflater.inflate(R.layout.activity_eta_column, null);

            }

            // ColLT
            TextView txtID = (TextView) convertView.findViewById(R.id.ColLT);
            txtID.setText(MyArrList.get(position).get("Code"));


            // ColDealer
            TextView txtCode = (TextView) convertView.findViewById(R.id.colETA);
            txtCode.setText(MyArrList.get(position).get("Name"));

            if (!txtCode.getText().equals("Name")){
                final Button cmdShared = (Button) convertView.findViewById(R.id.CmdProcess);
                cmdShared.setVisibility(View.VISIBLE);
                cmdShared.setText("อ่าน");
                cmdShared.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        db.updateWIStatus(MyArrList.get(position).get("ID"));
                        Intent intent = new Intent(getBaseContext(), WIActivity.class);
                        intent.putExtra("WI", MyArrList.get(position).get("Path"));
                        startActivity(intent);
                    }
                });
            }else{
                Button cmdShared = (Button) convertView.findViewById(R.id.CmdProcess);
                cmdShared.setVisibility(View.INVISIBLE);
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
                result = db.SelectWI("ALL");

            }catch (Exception ex){
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
                   /* map = new HashMap<String, String>();

                    map.put("ID", "ID");
                    map.put("Code", "Code");
                    map.put("Name", "Name");
                    map.put("Path", "Path");
                    *//*map.put("", "");*//*
                    MyArrList.add(map);*/



                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        if (sv.getQuery().toString().equals("")){
                            map = new HashMap<String, String>();

                            map.put("ID", jsonobject.getString("id"));
                            map.put("Code", jsonobject.getString("wicd"));
                            map.put("Name", jsonobject.getString("widesc"));
                            map.put("Path", jsonobject.getString("path"));
                            MyArrList.add(map);
                        }else
                        if ( jsonobject.getString("widesc").toString().toLowerCase().contains(sv.getQuery().toString().toLowerCase())){

                            map = new HashMap<String, String>();

                            map.put("ID", jsonobject.getString("id"));
                            map.put("Code", jsonobject.getString("wicd"));
                            map.put("Name", jsonobject.getString("widesc"));
                            map.put("Path", jsonobject.getString("path"));
                            MyArrList.add(map);
                        }


                    }



                    lisView1.setAdapter(new MenuWIActivity.CountryAdapter(MenuWIActivity.this));


                 //   lisView1.setTextFilterEnabled(true);

                    bar1.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } catch (JSONException e) {
                    Log.e("", "unexpected JSON exception", e);
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    bar1.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                // Toast.makeText(getBaseContext(), "Asset Load Complete", Toast.LENGTH_SHORT).show();

            }


        }

    }
/*


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                new_array.clear();
                HashMap<String, String> map = new HashMap<String, String>();;
                String t_id="", t_cd="", t_desc="", t_part="";

                if (orig == null)
                    orig = MyArrList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (Map<String, String> entry : orig) {
                            for (String key : entry.keySet()) {
                                String value = entry.get(key);
                               */
/* Boolean value = entry.get(key);
                                System.out.println("key = " + key);
                                System.out.println("value = " + value);*//*

                                if (key.equals("id"))
                                    t_id = value;
                                if (key.equals("wicd"))
                                    t_cd = value;
                                if (key.equals("widesc"))
                                    t_desc = value;
                                if (key.equals("path"))
                                    t_part = value;


                                if (key.equals("widesc")){
                                    if (value.toLowerCase()
                                            .contains(constraint.toString())){
                                        map = new HashMap<String, String>();

                                        map.put("ID", t_id);
                                        map.put("Code", t_cd);
                                        map.put("Name", t_desc);
                                        map.put("Path", t_part);

                                    }
                                    new_array.add(map);
                                }

                            }
                        }


                    }
                    oReturn.values = new_array;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
               // MyArrList = (ArrayList<Wi>) results.values;
            //MyArrList = (ArrayList<Wi>) results.values;
                MyArrList =   new_array;

              */
/*  notifyDataSetChanged();*//*

            }
        };
    }
*/

 /*   public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
*/

    @Override
    public void onBackPressed() {
    }

}
