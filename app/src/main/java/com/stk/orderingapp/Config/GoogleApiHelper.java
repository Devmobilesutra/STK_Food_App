package com.stk.orderingapp.Config;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


/**
 * Created by Swarali on 7/11/2017.
 */

public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = GoogleApiHelper.class.getSimpleName();
    public LocationRequest mLocationRequest;
    // boolean flag to toggle periodic location updates
    public boolean mRequestingLocationUpdates = false;
    private boolean isLocationAvailable;
    public Location mLastLocation;
    private Double currLong, currLat;

    public GoogleApiHelper(Context context) {
        this.mContext= context;
        buildGoogleApiClient();
        createLocationRequest();
        connect();
    }

    private void connect() {
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    public LocationRequest getLocationRequest() {
        return this.mLocationRequest;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mRequestingLocationUpdates = true;

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended: googleApiClient.connect()");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }

    protected void createLocationRequest() {
        Log.e("LocService", "createLocationRequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

        mRequestingLocationUpdates = true;
    }

    /**
     * Starting the location updates
     */
    public void startLocationUpdates() {
        Log.e("LocService", "startLocationUpdates");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (isLocationAvailable) {
                    mLastLocation = locationResult.getLastLocation();
                } else {
                    mLastLocation = null;
                }

                displayLocation();
            }


            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                isLocationAvailable = locationAvailability.isLocationAvailable();
                if (!isLocationAvailable) {
                    mLastLocation = null;
                    displayLocation();
                }
                Log.e("IsLocationAv", "isLocationAvailable=" + isLocationAvailable);
            }
        }, null);
    }

    /**
     * Method to display the location on UI
     */
    public void displayLocation() {
        Log.e("LocService", "displayLocation");
        if (mLastLocation != null) {
            currLat = mLastLocation.getLatitude();
            currLong = mLastLocation.getLongitude();
            //IOUtils.myToast("Location: "+ currLat + ", "+ currLong, mContext);
        } else {
            //IOUtils.myToast("Couldn't get the location. Make sure location is enabled on the device", mContext);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if(currLat == null){
            return 0.0;
        }
        MyApplication.set_session(MyApplication.SESSION_LATTITUDE, currLat + "");

        return currLat;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if(currLong == null){
            return 0.0;
        }
        MyApplication.set_session(MyApplication.SESSION_LONGITUDE, currLong + "");
        return currLong;
    }
}
