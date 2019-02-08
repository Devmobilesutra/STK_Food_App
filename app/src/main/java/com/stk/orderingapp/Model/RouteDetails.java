package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class RouteDetails extends DefaultResponse implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("route_name")
    @Expose
    private String route_name;

    @SerializedName("shop_list")
    @Expose
    private ArrayList<ShopDetails> shopDetails = new ArrayList<ShopDetails>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public ArrayList<ShopDetails> getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(ArrayList<ShopDetails> shopDetails) {
        this.shopDetails = shopDetails;
    }

    @Override
    public String toString() {
        return "RouteDetails{" +
                "id='" + id + '\'' +
                ", route_name='" + route_name + '\'' +
                ", shopDetails=" + shopDetails +
                '}';
    }
}
