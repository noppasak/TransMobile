package com.thnopp.it.trans;

/**
 * Created by CEVAUser on 5/30/2017.
 */

public class Config {

    //rest java
    public static final String LOGIN_URL = "http://192.168.1.33:8080/api/login1"; // "http://203.154.71.73:8080/trans/api/login";
    public static final String LOAD_VIN_URL = "http://192.168.1.33:8080/api/vin";//  = "http://THLT5CG7312T5F/tms/loadvin.php";
    public static final String CHK_VIN = "http://192.168.1.33:8080/api/updatevin";
    public static final String UPDATE_VIN_REM = "http://192.168.1.33:8080/api/updatevin_rem";// "http://203.154.71.73:8080/trans/api/updatevin";

    public static final String UPDATE_VIN_REM_1 = "http://192.168.1.33:8080/api/updatevin_1";
    public static final String UPDATE_RETROFIT = "http://192.168.1.33:8080/api/retrofit/update";

    public static final String GET_COUNTHD_URL = "http://192.168.1.33:8080/api/count/getcounthd";
    public static final String CHK_COUNT_VIN_URL = "http://192.168.1.33:8080/api/count/chkvin";
    public static final String UPDATE_COUNT_VIN_URL = "http://192.168.1.33:8080/api/count/confirmvin";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Volvo Picture Upload";
    public static final String HEAD_KEY = "Authorization";
    public static final String HEAD_VALUE = "Basic Y2V2YTpWcjF0ZWFtIQ==";
    public static final String UPDATE_URL = "http://203.154.71.73/tms_volvo/setup/trans.apk";

    public static final String WI_URL = "http://203.154.71.73:8080/tr_tracking/api/wi";


    //php
    public static final String FILE_UPLOAD_URL = "http://203.154.71.73/tms_volvo/fileupload.php";
}