package com.stk.orderingapp.test;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stk.orderingapp.Model.ModelProductList;
import java.util.List;

/**
 * Created by Shashi on 10/05/2018.
 */

public interface OnClickListenerProduct {

    void onAddButtonClick(ModelProductList modelProductList, LinearLayout linearLayout);

    void onOrderMinusClick(final int pos);

    void onProductNameClick(final int pos, LinearLayout linearLayout, ImageView imgView);

    public interface iProduct{
        public List<ModelProductList> getOrderDetail();
    }

}
