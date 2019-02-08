package com.stk.orderingapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_NOTIFICATION;
import com.stk.orderingapp.Model.NotificationMessage;
import com.stk.orderingapp.Model.NotificationResponse;
import com.stk.orderingapp.R;
import com.stk.orderingapp.adapters.AdapterNotification;
import com.stk.orderingapp.adapters.RecycleAdapter_Product;

import java.util.ArrayList;
import static com.stk.orderingapp.DataBase.TABLE_NOTIFICATION.getNotification;

public class ActitivtyNotificationAndOffers extends AppCompatActivity {
    TextView tvHeaderText = null, tvSubHeaderText = null,
            routeDate = null,
            tvTitle = null, tvDate = null,
            tvMessage = null, tvExpiryDate = null;
    Context context = null;
    ImageView ivDrawer = null;

    Spinner spinnerRoute = null;
    RecyclerView recyclerNotificationList = null;
    ArrayList<NotificationMessage> notificationResponses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actitivty_notification_and_offers);
        context = this;
        initComp();
        initCompListner();
    }

    private void initComp() {

        tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);
        routeDate = (TextView) findViewById(R.id.routeDate);
        spinnerRoute = (Spinner) findViewById(R.id.spinnerRoute);
        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        tvHeaderText.setText(getResources().getString(R.string.notificaionHeader));
        ivDrawer.setVisibility(View.VISIBLE);
        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow));

        spinnerRoute.setVisibility(View.GONE);
        routeDate.setVisibility(View.GONE);
        tvSubHeaderText.setVisibility(View.GONE);

        recyclerNotificationList = (RecyclerView) findViewById(R.id.recyclerNotificationList);

        notificationResponses = getNotification();
        if(notificationResponses.size() > 0) {

            recyclerNotificationList.setNestedScrollingEnabled(false);
            recyclerNotificationList.setItemViewCacheSize(200);
            recyclerNotificationList.setDrawingCacheEnabled(true);
            recyclerNotificationList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerNotificationList.smoothScrollBy(0, 10);
            AdapterNotification recycleAdapter_product = new AdapterNotification(context, notificationResponses);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerNotificationList.setLayoutManager(mLayoutManager);
               /* GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);*/
            //for smooth recycler
            recyclerNotificationList.setItemAnimator(new DefaultItemAnimator());
            recyclerNotificationList.setItemViewCacheSize(200);
            recyclerNotificationList.setDrawingCacheEnabled(true);
            recyclerNotificationList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerNotificationList.smoothScrollBy(0, 10);
            recyclerNotificationList.setAdapter(recycleAdapter_product);
        }
        else
           showDialogBox(context, getResources().getString(R.string.app_name), "There is no notifications.");
    }

    public void showDialogBox(Context context, String title, String message) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);

        dlgAlert.setTitle( context.getResources().getString(R.string.app_name)  );

        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);
                finish();
                startActivity(i);
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
        finish();
        startActivity(intent);
    }

    public void initCompListner(){
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
