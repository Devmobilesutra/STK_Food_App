package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JARVIS on 29-Mar-18.
 */

public class ModelProductList implements Serializable {


    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("dist_id")
    @Expose
    private String dist_id;

    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("prod_price")
    @Expose
    private String prod_price;

    @SerializedName("prod_uom")
    @Expose
    private String prod_uom;

    @SerializedName("last_stock")
    @Expose
    private String last_visit;

    @SerializedName("reatiler_id")
    @Expose
    private String reatiler_id;

    @SerializedName("orderQty")
    @Expose
    private int orderQty;

    @SerializedName("rejectQty")
    @Expose
    private int rejectQty;

    @SerializedName("stockQty")
    @Expose
    private int stockQty;

    @SerializedName("OrderId")
    @Expose
    private String OrderId;

    @SerializedName("remark1")
    @Expose
    private String remark1;


    @SerializedName("remark2")
    @Expose
    private String remark2;


    @SerializedName("remark3")
    @Expose
    private String remark3;

    @SerializedName("from_temp")
    @Expose
    private String from_temp;

    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    @SerializedName("item_icon")
    @Expose
    private String item_image;


    @SerializedName("stock_Quantity")
    @Expose
    private String stock_Quantity;

    @SerializedName("stock_date")
    @Expose
    private String stock_date;

    public String getStock_Quantity() {
        return stock_Quantity;
    }

    public void setStock_Quantity(String stock_Quantity) {
        this.stock_Quantity = stock_Quantity;
    }

    public String getStock_date() {
        return stock_date;
    }

    public void setStock_date(String stock_date) {
        this.stock_date = stock_date;
    }

    public String getFrom_temp() {
        return from_temp;
    }

    public void setFrom_temp(String from_temp) {
        this.from_temp = from_temp;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProd_uom() {
        String prodWithoutUOM = prod_uom;
        if(prod_uom.contains("g")) {
            String removeUOM[] = prod_uom.split("gm", 2);
            removeUOM = prod_uom.split("g", 2);
            prodWithoutUOM = removeUOM[0];
        }
        return prodWithoutUOM;
    }

    public void setProd_uom(String prod_uom) {
        this.prod_uom = prod_uom;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getLast_visit() {
        return last_visit;
    }

    public void setLast_visit(String last_visit) {
        this.last_visit = last_visit;
    }

    public String getReatiler_id() {
        return reatiler_id;
    }

    public void setReatiler_id(String reatiler_id) {
        this.reatiler_id = reatiler_id;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public int getRejectQty() {
        return rejectQty;
    }

    public void setRejectQty(int rejectQty) {
        this.rejectQty = rejectQty;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    @Override
    public String toString() {
        return "ModelProductList{" +
                "product_name='" + product_name + '\'' +
                ", dist_id='" + dist_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", prod_price='" + prod_price + '\'' +
                ", prod_uom='" + prod_uom + '\'' +
                ", last_visit='" + last_visit + '\'' +
                ", reatiler_id='" + reatiler_id + '\'' +
                ", orderQty=" + orderQty +
                ", rejectQty=" + rejectQty +
                ", stockQty=" + stockQty +
                ", OrderId='" + OrderId + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", from_temp='" + from_temp + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", item_image='" + item_image + '\'' +
                ", stock_Quantity='" + stock_Quantity + '\'' +
                ", stock_date='" + stock_date + '\'' +
                '}';
    }
}
