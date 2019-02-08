package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JARVIS on 19-Apr-18.
 */

public class StockDetails implements Serializable {

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("date")
    @Expose
    private String date;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "StockDetails{" +
                "quantity='" + quantity + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public void setDate(String date) {
        this.date = date;
    }
}

