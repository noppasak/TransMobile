package com.thnopp.it.trans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by THLT88 on 2/7/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 6;

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Database Name
    private static final String DATABASE_NAME = "DB_TRANS";

    // Table Names

    private static final String TABLE_WI = "wi";
    private static final String TABLE_DELIVERY = "delivery";
    private static final String TABLE_DELIVERY_DET = "delivery_det";

    private static final String TABLE_LT = "deliverylt";



    // Common column names

    // delivery column names

    private static final String KEY_DEL_ID = "detid";
    private static final String KEY_DEL_ORDER = "vin";
    private static final String KEY_DEL_DEST = "dest";
    private static final String KEY_DEL_TRANSDT = "transdt";
    private static final String KEY_DEL_ARRIVAL = "arrivaldt";
    private static final String KEY_DEL_DEPART = "departdt";
    private static final String KEY_DEL_STATUS = "status";
    private static final String KEY_DEL_TRAILER = "trailer";
    private static final String KEY_DEL_FUEL = "fuelused";
    private static final String KEY_DEL_DISTANCE = "distance";
    private static final String KEY_DEL_SENDER = "sender";
    private static final String KEY_DEL_RECEIVER = "receiver";
    private static final String KEY_DEL_REMARK = "remark";
    private static final String KEY_DEL_Q1 = "q1";
    private static final String KEY_DEL_Q2 = "q2";
    private static final String KEY_DEL_Q3 = "q3";
    private static final String KEY_DEL_Q4 = "q4";
    private static final String KEY_DEL_Q5 = "q5";
    private static final String KEY_DEL_Q6 = "q6";
    private static final String KEY_DEL_COMMENT = "comment";
    private static final String KEY_DEL_CARRIER = "carrier";
    private static final String KEY_DEL_RECEIVE_BY = "receive_by";
    private static final String KEY_DEL_SOURCE = "source";

    private static final String KEY_ID = "id";
    private static final String KEY_MASTER_STATUS = "status";
    // lt column
    private static final String KEY_LT_ID = "ltid";
    private static final String KEY_LT_STATUS = "status";
    private static final String KEY_DET_PATH = "path";
    private static final String KEY_DET_DATE = "postdt";
    private static final String KEY_PATH = "path";

    private static final String KEY_WI_LASTDATE = "lastupdate";
    private static final String KEY_WI_PATH = "path";
    private static final String KEY_WI_CODE = "wicd";
    private static final String KEY_WI_DESC = "widesc";

    // Table Create Statements
    // delivery det data
    private static final String CREATE_TABLE_DELIVERY = "CREATE TABLE "
            + TABLE_DELIVERY + "(" + KEY_DEL_ID + " TEXT PRIMARY KEY ," + KEY_DEL_ORDER + " TEXT  , " +
            ""  + KEY_DEL_DEST + " TEXT , " + KEY_DEL_TRANSDT + " TEXT , "+
            ""  + KEY_DEL_FUEL + " TEXT , " + KEY_DEL_DISTANCE + " TEXT , "+  ""  + KEY_DEL_CARRIER + " TEXT , " +
            ""  + KEY_DEL_SENDER + " TEXT , " + KEY_DEL_RECEIVER + " TEXT , "+ ""  + KEY_DEL_REMARK + " TEXT , " + KEY_DEL_Q1 + " INTEGER , "+
            ""  + KEY_DEL_Q2 + " INTEGER , " + KEY_DEL_Q3 + " INTEGER , "+ ""  + KEY_DEL_Q4 + " INTEGER , " + KEY_DEL_Q5 + " INTEGER , "+
            ""  + KEY_DEL_Q6 + " INTEGER , " + KEY_DEL_COMMENT + " TEXT , "+ ""  + KEY_DEL_RECEIVE_BY + " TEXT , " + ""  + KEY_DEL_SOURCE + " TEXT , " +
            ""  + KEY_DEL_STATUS + " TEXT , " + KEY_LT_ID + " TEXT , "+ KEY_DEL_ARRIVAL + " DATETIME  , "
            + KEY_DEL_TRAILER + " TEXT  , "  + KEY_DEL_DEPART + " DATETIME" + ")";

    private static final String CREATE_TABLE_DET = "CREATE TABLE "
            + TABLE_DELIVERY_DET + "(" + KEY_DEL_ID + " TEXT ," + KEY_DET_PATH
            + " TEXT PRIMARY KEY ,"  + KEY_DEL_STATUS + " TEXT ," +  KEY_DET_DATE + " DATETIME )";

    // wi table create statement
    private static final String CREATE_TABLE_WI = "CREATE TABLE "
            + TABLE_WI + "(" +  KEY_ID + " TEXT PRIMARY KEY , " +
            ""  + KEY_WI_CODE + " TEXT , " + KEY_WI_DESC  + " TEXT , " +
            ""  + KEY_WI_PATH + " TEXT , " + KEY_WI_LASTDATE  + " TEXT, " + KEY_MASTER_STATUS + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_DELIVERY);
        db.execSQL(CREATE_TABLE_DET);
        db.execSQL(CREATE_TABLE_WI);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_DET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WI);
        // create new tables
        onCreate(db);
    }

    //----DDL ----//
    /*
    add delivery
    delete delivery by id
    delete all delivery
    get delivery by id
     */

    public void InsertDelivery(Vinmaster v){
        SQLiteDatabase db = this.getWritableDatabase();
        Vinmaster chkV = findVIN(v.getId());
        if (chkV != null){
            //do nothing

        }else{
            ContentValues values = new ContentValues();
            values.put(KEY_DEL_ID, v.getId());
            values.put(KEY_DEL_ORDER, v.getVin());
            values.put(KEY_LT_ID, v.getLtcode());
            values.put(KEY_DEL_STATUS, v.getStatus());
            values.put(KEY_DEL_TRAILER, v.getTrailer());
            values.put(KEY_DEL_DEST,v.getDealer_name());
            values.put(KEY_DEL_TRANSDT,v.getDealer());
            values.put(KEY_DEL_FUEL,v.getEngine());
            values.put(KEY_DEL_DISTANCE,v.getRef());
            values.put(KEY_DEL_SENDER,v.getSender());
            values.put(KEY_DEL_RECEIVER,v.getReceiver());
            values.put(KEY_DEL_REMARK,v.getRemark());
            values.put(KEY_DEL_CARRIER,v.getCarrier());
            values.put(KEY_DEL_SOURCE,v.getSource());

            // values.put(KEY_MASTER_SCANDT, v.getVin());
            //  values.put(KEY_MASTER_SCANDT, getDateTime(v.getScandt()));

            db.insert(TABLE_DELIVERY,null,values);
        }

    }

    public void InsertWI(Wi d){
        Date date = new Date();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String lastupdate="";
        List<Wi> lstd = getWI(String.valueOf(d.getId()));
        if (!lstd.isEmpty()){
            for (Wi w : lstd){
                lastupdate = w.getLastupdate();
            }
            if (d.getLastupdate().equals(lastupdate)){
                //do nothing
            }else{
                db.execSQL("delete from "+ TABLE_WI +" where " + KEY_ID + " = '" + d.getId() +"'");

                values.put(KEY_ID, d.getId());
                values.put(KEY_WI_CODE, d.getWicd());
                values.put(KEY_WI_DESC,d.getWi_desc());
                values.put(KEY_PATH, d.getPath());
                values.put(KEY_WI_LASTDATE,d.getLastupdate());
                values.put(KEY_MASTER_STATUS, "A");
                db.insert(TABLE_WI,null,values);
            }

        }else{
            values.put(KEY_ID, d.getId());
            values.put(KEY_WI_CODE, d.getWicd());
            values.put(KEY_WI_DESC,d.getWi_desc());
            values.put(KEY_PATH, d.getPath());
            values.put(KEY_WI_LASTDATE,d.getLastupdate());
            values.put(KEY_MASTER_STATUS, "A");
            db.insert(TABLE_WI,null,values);
        }



    }
    public List<Wi> getWI(String wi) {
        List<Wi> todos = new ArrayList<Wi>();
        String selectQuery = "SELECT  " + KEY_WI_CODE  + ", " + KEY_WI_LASTDATE + " FROM " + TABLE_WI +
                " WHERE " + KEY_ID + "= '" + wi + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Wi td = new Wi();
                td.setWicd(c.getString(c.getColumnIndex(KEY_WI_CODE)));
                td.setLastupdate(c.getString(c.getColumnIndex(KEY_WI_LASTDATE)));
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }
    public List<Wi> getAllWIUnread() {
        List<Wi> todos = new ArrayList<Wi>();
        String selectQuery = "SELECT  * FROM " + TABLE_WI +" WHERE " + KEY_MASTER_STATUS + "= 'A'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Wi td = new Wi();
                td.setId(c.getString((c.getColumnIndex(KEY_ID))));
                td.setPath((c.getString(c.getColumnIndex(KEY_WI_PATH))));
                td.setWi_desc(c.getString(c.getColumnIndex(KEY_WI_DESC)));
                td.setWicd(c.getString(c.getColumnIndex(KEY_WI_CODE)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }
    public void updateWIStatusShow(String wi){

        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE " + TABLE_WI + " SET " + KEY_MASTER_STATUS + " = 'S' WHERE " + KEY_ID + " = '"+ wi +"'";

        db.execSQL(strSQL);

    }

    public void updateWIStatus(String wi){

        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE " + TABLE_WI + " SET " + KEY_MASTER_STATUS + " = 'U' WHERE " + KEY_ID + " = '"+ wi +"'";

        db.execSQL(strSQL);

    }
    public JSONArray SelectWI(String status){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String strSQL="";
            if (status.equals("UNREAD"))
                strSQL = "SELECT  * FROM " + TABLE_WI + " WHERE " + KEY_MASTER_STATUS + " = 'A'";
            else if (status.equals("ALL"))
                strSQL = "SELECT  * FROM " + TABLE_WI;

            Cursor c = db.rawQuery(strSQL, null);
            JSONArray resultSet     = new JSONArray();
            c.moveToFirst();
            while (c.isAfterLast() == false){

                int totalColumn = c.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( c.getColumnName(i) != null )
                    {
                        try
                        {
                            if( c.getString(i) != null )
                            {
                                Log.d("TAG_NAME", c.getString(i) );
                                rowObject.put(c.getColumnName(i) ,  c.getString(i) );
                            }
                            else
                            {
                                rowObject.put( c.getColumnName(i) ,  "" );
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                c.moveToNext();
            }
            c.close();
            Log.d("TAG_NAME", resultSet.toString() );
            return resultSet;
        } catch (Exception e){
            return null;
        }
    }

    public void InsertVIN(Scanvin vin){
        Date cur = new Date();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEL_ID, vin.getId());
        values.put(KEY_DET_PATH, vin.getPath());
        values.put(KEY_DEL_STATUS,"A");
        values.put(KEY_DET_DATE, getDateTime(cur));
        db.insert(TABLE_DELIVERY_DET,null,values);
    }



    private String getDateTime(Date datevalue) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date =datevalue;
        return dateFormat.format(date);
    }


    public void updateDepart(Long v){
        Date cur = new Date();

        SQLiteDatabase db = this.getReadableDatabase();

            Vinmaster chkv = findVINNonAbyVIN(v);

            if (chkv != null){

                    String strSQL = "UPDATE " + TABLE_DELIVERY + " SET " + KEY_DEL_DEPART + " = '" +
                            getDateTime(cur) + "' WHERE " + KEY_DEL_ID + " = "+ v +" and " + KEY_DEL_DEPART  + " IS NULL";

                    db.execSQL(strSQL);
            }

    }

    public void updateArrival(Long v){
        Date cur = new Date();
        //if this order is ceva wh, check all order is complete or not
        //if not, not update
        SQLiteDatabase db = this.getReadableDatabase();
        int count=0, count_a=0;


            count =  chkDepartCEVA(v);
            if (count >=1){
                String strSQL = "UPDATE " + TABLE_DELIVERY + " SET " + KEY_DEL_ARRIVAL + " = '" +
                        getDateTime(cur) + "' WHERE " + KEY_DEL_ID + " = "+ v +" and " + KEY_DEL_ARRIVAL  + " IS NULL";
                db.execSQL(strSQL);

            }




    }

    public void updateVIN_upload_status(Long v){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE " + TABLE_DELIVERY + " SET " + KEY_DEL_STATUS + " = 'U'" +
                 " WHERE " + KEY_DEL_ID + " = "+ v +"";

        db.execSQL(strSQL);

    }

    public void updateVIN_det_upload_status(String path){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE " + TABLE_DELIVERY_DET + " SET " + KEY_DEL_STATUS + " = 'U'" +
                " WHERE " + KEY_DET_PATH + " = '"+ path +"'";

        db.execSQL(strSQL);

    }

    public void DeleteVIN(Long v){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "DELETE FROM " + TABLE_DELIVERY +
                " WHERE " + KEY_DEL_ID + " = "+ v +" AND " + KEY_DEL_DEPART + " IS NULL" ;

        db.execSQL(strSQL);

    }


    public void DeleteVIN_Old(){
        SQLiteDatabase db = this.getReadableDatabase();
        Date now = new Date();
        Long days = 5L;
        now.setTime( now.getTime() - days*1000*60*60*24 );

        android.text.format.DateFormat df = new android.text.format.DateFormat();


        String strSQL = "DELETE FROM " + TABLE_DELIVERY +
                " WHERE " + KEY_DEL_DEPART + "<= '" + df.format("yyyy-MM-dd hh:mm:ss",now) + "'" ;

        db.execSQL(strSQL);

        strSQL = "DELETE FROM " + TABLE_DELIVERY_DET +
                " WHERE " + KEY_DET_DATE + "<= '" + df.format("yyyy-MM-dd hh:mm:ss",now) + "'" ;

        db.execSQL(strSQL);

    }


    public JSONArray SelectDelivery(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String strSQL = "SELECT  * FROM " + TABLE_DELIVERY + " ORDER BY " + KEY_DEL_TRANSDT;
            Cursor c = db.rawQuery(strSQL, null);
            JSONArray resultSet     = new JSONArray();
            c.moveToFirst();
            while (c.isAfterLast() == false){

                int totalColumn = c.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( c.getColumnName(i) != null )
                    {
                        try
                        {
                            if( c.getString(i) != null )
                            {
                                rowObject.put(c.getColumnName(i) ,  c.getString(i) );
                            }
                            else
                            {
                                rowObject.put( c.getColumnName(i) ,  "" );
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                c.moveToNext();
            }
            c.close();
            return resultSet;
        } catch (Exception e){
            return null;
        }
    }

    public JSONArray SelectLT(String user){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String strSQL = "SELECT  * FROM " + TABLE_DELIVERY + " WHERE " + KEY_DEL_STATUS + " = 'A' "
                    +" AND " + KEY_DEL_ARRIVAL + " IS NULL  AND " + KEY_DEL_TRAILER +" = '" + user +"'";
            Cursor c = db.rawQuery(strSQL, null);
            JSONArray resultSet     = new JSONArray();
            c.moveToFirst();
            while (c.isAfterLast() == false){

                int totalColumn = c.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( c.getColumnName(i) != null )
                    {
                        try
                        {
                            if( c.getString(i) != null )
                            {
                                rowObject.put(c.getColumnName(i) ,  c.getString(i) );
                            }
                            else
                            {
                                rowObject.put( c.getColumnName(i) ,  "" );
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                c.moveToNext();
            }
            c.close();
            return resultSet;
        } catch (Exception e){
            return null;
        }
    }





    public int chkRecord_Pending(Long id){
        int result = 0;
        List<Scanvin> todos = new ArrayList<Scanvin>();
        String selectQuery = "SELECT  COUNT(" + KEY_DEL_ID +") AS " + KEY_DEL_ID + " FROM " + TABLE_DELIVERY + " WHERE "
                + KEY_DEL_STATUS + " = 'A' AND " + KEY_LT_ID + " = "+ id + "";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                //  result = Long.parseLong(c.getString(c.getColumnIndex(KEY_DESC1)));
                result = c.getInt(c.getColumnIndex(KEY_DEL_ID)); // getLong(c,c.getColumnIndex(KEY_DESC1));
            } while (c.moveToNext());
        }

        return result;
    }


    public int chkDepartCEVA(Long id){
        int result = 0;
        List<Scanvin> todos = new ArrayList<Scanvin>();
        String selectQuery = "SELECT  COUNT(" + KEY_DEL_ORDER + ") AS " + KEY_DEL_ORDER + " FROM " + TABLE_DELIVERY + " WHERE "
                + KEY_DEL_ID + " = " + id + " AND "
                + KEY_DEL_DEPART + " IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_DEL_ORDER)); // getLong(c,c.getColumnIndex(KEY_DESC1));
            } while (c.moveToNext());
        }

        return result;
    }



    public int chkPendingPOD(){
        int result = 0;
        List<Scanvin> todos = new ArrayList<Scanvin>();
        String selectQuery = "SELECT  COUNT(" + KEY_DEL_ORDER + ") AS " + KEY_DEL_ORDER + " FROM " + TABLE_DELIVERY + " WHERE "
                + KEY_DEL_STATUS + "='A' AND " + KEY_DEL_DEPART + " IS NULL AND " + KEY_DEL_ARRIVAL + " IS NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_DEL_ORDER)); // getLong(c,c.getColumnIndex(KEY_DESC1));
            } while (c.moveToNext());
        }

        return result;
    }

    public int chkPendingPOD_Incomplete(){
        int result = 0;
        List<Scanvin> todos = new ArrayList<Scanvin>();
        String selectQuery = "SELECT  COUNT(" + KEY_DEL_ORDER + ") AS " + KEY_DEL_ORDER + " FROM " + TABLE_DELIVERY + " WHERE "
                + KEY_DEL_STATUS + "='A' AND " + KEY_DEL_DEPART + " IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                result = c.getInt(c.getColumnIndex(KEY_DEL_ORDER)); // getLong(c,c.getColumnIndex(KEY_DESC1));
            } while (c.moveToNext());
        }

        return result;
    }


    public void delPendingPOD_Incomplete(){

        SQLiteDatabase db = this.getReadableDatabase();

        String strSQL = "DELETE FROM " + TABLE_DELIVERY +
                " WHERE "  + KEY_DEL_STATUS + "='A' AND " + KEY_DEL_DEPART + " IS NULL";

        db.execSQL(strSQL);

    }


    public long getLong(Cursor cursor, int columnIndex )
    {
        long value = 0;

        try
        {
            if ( !cursor.isNull( columnIndex ) )
            {
                value = cursor.getLong( columnIndex );
            }
        }
        catch ( Throwable tr )
        {

        }

        return value;
    }
    public List<Vinmaster> getPOD() {
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_STATUS + "='A' and "
                + KEY_DEL_DEPART + " IS NOT NULL";

       // String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_STATUS + "='A'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        Date d=new Date();
        String s;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                if(c.getString(c.getColumnIndex(KEY_DEL_ARRIVAL))==null){
                    s="N";
                }else{
                    s= c.getString(c.getColumnIndex(KEY_DEL_ARRIVAL));

                    try {
                        d=  dateFormat.parse(s);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                        s="N";
                    }
                }



                Date d1=new Date();
                String s1= c.getString(c.getColumnIndex(KEY_DEL_DEPART));

                    try {
                        d1=  dateFormat.parse(s1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                        s1 = "N";
                    }



                Vinmaster td = new Vinmaster();
                td.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                if (!s.equals("N"))
                    td.setArrivaldt(d);
                if (!s1.equals("N"))
                    td.setScandt(d1);
                td.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                td.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));




                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public List<Scanvin> getPOD_Det() {
        List<Scanvin> todos = new ArrayList<Scanvin>();
        String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY_DET +" WHERE " + KEY_DEL_STATUS + "='A'";

        // String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_STATUS + "='A'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Scanvin td = new Scanvin();
                td.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                td.setPath(c.getString(c.getColumnIndex(KEY_DET_PATH)));

                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public List<Vinmaster> getVIN_Old_data() {
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        Date m = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(m);
        cal.add(Calendar.DATE, -5); // 10 is the days you want to add or subtract
        m = cal.getTime();


        String t_d = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd H:mm:ss", m));


        String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_DEPART + " IS NOT NULL  AND "
                +  KEY_DEL_DEPART + " <= '" + t_d +"';";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {


                Vinmaster td = new Vinmaster();
                td.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                td.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                td.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));


                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public JSONArray listPendingOrder(String trailer){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String strSQL = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_DEPART + " IS NOT NULL  AND "
                    +  KEY_DEL_TRAILER + " = '" + trailer +"';";

            Cursor c = db.rawQuery(strSQL, null);
            JSONArray resultSet     = new JSONArray();
            c.moveToFirst();
            while (c.isAfterLast() == false){

                int totalColumn = c.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( c.getColumnName(i) != null )
                    {
                        try
                        {
                            if( c.getString(i) != null )
                            {
                                rowObject.put(c.getColumnName(i) ,  c.getString(i) );
                            }
                            else
                            {
                                rowObject.put( c.getColumnName(i) ,  "" );
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                c.moveToNext();
            }
            c.close();
            return resultSet;
        } catch (Exception e){
            return null;
        }
    }


    public Vinmaster findVIN(Long id){
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        SQLiteDatabase db = this.getReadableDatabase();
        Vinmaster C1 = null;
        Cursor cursor;
        String selectQuery = "SELECT   * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_ID + " = " + id + "";


        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                C1 = new Vinmaster();
                C1.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                C1.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                C1.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));
                todos.add(C1);
            } while (c.moveToNext());
        }

        return C1;


    }

    public Vinmaster findVINbyVIN(String id){
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        SQLiteDatabase db = this.getReadableDatabase();
        Vinmaster C1 = null;
        Cursor cursor;
        String selectQuery = "SELECT   * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_ORDER + " = '" + id + "'";

        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                C1 = new Vinmaster();
                C1.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                C1.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                C1.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));

                todos.add(C1);
            } while (c.moveToNext());
        }

        return C1;


    }

    public Vinmaster findVINNonAbyVIN(Long id){
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        SQLiteDatabase db = this.getReadableDatabase();
        Vinmaster C1 = null;
        Cursor cursor;
        String selectQuery = "SELECT   * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_ID + " = " + id + " AND "
                + "" + KEY_DEL_ARRIVAL + " IS NULL";

        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                C1 = new Vinmaster();
                C1.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                C1.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                C1.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));

                todos.add(C1);
            } while (c.moveToNext());
        }

        return C1;


    }

    public Vinmaster findAvaialble_Order(){
        List<Vinmaster> todos = new ArrayList<Vinmaster>();
        SQLiteDatabase db = this.getReadableDatabase();
        Vinmaster C1 = null;
        Cursor cursor;
        String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY +" WHERE " + KEY_DEL_STATUS + " ='A' AND "
                + "" + KEY_DEL_ARRIVAL + " IS NULL";

        Cursor c = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                C1 = new Vinmaster();
                C1.setId(c.getLong((c.getColumnIndex(KEY_DEL_ID))));
                C1.setVin(c.getString(c.getColumnIndex(KEY_DEL_ORDER)));
                C1.setLtcode(c.getString(c.getColumnIndex(KEY_LT_ID)));
                C1.setDealer_name(c.getString(c.getColumnIndex(KEY_DEL_DEST)));
                C1.setRef((c.getString(c.getColumnIndex(KEY_DEL_DISTANCE))));
                C1.setEngine((c.getString(c.getColumnIndex(KEY_DEL_FUEL))));
                C1.setSource((c.getString(c.getColumnIndex(KEY_DEL_SOURCE))));
                todos.add(C1);
            } while (c.moveToNext());
        }

        return C1;


    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
