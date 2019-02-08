package com.stk.orderingapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.Model.ShopDetails;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class TABLE_RETAILER_MASTER {
    public static String LOG_TAG ="TABLE_RETAILER_MASTER ";
    public static String NAME = "tableRetailerMaster";

    public static String COL_RETAILER_ID = "retailerId",
            COL_SHOP_NAME = "shopName",
            COL_ROUTE_ID = "routeId",
            COL_MOBILE_NO = "mobileNo",
            COL_EMAIL_ID = "emailId",
            COL_RETAILER_ADDRESS = "retailerAddress",
            COL_CITY = "city",
            COL_SHOP_IMAGE = "shopImage",
            COL_DITRIBUTOR_ID = "distributorId",
            COL_LAT = "latitude",
            COL_LON = "longitude",
            COL_SHOP_ID = "shop_id",
            COL_ENTERABLE_SHOP_ID = "enterable_shop_id";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + NAME + " ( "
            + COL_RETAILER_ID + " TEXT UNIQUE, "
            + COL_SHOP_NAME + " INTEGER, "
            + COL_ROUTE_ID + " TEXT, "
            + COL_MOBILE_NO + " TEXT, "
            + COL_EMAIL_ID + " TEXT, "
            + COL_RETAILER_ADDRESS + " TEXT, "
            + COL_CITY + " TEXT, "
            + COL_SHOP_IMAGE + " TEXT, "

            + COL_LAT + " TEXT, "
            + COL_LON + " TEXT, "
            + COL_SHOP_ID +" TEXT, "
            + COL_ENTERABLE_SHOP_ID +" TEXT, "

            +COL_DITRIBUTOR_ID +" TEXT ); ";


    public static void insertRetailerDetails(ShopDetails shopDetails) {
        MyApplication.log(LOG_TAG, "in insertRetailerDetails jsonArray-->" + shopDetails.toString());
        SQLiteDatabase db = MyApplication.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RETAILER_ID, shopDetails.getId());
        cv.put(COL_SHOP_NAME, shopDetails.getShop_name());
        cv.put(COL_ROUTE_ID, shopDetails.getRoute_id());
        cv.put(COL_MOBILE_NO, shopDetails.getMobile());
        cv.put(COL_EMAIL_ID, shopDetails.getEmail());
        cv.put(COL_RETAILER_ADDRESS, shopDetails.getAddress());
        cv.put(COL_CITY, shopDetails.getCity());
        cv.put(COL_DITRIBUTOR_ID, shopDetails.getDistributor_id());
       // cv.put(COL_SHOP_IMAGE, shopDetails.getShop_image());
        cv.put(COL_SHOP_IMAGE, shopDetails.getImage_url());


        cv.put(COL_SHOP_ID, shopDetails.getId() );
        cv.put(COL_LAT, shopDetails.getLatitude());
        cv.put(COL_LON, shopDetails.getLongitude());

        cv.put(COL_ENTERABLE_SHOP_ID, shopDetails.getEnterable_shop_id());

        long ret = db.insertWithOnConflict(NAME, COL_RETAILER_ID, cv, SQLiteDatabase.CONFLICT_REPLACE);
        MyApplication.log(LOG_TAG, "ret of insert or update is ret==> " + ret);
    }

    public static ArrayList<ModelDistributerList> getShopList() {
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ArrayList<ModelDistributerList> modelDistributerListArrayList = new ArrayList<>();
        MyApplication.log(LOG_TAG, "getShopList(), SESSION_ROUTE_ID-->" + MyApplication.get_session(MyApplication.SESSION_ROUTE_ID));

        String sql = "";
        if (MyApplication.get_session(MyApplication.SESSION_ROUTE_ID).equalsIgnoreCase("All") ||
                MyApplication.get_session(MyApplication.SESSION_ROUTE_ID).equalsIgnoreCase("")) {
            sql = "SELECT i.remark as remark,i.imageName as ImageName, o.OrderDate as OrderDate, m.* " +
                    "FROM " + TABLE_RETAILER_MASTER.NAME + " m LEFT JOIN " + TABLE_TEMP_ORDER_MASTER.NAME + " o ON " +
                    "m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID + "=" + TABLE_TEMP_ORDER_MASTER.COL_CLIENT_ID
                    + "  LEFT join " + TABLE_IMAGES.NAME + " i ON m." + COL_RETAILER_ID + "=i." + TABLE_IMAGES.COL_IMAGE_RETAILER_ID
                    + " group by m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID;
        } else {
            sql = "SELECT i.remark as remark,i.imageName as ImageName, o.OrderDate as OrderDate, m.* " +
                    "FROM " + TABLE_RETAILER_MASTER.NAME + " m LEFT JOIN "
                    + TABLE_TEMP_ORDER_MASTER.NAME + " o ON " +
                    "m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID + "=" + TABLE_TEMP_ORDER_MASTER.COL_CLIENT_ID
                    + " LEFT join " + TABLE_IMAGES.NAME + " i ON m." + COL_RETAILER_ID + "=i." + TABLE_IMAGES.COL_IMAGE_RETAILER_ID
                    + " WHERE m." + TABLE_RETAILER_MASTER.COL_ROUTE_ID + "='"
                    + MyApplication.get_session(MyApplication.SESSION_ROUTE_ID)
                    + "' group by m." + TABLE_RETAILER_MASTER.COL_RETAILER_ID;
        }
        MyApplication.log(LOG_TAG, "getShopList(), string sql =" + sql);

        Cursor c = db.rawQuery(sql, null);
        MyApplication.log(LOG_TAG, "getShopList(), count is " + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ModelDistributerList modelDistributerList = new ModelDistributerList();
                modelDistributerList.setDist_id(c.getString(c.getColumnIndexOrThrow(COL_RETAILER_ID)));
                modelDistributerList.setDist_address(c.getString(c.getColumnIndexOrThrow(COL_RETAILER_ADDRESS)));
                modelDistributerList.setDistributer_name(c.getString(c.getColumnIndexOrThrow(COL_SHOP_NAME)));
                modelDistributerList.setMobile_no(c.getString(c.getColumnIndexOrThrow(COL_MOBILE_NO)));
                modelDistributerList.setShop_image(c.getString(c.getColumnIndexOrThrow(COL_SHOP_IMAGE)));

                modelDistributerList.setLatitude(c.getString(c.getColumnIndexOrThrow(COL_LAT)));
                modelDistributerList.setLongitude(c.getString(c.getColumnIndexOrThrow(COL_LON)));
                //modelDistributerList.setShop_id(c.getString(c.getColumnIndexOrThrow(COL_SHOP_ID)));
                modelDistributerList.setShop_id(c.getString(c.getColumnIndexOrThrow(COL_ENTERABLE_SHOP_ID)));

                if (c.getString(c.getColumnIndexOrThrow("OrderDate")) == null) {
                    modelDistributerList.setLast_visit("No Order!");
                } else
                    modelDistributerList.setLast_visit(c.getString(c.getColumnIndexOrThrow("OrderDate")));

                if (c.getString(c.getColumnIndexOrThrow("remark")) == null) {
                    modelDistributerList.setRemark("No Remark!");
                } else
                    modelDistributerList.setRemark(c.getString(c.getColumnIndexOrThrow("remark")));

                if (c.getString(c.getColumnIndexOrThrow("ImageName")) == null) {
                    modelDistributerList.setImageSaved("No Image!");
                } else
                    modelDistributerList.setImageSaved(c.getString(c.getColumnIndexOrThrow("ImageName")));

                modelDistributerList.setEmail(c.getString(c.getColumnIndexOrThrow(COL_EMAIL_ID)));
                modelDistributerList.setCity(c.getString(c.getColumnIndexOrThrow(COL_CITY)));

                modelDistributerListArrayList.add(modelDistributerList);
            } while (c.moveToNext());

        }

        MyApplication.log("JARVIS", "modelDistributerListArrayList -->" + modelDistributerListArrayList.toString());
        return modelDistributerListArrayList;
    }

    public static void updateRetailerShopLocation(String shopId, String lat, String lon){
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_SHOP_ID, shopId);
        cv.put(COL_LAT, lat);
        cv.put(COL_LON, lon);

        long ret = db.update(NAME, cv, COL_SHOP_ID+" = ?", new String[]{shopId});
        MyApplication.log(LOG_TAG, "updateRetailerShopLocation() update is ret==>" + ret);
    }

    public static void updateRetailerImage(String shopId, String imagePath){
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_SHOP_ID, shopId);
        cv.put(COL_RETAILER_ID, shopId);
        cv.put(COL_SHOP_IMAGE, imagePath);

        long ret = db.update(NAME, cv, COL_SHOP_ID+" = ?", new String[]{shopId});
        MyApplication.log(LOG_TAG, "updateRetailerImage() update is ret==>" + ret);
    }

    public static String getLastEnterableShopId(String routeId){
        String enterableShopId = "000";
        MyApplication.log(LOG_TAG + " getLastEnterableShopId() ---> ", enterableShopId);

        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        String sql = " SELECT * FROM "+NAME +" WHERE  "+COL_RETAILER_ID+
                " = (SELECT MAX("+COL_RETAILER_ID+")  FROM "+NAME+" WHERE "+COL_ROUTE_ID+" ='"+routeId+"' )";
        Cursor c = db.rawQuery(sql, null);
        //HashMap<String, String> routeList = new HashMap<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            enterableShopId = c.getString(c.getColumnIndexOrThrow(COL_ENTERABLE_SHOP_ID));
        }

        MyApplication.log(LOG_TAG + " getLastEnterableShopId() ---> ", enterableShopId);
        return enterableShopId;

    }

    public static void deleteAllShop(){
        SQLiteDatabase db = MyApplication.db.getReadableDatabase();
        long res = db.delete(NAME, null, null);
        MyApplication.log(LOG_TAG ,"deleteAllShop() RES: "+res);
    }

}
