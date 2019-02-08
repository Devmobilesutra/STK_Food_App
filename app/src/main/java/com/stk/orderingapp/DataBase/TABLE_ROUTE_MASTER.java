package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Activity.ActivityDashboard;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.RouteDetails;
import com.stk.orderingapp.Model.ShopDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class TABLE_ROUTE_MASTER {
    public static String LOG_TAG = "TABLE_ROUTE_MASTER ";
    public static String NAME = "tableRouteMaster";
    public static String COL_ID = "id",
            COL_ROUTE_NAME = "routeName",
            COL_ROUTE_ID = "routeId",
            COL_IS_TOADAYS_ROUTE = "todays_route";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER ,"
            + COL_ROUTE_NAME + " TEXT, "
            + COL_IS_TOADAYS_ROUTE + ", TEXT DEFAULT 'N' , "
            + COL_ROUTE_ID + "  TEXT UNIQUE); ";

    public static void insertRouteDetails(ArrayList<RouteDetails> routeDetails) {
        // ArrayList<RouteDetails> routeDetails = null;
        ArrayList<ShopDetails> shopDetails = null;
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        if (routeDetails.size() > 0) {
            db.delete(NAME, null, null);

            MyApplication.set_session(MyApplication.SESSION_ROUTE_ID, routeDetails.get(0).getId());
        }
        for (int i = 0; i < routeDetails.size(); i++) {
            RouteDetails routeDetails_new = routeDetails.get(i);
            ContentValues cv = new ContentValues();
            cv.put(COL_ROUTE_ID, routeDetails_new.getId());
            cv.put(COL_ROUTE_NAME, routeDetails_new.getRoute_name());
            db.insertWithOnConflict(NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

            shopDetails = routeDetails_new.getShopDetails();
            for (int j = 0; j < shopDetails.size(); j++) {
                //ShopDetails shopDetails = shopDetails.get;
                TABLE_RETAILER_MASTER.insertRetailerDetails(shopDetails.get(j));
            }
        }
    }


    public static ArrayList<HashMap<String, String>> getRouteDetailsDayWiseRouteWIse() {
        MyApplication.log(LOG_TAG + " getRouteDetailsDayWiseRouteWIse() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<HashMap<String, String>> hashMapArrayList = null;
        String sql = "select * from " + NAME + " WHERE " + COL_IS_TOADAYS_ROUTE + " = 'Y' ";

        Cursor c = db.rawQuery(sql, null);
        HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            hashMapArrayList = new ArrayList<>();
            c.moveToFirst();
            do {
                routeList = new HashMap<>();

                routeList.put(COL_ROUTE_NAME, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME)));
                routeList.put(COL_ROUTE_ID, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_ID)));
                hashMapArrayList.add(routeList);

            } while (c.moveToNext());
            MyApplication.log(LOG_TAG + " getRouteDetailsDayWiseRouteWIse() ", hashMapArrayList.toString());
        }
        return hashMapArrayList;
    }


    public static ArrayList<HashMap<String, String>> getRouteDetails() {
        MyApplication.log(LOG_TAG + " getRouteDetails() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<HashMap<String, String>> hashMapArrayList = null ;
        String sql = "select * from " + NAME;

        Cursor c = db.rawQuery(sql, null);
        HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            hashMapArrayList = new ArrayList<>();
            c.moveToFirst();
            routeList.put(COL_ROUTE_NAME, "All");
            routeList.put(COL_ROUTE_ID, "All");
            hashMapArrayList.add(routeList);
            do {
                routeList = new HashMap<>();
                routeList.put(COL_ROUTE_NAME, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME)));
                routeList.put(COL_ROUTE_ID, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_ID)));
                hashMapArrayList.add(routeList);

            } while (c.moveToNext());
        }
        MyApplication.log(LOG_TAG + " getRouteDetails() ", hashMapArrayList.toString());
        return hashMapArrayList;
    }


    public static HashMap<String, String> getRouteDetailsByRoutIdDayWise(String routeId) {
        MyApplication.log(LOG_TAG + " getRouteDetailsByRoutIdDayWise() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        String sql = "select * from " + NAME + " WHERE " + COL_ROUTE_ID + " = '" + routeId + "' ";

        Cursor c = db.rawQuery(sql, null);
        HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                routeList = new HashMap<>();
                routeList.put(COL_ROUTE_NAME, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME)));
                routeList.put(COL_ROUTE_ID, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_ID)));

            } while (c.moveToNext());
        }
        MyApplication.log(LOG_TAG + " getRouteDetailsByRoutIdDayWise() FOR routeId: " + routeId, ", --->" + routeList.toString());
        return routeList;
    }

    public static ArrayList<HashMap<String, String>> getRouteDetailsSpinner() {
        MyApplication.log(LOG_TAG + " getRouteDetailsSpinner() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
        String sql = "select * from " + NAME;

        Cursor c = db.rawQuery(sql, null);
        HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            routeList.put(COL_ROUTE_NAME, "Select");
            routeList.put(COL_ROUTE_ID, "Select");
            hashMapArrayList.add(routeList);
            do {
                routeList = new HashMap<>();

                routeList.put(COL_ROUTE_NAME, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME)));
                routeList.put(COL_ROUTE_ID, c.getString(c.getColumnIndexOrThrow(COL_ROUTE_ID)));
                hashMapArrayList.add(routeList);

            } while (c.moveToNext());

        }

        MyApplication.log(LOG_TAG + " getRouteDetailsSpinner() ", hashMapArrayList.toString());
        return hashMapArrayList;
    }

    public static ArrayList<String> getRouteIDs() {
        MyApplication.log(LOG_TAG + " getRouteDetails() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<String> iDsList = new ArrayList<>();
        String sql = "select * from " + NAME;

        Cursor c = db.rawQuery(sql, null);
        //HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            //routeList.put(COL_ROUTE_NAME, "All");
            iDsList.add("0");
            do {
                iDsList.add(c.getString(c.getColumnIndexOrThrow(COL_ROUTE_ID)));
            } while (c.moveToNext());

        }

        MyApplication.log(LOG_TAG + " getRouteIDs() ", iDsList.toString());
        return iDsList;
    }

    public static ArrayList<String> getRouteNames() {
        MyApplication.log(LOG_TAG + " getRouteNames() ", "");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<String> iDsList = new ArrayList<>();
        String sql = "select * from " + NAME;

        Cursor c = db.rawQuery(sql, null);
        //HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            //routeList.put(COL_ROUTE_NAME, "All");
            iDsList.add("Select Route");
            do {
                iDsList.add(c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME)));
            } while (c.moveToNext());
        }

        MyApplication.log(LOG_TAG + " getRouteNames() ", iDsList.toString());
        return iDsList;
    }


    public static String getRouteNameFromId(String routeId) {
        String routeName = "";

        MyApplication.log(LOG_TAG + " getRouteNameFromId() routeId: ---> ", routeId);
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<String> iDsList = new ArrayList<>();
        String sql = "select * from " + NAME + " WHERE " + COL_ROUTE_ID + " = '" + routeId + "' ";

        Cursor c = db.rawQuery(sql, null);
        //HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            routeName = c.getString(c.getColumnIndexOrThrow(COL_ROUTE_NAME));
        }

        MyApplication.log(LOG_TAG + " getRouteNames() ---> ", routeName);
        return routeName;
    }


    /*
    public static void updateTodaysRouteTask(String routeId) {
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_IS_TOADAYS_ROUTE, "Y");

        long ret = db.update(NAME, cv, COL_ROUTE_ID + " = ?", new String[]{routeId});
        MyApplication.log(LOG_TAG, "updateTodaysRouteTask() update is ret==>" + ret);
    }

    public static void updateNoRoutesOnTodays() {
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_IS_TOADAYS_ROUTE, "N");

        long ret = db.update(NAME, cv, COL_IS_TOADAYS_ROUTE + " = ?", new String[]{"Y"});
        MyApplication.log(LOG_TAG, "updateNoRoutesOnTodays() update is ret==>" + ret);
    }*/


}
