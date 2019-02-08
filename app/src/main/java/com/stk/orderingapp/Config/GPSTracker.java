package com.stk.orderingapp.Config;

/**
 * Created by Arvind on 11/15/2016.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;
    boolean IsGpsOn = false;
    static Location location = null; // location
    static double latitude = 0.0; // latitude
    static double longitude = 0.0; // longitude
    static double accuracy = 0.0;   // ACCURACY
    public int networkUpdate;

    // The minimum distance to change Updates in meters
    //private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1/1000; // 1 meters
    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000*30; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 1/1000; // 1/2 minute
    static double gpsAccuracy = 0, netAccuracy = 0;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    String LOG_TAG = "GPSTracker ";

    public GPSTracker(Context context) {
        this.mContext = context;
        //setLocationToZero();
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            /*Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            locationManager.getBestProvider(criteria, true);*/

            if (!isGPSEnabled)
                this.IsGpsOn = false;
            else
                this.IsGpsOn = true;

            if (isGPSEnabled && isNetworkEnabled) {
                this.canGetLocation = true;


                // First get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                MyApplication.log(LOG_TAG + "getLocation() BY NET gpsAccuracy:-->" + gpsAccuracy + ",  netAccuracy:-->" + location.getAccuracy());
                                MyApplication.log(LOG_TAG + "getLocation() BY NET netAccuracy < gpsAccuracy ", "Coordinates:" + this.latitude + "," + this.longitude +
                                        " acc:" + this.location.getAccuracy());
                                this.latitude = location.getLatitude();
                                this.longitude = location.getLongitude();
                                this.accuracy = location.getAccuracy();

                                MyApplication.log(LOG_TAG + "getLocation() BY NET", "Coordinates:" + this.latitude + "," + this.longitude +
                                        " acc:" + this.location.getAccuracy());
                            }
                        }
                    } catch (Exception e) {
                        MyApplication.log(LOG_TAG + "getLocation() isNetworkEnabled NET EXCEPTION...---> ", e + "");
                    }
                }


                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    try {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                        PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        }
                    } catch (Exception e) {
                        MyApplication.log(LOG_TAG + "getLocation() isGPSEnabled() GPS EXCEPTION...---> ", e + "");
                    }

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                            this.accuracy = location.getAccuracy();
                            gpsAccuracy = this.accuracy;
                            MyApplication.log(LOG_TAG, "getLocation() BY_GPS Coordinates:" + this.latitude + "," + this.longitude +
                                    " acc:" + this.location.getAccuracy());
                        }
                    }
                }
            }
        } catch (Exception e) {
            MyApplication.log(LOG_TAG + "getLocation() MAIN EXCEPTION...---> ", e + "");
        }
        //stopUsingGPS();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS() {
        /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                 PackageManager.PERMISSION_GRANTED &&
                 ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                         PackageManager.PERMISSION_GRANTED) {

         }*/
        // if(locationManager != null)
        locationManager.removeUpdates(GPSTracker.this);
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null)
            latitude = location.getLatitude();
        return latitude;
    }

    public double getAccuracy() {
        if (location != null)
            this.accuracy = location.getAccuracy();
        return accuracy;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();
        // return longitude
        return longitude;
    }

    public void setLocationToZero() {
        /*location.setLatitude(0.0);
        location.setLongitude(0.0);
        location.setAccuracy(1000);*/
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.accuracy = 0.0;
       // this.location = null;
       // this.canGetLocation = false;
        getLocation();
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS Settings");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                alertDialog.setCancelable(true);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void showSettingsAlertForNetwork(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Network Settings");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                alertDialog.setCancelable(true);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.e("SUJATA GPSTracker ", "in onLocationChanged");
        if (location != null) {
            this.location = location;
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.accuracy = location.getAccuracy();
            MyApplication.log(LOG_TAG + "onLocationChanged() ", "GPScoordinates:  " + this.latitude + ",  " + this.longitude +
                    ",    Accuracy: " + this.getAccuracy());
           /* if(networkUpdate == 1) {
                MyApp.isFreshPoint = 1;
                stopUsingGPS();
            }*/
            //MyApp.isFreshPoint = 1;
            stopUsingGPS();
        }
    }

    public boolean isDeviceGPSEnable() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else {
            //showSettingsAlert("GPS is not enabled. Do you want to go to settings menu?");
            return false;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}