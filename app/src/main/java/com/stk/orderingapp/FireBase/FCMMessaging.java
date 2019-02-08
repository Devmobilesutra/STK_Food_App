package com.stk.orderingapp.FireBase;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.stk.orderingapp.Config.MyApplication;

import org.json.JSONObject;

/**
 * Created by Arvind on 30-Apr-18.
 */

public class FCMMessaging extends FirebaseInstanceIdService {
    public String LOG_TAG="FCMMessaging ";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MyApplication.log(LOG_TAG, "onTokenRefresh(), FCM --> " + refreshedToken);
        MyApplication.set_session(MyApplication.SESSION_FCM_ID, refreshedToken);

        String deviceId = "";
        deviceId = Settings.Secure.getString(getApplication().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        MyApplication.log(LOG_TAG, "onTokenRefresh(), DEVICE ID --> " + deviceId);
        MyApplication.set_session(MyApplication.SESSION_DEVICE_ID, deviceId);
    }
}