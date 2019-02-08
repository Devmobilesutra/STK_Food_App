package com.stk.orderingapp.Activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_ITEM_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_NOTIFICATION;
import com.stk.orderingapp.DataBase.TABLE_ORDER_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_ORDER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_TEMP_ORDER_MASTER;
import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import com.stk.orderingapp.drawer.NavigationDrawerCallbacks;
import com.stk.orderingapp.interfaces.IProduct;
import com.stk.orderingapp.test.OnClickListenerProduct;
import com.stk.orderingapp.test.RecycleAdapter_Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityProductList extends AppCompatActivity implements OnClickListenerProduct, IProduct, NavigationDrawerCallbacks {
    TextView tvHeaderText = null, tvSubHeaderText = null, routeDate = null;
    Context context = null;
    private LinearLayoutManager linearLayoutManager = null;
    List<ModelProductList> productList = null;
    RelativeLayout linearLayoutDateSpinner = null;
    Spinner spinnerRoute = null;
    ImageView ivDrawer = null;
    public static List<ModelProductList> prevProductList = new ArrayList<>();
    RecyclerView recyclerViewProductList = null;
    Button btnOrderPrev = null;
    IProduct iProduct = null;
    RecycleAdapter_Product recycleAdapter_product = null;
    public String LOG_TAG = "ActivityProductList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        context = this;

        initComp();
        initCompListner();
        getIntentData();
        //iProduct = new RecycleAdapter_Product();
    }

    private void initCompListner() {

        btnOrderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  MyApplication.createDataBase();
              /*  recycleAdapter_product.getModelList(new IProduct.Product() {
                    @Override
                    public void orderDetails(List<ModelProductList> modelProductLists) {
                        if (modelProductLists != null) {
                            MyApplication.log(LOG_TAG, "btnOrderPrev, input implementation -->" + modelProductLists.toString());
                        } else {
                            MyApplication.log(LOG_TAG, "btnOrderPrev, input implementation --> null");
                        }
                    }
                });*/

                List<ModelProductList> modelProductLists = recycleAdapter_product.getOrderDetail();
                if (modelProductLists != null) {
                    MyApplication.log(LOG_TAG, "ipruct implementation-->>>>>" + modelProductLists.toString());
                }

                if (modelProductLists == null) {
                    showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.noOrderDetails), 1);
                } else {
                    TABLE_ORDER_DETAILS.insertOrderDetails_new(modelProductLists, getIntent().getStringExtra("retailerId"));

                    if (MyApplication.isOnline(context)) {
                        MyApplication.log(LOG_TAG, "prevProductList ==>" + prevProductList.toString());
                        JSONArray jsonArrayOrderDetail = TABLE_ORDER_MASTER.getOrderDetails("");
                        if (jsonArrayOrderDetail == null) {
                            MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.noOrderDetailsError));
                        } else {
                            callSendOrderApi(jsonArrayOrderDetail);
                        }
                    } else {
                        showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection), 0);
                        // Toast.makeText(context, "Please Check Your Internet Connection. Then Try...", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityDashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getIntentData() {
        String retailerName = "";
        String retailerAddress = "";
        try {
            retailerName = getIntent().getStringExtra("retailerName");
            retailerAddress = getIntent().getStringExtra("retailerAddress");
            MyApplication.log(LOG_TAG, "getIntentData(),  RET_NAME:---> "+retailerName+", RET_ADDRESS--->"+retailerName);
        } catch (Exception e) {
            MyApplication.log(LOG_TAG, "in exception when getting REtailer Name and Addtess==>" + e.getMessage());
        }

        tvHeaderText.setText(retailerName);
        tvSubHeaderText.setText(retailerAddress);
        prevProductList = new ArrayList<>();
    }

    private void initComp() {
        tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        routeDate = (TextView) findViewById(R.id.routeDate);
        tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);
        btnOrderPrev = (Button) findViewById(R.id.btnOrderPrev);
        linearLayoutDateSpinner = (RelativeLayout) findViewById(R.id.linearLayoutDateSpinner);
        spinnerRoute = (Spinner) findViewById(R.id.spinnerRoute);

        linearLayoutDateSpinner.setVisibility(View.VISIBLE);
        spinnerRoute.setVisibility(View.GONE);
        btnOrderPrev.setVisibility(View.VISIBLE);

        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        ivDrawer.setVisibility(View.VISIBLE);

        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow));

        routeDate.setText("Last Visit:-" + TABLE_TEMP_ORDER_MASTER.getOrderDate(getIntent().getStringExtra("retailerId")));
        productList = TABLE_ITEM_DETAILS.getProductItems();

        if (productList != null && productList.size() > 0) {
            recyclerViewProductList = (RecyclerView) findViewById(R.id.recyclerViewProductList);
            recyclerViewProductList.setVisibility(View.VISIBLE);
            recyclerViewProductList.setNestedScrollingEnabled(false);
            recyclerViewProductList.setItemViewCacheSize(200);
            recyclerViewProductList.setDrawingCacheEnabled(true);
            recyclerViewProductList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerViewProductList.smoothScrollBy(0, 10);

            recycleAdapter_product = new RecycleAdapter_Product(context,
                    productList, getIntent().getStringExtra("retailerId"),
                    this, this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewProductList.setLayoutManager(mLayoutManager);

            //for smooth recycler
            recyclerViewProductList.setItemAnimator(new DefaultItemAnimator());
            recyclerViewProductList.setItemViewCacheSize(200);
            recyclerViewProductList.setDrawingCacheEnabled(true);
            recyclerViewProductList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerViewProductList.smoothScrollBy(0, 10);
            recyclerViewProductList.setAdapter(recycleAdapter_product);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, ActivityDashboard.class);
        startActivity(intent);
        finish();
    }

    //{"response_status":"success","response_message":"Order submitted successfully",
// "Order_details":{"order_status":"Open","order_date":"2018-04-10"}}
    public void callSendOrderApi(JSONArray jsonArray) {
        MyApplication.showDialog(context, "Please Wait...");
        RestClient.getWebServices().sendOrderToServer(MyApplication.getOrderDate(), String.valueOf(jsonArray), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                MyApplication.log(LOG_TAG, "success-->" + s.toString());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String response_status = jsonObject.getString("response_status");

                    if (response_status.equalsIgnoreCase("success")) {
                        TABLE_ORDER_MASTER.deleteRecords(getIntent().getStringExtra("retailerId"), "");
                        TABLE_ORDER_DETAILS.deleteRecords(getIntent().getStringExtra("retailerId"), "");
                        showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.orderSubmittedSuccessfully), 0);
                    } else {
                        MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.error));
                    }

                } catch (JSONException e) {

                }
                MyApplication.stopLoading();
            }

            @Override
            public void failure(RetrofitError error) {
                MyApplication.log(LOG_TAG, "failure-->" + error.getMessage());
                MyApplication.stopLoading();
            }
        });

    }

    public void showDialogBox(Context context, String title, String message, final int flag) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);

        dlgAlert.setTitle(title);

        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (flag == 0) {
                    dialog.dismiss();
                    Intent intent = new Intent(ActivityProductList.this, ActivityDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


    @Override
    public void onAddButtonClick(ModelProductList modelProductList, LinearLayout linearLayout) {
        if ((modelProductList.getOrderQty() == 0) &&
                (modelProductList.getRejectQty() == 0) &&
                (modelProductList.getStockQty() == 0)) {


            Toast.makeText(context, "Please Enter Quantities", Toast.LENGTH_LONG).show();
        } else {
            long ret = TABLE_ORDER_DETAILS.insertOrderDetails(modelProductList, getIntent().getStringExtra("retailerId"));
            if (ret != 0) {
                MyApplication.log(LOG_TAG, "in if");
                //  holder.linearRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.background_change));
                linearLayout.setBackground(context.getResources().getDrawable(R.drawable.background_change));

            } else {
                MyApplication.log(LOG_TAG, "in else ");
            }

        }

    }

    @Override
    public void onOrderMinusClick(final int pos) {
        ModelProductList modelProductList = productList.get(pos);
        if (modelProductList.getOrderQty() > 0) {
            modelProductList.setOrderQty((modelProductList.getOrderQty() - 1));

            //   ((RecycleAdapter_Product.MyViewHolder) holder).tvOrderQty.setText(item.getOrderQty() + "");
        }
        recycleAdapter_product.notifyDataSetChanged();
    }

    @Override
    public void onProductNameClick(int pos, LinearLayout linearLayout, ImageView img_up_down) {

        if (linearLayout.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
            img_up_down.setImageResource(R.drawable.arrow_down);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            img_up_down.setImageResource(R.drawable.arrow_up);
        }
    }

    @Override
    public List<ModelProductList> getOrderDetail() {
        return null;
    }
}
