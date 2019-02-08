package com.stk.orderingapp.Config;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.stk.orderingapp.DataBase.DataBase;
import com.stk.orderingapp.Model.NotificationMessage;
import com.stk.orderingapp.Model.NotificationResponse;
import com.stk.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JARVIS on 28-Mar-18.
 */

public class MyApplication extends MultiDexApplication {

    public static String SessionKey = "j5aD9uweHEAncbhd";// Must have 16
    private static final String TAG = "STKFoodApplication";
    // Session Variables
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    private GoogleApiHelper googleApiHelper;
    private static MyApplication mInstance;
    Context context;
    String PREFS_NAME = "jdviudsfb4r4327_Sdfj";

    public static AESAlgorithm aes;
    public static DataBase db = null;

    public static final String SESSION_APP_VERSION = "session_app_version",
            SESSION_USER_NAME = "user_name",
            SESSION_PASSWORD = "userPassword",
            SESSION_DEVICE_ID = "deviceId",
            SESSION_FCM_ID = "fcmId",
            SESSION_DIST_NAME = "distName",
            SESSION_DIST_ID = "distId",
            SESSION_SALESMAN_ID = "salesManId",
            SESSION_ORDER_ID = "SESSION_ORDER_ID",
            SESSION_ROUTE_ID = "session_route_id",
            SESSION_FROM_LOGIN = "session_from_login",
            SESSION_LOGIN = "SESSION_LOGIN",
            SESSION_LOGIN_PASS = "SESSION_LOGIN_PASS",
            SESSION_LOGIN_ID = "SESSION_LOGIN_ID",
            SESSION_LATTITUDE = "session_lattitude",
            SESSION_LONGITUDE = "session_longitude",
            SESSION_RETAILER_ID = "SESSION_RETAILER_ID",
            SESSION_USER_FULL_NAME = "user_full_name",
            SESSION_TODAYS_ROUTE_DATE = "todays_routes_date";

    private static ProgressDialog proDialog = null;

    @Override
    public void onCreate() {
        //MultiDex.install(getBaseContext().getTargetContext());
        super.onCreate();

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        aes = new AESAlgorithm();
        if (db == null) {
            db = new DataBase(this);
            db.getWritableDatabase();
            db.getReadableDatabase();
        }
    }

    public static void set_session(String key, String value) {
        Log.i("SetSession", "Key=" + key + "__value=" + value);
        String temp_key = aes.Encrypt(key);
        String temp_value = aes.Encrypt(value);
        MyApplication.editor.putString(temp_key, temp_value);
        MyApplication.editor.commit();
    }

    public static String get_session(String key) {
        String temp_key = aes.Encrypt(key);
        if (sharedPref.contains(temp_key)) {
            log("GetSession->Key ---> " + key + ", Value:----->" + aes.Decrypt(sharedPref.getString(temp_key, "")));
            return aes.Decrypt(sharedPref.getString(temp_key, ""));
        } else
            return "";
    }

    public static void log(String str) {
        if (str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            log(str.substring(4000));
        } else
            Log.i(TAG, str);
    }

    public static void log(String LOG_TAG, String str) {
        if (str.length() > 4000) {
            Log.i(TAG + ", " + LOG_TAG, str.substring(0, 4000));
            log(str.substring(4000));
        } else
            Log.i(TAG + ", " + LOG_TAG, str);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void showDialog(Context mContext, String message) {
        MyApplication.log(TAG, " in showDialog() message-" + message);
        try {
            if (mContext != null) {
                if (proDialog == null)
                    proDialog = ProgressDialog.show(mContext, null, message);
                proDialog.setCancelable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void stopLoading() {
        try {
            if (proDialog != null)
                proDialog.dismiss();
            proDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return simpleDateFormat.format(new Date());
//        return "";
    }

    public static String getDayRouteFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        return simpleDateFormat.format(new Date());
    }

    public static String getOrderDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(new Date());
//        return "";
    }

    public static String getDateTimeDBFormat() {
        String CurDateTime = "";
        Date date = new Date();
        //DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss a");
        CurDateTime = dateFormat1.format(date);
        log("getDateTimeDBFormat(), Date", "CurrentdateTime : " + CurDateTime);
        return CurDateTime;
    }

    public static String getDateTimeStamp() {
        String CurDateTime = "";
        Date d = new Date();
        Date date = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        //DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss a");
        CurDateTime = dateFormat1.format(date);
        log("getDateTimeStamp(), Date", "CurrentdateTime : " + CurDateTime);
        return CurDateTime;
    }

    public static String getDateDBFormat() {
        String CurDateTime = "";
        Date date = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        CurDateTime = dateFormat1.format(date);
        log("getDateDBFormat(), Date", "Currentdate:" + CurDateTime);
        return CurDateTime;
    }


    public static String getOrderId() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");

        MyApplication.set_session(MyApplication.SESSION_ORDER_ID, simpleDateFormat.format(new Date()));
        MyApplication.log("JARVIS", "ORDER id -->" + simpleDateFormat.format(new Date()));
        return simpleDateFormat.format(new Date());


    }

    public static void createDataBase() {

        MyApplication.log("JARVIS", "in createDatabase");

        File sd = Environment.getExternalStorageDirectory();
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {

            // File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            //     String currentDBPath = "/data/user/0/com.example.r_b_k.recyclerview/databases/DB3";
            String currentDBPath = "/data/data/com.stk.orderingapp/databases/dbSTKFood";

            // /data/data/com.stk.orderingapp/databases/dbSTKFood
            //   = "/data/"+ getApplicationContext().getPackageName() +"/"+"DB1";
            String backupDBPath = "dbSTKFood";
            /*File tmpDir = new File(backupDBPath);
            boolean exists = tmpDir.exists();
            if (!exists) {
                MyApplication.log("JARVIS", "in if");
                sd = new File(sd, backupDBPath);
            } else {
                MyApplication.log("JARVIS", "in else" + sd.toString());
            }*/
            File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {

                source = new FileInputStream(currentDB).getChannel();
                // PrintWriter out = new PrintWriter( source+".db");
                destination = new FileOutputStream(backupDB + ".db").getChannel();
                destination.transferFrom(source, 0, source.size());

                source.close();
                destination.close();
                //       Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void showDialogBox(Context context, String title, String message) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);

        dlgAlert.setTitle( context.getResources().getString(R.string.app_name)  );

        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }


    public static boolean is_marshmellow() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static String get_current_date_sec() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String ConvertString(Bitmap path) {
        String phototostring = "";
        try {
            ByteArrayOutputStream compressphotobytearrray = new ByteArrayOutputStream();

            Bitmap photobitmap = path;

            photobitmap.compress(Bitmap.CompressFormat.PNG, 100, compressphotobytearrray);
            byte[] imagebytearray = compressphotobytearrray.toByteArray();


            compressphotobytearrray.close();


            photobitmap.recycle();
            phototostring = Base64.encodeBytes(imagebytearray);


        } catch (Exception e) {
            log(TAG, "ConvertString() Error->" + e.getMessage());
        }
        return phototostring;
    }

   /* public static ArrayList<NotificationMessage> getNotification() {
        ArrayList<NotificationMessage> notificationMessages = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < 10; i++) {

                JSONObject jsonMessage = new JSONObject();

                jsonMessage.put("Title", "title-->" + i);
                jsonMessage.put("Message", "Message--->" + i);
                jsonMessage.put("DateExp", "DateExp-->" + i);
                jsonMessage.put("DateRec", "DateRec-->" + i);
                NotificationMessage notificationMessage = new NotificationMessage();
                notificationMessage.setExpireDate("DateExp-->" + i);
                notificationMessage.setNotificationMessage("Message--->" + i);
                notificationMessage.setRec_date("DateRec-->" + i);
                notificationMessage.setNotificationTitle("Title-->" + i);

//                jsonArray.put(jsonMessage);
                notificationMessages.add(notificationMessage);
            }
            //          jsonObject.add("notiMessage", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notificationMessages;
    }*/


    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }

    public static LocationRequest getLocationRequest() {
        return getGoogleApiHelper().getLocationRequest();
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
