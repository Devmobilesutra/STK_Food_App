package com.stk.orderingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JARVIS on 30-Mar-18.
 */

public class LoginResponse extends DefaultResponse implements Serializable {


    @SerializedName("salesman_details")
    @Expose
    private ModelSalemanDetails salesman_details;

    public ModelSalemanDetails getSalesman_details() {
        return salesman_details;
    }


    public void setSalesman_details(ModelSalemanDetails salesman_details) {
        this.salesman_details = salesman_details;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "salesman_details=" + salesman_details +
                '}';
    }
}
