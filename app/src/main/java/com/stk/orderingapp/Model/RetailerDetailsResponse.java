package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class RetailerDetailsResponse extends DefaultResponse implements Serializable {

    @SerializedName("route_list")
    @Expose
    private ArrayList<RouteDetails> route_list = new ArrayList<>();

    public ArrayList<RouteDetails> getRoute_list() {
        return route_list;
    }

    public void setRoute_list(ArrayList<RouteDetails> route_list) {
        this.route_list = route_list;
    }

    @Override
    public String toString() {
        return "RetailerDetailsResponse{" +
                "route_list=" + route_list +
                '}';
    }
}
