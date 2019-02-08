package com.stk.orderingapp.Config;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.stk.orderingapp.R;

import java.util.List;
import java.util.Locale;

import static com.google.android.gms.internal.zzahg.runOnUiThread;


/**
 * Created by Pramod Kale on 05/08/2016.
 */
public class GPSTracker1 extends Service {

    private final Context mContext;
    ProgressDialog progressDialog = null;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String LatLog;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5 * 1000; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    String LOG_TAG = "GPSTracker1";

    int arrLoactionSize = 0;
    String arrLocation[];
    Context context = this;
    Double lat = 0.0;
    Double longi = 0.0;

    Dialog dialog1;

    public GPSTracker1(Context context) {
        this.mContext = context;
       // getLocation();
    }

    public Location getLocation(Context context, String get_data) {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);
                    if(get_data.equalsIgnoreCase("visited")) {
                        progressDialog = ProgressDialog.show(context, null, null);
                    }
                    Log.d(LOG_TAG, "Network");
  /*                  if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
  */
                }
                // if GPS Enabled get lat/long using GPS Services
                /*if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(LOG_TAG, "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }*/
                if (latitude != 0.0 && longitude != 0.0) {
                //    setUpdatedLocation(latitude, longitude);
                }
                Log.d(LOG_TAG, "get my location latitude->" + latitude);
                Log.d(LOG_TAG, "get my location longitude->" + longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public Location getLocationPassive(Context context, String get_data) {
        Log.d(LOG_TAG, "getLocationPassive");
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            /*isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);*/

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d(LOG_TAG, "!isNetworkEnabled");// no network provider is enabled
            } else {
                Log.d(LOG_TAG, " else isNetworkEnabled");
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerPassive);
                    if(get_data.equalsIgnoreCase("visited")) {
                        progressDialog = ProgressDialog.show(context, null, null);
                    }
                        Log.d(LOG_TAG, "Network");

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                /*if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(LOG_TAG, "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }*/
                if (latitude != 0.0 && longitude != 0.0) {
                   // setUpdatedLocation(latitude, longitude);
                }
                Log.d(LOG_TAG, "get my location latitude->" + latitude);
                Log.d(LOG_TAG, "get my location longitude->" + longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


/*
    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "onLocationChanged");
        if(progressDialog.isShowing() && progressDialog !=null ){
            progressDialog.dismiss();
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            setUpdatedLocation(latitude, longitude);
            mContext.stopService(new Intent(mContext, GPSTracker1.class));
        }
        Log.d(LOG_TAG, "latitude->" + latitude);
        Log.d(LOG_TAG, "longitude->" + longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "onProviderDisabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "onProviderEnabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(LOG_TAG, "onStatusChanged");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

*/
    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        Log.d(LOG_TAG, "latitude->" + latitude);
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        Log.d(LOG_TAG, "longitude->" + longitude);
        // return longitude
        return longitude;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showSettingsAlert(Context context) {
        dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.gps_alert);
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog1.dismiss();
            }
        });
        Button btn_settings = (Button) dialog1.findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dialog1.dismiss();
            }
        });
    }

    public boolean canGetLocationGPRS() {
        MyApplication.log(LOG_TAG, "in canGetLocationGPRS");
        try {


            MyApplication.log(LOG_TAG,"in tryyyy");
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            MyApplication.log(LOG_TAG,"locationManager ENABALEE---->"+locationManager);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            MyApplication.log(LOG_TAG,"ISNETWROK ENABALEE---->"+isNetworkEnabled);

            if (isNetworkEnabled) {
                MyApplication.log(LOG_TAG, "in canGetLocation isGPSEnabled || isNetworkEnabled");
                // no network provider is enabled
                this.canGetLocation = true;
            } else {
                MyApplication.log(LOG_TAG, "in else");
                this.canGetLocation = false;
            }
        } catch (Exception e) {

        }
        MyApplication.log(LOG_TAG, "in else before return");
        return this.canGetLocation;
    }
    public boolean canGetLocationPASSIVE() {
        MyApplication.log(LOG_TAG, "in canGetLocationGPRS");
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

            if (isNetworkEnabled) {
                MyApplication.log(LOG_TAG, "in canGetLocation isGPSEnabled || isNetworkEnabled");
                // no network provider is enabled
                this.canGetLocation = true;
            } else {
                MyApplication.log(LOG_TAG, "in else");
                this.canGetLocation = false;
            }
        } catch (Exception e) {

        }
        MyApplication.log(LOG_TAG, "in else before return");
        return this.canGetLocation;
    }
    public boolean canGetLocation() {
        MyApplication.log(LOG_TAG, "in canGetLocation");
        try {

            MyApplication.log(LOG_TAG, "in canGetLocation tryyyyyyyyyyyyyy");
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);


            MyApplication.log(LOG_TAG, "in isGPSEnabled isGPSEnabled--->"+isGPSEnabled);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            MyApplication.log(LOG_TAG, "in isNetworkEnabled isNetworkEnabled--->"+isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                MyApplication.log(LOG_TAG, "in canGetLocation isGPSEnabled || isNetworkEnabled");
                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                MyApplication.log(LOG_TAG, "in else");
                this.canGetLocation = true;
            }
        } catch (Exception e) {

        }
        MyApplication.log(LOG_TAG, "in else before return-->"+canGetLocation);
        return this.canGetLocation;
    }

    public void setUpdatedLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            LatLog = latitude + "," + longitude;
            String cityName = "";
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            Address address = addresses.get(0);
            // ArrayList<String> addressFragments = new ArrayList<String>();

            // for(int i = 0; i < address.getMaxAddressLineIndex(); i++){

            String complete_adress = address.getAddressLine(0) + " " + address.getAddressLine(1) + " " + address.getAddressLine(2);
//

            //    }

            for (int i = 0; i < addresses.size(); i++) {
                cityName = addresses.get(i).getLocality();

                // String complete_adress  = addresses.get(0).getAddressLine(0)  + " " + addresses.get(0).getSubAdminArea() + " " + addresses.get(0).getSubLocality() + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea() + " "+addresses.get(0).getPostalCode() + " " + addresses.get(0).getCountryName();
                // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*/


                String areaName = addresses.get(i).toString();
                /*TreesApp.log("Area name addresses ", areaName + "");
                TreesApp.set_session(TreesApp.SESSION_USER_LATITUDE, latitude + "");
                TreesApp.set_session(TreesApp.SESSION_USER_LONGITUDE, longitude + "");
                TreesApp.set_session(TreesApp.SESSION_LATITUDE, latitude + "");
                TreesApp.set_session(TreesApp.SESSION_LONGITUDE, longitude + "");

                TreesApp.log("get_location LatLog", LatLog);
                TreesApp.log("get_location cityName", cityName);
                if (!cityName.equals("")) {
                    TreesApp.set_session(TreesApp.SESSION_GPS_CITY, cityName);
                }*/
            }
         //   updateMyActivity(mContext, "3", "location", "Activity_map");

        } catch (Exception e) {
            //TreesApp.log("get_location getMyLocation", e.getMessage() + "");
        }
    }

    static void updateMyActivity(Context context, String response_code, String response_message, String flag) {

        Intent intent = new Intent(flag);
        //put whatever data you want to send, if any
        intent.putExtra("response_code", response_code);
        intent.putExtra("response_message", response_message);
        //send broadcast
        context.sendBroadcast(intent);

    }

    /*public void removeUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            //TreesApp.log(LOG_TAG,"In removeUpdates");
        }
    }*/

   /* public boolean canGetLocationSetting(String type) {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            if()
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = true;
            } else {
                this.canGetLocation = false;
            }
        }catch (Exception e) {

        }
        return this.canGetLocation;
    }*/

    public final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();


            runOnUiThread(new Runnable() {
                @Override

                public void run() {


                    MyApplication.log("locationListenerGPS LATITUDE and LONGITITUDE", "LAT +"+latitude + " longi : "+longitude);
                    //Toast.makeText(MainActivity.this, "GPS Provider update", Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing() && progressDialog !=null ){
                        progressDialog.dismiss();
                    }
                }
            });

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public final LocationListener locationListenerPassive = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();


            runOnUiThread(new Runnable() {
                @Override

                public void run() {


                    MyApplication.log("locationListenerPassive LATITUDE and LONGITITUDE", "LAT +"+latitude + " longi : "+longitude);
                    if(progressDialog.isShowing() && progressDialog !=null ){
                        progressDialog.dismiss();
                    }
                }
            });

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public void getSettingsOrderBooking(Context context, String get_data) {
        MyApplication.log(LOG_TAG, "in getSettingsOrderBooking");
        String locSetting ="GPS,GPRS,PASSIVE";
        arrLocation = locSetting.split(",");
        arrLoactionSize = arrLocation.length;
        MyApplication.log("in", "size == 3arrLocation[0] "+arrLocation[0]+"arrLocation[1] "+arrLocation[1]+"arrLocation[2] "+arrLocation[2]);
        MyApplication.log("in", "size == 3 arrLoactionSize "+arrLoactionSize);
        if (arrLoactionSize == 3) {
            MyApplication.log("in", "size == 3");
            if (arrLocation[0].equalsIgnoreCase("GPS")) {
                MyApplication.log("in", "GPS call0");
                lat = MyApplication.getGoogleApiHelper().getLatitude();
                longi = MyApplication.getGoogleApiHelper().getLongitude();
                MyApplication.log("LATITUDE -->" + longi);
                MyApplication.log("LONGITUDE -->" + lat);
            } else if (arrLocation[0].equalsIgnoreCase("GPRS") && canGetLocationGPRS()) {
                MyApplication.log("in", "GPRS call0");
//                GPSTracker1 gpsTracker1 = new GPSTracker1(context);
                getLocation(context, get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());


            } else if (arrLocation[0].equalsIgnoreCase("PASSIVE") && canGetLocationPASSIVE()) {
                MyApplication.log("in", "PASSIVE call0");
                getLocationPassive(context,get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());

            }
            if (arrLocation[1].equalsIgnoreCase("GPS")) {
                MyApplication.log("in", "GPS call 01");
                lat = MyApplication.getGoogleApiHelper().getLatitude();
                longi = MyApplication.getGoogleApiHelper().getLongitude();
                MyApplication.log("LATITUDE -->" + longi);
                MyApplication.log("LONGITUDE -->" + lat);
            } else if (arrLocation[1].equalsIgnoreCase("GPRS") && canGetLocationGPRS()) {
                MyApplication.log("in", "GPRS call 01");
                getLocation(context,get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());

            } else if (arrLocation[1].equalsIgnoreCase("PASSIVE") && canGetLocationPASSIVE()) {
                MyApplication.log("in", "PASSIVE call 01");
                getLocationPassive(context,get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());
            }
            if (arrLocation[2].equalsIgnoreCase("GPS") ) {
                MyApplication.log("in", "GPS call 02");
                lat = MyApplication.getGoogleApiHelper().getLatitude();
                longi = MyApplication.getGoogleApiHelper().getLongitude();
                MyApplication.log("LATITUDE -->" + longi);
                MyApplication.log("LONGITUDE -->" + lat);
            } else if (arrLocation[2].equalsIgnoreCase("GPRS") && canGetLocationGPRS()) {
                MyApplication.log("in", "GPRS call 02");
                getLocation(context,get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());

            } else if (arrLocation[2].equalsIgnoreCase("PASSIVE") && canGetLocationPASSIVE()) {
                MyApplication.log("in", "PASSIVE call 02");
                getLocationPassive(context,get_data);
                MyApplication.log("LATITUDE -->" + getLatitude());
                MyApplication.log("LONGITUDE -->" + getLongitude());
            }
        } else if (arrLocation.length == 2) {

            if (arrLocation[0].equalsIgnoreCase("GPS") && canGetLocation()) {
                MyApplication.log("in", "GPS call 10");
            } else if (arrLocation[0].equalsIgnoreCase("GPRS") && canGetLocation()) {
                MyApplication.log("in", "GPRS call 11");
            } else if (arrLocation[0].equalsIgnoreCase("PASSIVE") && canGetLocation()) {
                MyApplication.log("in", "PASSIVE call 12");
            }
            if (arrLocation[1].equalsIgnoreCase("GPS") && canGetLocation()) {
                MyApplication.log("in", "GPS call 20");
            } else if (arrLocation[1].equalsIgnoreCase("GPRS") && canGetLocation()) {
                MyApplication.log("in", "GPRS call 21");
            } else if (arrLocation[1].equalsIgnoreCase("PASSIVE") && canGetLocation()) {
                MyApplication.log("in", "PASSIVE call 22");
            }

        } else if (arrLoactionSize == 1) {

            if (arrLocation[0].equalsIgnoreCase("GPS") && canGetLocation()) {
                MyApplication.log("in", "GPS call 33");
            } else if (arrLocation[0].equalsIgnoreCase("GPRS") && canGetLocation()) {
                MyApplication.log("in", "GPRS call34");
            } else if (arrLocation[0].equalsIgnoreCase("PASSIVE") && canGetLocation()) {
                MyApplication.log("in", "PASSIVE call 35");
            }
        }
    }
}

