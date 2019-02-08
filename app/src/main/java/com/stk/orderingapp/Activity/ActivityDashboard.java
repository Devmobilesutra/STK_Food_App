package com.stk.orderingapp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stk.orderingapp.Config.GPSTracker;
import com.stk.orderingapp.Config.GPSTracker1;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_IMAGES;
import com.stk.orderingapp.DataBase.TABLE_ORDER_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_ORDER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_RETAILER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_ROUTE_MASTER;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.Model.RetailerDetailsResponse;
import com.stk.orderingapp.Model.ShopDetails;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;
import com.stk.orderingapp.adapters.SpinnerRouteAdapter;
import com.stk.orderingapp.drawer.NavigationDrawerCallbacks;
import com.stk.orderingapp.drawer.NavigationDrawerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ActivityDashboard extends AppCompatActivity implements NavigationDrawerCallbacks {
    public static String LOG_TAG = "ActivityDashboard ";
    TextView tvHeaderText = null, tvSubHeaderText = null;
    RecyclerView recyclerViewDistributerList = null;
    static Context context = null;
    static Activity activity = null;
    private LinearLayoutManager linearLayoutManager = null;
    ArrayList<ModelDistributerList> distributerListAdapters = null;
    public static RecycleAdapter recycleAdapter = null;
    static Spinner spinnerRoute = null;
    ImageView ivSync = null;
    TextView routeDate = null;
    static ArrayList<HashMap<String, String>> routeNames = null;
    RelativeLayout linearLayoutDateSpinner = null;
    private int permission_count = 2;
    public static String new_img_name = "";
    LinearLayout lineImg;
    ImageView imgPrev = null, ivDrawer = null;

    private int CAMERA = 3;
    String retilerId = "";
    String retilerName = "", remark = "", imageString = "";
    TextView savedMessage = null;
    private static NavigationDrawerFragment mNavigationDrawerFragment;
    ImageView btn_add_shop;

    // SHOP IMAGE UPDATE
    private int CAMERASHOPICON = 4;
    private static final int CAMERA_REQUEST = 1888, MY_CAMERA_PERMISSION_CODE = 100;
    String imagePath = "";
    File fileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        activity = this;

        MyApplication.set_session("imagePath", "");
        MyApplication.log(LOG_TAG, "onCreate(), FCM --> " + MyApplication.get_session(MyApplication.SESSION_FCM_ID));
        initComp();

        MyApplication.log(LOG_TAG, "onCreate(), SESSION_TODAYS_ROUTE_DATE: " + MyApplication.SESSION_TODAYS_ROUTE_DATE);
        if (!MyApplication.get_session(MyApplication.SESSION_TODAYS_ROUTE_DATE).equals(MyApplication.getDateDBFormat())) {
            //TABLE_ROUTE_MASTER.updateNoRoutesOnTodays();
            TABLE_RETAILER_MASTER.deleteAllShop();
            fetchedTodaysRouteShopDetais(context);  // getTodaysRoutes();
        } else
            setSpinnerRoute();

        initCompListner();
    }

    private void initCompListner() {

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_drawer_call();
            }
        });

        ivSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.set_session(MyApplication.SESSION_FROM_LOGIN, "false");
                ActivityLogin.callRetailerDetailsApi(context);
            }
        });

        btn_add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), ActivityCreateShop.class);
                    i.putExtra("ROUTE_ID", MyApplication.get_session(MyApplication.SESSION_ROUTE_ID));
                    i.putExtra("NEW_ENTERABLE_SHOP_ID", "");
                    i.putExtra("UPDATE_SHOP", "N");

                    finish();
                    startActivity(i);

                } catch (Exception e) {
                    MyApplication.log(LOG_TAG + " btn_add_shop.setOnClickListener(), Exception: " + e.getMessage());
                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name),
                            "");
                }
            }
        });
    }

    public void initComp() {

        tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);
        tvHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_DIST_NAME));
        tvSubHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_USER_FULL_NAME));
        spinnerRoute = (Spinner) findViewById(R.id.spinnerRoute);
        routeDate = (TextView) findViewById(R.id.routeDate);

        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setVisibility(View.VISIBLE);
        tvDate.setText(MyApplication.getCurrentDate());

        ivSync = (ImageView) findViewById(R.id.ivSync);
        imgPrev = (ImageView) findViewById(R.id.imgPrev);
        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        ivDrawer.setVisibility(View.VISIBLE);
        //  ivSync.setVisibility(View.VISIBLE);
        linearLayoutDateSpinner = (RelativeLayout) findViewById(R.id.linearLayoutDateSpinner);
        lineImg = (LinearLayout) findViewById(R.id.lineImg);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer));
        mNavigationDrawerFragment.closeDrawer();

        linearLayoutDateSpinner.setVisibility(View.VISIBLE);
        // routeDate.setText(MyApplication.getCurrentDate());
        routeDate.setText("Day : " + MyApplication.getDayRouteFormat());

        btn_add_shop = (ImageView) findViewById(R.id.btn_add_shop);
        //To get Route List from Data Base..
        //  setSpinnerRoute();

        recyclerViewDistributerList = (RecyclerView) findViewById(R.id.recyclerViewDistributerList);
//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerViewDistributerList.setVisibility(View.VISIBLE);
        //      swipeRefresh.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(ActivityDashboard.this);
        recyclerViewDistributerList.setLayoutManager(linearLayoutManager);
        recyclerViewDistributerList.setHasFixedSize(true);
        // setRecyclerViewAdapter();
    }

    public void setSpinnerRoute() {
        routeNames = TABLE_ROUTE_MASTER.getRouteDetails();
        //routeNames = TABLE_ROUTE_MASTER.getRouteDetailsDayWiseRouteWIse();
        if (routeNames != null) {
            if (routeNames.size() > 0) {
                SpinnerRouteAdapter spinnerRouteAdapter = new SpinnerRouteAdapter(context, routeNames);
                spinnerRoute.setAdapter(spinnerRouteAdapter);
                setRecyclerViewAdapter();
            }

            spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos = 0;
                    MyApplication.log(LOG_TAG, "spinnerRoute.setOnItemSelectedListener-->" + routeNames.get(position).get("routeId"));
                    MyApplication.set_session(MyApplication.SESSION_ROUTE_ID, routeNames.get(position).get("routeId"));
                    setRecyclerViewAdapter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            int pos = 0;
            //if (routeNames != null) {

            for (int i = 0; i < routeNames.size(); i++) {
                if (routeNames.get(i).get("routeId").equalsIgnoreCase(MyApplication.get_session(MyApplication.SESSION_ROUTE_ID))) {
                    pos = i;
                    break;
                }
            }
            spinnerRoute.setSelection(pos);
            // }
        } else {
            btn_add_shop.setVisibility(View.GONE);
            MyApplication.showDialogBox(context, context.getResources().getString(R.string.app_name), "No Route List Today...");
        }
    }

    private void setRecyclerViewAdapter() {
        //To get Distributer List From data base
        distributerListAdapters = TABLE_RETAILER_MASTER.getShopList();
        recycleAdapter = new RecycleAdapter(distributerListAdapters);
        recyclerViewDistributerList.setAdapter(recycleAdapter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    class RecycleAdapter extends RecyclerView.Adapter {

        final List<ModelDistributerList> rowItem;

        public RecycleAdapter(List<ModelDistributerList> rowItems) {
            MyApplication.log(LOG_TAG + " RecycleAdapter() ", " data--> " + rowItems.toString());
            this.rowItem = rowItems;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((MyViewHolder) holder).lin_data_cover.setBackground(getResources().getDrawable(R.drawable.border_background));

            final ModelDistributerList item = rowItem.get(position);
            ((MyViewHolder) holder).tvDistributerName.setText(item.getDistributer_name());
            ((MyViewHolder) holder).tvDistributerAddress.setText(item.getDist_address());
            ((MyViewHolder) holder).tvLastVisitDate.setText(item.getMobile_no());
            ((MyViewHolder) holder).tvLastOrderDate.setText("Last Visit:-" + item.getLast_visit());
            ((MyViewHolder) holder).tvRemark.setText("Remark:-" + item.getRemark());

            ((MyViewHolder) holder).tvDistributerEnterableId.setText("" + item.getShop_id());

            // IF ORDER IS SUBMITTED CHANGE COLOR
            if (item.getLast_visit().equals(MyApplication.getOrderDate())) {
                ((MyViewHolder) holder).lin_data_cover.setBackgroundColor(getResources().getColor(R.color.grey_500));
            }

            Glide.with(context)
                    .load(item.getShop_image())
                    .fitCenter()
                    .placeholder(R.drawable.stkfood_logo)
                    .error(R.drawable.ic_broken_image)
                    .into(((MyViewHolder) holder).shopImage);
        }

        @Override
        public int getItemCount() {
            return rowItem == null ? 0 : rowItem.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_dist_list, parent, false);
            vh = new RecycleAdapter.MyViewHolder(view);
            return vh;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvDistributerName = null, tvDistributerEnterableId, tvDistributerAddress = null, tvLastVisitDate = null, tvLastOrderDate = null, tvRemark = null;
            LinearLayout linearRecyclerView = null;
            ImageView shopImage = null, ivCall = null, ivInfo = null, imgStockRemark = null, ivLocation = null, imgUpdateShop; //ivCamera = null,
            LinearLayout lin_data_cover;

            public MyViewHolder(View itemView) {

                super(itemView);
                tvDistributerName = (TextView) itemView.findViewById(R.id.tvDistributerName);
                tvDistributerAddress = (TextView) itemView.findViewById(R.id.tvDistributerAddress);
                tvLastVisitDate = (TextView) itemView.findViewById(R.id.tvLastVisitDate);
                tvRemark = (TextView) itemView.findViewById(R.id.tvRemark);
                tvLastOrderDate = (TextView) itemView.findViewById(R.id.tvLastOrderDate);
                linearRecyclerView = (LinearLayout) itemView.findViewById(R.id.linearRecyclerView);
                shopImage = (ImageView) itemView.findViewById(R.id.shopImage);

                ivCall = (ImageView) itemView.findViewById(R.id.ivCall);
                //ivCamera = (ImageView) itemView.findViewById(R.id.ivCamera);
                imgStockRemark = (ImageView) itemView.findViewById(R.id.imgStockRemark);
                lin_data_cover = (LinearLayout) itemView.findViewById(R.id.lin_data_cover);
                ivLocation = (ImageView) itemView.findViewById(R.id.ivLocation);
                tvDistributerEnterableId = (TextView) itemView.findViewById(R.id.tvDistributerEnterableId);
                imgUpdateShop = (ImageView) itemView.findViewById(R.id.imgUpdateShop);

                ivCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            ModelDistributerList item = rowItem.get(pos);
                            MyApplication.log(LOG_TAG, " ivCall IS PRESEED for " + item.getDistributer_name());
                            showDialogBox(context, getResources().getString(R.string.app_name),
                                    getResources().getString(R.string.callMessage), "call", item.getMobile_no(), item.getDistributer_name());
                        }
                    }
                });

                shopImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (MyApplication.isOnline(context)) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                ModelDistributerList item = rowItem.get(pos);
                                retilerId = item.getDist_id();
                                retilerName = item.getDistributer_name();
                                MyApplication.log(LOG_TAG + "", " Update SHOP IMAGE: ");

                                try {
                                    MyApplication.set_session("imagePath", "");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                                                == PackageManager.PERMISSION_DENIED) {

                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                                    MY_CAMERA_PERMISSION_CODE);
                                        } else {
                                            updateShopImage(item, pos);
                                        }
                                    } else {
                                        updateShopImage(item, pos);
                                    }
                                } catch (Exception e) {
                                    MyApplication.log(LOG_TAG, " img_shop.setOnClickListener() ERROR: " + e.getMessage());
                                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Unable to open Camera");
                                }
                            }
                        } else {
                            MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                        }
                    }
                });


//
                imgStockRemark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyApplication.isOnline(context)) {

                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                ModelDistributerList item = rowItem.get(pos);
                                retilerId = item.getDist_id();
                                if (TABLE_ORDER_DETAILS.isStockTaken(retilerId)) {
                                    retilerName = item.getDistributer_name();
                                    dialogUploadStockImageAndRemark(item, pos);
                                } else
                                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name),
                                            "First take stock and try again.");
                            }
                        } else
                            MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                    }
                });

                ivLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            ModelDistributerList item = rowItem.get(pos);
                            MyApplication.log(LOG_TAG, "LOCATION IS PRESEED for " + item.getDistributer_name());
                            String lat = "" + item.getLatitude();
                            String lon = "" + item.getLongitude();
                            GPSTracker gps = new GPSTracker(ActivityDashboard.this);
                            gps.setLocationToZero();
                            gps.getLocation();
                            if (!(gps.isDeviceGPSEnable()) || !(gps.canGetLocation())) {
                                gps.showSettingsAlert("Start GPS Location and try again...");
                                return;
                            } else {
                                gps.getLocation();
                                item.setLongitude("" + gps.getLongitude());
                                item.setLatitude("" + gps.getLatitude());
                                showDialogLocation(context, item);
                                if (gps.canGetLocation()) {
                                    gps.getLocation();
                                    String latitude = "" + gps.getLatitude();
                                    String longitude = "" + gps.getLongitude();
                                    Double accuracy = gps.getAccuracy();
                                    MyApplication.log(LOG_TAG, " LAT: " + latitude + ", LON: " + longitude + ", ACC: " + accuracy);
                                    Toast.makeText(context, " LAT: " + latitude + ", LON: " + longitude + ", ACC: " + accuracy, Toast.LENGTH_LONG).show();
                                } else
                                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Please click again");
                            }
                        }
                    }
                });

                linearRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            ModelDistributerList item = rowItem.get(pos);
                            TABLE_ORDER_MASTER.getOrderIdAndInsert(item.getDist_id(), MyApplication.get_session(MyApplication.SESSION_DIST_ID));
                            Intent intent = new Intent(ActivityDashboard.this, ActivityProductList.class);
                            intent.putExtra("retailerName", item.getDistributer_name());
                            intent.putExtra("retailerAddress", item.getDist_address());
                            intent.putExtra("retailerId", item.getDist_id());
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                imgUpdateShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                ModelDistributerList item = rowItem.get(pos);
                                //String shopId = item.getDist_id();
                                String shopId = item.getShop_id();
                                MyApplication.log(LOG_TAG, "imgUpdateShop(), SHOP_ID--> " + shopId + ",  SHOP_NAME-->" + item.getDistributer_name());

                                String routValue = "" + MyApplication.get_session(MyApplication.SESSION_ROUTE_ID);
                                if (!routValue.equalsIgnoreCase("All")) {
                                    /*String lastEnterableId = TABLE_RETAILER_MASTER.getLastEnterableShopId(
                                            MyApplication.get_session(MyApplication.SESSION_ROUTE_ID));
                                    String[] part = lastEnterableId.split("(?<=\\D)(?=\\d)");
                                    int newEnterableId = (Integer.parseInt(part[1])) + 1;*/

                                    Intent i = new Intent(getApplicationContext(), ActivityCreateShop.class);
                                    i.putExtra("ROUTE_ID", MyApplication.get_session(MyApplication.SESSION_ROUTE_ID));
                                    i.putExtra("NEW_ENTERABLE_SHOP_ID", shopId);
                                    i.putExtra("UPDATE_SHOP", "Y");
                                    i.putExtra("SHOP_NAME", item.getDistributer_name());
                                    i.putExtra("MOBILE", item.getMobile_no());
                                    i.putExtra("MAIL", item.getEmail());
                                    i.putExtra("CITY", item.getCity());
                                    i.putExtra("IMAGE_PATH", item.getShop_image());
                                    i.putExtra("ADDRESS", item.getDist_address());
                                    finish();
                                    startActivity(i);
                                } else
                                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name),
                                            "Please select the route first.");
                            }
                        } catch (Exception e) {
                            MyApplication.log(LOG_TAG + " btn_add_shop.setOnClickListener(), Exception: " + e.getMessage());
                            MyApplication.showDialogBox(context, getResources().getString(R.string.app_name),
                                    "Sorry, You cant register new shop this time.");
                        }
                    }
                });


            }
        }

    }

    @Override
    public void onBackPressed() {
        showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.exitMessage), "exit", "", "");
    }

    public void showDialogBox(final Context context, String title, final String message, final String flag, final String mobileNo, final String retailerName) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);

        if (flag.equalsIgnoreCase("call"))
            dlgAlert.setMessage(message + "\n " + retailerName + "\n On " + mobileNo);
        else
            dlgAlert.setTitle(title);

        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (flag.equalsIgnoreCase("exit")) {
                    finishAffinity();
                    System.exit(0);

                } else if (flag.equalsIgnoreCase("call")) {
                    dlgAlert.setMessage(message + "\n " + retailerName + "\n On " + mobileNo);
                    if (!TextUtils.isEmpty(mobileNo) && mobileNo.length() >= 10) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + mobileNo));
                        activity.startActivity(callIntent);

                    } else {
                        Toast.makeText(context, "Not given the valid mobile number.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }


    public static void showDialogLocation(final Context context, final ModelDistributerList item) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage("Shop on Map");
        dlgAlert.setTitle(context.getResources().getString(R.string.app_name));

        dlgAlert.setPositiveButton("See Shop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(context.getApplicationContext(), ActivityShowShopOnMap.class);
                i.putExtra("SHOP_NAME", item.getDistributer_name());
                i.putExtra("LAT", item.getLatitude());
                i.putExtra("LONG", item.getLongitude());
                context.startActivity(i);
            }
        });

        dlgAlert.setNegativeButton("Set Shop Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GPSTracker gps = new GPSTracker(context);
                gps.getLocation();
                MyApplication.log(LOG_TAG, " SHOP_ID: " + item.getDist_id());    //  Enterable ==> item.getShop_id());
                Intent i = new Intent(context.getApplicationContext(), ActivitySetShopLocation.class);

                i.putExtra("SHOP_NAME", item.getDistributer_name());
                i.putExtra("LAT", gps.getLatitude());
                i.putExtra("LONG", gps.getLongitude());
                i.putExtra("SHOP_ID", item.getDist_id());

                context.startActivity(i);
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.log(LOG_TAG, "onActivityResult() , resultCode: " + resultCode);

        // FOR REMARK
        if (requestCode == 3 && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageString = MyApplication.ConvertString(thumbnail);
            savedMessage.setText(retilerName + "_image");
        }

        // FOR SHOP IMAGE UPDATE
        if (requestCode == CAMERASHOPICON && data != null) {
            MyApplication.log(LOG_TAG, "onActivityResult() CAMERA SHOP ICON ");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (TextUtils.isEmpty(imagePath)) {
                File imageStorageDir = new File(Environment.getExternalStorageDirectory(),
                        getResources().getString(R.string.app_name));
                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }
                imagePath = imageStorageDir + File.separator + "IMG-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                fileImage = new File(imagePath);
                MyApplication.set_session("imagePath", imagePath);
            }
            try {
                MyApplication.set_session("imagePath", imagePath);
                fileImage = new File(imagePath);
                FileOutputStream out = new FileOutputStream(imagePath);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                if (fileImage != null) {
                    if (fileImage.isFile() && fileImage.exists()) {
                        Toast.makeText(context, "Image is captured", Toast.LENGTH_LONG).show();
                        /*MyApplication.log(LOG_TAG, "onActivityResult() SHOWING IMAGE BY GLIDE, fileImage:-> " + fileImage.getAbsolutePath());
                         Glide.with(context)
                                .load(fileImage.getAbsoluteFile())
                                .asBitmap()
                                .placeholder(R.drawable.ic_broken_image)
                                .into(img_shop);*/
                    }
                }

                MyApplication.log(LOG_TAG, "onActivityResult() , IMAGE PATH IS CREATED: Path: " + imagePath);
                MyApplication.log(LOG_TAG, "onActivityResult() , FILE ABSOLUTE PATH IS : " + fileImage.getAbsolutePath());

            } catch (Exception e) {
                MyApplication.log(LOG_TAG, "onActivityResult() , Exception: " + e.getMessage());
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public int getPermissionCount() {
        int count = 2;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        int camera_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (camera_permission == permission_granted)
            count -= 1;
        int storage_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage_permission == permission_granted)
            count -= 1;
        return count;
    }

    @TargetApi(23)
    private void check_app_persmission() {
        permission_count = 2;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        MyApplication.log(LOG_TAG, "PermissionGrantedCode->" + permission_granted);

        int storage_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        MyApplication.log(LOG_TAG, "StoragePermission->" + storage_permission);
        if (storage_permission == permission_granted)
            permission_count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        MyApplication.log(LOG_TAG, "CameraPermission->" + camera_permission);
        if (camera_permission == permission_granted)
            permission_count -= 1;

        MyApplication.log(LOG_TAG, "PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                MyApplication.log(LOG_TAG, "i->" + i);

                if (storage_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionArray[i] = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        MyApplication.log(LOG_TAG, "i->WRITE_EXTERNAL_STORAGE");
                        // break;
                    }
                }

                if (camera_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(android.Manifest.permission.CAMERA)) {
                        permissionArray[i] = Manifest.permission.CAMERA;
                        MyApplication.log(LOG_TAG, "i->CAMERA");
                        //break;
                    }
                }

            }
            MyApplication.log(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));

            requestPermissions(permissionArray, permission_count);
        }
    }

    public static Bitmap compressImage(String filePath) {

        Bitmap scaledBitmap = null;
        MyApplication.log(LOG_TAG, "path->" + filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        MyApplication.log(LOG_TAG, "BactualWidth->" + actualWidth);
        Log.i(LOG_TAG, "BactualHeight->" + actualHeight);
//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 768.0f;
        float maxWidth = 1024.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        Log.i(LOG_TAG, "AactualWidth->" + actualWidth);
        Log.i(LOG_TAG, "AactualHeight->" + actualHeight);
//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.i(LOG_TAG, "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.i(LOG_TAG, "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.i(LOG_TAG, "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.i(LOG_TAG, "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        File imageStorageDir = new File(Environment.getExternalStorageDirectory(), "Zylemini");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        new_img_name = "IMG_" + String.valueOf(MyApplication.get_current_date_sec()) + ".jpg";
        String new_str_path = imageStorageDir + File.separator + new_img_name;


        try {
            out = new FileOutputStream(new_str_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//          write the compressed bitmap at the destination specified by filename.
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        return scaledBitmap;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
       /* File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/STKFOOD");*/
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory(), "STKFOOD");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            MyApplication.log(LOG_TAG, "saveImage() -->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static void btn_drawer_call() {
        //   MyApplication.logi("ActivityDashboard", "in btn drawer");

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else
            mNavigationDrawerFragment.openDrawer1();
    }

    public void dialogUploadStockImageAndRemark(final ModelDistributerList model, final int pos) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.camera_reamrk_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setWidth(width - 40);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        final EditText edt_client_name = (EditText) popupView.findViewById(R.id.edt_client_name);

        ImageView img_back = (ImageView) popupView.findViewById(R.id.img_back);
        ImageView ivCamera = (ImageView) popupView.findViewById(R.id.ivCamera);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCamera();
            }
        });

        TextView btn_create = (TextView) popupView.findViewById(R.id.btn_update);
        savedMessage = (TextView) popupView.findViewById(R.id.savedMessage);
        savedMessage.setVisibility(View.VISIBLE);
        // if button is clicked, close the custom dialog
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark = "" + edt_client_name.getText().toString().trim();
                if (TextUtils.isEmpty(imageString)) {
                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Plaese take a photo of stock.");
                } else if (TextUtils.isEmpty(remark)) {
                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Plaese enter remark.");
                } else {

                    TABLE_IMAGES.insertIntoImages(retilerName + "_image",
                            MyApplication.getCurrentDate(),
                            MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                            "1",
                            retilerId,
                            imageString,
                            remark);

                    //uploadStockImageAndRemark();
                    MyApplication.showDialog(context, "Uploading stock data. Please Wait...");
                    if (TABLE_IMAGES.getAllImages() != null && TABLE_IMAGES.getAllImages().length() > 0) {
                        JSONObject jsonObject = TABLE_IMAGES.getAllImages();
                        RestClient.getWebServices().uploadImagesAndRemark(jsonObject,
                                new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {
                                        MyApplication.log("uplodStockImageRemark() ", " in success--->" + s);
                                        MyApplication.stopLoading();

                                        TABLE_IMAGES.deleteImages();
                                        Toast.makeText(context, "Stock details uploaded successfully", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        MyApplication.log("", " in failure--->" + error.getMessage());
                                        MyApplication.stopLoading();
                                    }
                                });
                    } else {
                        MyApplication.stopLoading();
                    }

                    popupWindow.dismiss();
                    recycleAdapter.rowItem.get(pos).setImageSaved(retilerName + "_image");
                    recycleAdapter.rowItem.get(pos).setRemark(remark);
                    recycleAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void updateShopImage(final ModelDistributerList model, final int pos) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_update_shop_image, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(width - 40);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ImageView img_back = (ImageView) popupView.findViewById(R.id.img_back);
        final ImageView ivCamera = (ImageView) popupView.findViewById(R.id.ivCamera);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraUpdateShopImage();
            }
        });

        TextView btn_create = (TextView) popupView.findViewById(R.id.btn_update);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.log(LOG_TAG, "updateShopImage(),  imagePath:-> " + imagePath);
                imagePath = MyApplication.get_session("imagePath");
                fileImage = new File(imagePath);

                if (TextUtils.isEmpty(imagePath)) {
                    Toast.makeText(context, "Please take a photo of Shop", Toast.LENGTH_LONG).show();
                    return;
                }
                if (fileImage == null && !fileImage.isFile()) {
                    //if(!fileImage.exists()) {
                    Toast.makeText(context, "Please take a photo of Shop", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Glide.with(context)
                            .load(fileImage.getAbsoluteFile())
                            .asBitmap()
                            .placeholder(R.drawable.ic_broken_image)
                            .into(ivCamera);

                    if (MyApplication.isOnline(context)) {

                        MyApplication.showDialog(context, "Please Wait...");


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("shop_name", model.getDistributer_name());
                        // map.put("shop_auto_id", model.getDist_id());
                        // map.put("shop_id", "0");
                        map.put("distributor_id", MyApplication.get_session(MyApplication.SESSION_DIST_ID));
                        map.put("route_id", MyApplication.get_session(MyApplication.SESSION_ROUTE_ID));
                        map.put("mobile", model.getMobile_no());
                        map.put("email", model.getEmail());
                        map.put("address", model.getDist_address());
                        map.put("city", model.getCity());
                        ;

                        map.put("shop_auto_id", model.getShop_id());
                        map.put("shop_id", model.getShop_id());
                        map.put("latitude", model.getLatitude());
                        map.put("longitude", model.getLongitude());

                        if (fileImage != null)
                            map.put("image_url1", new TypedFile("image*//*", fileImage));

                        RestClient.getWebServices().registerShop(map,
                                new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {

                                        MyApplication.log(LOG_TAG, "Responce IMAGE UPDATE ----> " + s);
                                        try {
                                            JSONObject res = new JSONObject(s);  // "status":"TRUE","message":"Shop Registered Successfully."}
                                            String status = res.getString("status");
                                            String message = res.getString("message");

                                            MyApplication.stopLoading();
                                            if (status.equals("TRUE")) {
                                                if (fileImage.exists())
                                                    fileImage.delete();
                                                MyApplication.set_session("imagePath", "");
                                                fileImage = new File("");
                                                imagePath = "";
                                                JSONArray shopDetailsArray = res.getJSONArray("shop_details");
                                                JSONObject shopObject = shopDetailsArray.getJSONObject(0);

                                                ShopDetails shopDetails = new ShopDetails();
                                                shopDetails.setId(shopObject.getString("id"));
                                                //shopDetails.setSh(shopObject.getString("shop_id));
                                                shopDetails.setRoute_id(shopObject.getString("route_id"));
                                                shopDetails.setDistributor_id(shopObject.getString("distributor_id"));
                                                shopDetails.setImage_url(shopObject.getString("image_url"));

                                                TABLE_RETAILER_MASTER.updateRetailerImage(shopObject.getString("id"), shopObject.getString("image_url"));
                                                MyApplication.set_session("imagePath", "");

                                                recycleAdapter.rowItem.get(pos).setShop_image(shopObject.getString("image_url"));
                                                recycleAdapter.notifyDataSetChanged();
                                                popupWindow.dismiss();

                                                MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Image saved successfully");
                                            } else
                                                MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Fail to save Image...");

                                        } catch (JSONException e) {
                                            MyApplication.stopLoading();
                                            MyApplication.log(LOG_TAG + "RESPONSE JSONException  ->" + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        fileImage = new File("");
                                        imagePath = "";
                                        MyApplication.log(LOG_TAG + " RetrofitError  ->" + error.getMessage());
                                        MyApplication.stopLoading();
                                    }
                                });
                    } else {
                        MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });
    }


    public void showCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (MyApplication.is_marshmellow()) {
                if (getPermissionCount() > 3)
                    check_app_persmission();
                else
                    startActivityForResult(intent, CAMERA);

            } else {
                startActivityForResult(intent, CAMERA);
            }
        } catch (Exception e) {
            MyApplication.log(LOG_TAG + " showCamera() Exception---> ", e.getMessage());
            Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);
        }
    }

    public void openCameraUpdateShopImage() {
        try {
            MyApplication.set_session("imagePath", "");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imageStorageDir = new File(Environment.getExternalStorageDirectory(),
                    getResources().getString(R.string.app_name));
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            imagePath = imageStorageDir + File.separator + "IMG-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            fileImage = new File(imagePath);
            startActivityForResult(intent, CAMERASHOPICON);

        } catch (Exception e) {
            MyApplication.log(LOG_TAG, "openCamera Exception  ERROR: " + e.getMessage());
            Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);
        }
    }

    public void getTodaysRoutes() {
        MyApplication.showDialog(context, "Please Wait...");
        RestClient.getWebServices().getTodaysRoutes(
                MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        MyApplication.log(LOG_TAG, " getTodaysRoutes() Responce is----> " + s);
                        try {

                            MyApplication.set_session(MyApplication.SESSION_TODAYS_ROUTE_DATE, MyApplication.getDateDBFormat());

                            JSONObject locUpdateObj = new JSONObject(s);
                            String status = locUpdateObj.getString("response_status");
                            String message = locUpdateObj.getString("response_message");

                            if (status.equals("success")) {
                                JSONArray route_list = locUpdateObj.getJSONArray("route_list");
                                HashMap<String, String> routeList = new HashMap<>();

                                for (int i = 0; i < route_list.length(); i++) {
                                    JSONObject routeObj = route_list.getJSONObject(i);
                                    String routeId = routeObj.getString("id");
                                    if (i == 0) {
                                        MyApplication.set_session(MyApplication.SESSION_ROUTE_ID, routeId);
                                    }
                                    //TABLE_ROUTE_MASTER.updateTodaysRouteTask(routeId);
                                }
                                setSpinnerRoute();
                            }

                            MyApplication.stopLoading();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            MyApplication.log(LOG_TAG, " getTodaysRoutes() RESPONSE ERROR PARSING " + e.getMessage());
                            MyApplication.stopLoading();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log(" getTodaysRoutes() RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }

    public void fetchedTodaysRouteShopDetais(final Context context) {
        MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails(), SES_SALESMAN_ID->" + (MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID)));
        MyApplication.showDialog(context, "Please Wait...");

        RestClient.getWebServices().salesPersonLoginWithRoute(MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                "", //"ALL",  //"fri",
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails(), ", "Responce is---->" + s);

                        MyApplication.set_session(MyApplication.SESSION_TODAYS_ROUTE_DATE, MyApplication.getDateDBFormat());

                        RetailerDetailsResponse retailerDetailsResponse = new Gson().fromJson(s, RetailerDetailsResponse.class);
                        if (retailerDetailsResponse.getResponse_status().equalsIgnoreCase("true")) {
                            TABLE_ROUTE_MASTER.insertRouteDetails(retailerDetailsResponse.getRoute_list());
                            MyApplication.stopLoading();
                            setSpinnerRoute();
                        } else {
                            MyApplication.stopLoading();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails() ERROR  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.set_session("imagePath", "");
    }

}