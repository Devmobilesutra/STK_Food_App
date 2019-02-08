package com.stk.orderingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.Model.NotificationMessage;
import com.stk.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shashi on 03/05/2018.
 */

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {
    ArrayList<NotificationMessage> rowItems;
    Context context = null;

    public AdapterNotification(Context context, ArrayList<NotificationMessage> rowItems) {
        this.rowItems = rowItems;
        this.context = context;
    }

    @Override
    public AdapterNotification.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_notification_offers, parent, false);
        //  vh = new RecycleAdapter_Product.MyViewHolder(view);
        return new AdapterNotification.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterNotification.MyViewHolder holder, int position) {

        NotificationMessage notificationMessage = rowItems.get(position);

        holder.tvTitle.setText(notificationMessage.getNotificationTitle());
        holder.tvDate.setText(notificationMessage.getRec_date());
        holder.tvMessage.setText(notificationMessage.getNotificationMessage());
        holder.tvExpiryDate.setText(notificationMessage.getExpireDate());

    }

    @Override
    public int getItemCount() {
        return rowItems == null ? 0 : rowItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle = null, tvDate = null, tvMessage = null, tvExpiryDate = null;


        public MyViewHolder(View itemView) {

            super(itemView);

            MyApplication.log("JARVIS", "in RecycleAdapter_Product MyViewHolder data-->");
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvExpiryDate = (TextView) itemView.findViewById(R.id.tvExpiryDate);

        }
    }
}
