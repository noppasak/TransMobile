package com.thnopp.it.trans;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/*import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;*/


/**
 * Created by THLT88 on 3/14/2018.
 */

public class ScheduleService_long extends Service implements LocationListener {
    // constant
    // old public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    public static final long NOTIFY_INTERVAL = 24 * 60 * 60 * 1000; // 1 days

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    String param, status;
    DatabaseHelper db;
    List<Scanvin> lst;
    List<Vinmaster> lst_pod, lst_pod_del,lst_pod_user;
    List<Dealerinst> lst_inst;



    Intent intent;

    //end for location tracking
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    String username = prefs.getString("username","");
                    Global.user = username;
                    // display toast
                 /*   Toast.makeText(getApplicationContext(), getDateTime(),
                            Toast.LENGTH_SHORT).show();
*/
                    //do here
                    status = "A";
                    param = username;


                 /*   db = DatabaseHelper.getInstance(getApplicationContext());


                    new ScheduleService_long.GetWIMaster().execute();
                    
                    db.closeDB();*/

                    //clear old file older than 4 day


                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

        private void delOldFile(){
            int x = 4;
            File externalStorageDir =  Environment.getExternalStorageDirectory();
            File folder = new File(externalStorageDir, "Picture");

            /*File folder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES),  Config.IMAGE_DIRECTORY_NAME); //This is just to cast to a File type since you pass it as a String
            */
            File[] filesInFolder = folder.listFiles();  // This returns all the folders and files in your path

            try{
                for (File file : filesInFolder) { //For each of the entries do:
                    if (!file.isDirectory()) { //check that it's not a dir
                        long diff = new Date().getTime() - file.lastModified();

                        if (diff > x * 24 * 60 * 60 * 1000) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e){
                Log.e("", "Delete Old File", e);
            }
        }

    }

    public class GetWIMaster extends AsyncTask<Void, Void, Void> {
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
                result = rs.GetJsonString(param,"WI");
            }catch (Exception ex){
//                Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
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
                if (status.equals("A")) {
                    // get data to buildings
                    try {
                        // String id, assetid, desc, branch, building, department, floor, room, location;
                        String id;
                        String wicd;
                        String widesc;
                        String path;
                        String lastupdate;

                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getString("id");
                            wicd = jsonobject.getString("wicd");
                            widesc = jsonobject.getString("widesc");
                            path = jsonobject.getString("path");
                            lastupdate = jsonobject.getString("lastupdate");


                            Wi d = new Wi();
                            d.setId(id);
                            d.setWicd(wicd);
                            d.setWi_desc(widesc);
                            d.setPath(path);
                            d.setLastupdate(lastupdate);

                            db.InsertWI(d);

                        }
                    } catch (JSONException e) {
                        Log.e("", "unexpected JSON exception", e);
                        //  Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    // Toast.makeText(getBaseContext(), "Asset Load Complete", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }


/*
    private void showNotification() {
        Context context = getApplicationContext();// MainActivity.this;
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("แจ้งเวลารับงาน")
                .setContentText("แจ้งเวลาให้ไปรับรถที่ Motor Pool")
                .setContentIntent(getPendingIntent(context , "Hello"))
                .setContentInfo("Info");

        notificationManager.notify(*/
/*notification id*//*
1, notificationBuilder.build());

    }
*/


/*

    private void showNotification_Inst(String dealer, int count) {
        Context context = getApplicationContext();// MainActivity.this;
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_"+ dealer;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("แจ้ง WI เพิ่มเติม")
                .setContentText("WI เพิ่มเติมของ Dealer" + dealer)
                .setContentIntent(getPendingIntent1(context , dealer))
                .setContentInfo("Info");

        notificationManager.notify(*/
/*notification id*//*
count, notificationBuilder.build());

    }
*/



}
