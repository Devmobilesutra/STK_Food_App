package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.StockDetails;

import java.util.ArrayList;

/**
 * Created by JARVIS on 19-Apr-18.
 */

public class TABLE_STOCK_DETAILS {

    public static String NAME = "tableStockMaster";
    public static String COL_ID = "id",
            COL_QUANTITY = "quantity",
            COL_ITEM_ID = "intemId",
            COL_DATE = "date";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER ,"
            + COL_ITEM_ID + " TEXT UNIQUE, "
            + COL_QUANTITY + " TEXT , "
            + COL_DATE + "  TEXT ); ";

    public static void insertStockDetails(ArrayList<StockDetails> stockDetails, String itemId) {
        MyApplication.log("JARVIS", "in stockDetails -->" + stockDetails.toString());
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        ContentValues cv = new ContentValues();
        for (int i = 0; i < stockDetails.size(); i++) {
            StockDetails stockDetails1 = stockDetails.get(i);
            cv.put(COL_ITEM_ID, itemId);
            cv.put(COL_DATE, stockDetails1.getDate());
            cv.put(COL_QUANTITY, stockDetails1.getQuantity());

            db.insertWithOnConflict(NAME, COL_ITEM_ID, cv, SQLiteDatabase.CONFLICT_REPLACE);

        }


    }
}
