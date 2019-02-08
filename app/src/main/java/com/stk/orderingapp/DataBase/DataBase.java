package com.stk.orderingapp.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stk.orderingapp.Config.MyApplication;

/**
 * Created by JARVIS on 28-Mar-18.
 */

public class DataBase extends SQLiteOpenHelper {
    String LOG_TAG = "DataBase";
    private final static String DB_NAME = "dbSTKFood";
    private final static int DB_VERSION = 1;

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        MyApplication.log(LOG_TAG, " in Constructor of Database");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MyApplication.log(LOG_TAG, " in create of Database");
        try {


            MyApplication.log(LOG_TAG+" onCreate()  ", "TABLE_ATTENDANCE_REPORT->  " + TABLE_ATTENDANCE_REPORT.CREATE_TABLE);
            db.execSQL(TABLE_ATTENDANCE_REPORT.CREATE_TABLE);


            MyApplication.log("JARVIS", "RETAILER " + TABLE_RETAILER_MASTER.CREATE_TABLE);
            db.execSQL(TABLE_RETAILER_MASTER.CREATE_TABLE);
            MyApplication.log("JARVIS", "route " + TABLE_ROUTE_MASTER.CREATE_TABLE);
            db.execSQL(TABLE_ROUTE_MASTER.CREATE_TABLE);

            MyApplication.log("JARVIS", "item " + TABLE_ITEM_DETAILS.CREATE_TABLE);
            db.execSQL(TABLE_ITEM_DETAILS.CREATE_TABLE);

            MyApplication.log("JARVIS", "order master " + TABLE_ORDER_MASTER.CREATE_TABLE);
            db.execSQL(TABLE_ORDER_MASTER.CREATE_TABLE);

            MyApplication.log("JARVIS", "order details" + TABLE_ORDER_DETAILS.CREATE_TABLE);
            db.execSQL(TABLE_ORDER_DETAILS.CREATE_TABLE);


            MyApplication.log("JARVIS", "TEMP_ORDER_DETAILS " + TEMP_ORDER_DETAILS.CREATE_TABLE);
            db.execSQL(TEMP_ORDER_DETAILS.CREATE_TABLE);

            MyApplication.log("JARVIS", "TABLE_TEMP_ORDER_MASTER" + TABLE_TEMP_ORDER_MASTER.CREATE_TABLE);
            db.execSQL(TABLE_TEMP_ORDER_MASTER.CREATE_TABLE);

            MyApplication.log("JARVIS", "TABLE_STOCK_DETAILS" + TABLE_STOCK_DETAILS.CREATE_TABLE);
            db.execSQL(TABLE_STOCK_DETAILS.CREATE_TABLE);

            MyApplication.log("JARVIS", "TABLE_IMAGES" + TABLE_IMAGES.CREATE_TABLE);
            db.execSQL(TABLE_IMAGES.CREATE_TABLE);

            MyApplication.log("JARVIS", "NOTIFICATION--->" + TABLE_NOTIFICATION.CREATE_TABLE);
            db.execSQL(TABLE_NOTIFICATION.CREATE_TABLE);

        } catch (SQLException e) {
            MyApplication.log(LOG_TAG+" onCreate() ", "in DataBase on create TABLE EXCEPTION " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
