package com.stk.orderingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;

import java.util.List;

/**
 * Created by JARVIS on 26-Apr-18.
 */

public class AdapeterProductTest extends RecyclerView.Adapter {

    List<ModelProductList> modelProductLists = null;

    public AdapeterProductTest(List<ModelProductList> modelProductLists) {
        this.modelProductLists = modelProductLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder  vh;
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_list, parent, false);


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
