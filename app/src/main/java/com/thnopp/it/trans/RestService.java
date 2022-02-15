package com.thnopp.it.trans;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Seyha Uchiha on 1/2/2018.
 */

public class RestService {
    HttpEntity resEntity;
    MultipartEntity entity;
    private ProgressDialog pDialog;
    private Context context;
    private HttpClient httpclient;
    int chk;
    String Url;
    String Company;
    String User;
    String Pass;
    DatabaseHelper db;
    List<Scanvin> lst;

    public RestService(Context context,
                       String url,
                       String company,
                       String user,
                       String pass) {
        this.context = context;
/*        pDialog = new ProgressDialog(context, R.style.MyThemeDialog);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setCancelable(false);*/
        // pDialog.show();
        httpclient = new DefaultHttpClient();


//        SharedPreferences prefs = context.getSharedPreferences("ConfigSerivce", MODE_PRIVATE);
//        if (prefs != null) {
//            String url = prefs.getString("Url", "No");
//            String company = prefs.getString("CompanyID", "d"); //
//            String user = prefs.getString("User", "No"); //
//            String pass = prefs.getString("Password", "No"); //
        //    if(!url.equals("No")) {
        this.Url = url;
        this.Company = company;
        this.User = user;
        this.Pass = pass;
        //    }
        // }

        //   this.Url=Url;
    }


    // Login to API
    public Result LogIn() {
        Result result = new Result();

        InputStream inputStream = null;
        try {

            // 1. create HttpClient
            httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(Config.LOGIN_URL);
            String jsons = "";

            // 3. build jsonObject
            JSONObject json = new JSONObject();


            json.put("name", User);
            json.put("password", Pass);


            // 4. convert JSONObject to JSON to String
            jsons = json.toString();
            jsons = "[" + jsons + "]";
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(jsons, "UTF-8");
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            //  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
          //  httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // 9. receive response as inputStream
            // if(httpResponse.getEntity().;)

            if (httpResponse.getStatusLine().getStatusCode() == 202) {
                result.Check = true;
            } else {
                inputStream = httpResponse.getEntity().getContent();
                //  String jsonss = convertInputStreamToString(inputStream);
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                if (reader != null) {
                    try {
                        reader.beginObject();

                        while (reader.hasNext()) {

                            String name = reader.nextName();
                            if (name.equals("exceptionMessage")) {
                                result.Message = reader.nextString(); // Toast.makeText(view.getContext(),name,Toast.LENGTH_LONG).show();
                                //   reader.endObject();
                                break;
                            } else {
                                reader.skipValue();
                            }

                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

        } catch (Exception e) {
            result.Message = e.getMessage();//  Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public boolean Connect() {


        InputStream inputStream = null;
        try {

            // 1. create HttpClient
            httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(Config.LOGIN_URL);

            String jsons = "";

            // 3. build jsonObject
            JSONObject json = new JSONObject();


            json.put("name", User);
            json.put("password", Pass);
            json.put("company", Company);


            // 4. convert JSONObject to JSON to String
            jsons = json.toString();
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(jsons, "UTF-8");
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            //  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream

            // if(httpResponse.getEntity().;)

            if (httpResponse.getStatusLine().getStatusCode() == 202) {
                return true;
            } else {
                inputStream = httpResponse.getEntity().getContent();
                String jsonss = convertInputStreamToString(inputStream);

                Toast.makeText(context, "Fail to Log in : " + jsonss, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Fail to Log in : " + e.getMessage(), Toast.LENGTH_LONG).show();
            //  Log.d("InputStream", e.getLocalizedMessage());
        }

        return false;
    }




    public String DelPOD(List<Vinmaster> lst) {
        String R = "OK";
        String VIN, ltcode, dealer, user;

        db = new DatabaseHelper(context);
        int res;
        try {
            for (Vinmaster t : lst) {
                //db.delALLVIN_by_VIN(t.getVin());
            }
            db.closeDB();
            return R;
        } catch (Exception ex) {
            return null;
        }


    }

    public String PostPOD_F(List<Vinmaster> lst) {
        String R = "OK";
        String VIN, ltcode, user, depart, arrival;
        Long id;
        db = DatabaseHelper.getInstance(context);
        int res;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
            String t_a, t_d;
            for (Vinmaster t : lst) {
                /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String arrival = sdf.format(t.getScandt());*/

                if (t.getArrivaldt()==null)
                    t_a = null;
                else
                    t_a = sdf.format(t.getArrivaldt());

                t_d = sdf.format(t.getScandt());

                id = t.getId();
                ltcode = t.getLtcode().toString();
                VIN = t.getVin().toString();
                depart = t_d;
                arrival = t_a;



                res = postPOD(depart,arrival, VIN, ltcode, id);
                if (res == 0) {
                    R = null;
                    //   db.updateVIN_statusPOD(VIN);
                } else {
                    if (t.getArrivaldt()!=null)
                    //ok
                    db.updateVIN_upload_status(id);
                }

            }
            db.closeDB();
            return R;
        } catch (Exception ex) {
            return null;
        }


    }


    public int postPOD(String depart, String arrival, String VIN, String ltcode, Long id) {
        int result = 0;
        try {
            String sResponse = null;

            int i = 0;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Config.CHK_VIN);
            entity = new MultipartEntity();

            if (arrival==null)
                arrival="";

            entity.addPart("vin",
                    new StringBody(VIN));
            entity.addPart("arrival",
                    new StringBody(arrival));
            entity.addPart("depart",
                    new StringBody(depart));
            entity.addPart("ltcode", new StringBody(ltcode));
            entity.addPart("id", new StringBody(id.toString()));

            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);
            //HttpResponse response = httpClient.execute(httpPost,localContext);
            //sResponse = EntityUtils.getContentCharSet(response.getEntity());
            String r = "true", r2 = "ok";
            if (sResponse.contains(r.toLowerCase())) {
                result = 0;
            } else if (sResponse.contains(r2.toUpperCase())) {
                result = 1;//ok
            }
            System.out.println("sResponse : " + sResponse);
        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }

    public String postDispatch( String VIN, String user) {
        String result = "";
        try {
            String sResponse = null;

            int i = 0;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Config.CHK_VIN);
            entity = new MultipartEntity();


            entity.addPart("vin",
                    new StringBody(VIN));
            entity.addPart("user",
                    new StringBody(user));

            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);
            //HttpResponse response = httpClient.execute(httpPost,localContext);
            //sResponse = EntityUtils.getContentCharSet(response.getEntity());
            String r2 = "ok";
            if (sResponse.contains(r2.toUpperCase())) {
                result = "ok";//ok
            }else{
                result = sResponse.toString();
            }
            System.out.println("sResponse : " + sResponse);
        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }

    public String postDispatch_rem( String VIN, String user,String rem) {
        String result = "";
        try {
            String sResponse = null;

            int i = 0;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Config.UPDATE_VIN_REM);
            entity = new MultipartEntity();


            entity.addPart("vin",
                    new StringBody(VIN));
            entity.addPart("user",
                    new StringBody(user));
            entity.addPart("rem",
                    new StringBody(rem));
            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);
            //HttpResponse response = httpClient.execute(httpPost,localContext);
            //sResponse = EntityUtils.getContentCharSet(response.getEntity());
            String r2 = "ok";
            if (sResponse.contains(r2.toUpperCase())) {
                result = "ok";//ok
            }else{
                result = sResponse.toString();
            }
            System.out.println("sResponse : " + sResponse);
        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }

    public String postDispatch_1( String VIN1,String VIN, String user) {
        String result = "";
        try {
            String sResponse = null;

            int i = 0;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Config.UPDATE_VIN_REM_1);
            entity = new MultipartEntity();

            entity.addPart("vin1",
                    new StringBody(VIN1));
            entity.addPart("vin",
                    new StringBody(VIN));
            entity.addPart("user",
                    new StringBody(user));

            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);
            //HttpResponse response = httpClient.execute(httpPost,localContext);
            //sResponse = EntityUtils.getContentCharSet(response.getEntity());
            String r2 = "ok";
            if (sResponse.contains(r2.toUpperCase())) {
                result = "ok";//ok
            }else{
                result = sResponse.toString();
            }
            System.out.println("sResponse : " + sResponse);
        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }

    public String postRetrofit( String VIN,String Result, String user) {
        String result = "";
        try {
            String sResponse = null;

            int i = 0;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Config.UPDATE_RETROFIT);
            entity = new MultipartEntity();

            entity.addPart("vin",
                    new StringBody(VIN));
            entity.addPart("result",
                    new StringBody(Result));
            entity.addPart("user",
                    new StringBody(user));

            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);
            //HttpResponse response = httpClient.execute(httpPost,localContext);
            //sResponse = EntityUtils.getContentCharSet(response.getEntity());
            String r2 = "ok";
            if (sResponse.contains(r2.toUpperCase())) {
                result = "ok";//ok
            }else{
                result = sResponse.toString();
            }
            System.out.println("sResponse : " + sResponse);
        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }


    public String delVIN() {
        String result = "";
        // db.delVIN();


        return result;

    }




    public String GetJsonString(String EntityName, String Typ) {

        String sResponse = null;

        try {
            HttpGet httpPost = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            if (Typ.equals("VIN"))
                httpPost = new HttpGet(Config.LOAD_VIN_URL + "?user=" + EntityName);
            else if (Typ.equals("WI"))
                httpPost = new HttpGet(Config.WI_URL + "?user=CVL000");
            /*entity = new MultipartEntity();
            entity.addPart("user", new StringBody(EntityName));

            httpPost.setEntity(entity);*/
            httpPost.setHeader(Config.HEAD_KEY, Config.HEAD_VALUE);
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
            sResponse = EntityUtils.toString(resEntity);

            // return  sResponse;

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            return null;
        }

        // 11. return result
        return sResponse;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public String PostDataL(List<Scanvin> lst){
        String R="";
        String VIN, TID, Path;
        int Desc;
        db = new DatabaseHelper(context);
        int res;

        for (Scanvin t : lst) {
            TID = t.getId().toString();
            VIN = "";
            Path = t.getPath().toString();
            Desc = 0; //t.getDesc();

            res = uploadData(TID, VIN, Path,Desc);
            if (res == 0) {
                R= "Incomplete";
            } else {
                //ok
                // db.delVIN1(Path);
                // Not delete delivery_det waiting schedule delete
                db.updateVIN_det_upload_status(Path);
            }

        }
        return  R;

    }
    public int uploadData(String ID, String VIN, String filePath,int Desc){
        int result = 0;
        try {
            String sResponse = null;


            int i = 0;
            if (!(file(filePath).exists() && !file(filePath).isDirectory())) {
                //not found the file
            }else{
                Bitmap bitmap = decodeFile1(file(filePath)); // decodeFile1(file(filePath));
                if (bitmap==null){
                    //   db.delVIN1(filePath);
                }else{
                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpContext localContext = new BasicHttpContext();
                        HttpPost httpPost = new HttpPost(Config.FILE_UPLOAD_URL);
                        entity = new MultipartEntity();

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                        byte[] data = bos.toByteArray();

                        entity.addPart("vin",
                                new StringBody(VIN));
                        entity.addPart("delid",
                                new StringBody(ID));
                        entity.addPart("type", new StringBody(String.valueOf(Desc)));
                        entity.addPart("image", new ByteArrayBody(data,
                                "image/jpeg", filePath));

                        httpPost.setEntity(entity);
                        HttpResponse response = httpClient.execute(httpPost);
                        resEntity = response.getEntity();
                        sResponse = EntityUtils.toString(resEntity);
                        //HttpResponse response = httpClient.execute(httpPost,localContext);
                        //sResponse = EntityUtils.getContentCharSet(response.getEntity());

                        String r = "true", r2 = "false";
                        if (sResponse.contains(r.toLowerCase())){
                            result  = 0;

                        }else if (sResponse.contains(r2.toLowerCase())){
                            result = 1;//ok
                            db.updateVIN_det_upload_status(filePath);
                            File fdelete = new File(filePath);
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    System.out.println("file Deleted :" + filePath);
                                } else {
                                    System.out.println("file not Deleted :" + filePath);
                                }
                            }
                        }
                        System.out.println("sResponse : " + sResponse);
                    }catch (Exception e) {

                        Log.e(e.getClass().getName(), e.getMessage(), e);

                    }
                }
            }
                //Bitmap bitmap = decodeFile(filePath);


        } catch (Exception e) {

            Log.e(e.getClass().getName(), e.getMessage(), e);

        }

        return result;
    }

    private Bitmap decodeFile1(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=58024;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 58024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 2;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        //Bitmap bitmap1 = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(filePath,o2));
        return bitmap;
    }


    public File file(String file){

        int drawableResourceId =
                context.getResources().getIdentifier("dmslog",
                        "drawable",
                        context.getPackageName());
        //String filePath = context.getFilesDir().getPath().toString() + "/dmslog.png";
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
        // String filePath = mediaStorageDir.getPath()+File.separator + file;

        String filePath = file;
        //String filePath = context.getFilesDir().getPath().toString() + "/"+ Config.GASSET +".png";

        File f = new File(filePath);
        /* if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        return f;

    }

}

