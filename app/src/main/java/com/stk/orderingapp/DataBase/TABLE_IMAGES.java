package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.stk.orderingapp.Config.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JARVIS on 21-Apr-18.
 */

public class TABLE_IMAGES {

    public static String LOG_TAG = "TABLE_IMAGES ";
    public static String NAME = "tableImages";
    public static String COL_ID = "id",
            COL_IMAGE_NAME = "imageName",
            COL_IMAGE_DATA = "imageData",
            COL_IMAGE_DATE = "imageDate",
            COL_IMAGE_SALESMAN_ID = "salesmanId",
            COL_REMARK = "remark",
            COL_IMAGE_DISTRIBUTOR_ID = "distributorId",
            COL_IMAGE_RETAILER_ID = "retailerId";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER ,"
            + COL_IMAGE_NAME + " TEXT, "
            + COL_IMAGE_DATE + " TEXT, "
            + COL_IMAGE_SALESMAN_ID + " TEXT, "
            + COL_REMARK + " TEXT, "
            + COL_IMAGE_DISTRIBUTOR_ID + " TEXT, "
            + COL_IMAGE_RETAILER_ID + " TEXT UNIQUE, "
            + COL_IMAGE_DATA + "  BLOB); ";


    public static void insertIntoImages(String imageName, String imageDate, String sIs, String dId,
                                        String rId, String imageData, String remark) {
        MyApplication.log(LOG_TAG + "  insertIntoImages() ", " imageName-->" + imageName
                + " imageDate-->" + imageDate +
                " sIs-->" + sIs +
                " dId-->" + dId +
                " rId-->" + rId +
                " imageData-->" + imageData);

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_IMAGE_NAME, imageName);
        cv.put(COL_IMAGE_DATE, imageDate);
        cv.put(COL_IMAGE_SALESMAN_ID, sIs);
        cv.put(COL_IMAGE_DISTRIBUTOR_ID, dId);
        cv.put(COL_IMAGE_RETAILER_ID, rId);
        cv.put(COL_IMAGE_DATA, imageData);
        cv.put(COL_REMARK, remark);
        long ret = db.insertWithOnConflict(NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        MyApplication.log(LOG_TAG + "  insertIntoImages() ", "ret valure is--->" + ret);

    }

    public static JSONObject getAllImages() {
        MyApplication.log(LOG_TAG + "  insertIntoImages() ", "in getAllImages");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        String sql = "select * from " + NAME;

        Cursor c = db.rawQuery(sql, null);
        JSONArray jsonArray = new JSONArray();
        JSONObject j = new JSONObject();
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("imageName", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_NAME)));
                    jsonObject.put("date", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_DATE)));
                    jsonObject.put("salesmanId", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_SALESMAN_ID)));
                    jsonObject.put("distrubutorId", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_DISTRIBUTOR_ID)));
                    jsonObject.put("retailerId", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_RETAILER_ID)));
                    jsonObject.put("imageData", c.getString(c.getColumnIndexOrThrow(COL_IMAGE_DATA)));
                    jsonObject.put("remark", "hello");

                    jsonArray.put(jsonObject);
                } while (c.moveToNext());
                j.put("data", jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    public static void deleteImages() {

        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        long ret = db.delete(NAME, null, null);
        MyApplication.log(LOG_TAG + "  insertIntoImages() ", " delete count in images ret-->" + ret);
    }
}
