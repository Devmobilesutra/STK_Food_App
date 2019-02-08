package com.stk.orderingapp.drawer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.stk.orderingapp.Activity.ActitivtyNotificationAndOffers;
import com.stk.orderingapp.Activity.ActivityAttendanceCheck;
import com.stk.orderingapp.Activity.ActivityDashboard;
import com.stk.orderingapp.Activity.ActivityLogin;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_RETAILER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_ROUTE_MASTER;
import com.stk.orderingapp.Model.RetailerDetailsResponse;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    JSONObject jsonObject = new JSONObject();
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    Context context = null;
    private SwipeRefreshLayout swipe_refresh_layout;
    String Response = "";
    ProgressDialog progressDialog;
    String LOG_TAG = "NavigationDrawerFragment";
    TextView txt_username = null, txt_sync = null,
            txt_noticationsAndOffers = null, txt_dataCollection = null, txt_sync_new = null,
            txt_about_us, add_new_outlet_txt,  txt_checkout, txt_sync_shop;
    String str_sync_strat = "", str_sync_end, str_sync_interval = "";
    Button btn_logout = null;
    String ResponseSuccessString = "";
    Date lasync_date = null;

    final String inputFormat = "HH:mm:ss";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat);
    String compareStringOne = "", compareStringTwo = "";
    Date date;
    Date dateCompareOne;
    Date dateCompareTwo;


    //Context context = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        context = getActivity();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //  txt_sync_new = (TextView) view.findViewById(R.id.txt_sync_new);
        txt_username = (TextView) view.findViewById(R.id.txt_username);
        txt_sync = (TextView) view.findViewById(R.id.txt_sync);
        txt_noticationsAndOffers = (TextView) view.findViewById(R.id.txt_noticationsAndOffers);
        txt_checkout = (TextView) view.findViewById(R.id.txt_checkout);
        txt_sync_shop = (TextView) view.findViewById(R.id.txt_sync_shop);

        //selectItem(mCurrentSelectedPosition);
        initComponentListeners();
        //  check_for_sync();//hide
        //check_for_sync_new();
        ;
        compareStringOne = str_sync_strat + ":00";
        compareStringTwo = str_sync_end + ":00";


        return view;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout) {

        mFragmentContainerView = (View) getActivity().findViewById(fragmentId).getParent();
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.black));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }


  /*  public void check_for_sync() {
     //   MyApplication.logi(LOG_TAG, "check_for_sync");

        try {
            lasync_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(((MyApplication) getActivity().getApplication()).get_last_sync_from_prfs()));
            MyApplication.logi(LOG_TAG, "check_for_sync lasync_date" + lasync_date);


        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Date curr_date = null, curr_d = null;
        try {
            curr_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(((MyApplication) getActivity().getApplication()).CurDate_only + " 07:00:00"));
            curr_d = (((MyApplication) getActivity().getApplication()).formatdate.parse(((MyApplication) getActivity().getApplication()).CurDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //  MyApplication.logi(LOG_TAG, "check_for_sync  date" + lasync_date.before(curr_date) + "--" + curr_d.after(curr_date));
        if (curr_date != null && curr_d != null) {
            if (lasync_date.before(curr_date) && curr_d.after(curr_date)) {
                if (((MyApplication) getActivity().getApplication()).isNetworkAvailable()) {
                    MyApplication.logi(LOG_TAG, "check_for_sync  sync");
                    new HttpgetData().execute();
                } else {
                    Internet_problem();
                }


            } else {
                MyApplication.logi(LOG_TAG, "check_for_sync no sync");
            }
        }
    }*/

  /*  public void check_for_sync_new() {
        MyApplication.logi(LOG_TAG, "check_for_sync");
        Date lasync_date = null;
        try {
            lasync_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(((MyApplication) getActivity().getApplication()).get_last_sync_from_prfs()));
            MyApplication.logi(LOG_TAG, "check_for_sync lasync_date" + lasync_date);
            // lasync_date=(((MyApplication)getActivity().getApplication()).formatdate.parse("2016-12-21 20:51:15"));

        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Date static_satrt_date = null, current_date = null, static_end_date = null;
        try {

            String str_strat_date_server = ((MyApplication) getActivity().getApplication()).CurDate_only + " " + str_sync_strat + ":00";
            String str_end_date_server = ((MyApplication) getActivity().getApplication()).CurDate_only + " " + str_sync_end + ":00";
            MyApplication.logi(LOG_TAG, "static_date->" + str_strat_date_server + "->" + str_end_date_server);

            static_satrt_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(str_strat_date_server));
            static_end_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(str_end_date_server));

            current_date = (((MyApplication) getActivity().getApplication()).formatdate.parse(((MyApplication) getActivity().getApplication()).CurDate));
            // current_date=(((MyApplication)getActivity().getApplication()).formatdate.parse("2016-12-22 19:51:15"));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MyApplication.logi(LOG_TAG, "current_date->" + current_date);
        if (lasync_date.before(static_satrt_date) && current_date.after(static_satrt_date)) {

            MyApplication.logi(LOG_TAG, "check_for_sync no sync if");

            if (((MyApplication) getActivity().getApplication()).isNetworkAvailable()) {
                try {
                    jsonObject = MyApplication.readdata();
                    MyApplication.logi("readdata", "json->" + jsonObject);

                    HttpPutData httpPutData = new HttpPutData();
                    httpPutData.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Internet_problem();
            }

        } else if (lasync_date.before(static_end_date) && current_date.after(static_end_date)) {
            MyApplication.logi(LOG_TAG, "check_for_sync no sync  else");

            if (((MyApplication) getActivity().getApplication()).isNetworkAvailable()) {
                try {
                    jsonObject = MyApplication.readdata();
                    MyApplication.logi("readdata", "json->" + jsonObject);

                    HttpPutData httpPutData = new HttpPutData();
                    httpPutData.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Internet_problem();
            }

        } else {
            MyApplication.logi(LOG_TAG, " else");
        }
    }*/


    public void initComponentListeners() {

        txt_username.setText("Welcome " + MyApplication.get_session(MyApplication.SESSION_USER_FULL_NAME));
     /*   btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout Application..!")
                        .setMessage("Do You Want To logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Exit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Yes button clicked, do something
                                        Toast.makeText(getActivity(),
                                                "Exit button pressed",
                                                Toast.LENGTH_SHORT).show();

                                        MyApplication.set_session("Logout", "Y");

                                        String date = MyApplication.get_current_date_sec_new("dateTime");
                                        MyApplication.set_session(MyApplication.SESSION_CURENT_DATE, "");
                                        MyApplication.logi(LOG_TAG, " in logout date in hhmmss am pm ->" + date);
                                        int ret = TABLE_LOG_USAGES.inserTableLogUsages("Logout", date);
                                        if (ret == 0)
                                            MyApplication.logi(LOG_TAG, "Logout Successfully inserted");
                                        else
                                            MyApplication.logi(LOG_TAG, "Logout NOT inserted");

                                        Intent i = new Intent(getActivity(), ActivityLogin.class);
                                        startActivity(i);
                                        getActivity().finish();

                                        *//*System.runFinalization();
                                        System.exit(0);
                                        getActivity().finish();*//*
                                    }
                                }).setNegativeButton("No", null) // Do nothing on no
                        .show();


            }
        });*/

        txt_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.set_session(MyApplication.SESSION_FROM_LOGIN, "false");
                //ActivityLogin.callRetailerDetailsApi(context);
                ActivityLogin.callUploadImageApi(context);
            }
        });

        txt_noticationsAndOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDashboard.btn_drawer_call();

                Intent intent = new Intent(context, ActitivtyNotificationAndOffers.class);
                getActivity().finish();
                startActivity(intent);

                //MyApplication.showDialogBox(context, getResources().getString(R.string.app_name),
                  //      "You dont have any notification or any offer yet.");
            }
        });

        txt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivityAttendanceCheck.class);
                getActivity().finish();
                startActivity(intent);
                ActivityDashboard.btn_drawer_call();

            }
        });

        txt_sync_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDashboard.btn_drawer_call();

                TABLE_RETAILER_MASTER.deleteAllShop();
                MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails(), SES_SALESMAN_ID->" + (MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID)));
                MyApplication.showDialog(context, "Please Wait...");

                RestClient.getWebServices().salesPersonLoginWithRoute(MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                        "", //"ALL",  //"fri",
                        new Callback<String>() {
                            @Override
                            public void success(String s, retrofit.client.Response response) {
                                MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails(), ", "Responce is---->" + s);

                                MyApplication.set_session(MyApplication.SESSION_TODAYS_ROUTE_DATE, MyApplication.getDateDBFormat());

                                RetailerDetailsResponse retailerDetailsResponse = new Gson().fromJson(s, RetailerDetailsResponse.class);
                                if (retailerDetailsResponse.getResponse_status().equalsIgnoreCase("true")) {
                                    TABLE_ROUTE_MASTER.insertRouteDetails(retailerDetailsResponse.getRoute_list());
                                    MyApplication.stopLoading();

                                } else {
                                    MyApplication.stopLoading();
                                }
                                Intent i = new Intent(getActivity(), ActivityDashboard.class);
                                context.startActivity(i);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                MyApplication.log(LOG_TAG + " fetchedTodaysRouteShopDetails() ERROR  ->" + error.getMessage());
                                MyApplication.stopLoading();
                            }
                        });

            }
        });


    }


    public void showToastMsg(String Msg) {
        //  Toast.makeText(getActivity(), Msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }


    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }

    }

    public void openDrawer1() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    public View getGoogleDrawer() {
        return mFragmentContainerView.findViewById(R.id.googleDrawer);
    }


    public static class RoundImage extends Drawable {
        private final Bitmap mBitmap;
        private final Paint mPaint;
        private final RectF mRectF;
        private final int mBitmapWidth;
        private final int mBitmapHeight;

        public RoundImage(Bitmap bitmap) {
            mBitmap = bitmap;
            mRectF = new RectF();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);

            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawOval(mRectF, mPaint);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRectF.set(bounds);
        }

        @Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmapWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmapHeight;
        }

        public void setAntiAlias(boolean aa) {
            mPaint.setAntiAlias(aa);
            invalidateSelf();
        }

        @Override
        public void setFilterBitmap(boolean filter) {
            mPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        @Override
        public void setDither(boolean dither) {
            mPaint.setDither(dither);
            invalidateSelf();
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

    }


    /*class HttpgetData extends AsyncTask<Void, String, String> {
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.getting_data), getResources().getString(R.string.login_progreass_dialog_messages), true, false);

//            progressDialog.setCancelable(false);
        }

        protected String doInBackground(Void... params) {
            MyApplication.logi(LOG_TAG, "In doInBackground");

            //String urlstring = "http://zylemdemo.com/ZylemMiniWebAPI/api/APIData/GetApiData?user=" + MyApplication.get_session(MyApplication.SESSION_USERNAME) + "&password=" + MyApplication.get_session(MyApplication.SESSION_PASSWORD) + "&deviceid=" + MyApplication.get_session(MyApplication.SESSION_DIVICEID) + "";
            String urlstring = "http://zylemdemo.com/ZylemMiniWebAPITesting/api/APIData/GetApiData?user=" + MyApplication.get_session(MyApplication.SESSION_USERNAME) + "&password=" + MyApplication.get_session(MyApplication.SESSION_PASSWORD) + "&deviceid=" + MyApplication.get_session(MyApplication.SESSION_DIVICEID) + "";

            try {
//                String url = MyApplication.getDataURl;
//                RequestBody formBody = new FormEncodingBuilder()
//                        .add("UserName", MyApplication.get_session(MyApplication.SESSION_USERNAME))
//                        .add("Password", MyApplication.get_session(MyApplication.SESSION_PASSWORD))
//                        .add("deviceid", MyApplication.get_session(MyApplication.SESSION_DIVICEID))
//                        .add("regid", "null")
//                        .build();

                Response = (MyApplication.get_server_call(urlstring));
                MyApplication.logi(LOG_TAG, "getResponse");

            } catch (Exception e) {
                MyApplication.logi(LOG_TAG, "Error ->" + e.getMessage().toString());
            }
            if (Response.equals("-0")) {

            } else {
                MyApplication.logi(LOG_TAG, "Data Bulk Parseing Start");
                int parseingResponse = (((MyApplication) getActivity().getApplication()).getdatafromserver2(Response));
                MyApplication.logi(LOG_TAG, "Data Bulk Parseing end");

                if (parseingResponse == 1) {
                    Response = "1";
                } else {
                    Response = "0";
                }
            }
            publishProgress("progress");

            return Response;


        }

        protected void onProgressUpdate(String... progress) {

            Log.i("Data", "Response" + Response);

            progressDialog.dismiss();
            //  MyApplication.set_session("FROM_NAV", "Y");
            ActivityDashboard activityDashboard = new ActivityDashboard();

            activityDashboard.btn_drawer_call();
            getCurrentActivityAndRefresh();
            *//*ZylemIntentService zylemIntentService = new ZylemIntentService();
            zylemIntentService.getCurrentActivityAndRefresh();*//*
            *//* if (Response.equals("1")) {

                Intent i = new Intent(getActivity(), ActivityDashboard.class);
                startActivity(i);
                getActivity().finish();

            } else {
                MyApplication.set_session(MyApplication.SESSION_DEVICE_INCORRECT, "yes");
                Intent i = new Intent(getActivity(), ActivityLogin.class);
                startActivity(i);
                getActivity().finish();


            }*//*

        }

        protected void onPostExecute(Long result) {

        }

    }// HTTP

    class HttpPutData extends AsyncTask<Void, String, String> {
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "",
                    "Uploading Data.......");
            progressDialog.setCancelable(false);

        }

        protected String doInBackground(Void... params) {
            MyApplication.logi("JARVIS", "");

            //String url = "http://zylemdemo.com/ZylemminiwebapiTesting/api/APIData/PostZylemJsonData";
            //String url = "http://zylemdemo.com/ZylemMiniWebAPI/api/APIData/PostZylemJsonData";
            String url = "http://zylemdemo.com/ZylemMiniWebAPITesting/api/APIData/PostZylemjsonData?LoginID=" + MyApplication.get_session(MyApplication.SESSION_USERNAME) + "&Password=" + MyApplication.get_session(MyApplication.SESSION_PASSWORD) + "&DeviceID=" + MyApplication.get_session(MyApplication.SESSION_DIVICEID);
            MyApplication.logi(LOG_TAG, "PostZylemJsonData : jsonObject-->" + jsonObject.toString());

           *//* RequestBody formBody = new FormEncodingBuilder()
                        .add("LoginID", MyApplication.get_session(MyApplication.SESSION_USERNAME))
                        .add("Password", MyApplication.get_session(MyApplication.SESSION_PASSWORD))
                        .add("DeviceID", MyApplication.get_session(MyApplication.SESSION_DIVICEID))
                        .add(jsonObject.toString())
                        .build();*//*

            Response = MyApplication.post_server_call2(url, jsonObject.toString());

            MyApplication.logi(LOG_TAG, "Response" + Response);

            publishProgress("progress");
            return Response;

        }

        protected void onProgressUpdate(String... progress) {
            if (Response != "-0") {
                String Nodays = "";
                progressDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(Response);
                    ResponseSuccessString = json.getString("success");
                    if (ResponseSuccessString.equalsIgnoreCase("true")) {
                        deleteFolder();
                        //  TABLE_LOG_USAGES.deleteIsSync();
                        //"data":[{"ID":"102","AppOrderID":"1611211739"},{"ID":"103","AppOrderID":"1611211700"}]
                        JSONArray m_jArry = json.getJSONArray("data");
                        for (int i = 0; i < m_jArry.length(); i++) {
                            JSONObject j_obj = m_jArry.getJSONObject(i);
                            String str_order_id = j_obj.getString("AppOrderID");
                            TABLE_ORDER_MASTER.updateSyncData(str_order_id);
                            TABLE_IMAGES_DETAILS.updateSyncDataImages(str_order_id);
                        }
                        MyApplication.update_is_sync(json);

                        MyApplication.deleteRecords();


                        if (((MyApplication) getActivity().getApplication()).isNetworkAvailable()) {
                            new HttpgetData().execute();
                        } else {
                            Internet_problem();
                        }

                        // MyApplication.Error_dialog("Successfully Data Uploaded ",context);
                    } else {
                        MyApplication.Error_dialog("Failed to Data Upload", context);
                    }
                    ResponseSuccessString = json.getString("successLog");
                    if (ResponseSuccessString.equalsIgnoreCase("true")) {
                        TABLE_LOG_USAGES.deleteIsSync();
                        //TABLE_UOM_MASTER.deleteIsSyncU0m();
                    } else {

                    }


                    if (json.has("NoDays")) {
                        // String Nodays = json.getString("NoDays");---first it ws this


                        Nodays = json.getString("NoDays");

                        MyDatabase.update_keeplastNdaysorderbook(Nodays);
                        // int session_value = Integer.parseInt(MyApplication.get_session(MyApplication.SESSION_KEEPLASTNDAYSORDERBOOK));

                    }


                    if (json.has("PreviousData")) {

                        MyApplication.logi(LOG_TAG, "in json has previous data");
                        int nodays = Integer.parseInt(Nodays);
                        MyApplication.logi(LOG_TAG, "nodays" + nodays);
                        String session_n_days = MyApplication.get_session(MyApplication.SESSION_KEEPLASTNDAYSORDERBOOK);
                        int session_days = Integer.parseInt(session_n_days);
                        MyApplication.logi(LOG_TAG, "session_days" + session_days);
                        if (nodays > session_days)

                        {
                            MyApplication.logi(LOG_TAG, "if no >");
                            insertData(json);


                        } else if (nodays != TABLE_ORDER_MASTER.count_of_orders()) {
                            MyApplication.logi(LOG_TAG, "in else if nodays != count of orders");
                            insertData(json);

                        } else {
                            MyApplication.logi(LOG_TAG, "in else of no data");
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                progressDialog.dismiss();
                Internet_problem();

            }
        }

        protected void onPostExecute(Long result) {

        }

    }// HTTP
*/

    /*  public void Internet_problem() {
          AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());

          dlgAlert.setMessage(getResources().getString(R.string.Internet_problem));
  //        dlgAlert.setTitle("Connection Problem");
          dlgAlert.setPositiveButton("OK",
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                          //dismiss the dialog
                          dialog.dismiss();
                          //   check_for_sync();

                      }
                  });
          dlgAlert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {

                  getActivity().finish();
                  System.exit(0);
              }
          });

          dlgAlert.setCancelable(false);
          dlgAlert.create().show();

      }
  */
    @Override
    public void onResume() {
        super.onResume();

    }


    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

  /*  public static void deleteFolder() {

        File imageStorageDir = new File(Environment.getExternalStorageDirectory(), "Zylemini");
        if (!imageStorageDir.exists()) {
            MyApplication.logi("JARVIS", "IN IF OF imageStorageDir.exists() ");
        } else {
            String[] entries = imageStorageDir.list();
            for (String s : entries) {
                File currentFile = new File(imageStorageDir.getPath(), s);
                boolean FileRET = currentFile.delete();
                MyApplication.logi("JARVIS", "value of ret file : " + FileRET);
            }

            MyApplication.logi("JARVIS", "IN ELSE OF imageStorageDir.exists() ");
            boolean RET = imageStorageDir.delete();
            MyApplication.logi("JARVIS", "value of ret folder: " + RET);
        }


    }*/

    //    private class Updater extends TimerTask {
//
//        public Updater() {
//        }
//
//        @Override
//        public void run() {
//            //to do task
//            App.log(LOG_TAG, "In  empty");
//            if (!App.get_session(App.SESSION_USER_LATITUDE).equalsIgnoreCase("")) {
//
//            } else {
//
//
//            }
//
//        }
//    }

/*  public void getCurrentActivityAndRefresh() {
        String currentApp = "", activityName = "";
        // Log.i(LOG_TAG, "In Else ResponseSuccessString true" + ResponseSuccessString);
        ActivityManager am = (ActivityManager) getActivity().getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        currentApp = am.getRunningTasks(1).get(0).topActivity.getClassName();
        // Log.i(LOG_TAG, "currentApp" + currentApp);


                       *//* String[] strArr = currentApp.split(".");
                        for (int i = 0; i < strArr.length; i++) {

                            activityName = strArr[i];
                        }*//*
        //Activity obj = null;
        Class<?> myClass1 = null;
        String myClass = currentApp;
        try {
            myClass1 = Class.forName(myClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //  obj = (Activity) myClass1.newInstance();


        if (myClass1 != null) {
            MyApplication.logi(LOG_TAG, "in if of myClass isnull not null");

            Intent myIntent = new Intent(getActivity().getApplicationContext(), myClass1);
            MyApplication.set_session("FROM_INTENT", "Y");
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String dateTime = MyApplication.get_current_date_sec_new("dateTime");
            myIntent.putExtra("CurrentTime", dateTime);
            MyApplication.set_session("last_syncc", dateTime);
            getActivity().getApplicationContext().startActivity(myIntent);
        } else {
            MyApplication.logi(LOG_TAG, "in else of myClass isnull");
        }


    }*/
  /*  private void insertData(JSONObject json) {
        try {
            JSONObject previousData = json.getJSONObject("PreviousData");
            MyApplication.logi(LOG_TAG, "count previousData-->" + previousData.toString());
            if (previousData.has("OrderMaster")) {
                JSONArray order_master_data = previousData.getJSONArray("OrderMaster");
                MyApplication.logi(LOG_TAG, "count order_master_data 123-->" + order_master_data.toString());
                TABLE_ORDER_MASTER.insertMasterOrder_after_countOforder(order_master_data);


            }
            if (previousData.has("OrderDetails")) {
                JSONArray order_details = previousData.getJSONArray("OrderDetails");
                MyApplication.logi(LOG_TAG, "count OrderDetails 123-->" + order_details.toString());
                TABLE_ORDER_DETAILS.insertOrderDetails_aftre_countOforder(order_details);
            }
        } catch (JSONException e) {
            MyApplication.logi(LOG_TAG, "exception of JSON in insertData" + e.getMessage());
        }
    }*/
}
