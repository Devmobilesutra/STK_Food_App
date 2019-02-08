package com.stk.orderingapp.ServerCallRetrofit;


import com.squareup.okhttp.OkHttpClient;
import com.stk.orderingapp.Config.Config;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.util.StringConverter;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {
    private static WebServices webServices = null;
    private static WebServices googleWebServices = null;


    public static WebServices getWebServices() {
        String LOG_TAG = "RestClient ";
        if (webServices == null) {

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);

            webServices = new RestAdapter.Builder()
                    .setEndpoint(Config.getBaseURL())
                    .setConverter(new StringConverter())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(okHttpClient))
                    .build()
                    .create(WebServices.class);

            MyApplication.log(LOG_TAG, " URL-> " + webServices.toString());
        }
        return webServices;
    }


}
