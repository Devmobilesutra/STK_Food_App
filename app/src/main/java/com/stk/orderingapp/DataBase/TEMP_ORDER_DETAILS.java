package com.stk.orderingapp.DataBase;

/**
 * Created by JARVIS on 11-Apr-18.
 */

public class TEMP_ORDER_DETAILS {

    public static String NAME = "tempOrderDetails";
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
}
