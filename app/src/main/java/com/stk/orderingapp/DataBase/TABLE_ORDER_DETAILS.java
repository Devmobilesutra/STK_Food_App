package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ModelProductList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JARVIS on 02-Apr-18.
 */

public class TABLE_ORDER_DETAILS {
    public static String LOG_TAG="TABLE_ORDER_DETAILS ";

    public static String NAME = "tableOrderDetails";
    public static String COL_ID = "id",
            COL_ORDER_ID = "orderId",

    COL_CLIENT_ID = "clientId",
            COL_ORDER_DATE = "orderDate",
            COL_PRODUCT_ID = "productId",
            COL_ORDER_ITEM_QTY = "itemOrderQty",
            COL_REJECT_ITEM_QTY = "itemRejectQty",
            COL_STOCK_ITEM_QTY = "itemStockQty";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + "  INTEGER , "
            + COL_ORDER_ID + " TEXT , "
            + COL_CLIENT_ID + " TEXT , "
            + COL_ORDER_DATE + " TEXT , "
            + COL_PRODUCT_ID + " TEXT , "
            + COL_ORDER_ITEM_QTY + " TEXT, "
            + COL_REJECT_ITEM_QTY + " TEXT, "
            + COL_STOCK_ITEM_QTY + " TEXT," +
            " UNIQUE (" + COL_PRODUCT_ID + "," + COL_CLIENT_ID + " )); ";


    public static long insertOrderDetails(ModelProductList modelProductList, String clientId) {

        MyApplication.log(LOG_TAG, "in insertOrderDetails-->" + modelProductList.toString());
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_CLIENT_ID, clientId);
        cv.put(COL_ORDER_ID, MyApplication.get_session(MyApplication.SESSION_ORDER_ID));
        cv.put(COL_ORDER_DATE, MyApplication.getOrderDate());
        cv.put(COL_PRODUCT_ID, modelProductList.getProduct_id());
        cv.put(COL_ORDER_ITEM_QTY, modelProductList.getOrderQty());
        cv.put(COL_REJECT_ITEM_QTY, modelProductList.getRejectQty());
        cv.put(COL_STOCK_ITEM_QTY, modelProductList.getStockQty());


        long ret = db.insertWithOnConflict(NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        MyApplication.log(LOG_TAG, " ret is ->" + ret);
        return ret;
    }

    public static JSONArray getOrderDetails(String orderId) {
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        JSONArray jsonArrayOrderDetails = new JSONArray();

        String sql = "select * from " + NAME
                + " where " + COL_ORDER_ID + "='" + orderId + "'";


        Cursor c = db.rawQuery(sql, null);
        MyApplication.log(LOG_TAG, " in getOrderDetailsDetails() count id -->" + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                JSONObject jsonObject1 = new JSONObject();

                try {
                    jsonObject1.put("order_no", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_DETAILS.COL_ORDER_ID)));

                    jsonObject1.put("item_id", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_DETAILS.COL_PRODUCT_ID)));
                    jsonObject1.put("quantity", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_DETAILS.COL_ORDER_ITEM_QTY)));
                    jsonObject1.put("stock_quantity", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_DETAILS.COL_STOCK_ITEM_QTY)));
                    jsonObject1.put("reject_quantity", c.getString(c.getColumnIndexOrThrow(TABLE_ORDER_DETAILS.COL_REJECT_ITEM_QTY)));
                    jsonObject1.put("uom", "0");
                    jsonObject1.put("rate", "0");
                    jsonObject1.put("amount", "0");

                    jsonArrayOrderDetails.put(jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());

        } else {
            return jsonArrayOrderDetails;
        }

        MyApplication.log(LOG_TAG, "details-->" + jsonArrayOrderDetails);
        return jsonArrayOrderDetails;
    }

    public static void deleteRecords(String retilaerId, String flag) {
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        MyApplication.log(LOG_TAG, " flag is deleteRecords-->" + flag);
       /* String sql = "INSERT OR REPLACE INTO " + TEMP_ORDER_DETAILS.NAME + " select * from " + TABLE_ORDER_DETAILS.NAME + " where " + TABLE_ORDER_DETAILS.COL_ORDER_ID + "='" + MyApplication.get_session(MyApplication.SESSION_ORDER_ID) + "'";
        MyApplication.log(LOG_TAG, "copy one from another tablr--.>" + sql.toString());*/
        String sql_delete = "";
       /* String sql_delete1 = "delete from " + TEMP_ORDER_DETAILS.NAME + " where " + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + "="
                + "(select " + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + " from " + TABLE_ORDER_DETAILS.NAME + ", "
                + TEMP_ORDER_DETAILS.NAME + " where  "
                + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + "=" + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + " and "
                + TABLE_ORDER_DETAILS.COL_CLIENT_ID + "=" + retilaerId + ")";*/

        if (flag.equalsIgnoreCase("drawer")) {
            sql_delete = "delete from " + TEMP_ORDER_DETAILS.NAME + " where " + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + "="
                    + "(select " + "d." + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + " from "
                    + TABLE_ORDER_DETAILS.NAME + " d, "
                    + TEMP_ORDER_DETAILS.NAME + " t where  " + "d."
                    + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + "=" + "t."
                    + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + " and " + "d."
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + " in ( select " + TABLE_RETAILER_MASTER.COL_RETAILER_ID + " from "
                    + TABLE_RETAILER_MASTER.NAME + "))";
        } else {
            sql_delete = "delete from " + TEMP_ORDER_DETAILS.NAME + " where " + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + "="
                    + "(select " + "d." + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + " from "
                    + TABLE_ORDER_DETAILS.NAME + " d, "
                    + TEMP_ORDER_DETAILS.NAME + " t where  " + "d."
                    + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + "=" + "t."
                    + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + " and " + "d."
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + "=" + retilaerId + ")";


        }
        MyApplication.log(LOG_TAG, "delet s sql_delete --.>" + sql_delete.toString());

        // db.rawQuery(sql_delete, null);
        //  MyApplication.log(LOG_TAG, "count in copy from details another-->" + c.getCount());
        //    long ret = db.delete(NAME, COL_ORDER_ID + "=?", new String[]{MyApplication.get_session(MyApplication.SESSION_ORDER_ID)});
        String insertSql = "";

        if (flag.equalsIgnoreCase("")) {
            insertSql = "INSERT OR REPLACE INTO " + TEMP_ORDER_DETAILS.NAME
                    + "( " + TEMP_ORDER_DETAILS.COL_ORDER_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_CLIENT_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_ORDER_DATE + ", "
                    + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_ORDER_ITEM_QTY + ", "
                    + TEMP_ORDER_DETAILS.COL_REJECT_ITEM_QTY + ", "
                    + TEMP_ORDER_DETAILS.COL_STOCK_ITEM_QTY + ")"
                    + " select "
                    + TABLE_ORDER_DETAILS.COL_ORDER_ID + ","
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + ","
                    + TABLE_ORDER_DETAILS.COL_ORDER_DATE + ","
                    + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + ","
                    + TABLE_ORDER_DETAILS.COL_ORDER_ITEM_QTY + ","
                    + TABLE_ORDER_DETAILS.COL_REJECT_ITEM_QTY + ","
                    + TABLE_ORDER_DETAILS.COL_STOCK_ITEM_QTY + " "
                    + " from " + TABLE_ORDER_DETAILS.NAME + " where "
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + "=" + retilaerId;
        } else if (flag.equalsIgnoreCase("drawer")) {

            insertSql = "INSERT OR REPLACE INTO " + TEMP_ORDER_DETAILS.NAME
                    + "( " + TEMP_ORDER_DETAILS.COL_ORDER_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_CLIENT_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_ORDER_DATE + ", "
                    + TEMP_ORDER_DETAILS.COL_PRODUCT_ID + ", "
                    + TEMP_ORDER_DETAILS.COL_ORDER_ITEM_QTY + ", "
                    + TEMP_ORDER_DETAILS.COL_REJECT_ITEM_QTY + ", "
                    + TEMP_ORDER_DETAILS.COL_STOCK_ITEM_QTY + ")"
                    + " select "
                    + TABLE_ORDER_DETAILS.COL_ORDER_ID + ","
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + ","
                    + TABLE_ORDER_DETAILS.COL_ORDER_DATE + ","
                    + TABLE_ORDER_DETAILS.COL_PRODUCT_ID + ","
                    + TABLE_ORDER_DETAILS.COL_ORDER_ITEM_QTY + ","
                    + TABLE_ORDER_DETAILS.COL_REJECT_ITEM_QTY + ","
                    + TABLE_ORDER_DETAILS.COL_STOCK_ITEM_QTY + " "
                    + " from " + TABLE_ORDER_DETAILS.NAME + " where "
                    + TABLE_ORDER_DETAILS.COL_CLIENT_ID + " in ( select " + TABLE_RETAILER_MASTER.COL_RETAILER_ID + " from "
                    + TABLE_RETAILER_MASTER.NAME + ")";

        }
        MyApplication.log(LOG_TAG, "delet s insertSql --.>" + insertSql.toString());
        Cursor ret = db.rawQuery(insertSql, null);
        MyApplication.log(LOG_TAG, "inserted details valure asre-->" + ret.getCount());
        if (flag.equalsIgnoreCase("drawer")) {

            db.delete(NAME, null, null);
        } else {
            db.delete(NAME, COL_ORDER_ID + "=?", new String[]{MyApplication.get_session(MyApplication.SESSION_ORDER_ID)});

        }
    }

    public static List<ModelProductList> getSavedItem(String retailerId) {
        MyApplication.log(LOG_TAG, " in getSavedItem() retailerId" + retailerId);
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        List<ModelProductList> modelProductLists = new ArrayList<>();
        String sql = " select * from " + TABLE_ORDER_DETAILS.NAME + " where " + COL_CLIENT_ID + " = '" + retailerId + "'";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                ModelProductList modelProductList = new ModelProductList();
                modelProductList.setProduct_id(cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID)));
                modelProductList.setOrderQty(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_ITEM_QTY))));
                modelProductList.setRejectQty(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COL_REJECT_ITEM_QTY))));
                modelProductList.setStockQty(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COL_STOCK_ITEM_QTY))));
                modelProductList.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE)));
                modelProductList.setFrom_temp("false");
                modelProductLists.add(modelProductList);
                MyApplication.log(LOG_TAG, "getSavedItem() Data-->" + modelProductList.toString());
            } while (cursor.moveToNext());

        } else {

            String sql_temp = " select * from " + TEMP_ORDER_DETAILS.NAME + " where " + COL_CLIENT_ID + " = '" + retailerId + "'";
/*
        String sql = "select * from " + NAME
                + " where " + COL_ORDER_ID + "='" + orderId + "'";*/
            MyApplication.log(LOG_TAG, " in getSavedItem() retailerId sql " + sql);


            Cursor c = db.rawQuery(sql_temp, null);
            MyApplication.log(LOG_TAG, " get count in getSavedItem--" + c.getCount());
            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    ModelProductList modelProductList = new ModelProductList();
                    modelProductList.setProduct_id(c.getString(c.getColumnIndexOrThrow(COL_PRODUCT_ID)));
                    modelProductList.setOrderQty(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(COL_ORDER_ITEM_QTY))));
                    modelProductList.setRejectQty(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(COL_REJECT_ITEM_QTY))));
                    modelProductList.setStockQty(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(COL_STOCK_ITEM_QTY))));
                    modelProductList.setOrderDate(c.getString(c.getColumnIndexOrThrow(COL_ORDER_DATE)));
                    modelProductList.setFrom_temp("true");
                    modelProductLists.add(modelProductList);

                    MyApplication.log(LOG_TAG, "modelProductListmodelProductListmodelProductList -->" + modelProductList.toString());
                } while (c.moveToNext());

            }
        }
        MyApplication.log(LOG_TAG, "saved item list -->" + modelProductLists.toString());
        return modelProductLists;
    }


    public static void insertOrderDetails_new(List<ModelProductList> orderDetail, String cleintId) {
        MyApplication.log(LOG_TAG, " in" + orderDetail.toString());
        for (int i = 0; i < orderDetail.size(); i++) {
            ModelProductList modelProductList = orderDetail.get(i);
            insertOrderDetails(modelProductList, cleintId);
        }
    }

    public static boolean isStockTaken(String retailerId ){
        boolean res = false;
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        String sql = " SELECT "+ COL_STOCK_ITEM_QTY +" FROM " + TABLE_ORDER_DETAILS.NAME + " WHERE "
                + COL_CLIENT_ID + " = '" + retailerId + "'  AND "+COL_ORDER_DATE+ " = '"+MyApplication.getOrderDate()+"' ";

        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            {
               int stock = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(COL_STOCK_ITEM_QTY)));
               if(stock > 0 )
                   res = true;
            }
            while (c.moveToNext()) ;
            MyApplication.log(LOG_TAG, "isStockTaken(), TABLE_ORDER_DETAILS, COUNT-->"+c.getCount()+", res---> "+res);
        }else{
            sql = " SELECT "+ COL_STOCK_ITEM_QTY +" FROM " + TEMP_ORDER_DETAILS.NAME + " WHERE "
                    + COL_CLIENT_ID + " = '" + retailerId + "'  AND "+COL_ORDER_DATE+ " = '"+MyApplication.getOrderDate()+"' ";

            c = db.rawQuery(sql, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                {
                    int stock = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(COL_STOCK_ITEM_QTY)));
                    if(stock > 0 )
                        res = true;
                }
                while (c.moveToNext()) ;
                MyApplication.log(LOG_TAG, "isStockTaken(), TEMP_ORDER_DETAILS, COUNT-->"+c.getCount()+", res---> "+res);
            }
        }

        return res;
    }

}
