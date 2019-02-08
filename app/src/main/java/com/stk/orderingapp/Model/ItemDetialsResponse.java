package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JARVIS on 31-Mar-18.
 */

public class ItemDetialsResponse extends DefaultResponse implements Serializable {

    @SerializedName("item_list")
    @Expose
    private ArrayList<ItemDetails> itemDetails;

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;


    }

    @Override
    public String toString() {
        return "ItemDetialsResponse{" +
                "itemDetails=" + itemDetails +
                '}';
    }
}
