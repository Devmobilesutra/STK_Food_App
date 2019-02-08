package com.stk.orderingapp.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_ORDER_DETAILS;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;
import com.stk.orderingapp.interfaces.IProduct;

import java.util.List;

/**
 * Created by JARVIS on 29-Mar-18.
 */


public class RecycleAdapter_Product extends RecyclerView.Adapter<MyViewHolder> implements OnClickListenerProduct.iProduct {

    public List<ModelProductList> rowItem = null;
    List<ModelProductList> prevOrder = null;
    Context context = null;
    int orderQty = 0, rejectQty = 0, stockQty = 0;
    //  List<ModelProductList> modelProductLists = null;
    String LOG_TAG = "RecycleAdapter_Product",  retilerId = "";
    int row_index = -1;
    OnClickListenerProduct onClickListenerProduct = null;

    //IProduct iProduct = (IProduct) this;

    public List<ModelProductList> getRowItem() {
        return rowItem;
    }


    public RecycleAdapter_Product(Context context, List<ModelProductList> rowItems,
                                  String retilerId,
                                  OnClickListenerProduct onClickListenerProduct, IProduct iProduct) {
        MyApplication.log(LOG_TAG, "in RecycleAdapter_Product data-->" + rowItems.toString());
        this.rowItem = rowItems;
        //  this.modelProductLists = modelProductLists;
        prevOrder = TABLE_ORDER_DETAILS.getSavedItem(retilerId);
        this.context = context;
        this.retilerId = retilerId;
        this.retilerId = retilerId;
        this.onClickListenerProduct = onClickListenerProduct;
        //  this.iProduct = iProduct;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_list, parent, false);
        return new MyViewHolder(view, rowItem, onClickListenerProduct);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MyApplication.log(LOG_TAG, "in RecycleAdapter_Product onBindViewHolder data-->");

        holder.tvRejectQty.setTag(position);
        holder.tvOrderQty.setTag(position);
        holder.tvStockQty.setTag(position);
        holder.tvLastOrderDate.setTag(position);

        holder.line1.clearAnimation();
        holder.line1.clearFocus();
        //    holder.line1.clearColorFilter();


        final ModelProductList item = rowItem.get(position);
        holder.btnOrderPrev.setTag(position);

        MyApplication.log(LOG_TAG, "in onBindViewHolder data-->" + item.toString());
        holder.tvProductName.setText(item.getProduct_name());
        holder.tvProductRs.setText("Rs. " + item.getProd_price());
        holder.tvProductUOM.setText(item.getProd_uom());
        holder.tvLastDateStock.setText("Distributor Stock on " + item.getStock_date() + " is " + item.getStock_Quantity());

        if (item.getRejectQty() == 0) {
            holder.tvRejectQty.setHint("0");
            holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
        } else {
            holder.tvRejectQty.setText(item.getRejectQty() + "");
            holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
        }
        if (item.getOrderQty() == 0) {
            holder.tvOrderQty.setHint("0");
            holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
        } else {
            holder.tvOrderQty.setText(item.getOrderQty() + "");
            holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
        }
        if (item.getStockQty() == 0) {
            holder.tvStockQty.setHint("0");
            holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
        } else {
            holder.tvStockQty.setText(item.getStockQty() + "");
            holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
        }

        holder.remark_one.setText("\u2022 " + item.getRemark1() + "");
        holder.remark_two.setText("\u2022 " + item.getRemark2() + "");
        holder.remark_three.setText("\u2022 " + item.getRemark3() + "");

        Glide.with(context)
                .load(item.getItem_image())
                .fitCenter()
                .placeholder(R.drawable.stkfood_logo)
                .error(R.drawable.ic_broken_image)
                .into(holder.ivProductImage);

        holder.tvStockQty.addTextChangedListener(new TextWatcher() {
            int pos = (int) holder.tvStockQty.getTag();

            ModelProductList item = rowItem.get(pos);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    item.setStockQty(Integer.parseInt(String.valueOf(s)));
                } catch (NumberFormatException e) {
                    MyApplication.log(LOG_TAG, " exception in OrderQty textchange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //    ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");
                holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
            }
        });

        holder.tvOrderQty.addTextChangedListener(new TextWatcher() {
            int pos = (int) holder.tvOrderQty.getTag();
            ModelProductList item = rowItem.get(pos);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    item.setOrderQty(Integer.parseInt(String.valueOf(s)));
                } catch (NumberFormatException e) {
                    MyApplication.log(LOG_TAG, " exception in OrderQty textc hange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
            }
        });


        holder.tvRejectQty.addTextChangedListener(new TextWatcher() {

            int pos = (int) holder.tvRejectQty.getTag();
            ModelProductList item = rowItem.get(pos);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    item.setRejectQty(Integer.parseInt(String.valueOf(s)));
                } catch (NumberFormatException e) {
                    MyApplication.log(LOG_TAG, " exception in rejecctQuty textc hange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());

            }
        });

        holder.ivOrderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* int pos =(int)holder.ivOrderMinus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                if (item.getOrderQty() > 0) {
                    item.setOrderQty((item.getOrderQty() - 1));
                    ((MyViewHolder) holder).tvOrderQty.setText(item.getOrderQty() + "");
                    holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
                }
            }
        });


        holder.ivOrderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int pos =(int)holder.ivOrderPlus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                item.setOrderQty((item.getOrderQty() + 1));
                ((MyViewHolder) holder).tvOrderQty.setText(item.getOrderQty() + "");
                holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());

            }
        });


        holder.ivStockMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int pos =(int)holder.ivStockMinus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                if (item.getStockQty() > 0) {
                    item.setStockQty((item.getStockQty() - 1));
                    ((MyViewHolder) holder).tvStockQty.setText(item.getStockQty() + "");
                    holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
                }
            }
        });

        holder.ivStockPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   int pos =(int)holder.ivStockPlus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                item.setStockQty((item.getStockQty() + 1));
                ((MyViewHolder) holder).tvStockQty.setText(item.getStockQty() + "");
                holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());

            }
        });

        holder.ivRejectMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    int pos =(int)holder.ivRejectMinus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                if (item.getRejectQty() > 0) {
                    item.setRejectQty(item.getRejectQty() - 1);
                    ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");
                    holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
                }
            }
        });

        holder.ivRejectPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    int pos =(int)holder.ivRejectPlus.getTag();
                ModelProductList item = rowItem.get(pos);*/
                item.setRejectQty(item.getRejectQty() + 1);
                ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");
                holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
            }
        });

        String db_item_id = "", recPosition = "";
        for (int i = 0; i < prevOrder.size(); i++) {
            ModelProductList modelProductList = prevOrder.get(i);
            db_item_id = modelProductList.getProduct_id();
            MyApplication.log(LOG_TAG, "in product id from db  db_item_id-->" + db_item_id);

            for (int j = 0; j < rowItem.size(); j++) {
                ModelProductList modelProductList_adpter = rowItem.get(position);
                recPosition = rowItem.get(position).getProduct_id();

                MyApplication.log(LOG_TAG, "in product id from recycler view  recPosition-->" + recPosition);

                if (recPosition.equalsIgnoreCase(db_item_id)) {
                    if (modelProductList.getFrom_temp().equalsIgnoreCase("false")) {
                        holder.linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                        holder.line1.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                    }

                    if (item.getRejectQty() == 0) {
                        holder.tvRejectQty.setHint("0");
                        holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
                    } else {
                        holder.tvRejectQty.setText(modelProductList.getRejectQty() + "");
                        holder.tvRejectQty.setSelection(holder.tvRejectQty.getText().length());
                    }
                    if (modelProductList.getOrderQty() == 0) {
                        holder.tvOrderQty.setHint("0");
                        holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
                    } else {
                        holder.tvOrderQty.setText(modelProductList.getOrderQty() + "");
                        holder.tvOrderQty.setSelection(holder.tvOrderQty.getText().length());
                    }
                    if (modelProductList.getStockQty() == 0) {
                        holder.tvStockQty.setHint("0");
                        holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
                    } else {
                        holder.tvStockQty.setText(modelProductList.getStockQty() + "");
                        holder.tvStockQty.setSelection(holder.tvStockQty.getText().length());
                    }

                    holder.tvLastOrderDate.setText("Last Order:- " + modelProductList.getOrderDate() + "");
                    modelProductList_adpter.setStockQty(modelProductList.getStockQty());
                    modelProductList_adpter.setOrderQty(modelProductList.getOrderQty());
                    modelProductList_adpter.setRejectQty(modelProductList.getRejectQty());

                }

            }


        }
    }


    @Override
    public int getItemCount() {
        return rowItem == null ? 0 : rowItem.size();
    }


    @Override
    public List<ModelProductList> getOrderDetail() {
        int flag = 0;
        for (int i = 0; i < rowItem.size(); i++) {
            ModelProductList modelProductList = rowItem.get(i);

            if (modelProductList.getOrderQty() > 0 || modelProductList.getRejectQty() > 0 || modelProductList.getStockQty() > 0) {
                flag = 1;
            }
        }

        if (flag == 1)
            return rowItem;
        else
            return null;
    }

    public void getModelList(IProduct.Product iProduct) {
        iProduct.orderDetails(rowItem);
    }
}

