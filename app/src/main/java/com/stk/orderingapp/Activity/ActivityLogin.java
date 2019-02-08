package com.stk.orderingapp.Activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.DataBase;
import com.stk.orderingapp.DataBase.TABLE_ATTENDANCE_REPORT;
import com.stk.orderingapp.DataBase.TABLE_IMAGES;
import com.stk.orderingapp.DataBase.TABLE_ITEM_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_NOTIFICATION;
import com.stk.orderingapp.DataBase.TABLE_ORDER_DETAILS;
import com.stk.orderingapp.DataBase.TABLE_ORDER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_ROUTE_MASTER;
import com.stk.orderingapp.Model.ItemDetialsResponse;
import com.stk.orderingapp.Model.LoginResponse;
import com.stk.orderingapp.Model.ModelSalemanDetails;
import com.stk.orderingapp.Model.RetailerDetailsResponse;
import com.stk.orderingapp.Model.RouteDetails;
import com.stk.orderingapp.Model.ShopDetails;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityLogin extends AppCompatActivity {
    Button btnLogin = null;
    public static String LOG_TAG = "ActivityLogin ";
    public static Context context = null;
    EditText userName = null, userPassword = null;
    public static DataBase db = null;
    static Activity activity = null;
    TextInputLayout txt_outlet_name = null, txt_outlet_name1 = null;
    private int permission_count = 5;
    CheckBox chkShowPass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        activity = this;
        if (MyApplication.is_marshmellow()) {
            if (getPermissionCount() > 0)
                check_app_persmission();
        }
        initComp();
        initComponentListener();
        setUpperHintColor(getResources().getColor(R.color.white));
    }


    private void initComp() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userName = (EditText) findViewById(R.id.userName);

        userName.setSelection(userName.getText().length());

        txt_outlet_name = (TextInputLayout) findViewById(R.id.txt_outlet_name);
        txt_outlet_name1 = (TextInputLayout) findViewById(R.id.txt_outlet_name1);
        chkShowPass = (CheckBox) findViewById(R.id.chkShowPass);
        Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(), "Nunito_Regular.ttf");
        btnLogin.setTypeface(tf);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userPassword.setSelection(userPassword.getText().length());

    }

    private void initComponentListener() {
        chkShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().equalsIgnoreCase("")) {
                    userName.setError("Please Enter User Name");
                } else if (userPassword.getText().toString().equalsIgnoreCase("")) {
                    userName.setError("Please Enter Password");
                } else {
                    if (MyApplication.isOnline(context)) {
                        if (!TextUtils.isEmpty(MyApplication.get_session(MyApplication.SESSION_FCM_ID)))
                            callLoginApi();
                        else
                            MyApplication.showDialogBox(context, "", "Your connectivity speed is slow, Please click again.");

                    } else {
                        MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });
    }

    private void callLoginApi() {
        MyApplication.showDialog(context, "Please Wait...");
        RestClient.getWebServices().salesPersonLogin(userName.getText().toString(),
                userPassword.getText().toString(),
                MyApplication.get_session(MyApplication.SESSION_FCM_ID),
                MyApplication.get_session(MyApplication.SESSION_DEVICE_ID),
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        MyApplication.log(LOG_TAG + "", "callLoginApi(), Responce is---->" + s);
                        LoginResponse loginResponse = new Gson().fromJson(s, LoginResponse.class);
                        if (loginResponse.getResponse_status().equalsIgnoreCase("true")) {
                            MyApplication.set_session(MyApplication.SESSION_LOGIN, "true");
                            ModelSalemanDetails modelSalemanDetails = loginResponse.getSalesman_details();
                            MyApplication.set_session(MyApplication.SESSION_USER_FULL_NAME, modelSalemanDetails.getName());
                            MyApplication.set_session(MyApplication.SESSION_USER_NAME, modelSalemanDetails.getUsername());
                            MyApplication.set_session(MyApplication.SESSION_PASSWORD, modelSalemanDetails.getPassword());
                            MyApplication.set_session(MyApplication.SESSION_SALESMAN_ID, modelSalemanDetails.getId());
                            MyApplication.set_session(MyApplication.SESSION_DIST_NAME, modelSalemanDetails.getDistributor_name());
                            MyApplication.set_session(MyApplication.SESSION_DIST_ID, modelSalemanDetails.getDistributor_id());

                            MyApplication.set_session(MyApplication.SESSION_LOGIN_ID, userName.getText().toString());
                            MyApplication.set_session(MyApplication.SESSION_LOGIN_PASS, userPassword.getText().toString());
                            MyApplication.stopLoading();
                            //callRetailerDetailsApi(context);  // NO NEED
                            callGetItemDetailsApi(context);
                        } else {
                            MyApplication.stopLoading();
                            MyApplication.set_session(MyApplication.SESSION_LOGIN, "false");
                            Toast.makeText(context, "Please enter Valid user Name and Password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log("RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });

    }

    public static void callRetailerDetailsApi(final Context context) {
        MyApplication.log(LOG_TAG + "callRetailerDetailsApi(), SES_SALESMAN_ID->" + (MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID)));
        MyApplication.showDialog(context, "Please Wait...");

        RestClient.getWebServices().salesPersonLoginWithRoute(MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                "",//"ALL",  //"fri",
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        MyApplication.log(LOG_TAG + "callRetailerDetailsApi(), ", "Responce is---->" + s);

                        RetailerDetailsResponse retailerDetailsResponse = new Gson().fromJson(s, RetailerDetailsResponse.class);
                        if (retailerDetailsResponse.getResponse_status().equalsIgnoreCase("true")) {

                            TABLE_ROUTE_MASTER.insertRouteDetails(retailerDetailsResponse.getRoute_list());
                            MyApplication.stopLoading();

                            callGetItemDetailsApi(context);
                            //  ActivityDashboard.setSpinnerRoute();
                        } else {
                            MyApplication.stopLoading();
                            MyApplication.log(LOG_TAG + " RetrofitError  ->" + "in else of success ");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log("RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }

    public static void callGetItemDetailsApi(final Context context) {

        MyApplication.log("RetrofitError  ->" + (MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID)));
        MyApplication.showDialog(context, "Please Wait...Fetching Items...");

        RestClient.getWebServices().getItemDetails(MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        MyApplication.log(LOG_TAG + "callGetItemDetailsApi(), ", "Responce is---->" + s);

                        ItemDetialsResponse itemDetialsResponse = new Gson().fromJson(s, ItemDetialsResponse.class);
                        if (itemDetialsResponse.getResponse_status().equalsIgnoreCase("true")) {
                            if (MyApplication.get_session(MyApplication.SESSION_FROM_LOGIN).equalsIgnoreCase("false")) {
                                MyApplication.set_session(MyApplication.SESSION_FROM_LOGIN, "true");
                                TABLE_ITEM_DETAILS.insertItemDetails(itemDetialsResponse.getItemDetails());
                                MyApplication.stopLoading();
                                //ActivityDashboard.setSpinnerRoute();
                                callUploadImageApi(context);
                            } else {
                                TABLE_ITEM_DETAILS.insertItemDetails(itemDetialsResponse.getItemDetails());
                                MyApplication.stopLoading();

                                // get Notifications
                                getNotificationData();
                            }
                        } else {
                            MyApplication.stopLoading();
                            MyApplication.log("RetrofitError  ->" + "in else of success ");
                        }

                    }

                    //RetailerDetailsResponse
                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log("RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }

    public static void callUploadImageApi(final Context context) {
        MyApplication.showDialog(context, "Uploading Images data. Please Wait...");
        // MyApplication.log("JARVIS", "dats is -->" + TABLE_IMAGES.getAllImages());
        if (TABLE_IMAGES.getAllImages() != null && TABLE_IMAGES.getAllImages().length() > 0) {
            // HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = TABLE_IMAGES.getAllImages();
            //map.put("data", jsonObject);
            RestClient.getWebServices().uploadImagesAndRemark(jsonObject,
                    new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            MyApplication.log("callUploadImageApi() ", " in success--->" + s);
                            MyApplication.stopLoading();

                            TABLE_IMAGES.deleteImages();
                            callUploadAllOrdersApi(context);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            MyApplication.log("", " in failure--->");
                            MyApplication.stopLoading();
                        }
                    });
        } else {
            MyApplication.stopLoading();
            callUploadAllOrdersApi(context);
        }
    }

    private static void callUploadAllOrdersApi(Context context) {
        MyApplication.showDialog(context, "Uploading Order data. Please Wait...");
        JSONArray jsonArrayOrderDetail = TABLE_ORDER_MASTER.getOrderDetails("drawer");
        if (jsonArrayOrderDetail.length() > 0) {
            RestClient.getWebServices().sendOrderToServer(MyApplication.getOrderDate(), String.valueOf(jsonArrayOrderDetail), new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String response_status = jsonObject.getString("response_status");
                        if (response_status.equalsIgnoreCase("success")) {
                            TABLE_ORDER_MASTER.deleteRecords("", "drawer");
                            TABLE_ORDER_DETAILS.deleteRecords("", "drawer");
                            ActivityDashboard.btn_drawer_call();
                            ActivityDashboard.recycleAdapter.notifyDataSetChanged();

                            MyApplication.stopLoading();
                            //   showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.orderSubmittedSuccessfully));

                        } else {
                            ActivityDashboard.btn_drawer_call();
                            MyApplication.stopLoading();
                            //     MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.error));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    MyApplication.log(LOG_TAG + "", "callUploadAllOrdersApi() failure-->" + error.getMessage());
                    MyApplication.stopLoading();
                }
            });
        } else {
            ActivityDashboard.btn_drawer_call();
            MyApplication.stopLoading();
        }
    }

    private void setUpperHintColor(int color) {
        try {
            Field field = txt_outlet_name.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(txt_outlet_name, myList);

            Method method = txt_outlet_name.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(txt_outlet_name, true);

            field = txt_outlet_name1.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            field.set(txt_outlet_name1, myList);

            Method method1 = txt_outlet_name1.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method1.setAccessible(true);
            method1.invoke(txt_outlet_name1, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.exitMessage));

    }


    public static void showDialogBox(Context context, String title, String message) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);


        dlgAlert.setTitle(title);

        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                //dialog.dismiss();
            }
        });

        dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //   System.exit(0);
                dialog.dismiss();
            }
        });


        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }

    @TargetApi(23)
    private void check_app_persmission() {
        permission_count = 5;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        // MyApplication.logi(LOG_TAG, "PermissionGrantedCode->" + permission_granted);

        int storage_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //MyApplication.logi(LOG_TAG, "StoragePermission->" + storage_permission);
        if (storage_permission == permission_granted)
            permission_count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        //MyApplication.logi(LOG_TAG, "CameraPermission->" + camera_permission);
        if (camera_permission == permission_granted)
            permission_count -= 1;

        int location_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        //  MyApplication.logi(LOG_TAG, "location_permission->" + location_permission);
        if (location_permission == permission_granted)
            permission_count -= 1;

        int location2_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        //      MyApplication.logi(LOG_TAG, "location_permission->" + location_permission);
        if (location2_permission == permission_granted)
            permission_count -= 1;

        int call_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        //    MyApplication.logi(LOG_TAG, "call_permission->" + call_permission);
        if (call_permission == permission_granted)
            permission_count -= 1;

//        MyApplication.logi(LOG_TAG, "PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                //              MyApplication.logi(LOG_TAG, "i->" + i);

                if (storage_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionArray[i] = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        //         MyApplication.logi(LOG_TAG, "i->WRITE_EXTERNAL_STORAGE");
                        // break;
                    }
                }

                if (camera_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(android.Manifest.permission.CAMERA)) {
                        permissionArray[i] = Manifest.permission.CAMERA;
                        //      MyApplication.logi(LOG_TAG, "i->CAMERA");
                        //break;
                    }
                }
                if (location_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        permissionArray[i] = Manifest.permission.ACCESS_FINE_LOCATION;
                        //         MyApplication.logi(LOG_TAG, "i->ACCESS_FINE_LOCATION");
                        //break;
                    }
                }
                if (location2_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        permissionArray[i] = Manifest.permission.ACCESS_COARSE_LOCATION;
                        //  MyApplication.logi(LOG_TAG, "i->ACCESS_COARSE_LOCATION");
                        //break;
                    }
                }
                if (call_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.CALL_PHONE)) {
                        permissionArray[i] = Manifest.permission.CALL_PHONE;
                        //   MyApplication.logi(LOG_TAG, "i->CALL_PHONE");
                        //break;
                    }
                }
            }
//            MyApplication.logi(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));
            requestPermissions(permissionArray, permission_count);
        }
    }


    public int getPermissionCount() {
        int count = 5;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        int camera_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (camera_permission == permission_granted)
            count -= 1;
        int storage_permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage_permission == permission_granted)
            count -= 1;
        int call_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (call_permission == permission_granted)
            count -= 1;
        int coarse_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (coarse_permission == permission_granted)
            count -= 1;
        int fine_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (fine_permission == permission_granted)
            count -= 1;
        return count;
    }

    public static void getNotificationData() {
        MyApplication.showDialog(context, "Please Wait...\nFetching Notification...");
        RestClient.getWebServices().getNotificationsData(MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID),
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        MyApplication.stopLoading();
                        MyApplication.log(LOG_TAG + "getNotificationsData(), ", "Responce is---->" + s);
                        try {
                            JSONObject jObj = new JSONObject(s);
                            String response_status = jObj.getString("response_status");
                            if (response_status.equalsIgnoreCase("true")) {
                                JSONArray offers_list = jObj.getJSONArray("offers_list");
                                TABLE_NOTIFICATION.insertNotificationData(offers_list);

                                // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
                                String today = MyApplication.getDateDBFormat();
                                String actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
                                if (actionStatus.equals("do_CHECK_IN")) {
                                    Intent i = new Intent(context, ActivityAttendanceCheck.class);
                                    context.startActivity(i);
                                    activity.finish();
                                } else {
                                    Intent intent = new Intent(context, ActivityDashboard.class);
                                    context.startActivity(intent);
                                    activity.finish();
                                    //  overridePendingTransition(R.anim.fade_in_call, R.anim.fade_out_call);
                                }
                            } else {
                                // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
                                String today = MyApplication.getDateDBFormat();
                                String actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
                                if (actionStatus.equals("do_CHECK_IN")) {
                                    Intent i = new Intent(context, ActivityAttendanceCheck.class);
                                    context.startActivity(i);
                                    activity.finish();
                                } else {
                                    Intent intent = new Intent(context, ActivityDashboard.class);
                                    context.startActivity(intent);
                                    activity.finish();
                                    //  overridePendingTransition(R.anim.fade_in_call, R.anim.fade_out_call);
                                }
                            }

                        } catch (JSONException e) {
                            MyApplication.stopLoading();
                            MyApplication.log(LOG_TAG + "getNotificationData(), JSONException(), ERROR---->", e.getMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log(LOG_TAG + "getNotificationData(), failure, ERROR---->", error.getMessage());
                        MyApplication.stopLoading();
                        // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
                        String today = MyApplication.getDateDBFormat();
                        String actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
                        if (actionStatus.equals("do_CHECK_IN")) {
                            Intent i = new Intent(context, ActivityAttendanceCheck.class);
                            context.startActivity(i);
                            activity.finish();
                        } else {
                            Intent intent = new Intent(context, ActivityDashboard.class);
                            context.startActivity(intent);
                            activity.finish();
                            //  overridePendingTransition(R.anim.fade_in_call, R.anim.fade_out_call);
                        }
                    }
                });
    }
}
