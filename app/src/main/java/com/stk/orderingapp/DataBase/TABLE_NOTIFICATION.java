package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ItemDetails;
import com.stk.orderingapp.Model.NotificationMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TABLE_NOTIFICATION {

    public static String LOG_TAG = "TABLE_NOTIFICATION";

    public static String NAME = "tableNotification";
    public static String COL_ID = "id",
            COL_NOTI_TITLE = "notiTitle",
            COL_NOTI_MESSAGE = "notiMessage",
            COL_EXP_DATE = "expDate",
            COL_REC_DATE = "recDate";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + COL_NOTI_TITLE + " TEXT, "
            + COL_NOTI_MESSAGE + " TEXT, "
            + COL_REC_DATE + " TEXT, "
            + COL_EXP_DATE + " TEXT); ";


    public static void insertNotifications(JSONObject jsonObject) {
        MyApplication.log(LOG_TAG, "insertNotifications(),  jsonObject -->" + jsonObject);

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String notification_flag = "", title = "", message = "", Sent_on = "", id = "", Valid_till = "";
        try {

            //  notification_flag = jsonObject.getString("notification_flag");
            title = jsonObject.getString("title");
            message = jsonObject.getString("message");
            Sent_on = jsonObject.getString("Sent_on");
            id = "" + jsonObject.getInt("id");
            Valid_till = jsonObject.getString("Valid_till");

            cv.put(COL_ID, id);
            cv.put(COL_NOTI_TITLE, title);
            cv.put(COL_NOTI_MESSAGE, message);
            cv.put(COL_REC_DATE, Sent_on);
            cv.put(COL_EXP_DATE, Valid_till);

            long res = db.insertWithOnConflict(NAME, COL_ID, cv, SQLiteDatabase.CONFLICT_REPLACE);

            MyApplication.log(LOG_TAG, "insertNotifications(), RES ----->  " + res+",  ID:--->"+id);
            //db.insertWithOnConflict(NAME, "", cv, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (JSONException e) {
            MyApplication.log(LOG_TAG, "insertNotifications(), JSONException ERROR  " + e.getMessage());
        }
    }


    public static ArrayList<NotificationMessage> getNotification() {
        ArrayList<NotificationMessage> notificationMessages = new ArrayList<>();
        try {
            SQLiteDatabase db = MyApplication.db.getReadableDatabase();
            String sql = "select * from " + NAME;
            Cursor c = db.rawQuery(sql, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    NotificationMessage notificationMessage = new NotificationMessage();
                    notificationMessage.setExpireDate("Expires On: " + c.getString(c.getColumnIndexOrThrow(COL_EXP_DATE)));
                    notificationMessage.setNotificationTitle("" + c.getString(c.getColumnIndexOrThrow(COL_NOTI_TITLE)));
                    notificationMessage.setRec_date("" + c.getString(c.getColumnIndexOrThrow(COL_REC_DATE)));
                    notificationMessage.setNotificationMessage("" + c.getString(c.getColumnIndexOrThrow(COL_NOTI_MESSAGE)));

                    notificationMessages.add(notificationMessage);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            MyApplication.log(LOG_TAG, "getNotification(), Exception ERROR  " + e.getMessage());
        }
        return notificationMessages;
    }

    public static  void deleteRecordsBeforeTodays(){
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        long res = db.delete(NAME, COL_EXP_DATE + " < " + MyApplication.getDateDBFormat(), null);
        MyApplication.log(LOG_TAG, " deleteRecordsBeforeTodays(), DELETED RECORDS ------>"+res);
    }

    public static void insertNotificationData(JSONArray jsonArray) {
        MyApplication.log(LOG_TAG, "insertNotificationData(),  jsonArray -->" + jsonArray);

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {

            for(int i=0; i< jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                cv.put(COL_ID, jsonObject.getString("id"));
                cv.put(COL_NOTI_TITLE, jsonObject.getString("title"));
                cv.put(COL_NOTI_MESSAGE, jsonObject.getString("message"));
                cv.put(COL_REC_DATE, jsonObject.getString("date(senton)")  );
                cv.put(COL_EXP_DATE, jsonObject.getString("validity") );

                long res = db.insertWithOnConflict(NAME, COL_ID, cv, SQLiteDatabase.CONFLICT_REPLACE);

                MyApplication.log(LOG_TAG, "insertNotificationData(), RES ----->  "+ res );
            }

        } catch (JSONException e) {
            MyApplication.log(LOG_TAG, "insertNotificationData(), JSONException ERROR  " + e.getMessage());
        }
    }
}
