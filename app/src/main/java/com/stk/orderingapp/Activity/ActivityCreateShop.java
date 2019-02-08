package com.stk.orderingapp.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stk.orderingapp.Config.GPSTracker;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_RETAILER_MASTER;
import com.stk.orderingapp.DataBase.TABLE_ROUTE_MASTER;
import com.stk.orderingapp.Manifest;
import com.stk.orderingapp.Model.LoginResponse;
import com.stk.orderingapp.Model.ModelSalemanDetails;
import com.stk.orderingapp.Model.ShopDetails;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;
import com.stk.orderingapp.adapters.SpinnerRouteAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ActivityCreateShop extends AppCompatActivity {

    ImageView ivDrawer; //HEADER BACK BUTTON
    Context context;
    ImageView img_shop;
    TextView tvHeaderText;

    String ROUTE_ID = "", ROUTE_NAME = "", UPDATE_SHOP = "", NEW_ENTERABLE_SHOP_ID = "", IMAGE_PATH="";
    String SHOP_NAME = "", MOBILE = "", ADDRESS = "", MAIL = "", CITY;
    String shopName = "", mob = "", email = "", address = "", city = "";

    Button btn_create;
    EditText edt_shop_name, edt_mob, edt_email, edt_address, edt_city, txt_route_name; // edt_shop_id

    private int CAMERA = 4;
    private static final int CAMERA_REQUEST = 1888, MY_CAMERA_PERMISSION_CODE = 100;

    String LOG_TAG = "ActivityCreateShop ", imagePath = "";
    File fileImage = null;

    GPSTracker gps;
    String latitude = "0.0", longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        MyApplication.set_session("imagePath", "");
        context = this;

        if (getIntent().getExtras() != null) {
            ROUTE_ID = getIntent().getExtras().getString("ROUTE_ID");
            NEW_ENTERABLE_SHOP_ID = getIntent().getExtras().getString("NEW_ENTERABLE_SHOP_ID");
            UPDATE_SHOP = getIntent().getExtras().getString("UPDATE_SHOP");
        } else {
            ROUTE_ID = MyApplication.get_session(MyApplication.SESSION_ROUTE_ID);
            /*String lastEnterableId = TABLE_RETAILER_MASTER.getLastEnterableShopId(ROUTE_ID);
            String[] part = lastEnterableId.split("(?<=\\D)(?=\\d)");
            int newEnterableId = (Integer.parseInt(part[1])) + 1;
            NEW_ENTERABLE_SHOP_ID = part[0] + "" + newEnterableId + "";*/
        }
        ROUTE_NAME = TABLE_ROUTE_MASTER.getRouteNameFromId(ROUTE_ID);

        initComponants();
        initBindData();
        initComponantListner();
        getGPSLocation();

    }

    public void initComponants() {
        RelativeLayout linearLayoutDateSpinner = (RelativeLayout) findViewById(R.id.linearLayoutDateSpinner);
        linearLayoutDateSpinner.setVisibility(View.GONE);

        tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        //tvHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_DIST_NAME));

        TextView tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);
        tvSubHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_USER_FULL_NAME));

        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        ivDrawer.setVisibility(View.VISIBLE);
        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow));

        img_shop = (ImageView) findViewById(R.id.img_shop);
        //spinnRoute = (Spinner) findViewById(R.id.spinnRoute);
        txt_route_name = (EditText) findViewById(R.id.txt_route_name);
        txt_route_name.setActivated(false);
        txt_route_name.setClickable(false);
        txt_route_name.setFocusable(false);
        btn_create = (Button) findViewById(R.id.btn_create);


        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        // edt_shop_id = (EditText) findViewById(R.id.edt_shop_id);

        edt_mob = (EditText) findViewById(R.id.edt_mob);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_city = (EditText) findViewById(R.id.edt_city);

    }

    public void initComponantListner() {
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyApplication.set_session("imagePath", "");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {

                            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            openCamera();
                        }
                    } else
                        openCamera();
                } catch (Exception e) {
                    MyApplication.log(LOG_TAG, " img_shop.setOnClickListener() ERROR: " + e.getMessage());
                    MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), "Unable to open Camera");
                }
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mob = "" + edt_mob.getText().toString().trim();
                email = "" + edt_email.getText().toString().trim();
                address = "" + edt_address.getText().toString().trim();
                city = "" + edt_city.getText().toString().trim();
                shopName = edt_shop_name.getText() + "".trim();

                MyApplication.log(LOG_TAG, " btn_create.setOnClickListener() , IMAGE PATH : " + imagePath);
                MyApplication.log(LOG_TAG, " btn_create.setOnClickListener() , NEW_ENTERABLE_SHOP_ID : " + NEW_ENTERABLE_SHOP_ID);


                if(!UPDATE_SHOP.equalsIgnoreCase("Y")) {
                    if (TextUtils.isEmpty(imagePath)) {
                        //if(!fileImage.exists()) {
                        Toast.makeText(context, "Please take a photo of Shop", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (fileImage == null && !fileImage.isFile()) {
                        //if(!fileImage.exists()) {
                        Toast.makeText(context, "Please take a photo of Shop", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(shopName)) {
                    Toast.makeText(context, "Please enter a shop name", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (TextUtils.isEmpty(NEW_ENTERABLE_SHOP_ID)) {
                    //if(!fileImage.exists()) {
                    Toast.makeText(context, "Please enter shop ID", Toast.LENGTH_LONG).show();
                    return;
                }*/

                if (TextUtils.isEmpty(ROUTE_NAME)) {
                    Toast.makeText(context, "First select the route", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.isEmpty(mob) && mob.length() != 10) {
                    Toast.makeText(context, "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (TextUtils.isEmpty(email) && !email.contains("@") && !email.contains(".com")) {
                    Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(context, "Please enter address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(context, "Please enter city", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (MyApplication.isOnline(context)) {
                        callShopRegisterService();
                    } else {
                        MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void initBindData() {
        txt_route_name.setText(ROUTE_NAME);
        if (UPDATE_SHOP.equalsIgnoreCase("Y")) {
            tvHeaderText.setText(getResources().getString(R.string.update_shop));
            btn_create.setText("Update");
            SHOP_NAME = "" + getIntent().getExtras().getString("SHOP_NAME");
            MOBILE = "" + getIntent().getExtras().getString("MOBILE");
            ADDRESS = "" + getIntent().getExtras().getString("ADDRESS");
            MAIL = "" + getIntent().getExtras().getString("MAIL");
            CITY = "" + getIntent().getExtras().getString("CITY");
            IMAGE_PATH = "" + getIntent().getExtras().getString("IMAGE_PATH");

            Glide.with(context)
                    .load(IMAGE_PATH)
                    .fitCenter()
                    .placeholder(R.drawable.stkfood_logo)
                    .error(R.drawable.ic_broken_image)
                    .into(img_shop);

            edt_email.setText(MAIL);
            edt_city.setText(CITY);
            edt_shop_name.setText(SHOP_NAME);
            if (MOBILE.length() >= 10)
                edt_mob.setText(MOBILE);
            edt_address.setText(ADDRESS);
        } else {
            tvHeaderText.setText(getResources().getString(R.string.register_shop));
        }
    }

    public void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);

        } catch (Exception e) {
            MyApplication.log(LOG_TAG, "openCamera Exception  ERROR: " + e.getMessage());
            Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.log(LOG_TAG, "onActivityResult() , resultCode: " + resultCode);
        if (requestCode == CAMERA && data != null) {
            MyApplication.log(LOG_TAG, "onActivityResult() , OKKKK: ");

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            File imageStorageDir = new File(Environment.getExternalStorageDirectory(),
                    getResources().getString(R.string.app_name));
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            imagePath = imageStorageDir + File.separator + "_" + MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID) + "_" + MyApplication.getDateTimeStamp() + ".jpg";
            fileImage = new File(imagePath);
            if (TextUtils.isEmpty(imagePath)) {
                imageStorageDir = new File(Environment.getExternalStorageDirectory(),
                        getResources().getString(R.string.app_name));
                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }
                imagePath = imageStorageDir + File.separator + "_" + MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID) + "_" + MyApplication.getDateTimeStamp() + ".jpg";
                fileImage = new File(imagePath);
                MyApplication.set_session("imagePath", imagePath);
            }
            try {
                FileOutputStream out = new FileOutputStream(imagePath);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                if (fileImage != null) {
                    if (fileImage.isFile() && fileImage.exists()) {
                        MyApplication.log(LOG_TAG, "onActivityResult() SHOWING IMAGE BY GLIDE, fileImage:-> " + fileImage.getAbsolutePath());
                        Glide.with(context)
                                .load(fileImage.getAbsoluteFile())
                                .asBitmap()
                                .placeholder(R.drawable.ic_broken_image)
                                .into(img_shop);
                    }
                }

            } catch (Exception e) {
                MyApplication.log(LOG_TAG, "onActivityResult() , Exception: " + e.getMessage());
            }

            MyApplication.log(LOG_TAG, "onActivityResult() , IMAGE PATH IS CREATED: Path: " + imagePath);
            MyApplication.log(LOG_TAG, "onActivityResult() , FILE ABSOLUTE PATH IS : " + fileImage.getAbsolutePath());

           /* Glide.with(context)
                    .load(fileImage.getAbsoluteFile())
                    .asBitmap()
                    .placeholder(R.drawable.ic_broken_image)
                    .into(img_shop);*/

            // WORKS COMMENTED CODE TOO
           /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Glide.with(context)
                    .load(byteArray)
                    .asBitmap()
                    .placeholder(R.drawable.ic_broken_image)
                    .into(img_shop);*/

            /*imageString = MyApplication.ConvertString(photo);
            byte[] imageByteArray = Base64.decode(imageString, Base64.DEFAULT);
            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    .placeholder(R.drawable.ic_broken_image)
                    .into(img_shop);*/
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


   /* public void setSpinnerRoute() {
        MyApplication.log(LOG_TAG, " setSpinnerRoute() ");

        routeNames = TABLE_ROUTE_MASTER.getRouteDetailsSpinner();
        SpinnerRouteAdapter spinnerRouteAdapter = new SpinnerRouteAdapter(context, routeNames);
        spinnRoute.setAdapter(spinnerRouteAdapter);
        spinnRoute.setSelection(0);

        spinnRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnId = routeNames.get(position).get(TABLE_ROUTE_MASTER.COL_ROUTE_ID);
                selectedSpinnValue = routeNames.get(position).get(TABLE_ROUTE_MASTER.COL_ROUTE_NAME);
                //Toast.makeText(context, parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(context, "selectedSpinnId: "+selectedSpinnId+"\nselectedSpinnValue: "+selectedSpinnValue, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/


    public void callShopRegisterService() {
        latitude = gps.getLatitude() + "";
        longitude = gps.getLongitude() + "";

        MyApplication.showDialog(context, "Please Wait...");

        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_name", shopName);

        if (UPDATE_SHOP.equalsIgnoreCase("Y")) {  // DO UPDATE SHOP
            map.put("shop_auto_id", NEW_ENTERABLE_SHOP_ID);
            map.put("shop_id", NEW_ENTERABLE_SHOP_ID);
        } else {                                              // DO REGISTER
            map.put("shop_auto_id", "");
            map.put("shop_id", "");
        }

        map.put("distributor_id", MyApplication.get_session(MyApplication.SESSION_DIST_ID));
        map.put("route_id", ROUTE_ID);
        map.put("mobile", mob);
        map.put("email", email);
        map.put("address", address);
        map.put("city", city);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        if (fileImage != null)
            map.put("image_url1", new TypedFile("image*//*", fileImage));
        else {
            if (UPDATE_SHOP.equalsIgnoreCase("Y"))
                map.put("image_url1", IMAGE_PATH);
        }

        RestClient.getWebServices().registerShop(map,
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        MyApplication.log(LOG_TAG, "Responce is----> " + s);
                        try {
                            JSONObject res = new JSONObject(s);  // "status":"TRUE","message":"Shop Registered Successfully."}
                            String status = res.getString("status");
                            String message = res.getString("message");

                            MyApplication.stopLoading();
                            if (status.equals("TRUE")) {
                                MyApplication.set_session("imagePath", "");
                                if (!UPDATE_SHOP.equalsIgnoreCase("Y")) {
                                    if (fileImage.exists())
                                        fileImage.delete();
                                }
                                JSONArray shopArray = res.getJSONArray("shop_details");
                                JSONObject shopObject = shopArray.getJSONObject(0);

                                ShopDetails shopDetails = new ShopDetails();
                                shopDetails.setId(shopObject.getString("id"));
                                shopDetails.setShop_name(shopObject.getString("shop_name"));
                                shopDetails.setEnterable_shop_id(shopObject.getString("shop_id"));
                                shopDetails.setRoute_id(shopObject.getString("route_id"));
                                shopDetails.setMobile(shopObject.getString("mobile"));
                                shopDetails.setEmail(shopObject.getString("email"));
                                shopDetails.setAddress(shopObject.getString("address"));
                                shopDetails.setCity(shopObject.getString("city"));
                                shopDetails.setDistributor_id(shopObject.getString("distributor_id"));
                                shopDetails.setImage_url(shopObject.getString("image_url"));
                                shopDetails.setLatitude(shopObject.getString("latitude"));
                                shopDetails.setLongitude(shopObject.getString("longitude"));

                                TABLE_RETAILER_MASTER.insertRetailerDetails(shopDetails);
                                MyApplication.set_session("imagePath", "");
                                showDialogBox(context, getResources().getString(R.string.app_name), message);
                            } else
                                MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), message);

                        } catch (JSONException e) {
                            MyApplication.stopLoading();
                            MyApplication.log(LOG_TAG + "RESPONSE JSONException  ->" + e.getMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log(LOG_TAG + " RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        MyApplication.set_session("imagePath", "");
        Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);
        finish();
        startActivity(i);
    }

    public void showDialogBox(Context context, String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);

        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }

    public void getGPSLocation() {
        gps = new GPSTracker(context);
        gps.setLocationToZero();
        gps.getLocation();
        if (!(gps.isDeviceGPSEnable()) || !(gps.canGetLocation())) {
            gps.showSettingsAlert("Please start GPS Location to get shop location...");
            return;
        } else {
            gps.getLocation();
            if (gps.canGetLocation()) {
                gps.getLocation();
                latitude = "" + gps.getLatitude();
                longitude = "" + gps.getLongitude();
                Double accuracy = gps.getAccuracy();
                MyApplication.log(LOG_TAG, " LAT: " + latitude + ", LON: " + longitude + ", ACC: " + accuracy);
            }
        }
    }
}
