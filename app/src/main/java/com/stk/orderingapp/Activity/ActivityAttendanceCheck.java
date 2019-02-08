package com.stk.orderingapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_ATTENDANCE_REPORT;
import com.stk.orderingapp.DataBase.TABLE_RETAILER_MASTER;
import com.stk.orderingapp.Model.ShopDetails;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ActivityAttendanceCheck extends AppCompatActivity {

    TextView txt_current_date_time;
    ImageView ivDrawer, img_camera; //HEADER BACK BUTTON
    Context context;
    Button btn_check_attendance;
    String actionStatus = "", today = "", checkTimeStamp = ""; // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT

    private int CAMERA = 3;
    private static final int CAMERA_REQUEST = 1888, MY_CAMERA_PERMISSION_CODE = 100;

    String LOG_TAG = "ActivityAttendanceCheck ", imageString = "", imagePath = "";
    File fileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_check);
        context = this;

        MyApplication.set_session("imagePath", "");
        today = MyApplication.getDateDBFormat();
        checkTimeStamp = MyApplication.getDateTimeDBFormat();
        actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);

        initComponants();
        // bindData();
        initComponantListner();
    }


    public void initComponants() {
        RelativeLayout linearLayoutDateSpinner = (RelativeLayout) findViewById(R.id.linearLayoutDateSpinner);
        linearLayoutDateSpinner.setVisibility(View.GONE);

        TextView tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        //tvHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_DIST_NAME));
        tvHeaderText.setText(getResources().getString(R.string.attendance));
        TextView tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);
        tvSubHeaderText.setText(MyApplication.get_session(MyApplication.SESSION_USER_FULL_NAME));

        txt_current_date_time = (TextView) findViewById(R.id.txt_current_date_time);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm a");
        String date = df.format(Calendar.getInstance().getTime());
        txt_current_date_time.setText(date);

        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        ivDrawer.setVisibility(View.VISIBLE);
        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow));

        img_camera = (ImageView) findViewById(R.id.img_camera);

        today = MyApplication.getDateDBFormat();
        checkTimeStamp = MyApplication.getDateTimeDBFormat();

        btn_check_attendance = (Button) findViewById(R.id.btn_check_attendance);
        actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
        // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
        if (actionStatus.equals("do_CHECK_IN")) {
            btn_check_attendance.setText("CHECK-IN");
        } else if (actionStatus.equals("do_CHECK_OUT")) {
            btn_check_attendance.setText("CHECK-OUT");
        } else
            btn_check_attendance.setText("CHECK-OUT");
    }

    public void bindData() {
        actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
        // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
        if (actionStatus.equals("do_CHECK_IN")) {
            btn_check_attendance.setText("CHECK-IN");
        } else if (actionStatus.equals("do_CHECK_OUT")) {
            btn_check_attendance.setText("CHECK-OUT");
        } else
            btn_check_attendance.setText("CHECK-OUT");

        // today = MyApplication.getDateDBFormat();
        //checkTimeStamp = MyApplication.getDateTimeDBFormat();
    }

    public void initComponantListner() {
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    MyApplication.log(LOG_TAG, " img_camera.setOnClickListener() ERROR: " + e.getMessage());
                }
            }
        });

        btn_check_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
                MyApplication.log(LOG_TAG, "btn_check_attendance(), actionStatus:---> " + actionStatus);

                if (!actionStatus.equals("do_NO_CHANGE")) {

                   /* imagePath = MyApplication.get_session("imagePath");
                    fileImage = new File(imagePath);*/

                    if (TextUtils.isEmpty(imagePath)) {
                        //if(!fileImage.exists()) {
                        Toast.makeText(context, "Please take a selfy...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (fileImage == null && !fileImage.isFile()) {
                        //if(!fileImage.exists()) {
                        Toast.makeText(context, "Please take a selfy...", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (MyApplication.isOnline(context)) {
                        callSendCheckReport();
                    } else {
                        MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection));
                    }
                } else
                    Toast.makeText(context, "You have already CHECKED OUT...", Toast.LENGTH_LONG).show();
            }
        });

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void callSendCheckReport() {
        MyApplication.showDialog(context, "Please Wait...");

        MyApplication.log(LOG_TAG, "callSendCheckReport()  ----> ");
        MyApplication.log(LOG_TAG, "callSendCheckReport(), DATE: -> " + today + ", TIME:--->" + checkTimeStamp);
        MyApplication.log(LOG_TAG, "callSendCheckReport(), SALESMAN->" + MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID));
        HashMap<String, Object> map = new HashMap<>();
        map.put("date", today);
        map.put("salesman_id", MyApplication.get_session(MyApplication.SESSION_SALESMAN_ID));

        if (actionStatus.equals("do_CHECK_IN")) {  //// do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT
            map.put("checkin", "yes");
            map.put("in_time", checkTimeStamp);
            if (fileImage != null)
                map.put("in_image", new TypedFile("image*//*", fileImage));
        } else {
            map.put("checkin", "no");
            map.put("out_time", checkTimeStamp);
            if (fileImage != null)
                map.put("out_image", new TypedFile("image*//*", fileImage));
        }

        RestClient.getWebServices().sendCheckReport(map,
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
                                if (fileImage.exists())
                                    fileImage.delete();
                                MyApplication.set_session("imagePath", "");
                                // String date, String check_in_time, String check_in_Flag,
                                //                                           String check_in_image_url
                                if (actionStatus.equals("do_CHECK_IN")) {
                                    TABLE_ATTENDANCE_REPORT.insertCheckInReport(
                                            today, checkTimeStamp, "Y", "");
                                } else {
                                    TABLE_ATTENDANCE_REPORT.updateCheckOutReport(
                                            today, checkTimeStamp, "Y", "");
                                }

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


    public void showDialogBox(Context context, String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);

        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyApplication.set_session("imagePath", "");
                if (actionStatus.equals("do_CHECK_OUT")) {
                    finish();
                    System.exit(0);
                } else {
                    Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);
                    finish();
                    startActivity(i);
                }
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.show();
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
                                .into(img_camera);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.set_session("imagePath", "");
    }

    @Override
    public void onBackPressed() {
        actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);
        if (!actionStatus.equals("do_CHECK_IN")) {
            Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);
            finish();
            startActivity(i);
        }
    }
}
