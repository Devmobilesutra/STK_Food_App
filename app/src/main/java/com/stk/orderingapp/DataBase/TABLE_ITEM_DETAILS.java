package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ItemDetails;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.Model.StockDetails;

import java.util.ArrayList;

/**
 * Created by JARVIS on 31-Mar-18.
 */

public class TABLE_ITEM_DETAILS {

    public static String NAME = "tableItemDetails";
    public static String COL_ID = "id",
            COL_ITEM_CODE = "itemCode",
            COL_ITEM_NAME = "itemName",
            COL_UNIT = "unit",
            COL_BOX = "box",
            COL_UNIT_PER_BOX = "ratePerBox",
            COL_ITEM_UOM = "itemUOM",
            COL_REMARK_ONE = "remarkOne",
            COL_REMARK_TWO = "remarkTwo",
            COL_REMARK_THREE = "remarkThree",
            COL_RATE_PER_UNIT = "ratePerUnit",
            COL_ITEM_IMAGE = "itemImage",
            COL_BARCODE = "barcode";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_ID + " INTEGER, "
            + COL_ITEM_CODE + " TEXT UNIQUE, "
            + COL_ITEM_NAME + " TEXT, "
            + COL_UNIT + " TEXT, "
            + COL_BOX + " TEXT, "
            + COL_UNIT_PER_BOX + " TEXT, "
            + COL_ITEM_UOM + " TEXT, "
            + COL_RATE_PER_UNIT + " TEXT, "
            + COL_REMARK_ONE + " TEXT, "
            + COL_REMARK_TWO + " TEXT, "
            + COL_REMARK_THREE + " TEXT, "
            + COL_ITEM_IMAGE + " TEXT, "
            + COL_BARCODE + " TEXT ); ";


    public static void insertItemDetails(ArrayList<ItemDetails> itemDetails) {
        MyApplication.log("JARVIS", "in insertItemDetails -->" + itemDetails);
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();

        ContentValues cv = new ContentValues();
        for (int i = 0; i < itemDetails.size(); i++) {
            ItemDetails itemDetails1 = itemDetails.get(i);
            cv.put(COL_ID, itemDetails1.getId());
            cv.put(COL_ITEM_CODE, itemDetails1.getItem_code());
            cv.put(COL_ITEM_NAME, itemDetails1.getItem_name());
            cv.put(COL_UNIT, itemDetails1.getUnit());
            cv.put(COL_BOX, itemDetails1.getBox());
            cv.put(COL_UNIT_PER_BOX, itemDetails1.getUnit_per_box());
            cv.put(COL_RATE_PER_UNIT, itemDetails1.getRate_per_unit());
            cv.put(COL_BARCODE, itemDetails1.getBarcode());
            cv.put(COL_REMARK_ONE, itemDetails1.getRemark1());
            cv.put(COL_REMARK_TWO, itemDetails1.getRemark2());
            cv.put(COL_REMARK_THREE, itemDetails1.getRemark3());
            cv.put(COL_ITEM_IMAGE, itemDetails1.getItem_image());
            cv.put(COL_ITEM_UOM, itemDetails1.getUom());

            db.insertWithOnConflict(NAME, COL_ITEM_CODE, cv, SQLiteDatabase.CONFLICT_REPLACE);

            TABLE_STOCK_DETAILS.insertStockDetails(itemDetails1.getStockDetails(), itemDetails1.getId());

        }


    }

    public static ArrayList<ModelProductList> getProductItems() {
        MyApplication.log("JARVIS", "in getProductItems");
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<ModelProductList> productListArrayList = new ArrayList<>();
/*
  sql = "select o.OrderDate as OrderDate, m.* from " + TABLE_RETAILER_MASTER.NAME + " m LEFT JOIN "
   + TABLE_TEMP_ORDER_MASTER.NAME + " o ON " +
                    "m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID + "=" + TABLE_TEMP_ORDER_MASTER.COL_CLIENT_ID
                    + " where m." + TABLE_RETAILER_MASTER.COL_ROUTE_ID + "='"
                    + MyApplication.get_session(MyApplication.SESSION_ROUTE_ID) + "'"
                    + " group by m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID;
* */


        String sql = "select s." + TABLE_STOCK_DETAILS.COL_DATE + " as date, s." + TABLE_STOCK_DETAILS.COL_QUANTITY + " as quantity, i.* from " + TABLE_ITEM_DETAILS.NAME + " i LEFT JOIN "
                + TABLE_STOCK_DETAILS.NAME + "  s ON i." + TABLE_ITEM_DETAILS.COL_ID + "=" + TABLE_STOCK_DETAILS.COL_ITEM_ID;


        MyApplication.log("JARVIS", "sql is for getting stock details" + sql);
        Cursor c = db.rawQuery(sql, null);
        MyApplication.log("JARVIS", "count is " + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

                ModelProductList modelProductList = new ModelProductList();
                modelProductList.setProduct_id(c.getString(c.getColumnIndexOrThrow(COL_ID)));
                modelProductList.setOrderDate("NO ORDER");
                modelProductList.setProduct_name(c.getString(c.getColumnIndexOrThrow(COL_ITEM_NAME)));
                modelProductList.setProd_price(c.getString(c.getColumnIndexOrThrow(COL_RATE_PER_UNIT)));
                modelProductList.setStockQty(0);
                modelProductList.setRejectQty(0);
                modelProductList.setOrderQty(0);
                modelProductList.setRemark1(c.getString(c.getColumnIndexOrThrow(COL_REMARK_ONE)));
                modelProductList.setRemark2(c.getString(c.getColumnIndexOrThrow(COL_REMARK_TWO)));
                modelProductList.setRemark3(c.getString(c.getColumnIndexOrThrow(COL_REMARK_THREE)));
                modelProductList.setItem_image(c.getString(c.getColumnIndexOrThrow(COL_ITEM_IMAGE)));
                modelProductList.setProd_uom(c.getString(c.getColumnIndexOrThrow(COL_ITEM_UOM)));
                modelProductList.setStock_Quantity(c.getString(c.getColumnIndexOrThrow("quantity")));
                modelProductList.setStock_date(c.getString(c.getColumnIndexOrThrow("date")));

                productListArrayList.add(modelProductList);
            } while (c.moveToNext());
        }

        MyApplication.log("JARVIS", "Product List-->" + productListArrayList);
        return productListArrayList;
    }
}
