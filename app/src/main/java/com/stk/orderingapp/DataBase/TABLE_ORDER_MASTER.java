package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JARVIS on 02-Apr-18.
 */

public class TABLE_ORDER_MASTER {

    public static String NAME = "tableOrderMaster";
    public static String COL_ID = "id",
            COL_ORDER_ID = "orderId",
            COL_CLIENT_ID = "clientId",
            COL_DIST_ID = "distributorId",
            COl_TOTAL_AMOUNT = "totalAmount",
            COL_ORDER_DATE = "orderDate",
            COL_ORDER_STATUS = "orderStatus",
            COL_ORDER_DELIVERY_STATUS = "deliveryStatus",
            COL_IS_SYNC = "isSync",
            COL_ORDER_DELIVERY_DATE = "delivery_date";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + "  INTEGER, "
            + COL_ORDER_ID + " TEXT , "
            + COL_CLIENT_ID + " TEXT UNIQUE, "
            + COL_DIST_ID + " TEXT , "
            + COl_TOTAL_AMOUNT + " TEXT , "
            + COL_ORDER_STATUS + " TEXT , "
            + COL_ORDER_DATE + " TEXT , "
            + COL_ORDER_DELIVERY_STATUS + " TEXT, "
            + COL_IS_SYNC + " TEXT, "
            + COL_ORDER_DELIVERY_DATE + " TEXT ); ";

    public static void getOrderIdAndInsert(String clientId, String distId) {
        MyApplication.log("JARVIS", "in getOrderId client Id-->" + clientId);
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();

        String sql = "select * from " + NAME + " where " + COL_CLIENT_ID + "='" + clientId + "' and " + COL_IS_SYNC + "='NO'";
        MyApplication.log("JARVIS", "SQL Query for gettting order id =>" + sql);

        Cursor c = db.rawQuery(sql, null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            MyApplication.set_session(MyApplication.SESSION_ORDER_ID, c.getString(c.getColumnIndexOrThrow(COL_ORDER_ID)));

        } else {
            MyApplication.getOrderId();
            ContentValues cv = new ContentValues();
            cv.put(COL_CLIENT_ID, clientId);
            cv.put(COL_DIST_ID, distId);
            cv.put(COL_ORDER_ID, MyApplication.get_session(MyApplication.SESSION_ORDER_ID));
            cv.put(COl_TOTAL_AMOUNT, "0");
            cv.put(COL_ORDER_DATE, MyApplication.getOrderDate());
            cv.put(COL_ORDER_STATUS, "Pending");
            cv.put(COL_ORDER_DELIVERY_STATUS, "Open");
            cv.put(COL_IS_SYNC, "NO");
            cv.put(COL_ORDER_DELIVERY_DATE, "0");

            long ret = db.insert(NAME, null, cv);

            MyApplication.log("JARVIS", "return valure is -->" + ret);

        }

    }

    public static JSONArray getOrderDetails(String flag) {
        MyApplication.log("JARVIS", " in getOrderDetails()");
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        String sql = "";
        if (flag.equalsIgnoreCase("")) {
            sql = "select m.* from " + TABLE_ORDER_MASTER.NAME
                    + " as m where m." + COL_IS_SYNC + "='NO' and m."
                    + TABLE_ORDER_MASTER.COL_ORDER_ID + "='" + MyApplication.get_session(MyApplication.SESSION_ORDER_ID) + "'";
        } else if (flag.equalsIgnoreCase("drawer")) {

            sql = "select m.* from " + TABLE_ORDER_MASTER.NAME
                    + " as m where m." + COL_IS_SYNC + "='NO'";

        }
        MyApplication.log("JARVIS", " in getOrderDetails() sql--v>-->" + sql);

        Cursor c = db.rawQuery(sql, null);
        MyApplication.log("JARVIS", " in master() count id -->" + c.getCount());
        JSONArray jsonArrayMaster = new JSONArray();
        JSONArray jsonArrayDetails = new JSONArray();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("order_no", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_ORDER_ID)));
                    jsonObject.put("order_date", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_ORDER_DATE)));
                    jsonObject.put("retailer_id", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_CLIENT_ID)));
                    jsonObject.put("distributor_id", MyApplication.get_session(MyApplication.SESSION_DIST_ID));
                    jsonObject.put("salesman_id", MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID));
                    jsonObject.put("salesman_latitude", "0.000");
                    jsonObject.put("salesman_longitude", "0.0000");
                    jsonObject.put("total_amount", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COl_TOTAL_AMOUNT)));
                    jsonObject.put("order_status", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_ORDER_STATUS)));
                    jsonObject.put("delivery_status", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_ORDER_DELIVERY_STATUS)));
                    jsonObject.put("delivery_date", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_MASTER.COL_ORDER_DELIVERY_DATE)));
                    JSONArray jsonArray = TABLE_ORDER_DETAILS.getOrderDetails(c.getString(c.getColumnIndexOrThrow(COL_ORDER_ID)));
                    if (jsonArray == null) {
                        return null;
                    } else {
                        jsonObject.put("item_order_details", jsonArray);
                        jsonArrayMaster.put(jsonObject);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } while (c.moveToNext());


        }

        MyApplication.log("JARVIS", "JSON MASTER-->" + jsonArrayMaster.toString());
        return jsonArrayMaster;
    }

    public static void deleteRecords(String retailerId, String flag) {
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        String sql = "";
        if (flag.equalsIgnoreCase("drawer")) {
            sql = "INSERT OR REPLACE INTO " + TABLE_TEMP_ORDER_MASTER.NAME + " select * from " + TABLE_ORDER_MASTER.NAME
                    + " where " + TABLE_ORDER_MASTER.COL_CLIENT_ID + " in ( select " + TABLE_RETAILER_MASTER.COL_RETAILER_ID + " from "
                    + TABLE_RETAILER_MASTER.NAME + ")";
            MyApplication.log("JARVIS", "copy one from another master tablr--.>" + sql.toString());

        } else {
            sql = "INSERT OR REPLACE INTO " + TABLE_TEMP_ORDER_MASTER.NAME + " select * from " + TABLE_ORDER_MASTER.NAME
                    + " where " + TABLE_ORDER_MASTER.COL_CLIENT_ID + "='" + retailerId + "'";
            MyApplication.log("JARVIS", "copy one from another master tablr--.>" + sql.toString());
        }

        Cursor c = db.rawQuery(sql, null);

        MyApplication.log("JARVIS", "count in copy from another-->" + c.getCount());
        if (flag.equalsIgnoreCase("drawer")) {
            sql = "delete  from " + NAME + " where " + TABLE_ORDER_MASTER.COL_CLIENT_ID + " in ( select " + TABLE_RETAILER_MASTER.COL_RETAILER_ID + " from "
                    + TABLE_RETAILER_MASTER.NAME + ")";
            c = db.rawQuery(sql, null);

            MyApplication.log("JARVIS", "delete  master tablr--.>" + sql.toString() + " count is delete ->" + c.getCount());
        } else {
            long ret = db.delete(NAME, COL_ORDER_ID + "=?", new String[]{MyApplication.get_session(MyApplication.SESSION_ORDER_ID)});

        }
        c.close();


        c.close();


    }

    public static void copyData() {
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();


        long ret = db.insert(NAME, null, null);

        MyApplication.log("JARVIS", "delete  master valure asre-->" + ret);

    }
}
