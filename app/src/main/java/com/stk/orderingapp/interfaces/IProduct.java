package com.stk.orderingapp.interfaces;

import com.stk.orderingapp.Model.ModelProductList;

import java.util.List;

/**
 * Created by Shashi on 18/05/2018.
 */

public interface IProduct {

    public List<ModelProductList> getOrderDetail();
    interface Product{

        public void orderDetails(List<ModelProductList> modelProductLists);
    }
}
