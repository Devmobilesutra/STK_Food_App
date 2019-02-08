package com.stk.orderingapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_RETAILER_MASTER;
import com.stk.orderingapp.Model.LoginResponse;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.Model.ModelSalemanDetails;
import com.stk.orderingapp.R;
import com.stk.orderingapp.ServerCallRetrofit.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivitySetShopLocation extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    //Our Map
    private GoogleMap mMap;
    //Google ApiClient
    private GoogleApiClient googleApiClient;
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    String shopName = "Current Shop Location", shopId = "", LOG_TAG = "ActivitySetShopLocation ";
    Context context = this;
    AlertDialog.Builder dlgAlert;
    ImageView img_setter_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_shop_location);
        context = this;

        dlgAlert = new AlertDialog.Builder(context);
        img_setter_marker = (ImageView) findViewById(R.id.img_setter_marker);

        if (getIntent().getExtras() != null) {
            shopName = getIntent().getExtras().getString("SHOP_NAME");
            latitude = getIntent().getExtras().getDouble("LAT");
            longitude = getIntent().getExtras().getDouble("LONG");
            shopId = getIntent().getExtras().getString("SHOP_ID");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        getCurrentLocation();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
                Toast.makeText(getApplicationContext(), "POINTS: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
            }
        });

        img_setter_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSetShopLocation(context, shopName, latitude + "", longitude + "");
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(latitude, longitude);
        // mMap.addMarker(new MarkerOptions().position(loc).title(" YOU ARE HERE..."  ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mMap.clear();
                LatLng loc = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

                //mMap.addMarker(new MarkerOptions().position(loc).title(" SET THIS LOCATION..."  ));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;

                MyApplication.log(LOG_TAG + "cenetrLat: ", cameraPosition.target.latitude + "");
                MyApplication.log(LOG_TAG + "centerLong: ", cameraPosition.target.longitude + "");
                //showDialogSetShopLocation(context, shopName, latitude + "", longitude + "");


            }
        });
    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {
        //mMap.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            if (location.getLongitude() != 0.0 && location.getLatitude() != 0.0) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //moving the map to location
                moveMap();
            }
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = "Set " + shopName + " Here...";
        //String msg = latitude + ", " + longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title(shopName)); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "POINTS: "+latitude + ", " + longitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

     /*mMap.setOnMapClickListener(new OnMapClickListener() {

        @Override
        public void onMapClick(LatLng point) {
            marker.remove();
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            locationTextView.setText(getCompleteAdressString(latitude,longitude ));
            drawMarker(point);
        }
    }*/

    @Override
    public void onMapClick(LatLng latLng) {
        //marker.remove();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        //locationTextView.setText(getCompleteAdressString(latitude,longitude ));
        //drawMarker(point);

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("SET THIS LOCATION");
        // adding marker
        mMap.addMarker(marker);
    }

    public void showDialogSetShopLocation(final Context context, final String shopName, final String lat, final String lon) {

        dlgAlert.setMessage("Set this location to " + shopName);
        dlgAlert.setTitle(context.getResources().getString(R.string.app_name));

        dlgAlert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, shopName + " is set at \n" + lat + ", " + lon, Toast.LENGTH_LONG).show();
                String userName = MyApplication.get_session(MyApplication.SESSION_USER_NAME);
                updateShopLocationAPI();
            }
        });

        dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dlgAlert.setCancelable(false);
        dlgAlert.show();
    }

    public void updateShopLocationAPI() {
        MyApplication.showDialog(context, "Please Wait...");
        RestClient.getWebServices().updateShopLocation(
                shopId,
                latitude + "",
                longitude + "",
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        MyApplication.log(LOG_TAG, "Responce is----> " + s);
                        // {"status":"TRUE","message":"Shop location updated successfully."}
                        try {
                            JSONObject locUpdateObj = new JSONObject(s);
                            String status = locUpdateObj.getString("status");
                            String message = locUpdateObj.getString("message");
                            if (status.equals("TRUE"))
                                TABLE_RETAILER_MASTER.updateRetailerShopLocation(shopId, latitude + "", longitude + "");

                            MyApplication.stopLoading();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            MyApplication.showDialogBox(context, getResources().getString(R.string.app_name), message );

                        } catch (JSONException e) {
                            MyApplication.log(LOG_TAG, "RESPONSE ERROR PARSING " + e.getMessage());
                            MyApplication.stopLoading();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        MyApplication.log("RetrofitError  ->" + error.getMessage());
                        MyApplication.stopLoading();
                    }
                });
    }
}