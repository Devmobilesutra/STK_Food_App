package com.stk.orderingapp.DataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;

/**
 * Created by JARVIS on 11-Apr-18.
 */

public class TABLE_TEMP_ORDER_MASTER {
    public static String NAME = "tempOrderMaster";
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
            + COL_ORDER_ID + " TEXT UNIQUE, "
            + COL_CLIENT_ID + " TEXT , "
            + COL_DIST_ID + " TEXT , "
            + COl_TOTAL_AMOUNT + " TEXT , "
            + COL_ORDER_STATUS + " TEXT , "
            + COL_ORDER_DATE + " TEXT , "
            + COL_ORDER_DELIVERY_STATUS + " TEXT, "
            + COL_IS_SYNC + " TEXT, "
            + COL_ORDER_DELIVERY_DATE + " TEXT ); ";

    public static String getOrderDate(String retailerId) {
        MyApplication.log("JARVIS", "in getOrderDate() retailerId" + retailerId);
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();

        String sql = " select * from " + TABLE_TEMP_ORDER_MASTER.NAME + " where " + COL_CLIENT_ID + " = '" + retailerId + "'";

        Cursor c = db.rawQuery(sql, null);

        String orderDate;
        if (c.getCount() > 0) {
            c.moveToFirst();
            orderDate = c.getString(c.getColumnIndexOrThrow(COL_ORDER_DATE));
        } else {
            orderDate = "N/A";
        }

        c.close();
        MyApplication.log("JARVIS", " order date" + orderDate);
        return orderDate;
    }
}
