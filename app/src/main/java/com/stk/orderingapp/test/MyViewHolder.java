package com.stk.orderingapp.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;

import java.util.List;

/**
 * Created by Shashi on 10/05/2018.
 */

class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tvProductName = null, tvProductRs = null, tvProductUOM = null, tvLastDateStock = null,
            remark_one = null,
            remark_two = null,
            remark_three = null,
            tvLastOrderDate = null;

    OnClickListenerProduct onClickListenerProduct = null;
    EditText tvRejectQty = null, tvOrderQty = null, tvStockQty = null;
    LinearLayout linearRecyclerView = null, line1 = null;
    RelativeLayout rel1 = null;
    ImageView
            ivProductImage = null,
            ivOrderMinus = null,
            ivOrderPlus = null,
            ivStockMinus = null,
            ivStockPlus = null,
            ivRejectMinus = null,
            ivRejectPlus = null,
            img_up_down = null;

    Button btnOrderPrev = null;
    List<ModelProductList> modelProductLists = null;

    public MyViewHolder(View itemView, List<ModelProductList> modelProductLists, OnClickListenerProduct onClickListenerProduct) {

        super(itemView);
        this.onClickListenerProduct = onClickListenerProduct;
        this.modelProductLists = modelProductLists;
        MyApplication.log("JARVIS", "in RecycleAdapter_Product MyViewHolder data-->");
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
        tvProductRs = (TextView) itemView.findViewById(R.id.tvProductRs);
        tvLastDateStock = (TextView) itemView.findViewById(R.id.tvLastDateStock);
        tvProductUOM = (TextView) itemView.findViewById(R.id.tvProductUOM);
        remark_one = (TextView) itemView.findViewById(R.id.remark_one);
        remark_two = (TextView) itemView.findViewById(R.id.remark_two);
        remark_three = (TextView) itemView.findViewById(R.id.remark_three);
        tvLastOrderDate = (TextView) itemView.findViewById(R.id.tvLastOrderDate);
        rel1 = (RelativeLayout) itemView.findViewById(R.id.rel1);
        btnOrderPrev = (Button) itemView.findViewById(R.id.btnAddToCart);

        ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
        ivOrderMinus = (ImageView) itemView.findViewById(R.id.ivOrderMinus);
        ivOrderPlus = (ImageView) itemView.findViewById(R.id.ivOrderPlus);
        ivStockMinus = (ImageView) itemView.findViewById(R.id.ivStockMinus);
        ivStockPlus = (ImageView) itemView.findViewById(R.id.ivStockPlus);
        ivRejectMinus = (ImageView) itemView.findViewById(R.id.ivRejectMinus);
        ivRejectPlus = (ImageView) itemView.findViewById(R.id.ivRejectPlus);
        img_up_down = (ImageView)  itemView.findViewById(R.id.img_up_down);

        tvRejectQty = (EditText) itemView.findViewById(R.id.tvRejectQty);
        tvStockQty = (EditText) itemView.findViewById(R.id.tvStockQty);
        tvOrderQty = (EditText) itemView.findViewById(R.id.tvOrderQty);

        tvOrderQty.setSelection(tvOrderQty.getText().length());
        tvStockQty.setSelection(tvStockQty.getText().length());
        tvRejectQty.setSelection(tvRejectQty.getText().length());



        linearRecyclerView = (LinearLayout) itemView.findViewById(R.id.linearProduct);
        line1 = (LinearLayout) itemView.findViewById(R.id.line1);
        //  EditText tvRejectQty = null, tvOrderQty = null, tvStockQty = null;
        tvRejectQty.setSelectAllOnFocus(true);
        tvOrderQty.setSelectAllOnFocus(true);
        tvStockQty.setSelectAllOnFocus(true);


        btnOrderPrev.setOnClickListener(this);
        ivOrderMinus.setOnClickListener(this);
        rel1.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivOrderMinus:
                onClickListenerProduct.onOrderMinusClick(getAdapterPosition());
                break;

            case R.id.btnAddToCart:
                ModelProductList modelProductList = modelProductLists.get(getAdapterPosition());
                onClickListenerProduct.onAddButtonClick(modelProductList, line1);
                break;

            case R.id.rel1:
                onClickListenerProduct.onProductNameClick(getAdapterPosition(), line1, img_up_down);
                break;
        }

    }
}