package com.stk.orderingapp.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_ATTENDANCE_REPORT;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivitySplashScreen extends AppCompatActivity {
    boolean active = true;
    final int splashTime = 100; // time to display the splash screen in m
    String newVersion = "", LOG_TAG = "ActivitySplashScreen";
    Context context;
    String currDate = "";
    ImageView splashlogo = null;
    Animation splashAnimation = null;

    String actionStatus = "", today = "", checkTimeStamp = ""; // do_CHECK_IN,  do_NO_CHANGE,  do_CHECK_OUT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        MyApplication.log(LOG_TAG, "in OnCretate splash screen");
        context = this;

        today = MyApplication.getDateDBFormat();
        actionStatus = TABLE_ATTENDANCE_REPORT.getCheckStatusForToday(today);


        initCompListener();
        //  check_for_upgrade();
        proceed();
    }

    private void initCompListener() {

        splashlogo = (ImageView) findViewById(R.id.splashlogo);
        splashAnimation = AnimationUtils.loadAnimation(context, R.anim.splash_animation);

        splashlogo.startAnimation(splashAnimation);
    }

  /*  private void callApi() {
        RestClient.getWebServices().getMoreProduct("MS123", new Callback<String>() {
            @Override
            public void success(String s, Response response) {

                MyApplication.log("JARVIS", "Responce is---->" + s);


            }

            @Override
            public void failure(RetrofitError error) {
                MyApplication.log("RetrofitError  ->" + error.getMessage());

            }
        });

    }*/

    void check_for_upgrade() {
        MyApplication.log("JARVIS", "incheck_for_upgrade");
        if (((MyApplication) getApplication()).isOnline(context)) {
            MyApplication.log("JARVIS", "in is online");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            Date date = new Date();
            currDate = simpleDateFormat.format(date);
            MyApplication.set_session("chk_update_date", currDate);
            new FetchAppVersionFromGooglePlayStore().execute();
        } else {
            proceed();
        }
    }

    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {
            try {
                MyApplication.log("JARVIS", "in do in background");
                return
                        Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.stk.orderingapp" + "&hl=en")
                                .timeout(10000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                                .select("div[itemprop=softwareVersion]")
                                .first()
                                .ownText();

            } catch (Exception e) {
                return "";
            }
        }

        protected void onPostExecute(String string) {
            newVersion = string;
            MyApplication.log("JARVIS", "in do in onPostExecute");
            Log.d("new Version", newVersion);
            MyApplication.log("JARVIS", "in do in onPostExecute newVersion" + newVersion);
            if (newVersion != "") {
                float server_version = Float.parseFloat(newVersion);


                float app_version = 0.0f;
                PackageManager manager = context.getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(context.getPackageName(), 0);
                    app_version = Float.parseFloat(info.versionName);
                    //      if(value(info.versionName)<value(newVersion));
                } catch (PackageManager.NameNotFoundException e) {
                    MyApplication.log(LOG_TAG, " VersionE->" + e);
                }
                MyApplication.log(LOG_TAG, "ActivitySplash AppVersion->" + info.versionName);
                MyApplication.log(LOG_TAG, "ActivitySplash ServerVersion->" + newVersion);

                MyApplication.log(LOG_TAG, "ActivitySplash AsyncOnPostExecuteIf");


                if (server_version > app_version) {
                    //current application is not updatedMy
                    MyApplication.log(LOG_TAG, "in update is available ");
                    MyApplication.set_session("UPDATE_APP", "TRUE");
                    show_upgrade_dialog();
                } else {
                    MyApplication.log(LOG_TAG, "in no update");
                    proceed();
                }

                //show_upgrade_dialog();


            } else {
                proceed();
            }
        }
    }

    public void proceed() {
        MyApplication.log("aaa", "in for proced");
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (active && (waited < splashTime)) {
                        sleep(1000);
                        if (active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {

                  /*  if (MyApplication.get_session(MyApplication.KEY).equalsIgnoreCase("")) {
                        MyApplication.log("", "in first if 1 = " + MyApplication.get_session(MyApplication.KEY));
                        MyApplication.set_session(MyApplication.KEY, "Y");
                        Intent openMainActivity2 = new Intent(ActivitySplashScreen.this, ProductTour3Activity.class);
                        startActivity(openMainActivity2);
                        finish();
                    } else */

                    if (!MyApplication.get_session(MyApplication.SESSION_LOGIN).equalsIgnoreCase("true")) {
                        Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (actionStatus.equals("do_CHECK_IN")) {
                            Intent i = new Intent(getApplicationContext(), ActivityAttendanceCheck.class);
                            finish();
                            startActivity(i);
                        } else {
                            Intent intent1 = new Intent(ActivitySplashScreen.this, ActivityDashboard.class);
                            finish();
                            startActivity(intent1);
                            //  overridePendingTransition(R.anim.fade_in_call, R.anim.fade_out_call);
                        }
                    }
                }
            }
        };
        splashTread.start();
    }

    void show_upgrade_dialog() {
        final Dialog dialog1 = new Dialog(ActivitySplashScreen.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_upgrade);

        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        final TextView title = (TextView) dialog1.findViewById(R.id.txt_head);
        final TextView message = (TextView) dialog1.findViewById(R.id.txt_title);
        final TextView txt_upgrade = (TextView) dialog1.findViewById(R.id.txt_upgrade);
        final TextView txt_cancel = (TextView) dialog1.findViewById(R.id.txt_cancel);

        /*title.setTypeface(custom_font);
        message.setTypeface(custom_font);
        txt_upgrade.setTypeface(custom_font);
        txt_cancel.setTypeface(custom_font);*/

        txt_cancel.setVisibility(View.GONE);
        txt_upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    MyApplication.log(LOG_TAG, "ActivitySplash 1");
                    startActivity(goToMarket);
                    MyApplication.log(LOG_TAG, "ActivitySplash 2");
                } catch (ActivityNotFoundException e) {
                    MyApplication.log(LOG_TAG, "ActivitySplash 3");
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                }

                //new FetchAppVersionFromGooglePlayStore().execute();
                dialog1.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                dialog1.dismiss();
                //    proceed();
            }
        });
    }
}
