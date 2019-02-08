package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JARVIS on 31-Mar-18.
 */

public class ItemDetails extends DefaultResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("item_code")
    @Expose
    private String item_code;

    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("box")
    @Expose
    private String box;

    @SerializedName("unit_per_box")
    @Expose
    private String unit_per_box;

    @SerializedName("rate_per_unit")
    @Expose
    private String rate_per_unit;

    @SerializedName("barcode")
    @Expose
    private String barcode;


    @SerializedName("remark1")
    @Expose
    private String remark1;


    @SerializedName("remark2")
    @Expose
    private String remark2;


    @SerializedName("remark3")
    @Expose
    private String remark3;


    @SerializedName("item_icon")
    @Expose
    private String item_image;

    @SerializedName("uom")
    @Expose
    private String uom;

    @SerializedName("last_update")
    @Expose
    private String last_update;

    @SerializedName("stock_details")
    @Expose
    private ArrayList<StockDetails> stockDetails;

    public ArrayList<StockDetails> getStockDetails() {
        return stockDetails;
    }

    public void setStockDetails(ArrayList<StockDetails> stockDetails) {
        this.stockDetails = stockDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getUnit_per_box() {
        return unit_per_box;
    }

    public void setUnit_per_box(String unit_per_box) {
        this.unit_per_box = unit_per_box;
    }

    public String getRate_per_unit() {
        return rate_per_unit;
    }

    public void setRate_per_unit(String rate_per_unit) {
        this.rate_per_unit = rate_per_unit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
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

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @Override
    public String toString() {
        return "ItemDetails{" +
                "id='" + id + '\'' +
                ", item_code='" + item_code + '\'' +
                ", item_name='" + item_name + '\'' +
                ", unit='" + unit + '\'' +
                ", box='" + box + '\'' +
                ", unit_per_box='" + unit_per_box + '\'' +
                ", rate_per_unit='" + rate_per_unit + '\'' +
                ", barcode='" + barcode + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", item_image='" + item_image + '\'' +
                ", uom='" + uom + '\'' +
                ", last_update='" + last_update + '\'' +
                '}';
    }
}
