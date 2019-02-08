package com.stk.orderingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stk.orderingapp.Activity.ActivityDashboard;
import com.stk.orderingapp.Activity.ActivityProductList;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_ORDER_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_ORDER_MASTER;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JARVIS on 29-Mar-18.
 */


public class RecycleAdapter_Product extends RecyclerView.Adapter<RecycleAdapter_Product.MyViewHolder> {

    final List<ModelProductList> rowItem;
    final List<ModelProductList> prevOrder;
    Context context = null;
    int orderQty = 0, rejectQty = 0, stockQty = 0;
    //  List<ModelProductList> modelProductLists = null;
    String retilerId = "";
    int row_index = -1;

    public RecycleAdapter_Product(Context context, List<ModelProductList> rowItems, String retilerId) {
        MyApplication.log("JARVIS", "in RecycleAdapter_Product data-->" + rowItems.toString());
        this.rowItem = rowItems;
        //  this.modelProductLists = modelProductLists;
        prevOrder = TABLE_ORDER_DETAILS.getSavedItem(retilerId);
        this.context = context;
        this.retilerId = retilerId;
        this.retilerId = retilerId;

    }

    @Override
    public RecycleAdapter_Product.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View vh;
//        RecyclerView.ViewHolder vh;
        ////Log.i("Main Activity", "in onCreateViewHolder ");

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_list, parent, false);
        //  vh = new RecycleAdapter_Product.MyViewHolder(view);
        return new RecycleAdapter_Product.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecycleAdapter_Product.MyViewHolder holder, final int position) {
        MyApplication.log("JARVIS", "in RecycleAdapter_Product onBindViewHolder data-->");
        //Log.i("Main Activity", "in onBindViewHolder ");

        holder.tvRejectQty.setTag(position);
        holder.tvOrderQty.setTag(position);
        holder.tvStockQty.setTag(position);
        holder.tvLastOrderDate.setTag(position);

        holder.line1.clearAnimation();
        holder.line1.clearFocus();
        //    holder.line1.clearColorFilter();

        final ModelProductList item = rowItem.get(position);
        holder.btnOrderPrev.setTag(position);

        MyApplication.log("JARVIS", "in onBindViewHolder data-->" + item.toString());
        holder.tvProductName.setText(item.getProduct_name());
        holder.tvProductRs.setText("Rs. " + item.getProd_price());
        holder.tvProductUOM.setText(item.getProd_uom());
        holder.tvLastDateStock.setText("Distributor Stock on " + item.getStock_date() + " is " + item.getStock_Quantity());
        holder.tvRejectQty.setText(item.getRejectQty() + "");
        holder.tvOrderQty.setText(item.getOrderQty() + "");
        holder.tvStockQty.setText(item.getStockQty() + "");
        holder.remark_one.setText("\u2022 " + item.getRemark1() + "");
        holder.remark_two.setText("\u2022 " + item.getRemark2() + "");
        holder.remark_three.setText("\u2022 " + item.getRemark3() + "");
        Glide.with(context)
                .load(item.getItem_image())
                .fitCenter()
                .placeholder(R.drawable.stkfood_logo)
                .error(R.drawable.ic_broken_image)
                .into(holder.ivProductImage);
        //  EditText tvRejectQty = null,  tvOrderQty = null, tvStockQty = null;

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
                    MyApplication.log("JARVIS", " exception in OrderQty textc hange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //    ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");

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
                    MyApplication.log("JARVIS", " exception in OrderQty textc hange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //    ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");

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
                    MyApplication.log("JARVIS", " exception in rejecctQuty textc hange --" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //    ((MyViewHolder) holder).tvRejectQty.setText(item.getRejectQty() + "");

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

            }
        });


     /*   if ( ActivityProductList.prevProductList.size() > 0 && ActivityProductList.prevProductList.get(position).getProduct_id().contains(item.getProduct_id())) {
            setViewToNonClickable(holder);
        }*/

        holder.btnOrderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) holder.btnOrderPrev.getTag();
                ModelProductList item = rowItem.get(pos);
                //   int pos = (int) ((MyViewHolder) holder).btnOrderPrev.getTag();
                //      ModelFarmer farmer = rowItems.get(pos);
                if ((item.getOrderQty() == 0) &&
                        (item.getRejectQty() == 0) &&
                        (item.getStockQty() == 0)) {


                    Toast.makeText(context, "Please Enter Quantities", Toast.LENGTH_LONG).show();
                } else {
                    row_index = position;
                    ModelProductList modelProductList = new ModelProductList();

                    modelProductList.setProduct_id(item.getProduct_id());
                    modelProductList.setProduct_name(item.getProduct_name());

                    modelProductList.setOrderQty(item.getOrderQty());

                    modelProductList.setRejectQty(item.getRejectQty());
                    modelProductList.setStockQty(item.getStockQty());

                    addToList(modelProductList);

                  /*  if (pos == position) {
                        ((MyViewHolder) holder).linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                        ((MyViewHolder) holder).line1.setBackground(context.getResources().getDrawable(R.drawable.background_change));

                    } else {
            *//*holder.row_linearlayout.setBackgroundColor(Color.parseColor("#ffffff"));
                        holder.tv1.setTextColor(Color.parseColor("#000000"));*//*
                    }*/

                    //   ((MyViewHolder) holder).linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                    //   ((MyViewHolder) holder).linearRecyclerView.setTag(position);

                    long ret = TABLE_ORDER_DETAILS.insertOrderDetails(modelProductList, retilerId);
                    if (ret != 0) {
                        MyApplication.log("JARVIS", "in if");
                        //  holder.linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                        holder.line1.setBackground(context.getResources().getDrawable(R.drawable.background_change));

                    } else {
                        MyApplication.log("JARVIS", "in else ");
                    }

                    // setViewToNonClickable(holder);

                }
                //  modelProductLists.add(modelProductList);


            }


        });
        String db_item_id = "", recPosition = "";
        for (int i = 0; i < prevOrder.size(); i++) {
            ModelProductList modelProductList = prevOrder.get(i);
            db_item_id = modelProductList.getProduct_id();
            MyApplication.log("JARVIS", "in product id from db  db_item_id-->" + db_item_id);

            for (int j = 0; j < rowItem.size(); j++) {
                ModelProductList modelProductList_adpter = rowItem.get(position);
                recPosition = rowItem.get(position).getProduct_id();

                MyApplication.log("JARVIS", "in product id from recycler view  recPosition-->" + recPosition);

                if (recPosition.equalsIgnoreCase(db_item_id)) {
                    if (modelProductList.getFrom_temp().equalsIgnoreCase("false")) {
                        holder.linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                        holder.line1.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                    }
                    holder.tvStockQty.setText(modelProductList.getStockQty() + "");
                    holder.tvOrderQty.setText(modelProductList.getOrderQty() + "");
                    holder.tvRejectQty.setText(modelProductList.getRejectQty() + "");
                    holder.tvLastOrderDate.setText("Last Order:- " + modelProductList.getOrderDate() + "");
                    modelProductList_adpter.setStockQty(modelProductList.getStockQty());
                    modelProductList_adpter.setOrderQty(modelProductList.getOrderQty());
                    modelProductList_adpter.setRejectQty(modelProductList.getRejectQty());
                }
            }
        }


    }

    private void addToList(ModelProductList modelProductList) {

        if (ActivityProductList.prevProductList.size() != 0) {
            for (int i = 0; i < ActivityProductList.prevProductList.size(); i++) {

                if (ActivityProductList.prevProductList.get(i).getProduct_id().equalsIgnoreCase(modelProductList.getProduct_id())) {
                    MyApplication.log("JARVIS", "in If");

                    ActivityProductList.prevProductList.remove(i);
                    ActivityProductList.prevProductList.add(modelProductList);

                } else {
                    MyApplication.log("JARVIS", "in Else");
                    ActivityProductList.prevProductList.add(modelProductList);
                }


            }
        } else {
            MyApplication.log("JARVIS", "in Else else");
            ActivityProductList.prevProductList.add(modelProductList);
        }

    }

    private void setViewToNonClickable(RecyclerView.ViewHolder holder) {


        ((MyViewHolder) holder).ivProductImage.setClickable(false);
        ((MyViewHolder) holder).ivOrderMinus.setClickable(false);
        ((MyViewHolder) holder).ivOrderPlus.setClickable(false);
        ((MyViewHolder) holder).ivStockMinus.setClickable(false);
        ((MyViewHolder) holder).ivStockPlus.setClickable(false);
        ((MyViewHolder) holder).ivRejectMinus.setClickable(false);
        ((MyViewHolder) holder).ivRejectPlus.setClickable(false);
        ((MyViewHolder) holder).btnOrderPrev.setClickable(false);
      /*  ((MyViewHolder) holder).linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
        ((MyViewHolder) holder).line1.setBackground(context.getResources().getDrawable(R.drawable.background_change));*/
    }


    @Override
    public int getItemCount() {

        //Log.i("Main Activity", "in getItemCount ");
        return rowItem == null ? 0 : rowItem.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName = null, tvProductRs = null, tvProductUOM = null, tvLastDateStock = null,

        remark_one = null,
                remark_two = null,
                remark_three = null,
                tvLastOrderDate = null;

        EditText tvRejectQty = null, tvOrderQty = null, tvStockQty = null;
        LinearLayout linearRecyclerView = null, line1 = null;
        ImageView
                ivProductImage = null,
                ivOrderMinus = null,
                ivOrderPlus = null,
                ivStockMinus = null,
                ivStockPlus = null,
                ivRejectMinus = null,
                ivRejectPlus = null;

        Button btnOrderPrev = null;

        public MyViewHolder(View itemView) {

            super(itemView);

            MyApplication.log("JARVIS", "in RecycleAdapter_Product MyViewHolder data-->");
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductRs = (TextView) itemView.findViewById(R.id.tvProductRs);
            tvLastDateStock = (TextView) itemView.findViewById(R.id.tvLastDateStock);
            tvProductUOM = (TextView) itemView.findViewById(R.id.tvProductUOM);
            remark_one = (TextView) itemView.findViewById(R.id.remark_one);
            remark_two = (TextView) itemView.findViewById(R.id.remark_two);
            remark_three = (TextView) itemView.findViewById(R.id.remark_three);
            tvLastOrderDate = (TextView) itemView.findViewById(R.id.tvLastOrderDate);


            btnOrderPrev = (Button) itemView.findViewById(R.id.btnAddToCart);


            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            ivOrderMinus = (ImageView) itemView.findViewById(R.id.ivOrderMinus);
            ivOrderPlus = (ImageView) itemView.findViewById(R.id.ivOrderPlus);
            ivStockMinus = (ImageView) itemView.findViewById(R.id.ivStockMinus);
            ivStockPlus = (ImageView) itemView.findViewById(R.id.ivStockPlus);
            ivRejectMinus = (ImageView) itemView.findViewById(R.id.ivRejectMinus);
            ivRejectPlus = (ImageView) itemView.findViewById(R.id.ivRejectPlus);

            tvRejectQty = (EditText) itemView.findViewById(R.id.tvRejectQty);
            tvStockQty = (EditText) itemView.findViewById(R.id.tvStockQty);
            tvOrderQty = (EditText) itemView.findViewById(R.id.tvOrderQty);

            tvOrderQty.setSelection(tvOrderQty.getText().length());
            tvStockQty.setSelection(tvStockQty.getText().length());
            tvRejectQty.setSelection(tvRejectQty.getText().length());

            linearRecyclerView = (LinearLayout) itemView.findViewById(R.id.linearProduct);
            line1 = (LinearLayout) itemView.findViewById(R.id.line1);

        }
    }
}

