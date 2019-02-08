package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TABLE_ATTENDANCE_REPORT {

    public static String NAME = "table_daily_attendance";
    public static String COL_ID = "id",
            COL_DATE = "report_date",
            COL_CHECK_IN_TIME = "check_in",
            COL_CHECK_OUT_TIME = "check_out",
            COL_CHECK_IN_IMAGE_URL = "check_in_image",
            COL_CHECK_OUT_IMAGE_URL = "check_out_image",
            COL_CHECK_IN_FLAG = "check_in_flag",
            COL_CHECK_OUT_FLAG = "check_out_flag";

    public static String LOG_TAG = "TABLE_ATTENDANCE_REPORT ";

    public static String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + COL_DATE + " TEXT , "
            + COL_CHECK_IN_TIME + " TEXT, "
            + COL_CHECK_OUT_TIME + " TEXT, "
            + COL_CHECK_IN_IMAGE_URL + " TEXT, "
            + COL_CHECK_OUT_IMAGE_URL + " TEXT, "
            + COL_CHECK_IN_FLAG + " TEXT NOT NULL DEFAULT 'N' , "
            + COL_CHECK_OUT_FLAG + "  TEXT NOT NULL DEFAULT 'N' )";

    public static void insertCheckInReport(String date, String check_in_time, String check_in_Flag,
                                           String in_image_url) {

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, date);
        cv.put(COL_CHECK_IN_TIME, check_in_time);
        cv.put(COL_CHECK_IN_IMAGE_URL, in_image_url);
        cv.put(COL_CHECK_IN_FLAG, check_in_Flag);

        long ret = db.insert(NAME, null, cv);
        //long ret = db.insertWithOnConflict(NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        MyApplication.log(LOG_TAG + "insertCheckInReport() ", "ret value is--->" + ret);

    }

    public static void updateCheckOutReport(String date, String check_out_time, String check_out_Flag,
                                            String check_out_image_url) {

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, date);
        cv.put(COL_CHECK_OUT_TIME, check_out_time);
        cv.put(COL_CHECK_OUT_IMAGE_URL, check_out_image_url);
        cv.put(COL_CHECK_OUT_FLAG, "Y");
        long ret = db.update(NAME, cv, COL_DATE + " = ?", new String[]{date + ""});
        MyApplication.log(LOG_TAG + "updateCheckOutReport() ", "ret valure is--->" + ret);

    }

    public static String getCheckStatusForToday(String today) {
        String status = "", check_in = "", check_out = "";  //        // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        String sql = " SELECT * from " + NAME + " WHERE " + COL_DATE + " ='" + today + "'";

        Cursor c = db.rawQuery(sql, null);
        MyApplication.log(LOG_TAG + "getCheckStatusForToday(), getCount() --> ", c.getCount() + "");
        if (c.getCount() > 0) {
            c.moveToFirst();
            check_in = c.getString(c.getColumnIndexOrThrow(COL_CHECK_IN_FLAG));
            check_out = c.getString(c.getColumnIndexOrThrow(COL_CHECK_OUT_FLAG));
            MyApplication.log(LOG_TAG + "getCheckStatusForToday() ", "IS_IN--> " + check_in + ", IS_OUT--->" + check_out);
            if (check_out.equals("Y") && check_in.equals("Y")) {
                status = "do_NO_CHANGE";
            } else {
                if (check_in.equals("Y") && !check_out.equals("Y"))
                    status = "do_CHECK_OUT";
                else
                    status = "do_CHECK_IN";
            }
        } else
            status = "do_CHECK_IN";

        MyApplication.log(LOG_TAG + " getCheckStatusForToday() ", "STATUS--->" + status);
        return status;
    }

}

