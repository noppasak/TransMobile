package com.thnopp.it.trans;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;


public class WiAdapter  extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<Wi> WiArrayList;
    public ArrayList<Wi> orig;

    public WiAdapter(Context context, ArrayList<Wi> WiArrayList) {
        super();
        this.context = context;
        this.WiArrayList = WiArrayList;
    }

    public class WiHolder
    {
        TextView id;
        TextView wicd;
        TextView wi_desc;
        TextView path;
        TextView lastupdate;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Wi> results = new ArrayList<Wi>();
                if (orig == null)
                    orig = WiArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Wi g : orig) {
                            if (g.getWi_desc().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                WiArrayList = (ArrayList<Wi>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return WiArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return WiArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
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
        txtID.setText(WiArrayList.get(position).getId());


        // ColDealer
        TextView txtCode = (TextView) convertView.findViewById(R.id.colETA);
        txtCode.setText(WiArrayList.get(position).getWicd());

        if (!txtCode.getText().equals("Name")){
            final Button cmdShared = (Button) convertView.findViewById(R.id.CmdProcess);
            cmdShared.setVisibility(View.VISIBLE);
            cmdShared.setText("อ่าน");
            cmdShared.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DatabaseHelper db;
                    db = DatabaseHelper.getInstance(context);

                    db.updateWIStatus(WiArrayList.get(position).getId());
                    Intent intent = new Intent(context, WIActivity.class);
                    intent.putExtra("WI", WiArrayList.get(position).getPath());
                    context.startActivity(intent);
                }
            });
        }else{
            Button cmdShared = (Button) convertView.findViewById(R.id.CmdProcess);
            cmdShared.setVisibility(View.INVISIBLE);
        }



        return convertView;

    }

}

  /*  @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WiHolder holder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_wi_column, parent, false);
            holder=new WiHolder();
            holder.id=(TextView) convertView.findViewById(R.id.);
            holder.age=(TextView) convertView.findViewById(R.id.txtAge);
            convertView.setTag(holder);
        }
        else
        {
            holder=(WiHolder) convertView.getTag();
        }

        holder.name.setText(WiArrayList.get(position).getName());
        holder.age.setText(String.valueOf(WiArrayList.get(position).getAge()));

        return convertView;

    }

    */

