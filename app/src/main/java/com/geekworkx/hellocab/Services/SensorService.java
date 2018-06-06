package com.geekworkx.hellocab.Services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class SensorService extends Service {
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private  Context mContext;
    public int counter=0;
    private String _PhoneNo;
    private double My_long=0,My_lat=0;
    private PrefManager pref;
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private  Location mLastLocation;
    private  Location mCurrentLocation;
    private String Tracking_type="";
    private DatabaseReference mDatabase;

    public SensorService(Context applicationContext) {
        super();
        mContext=applicationContext;
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        Log.i(TAG, "onCreate");



    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }
    }
    private class LocationListener implements android.location.LocationListener {


        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);

            if(location!=null) {
                mCurrentLocation=location;
                //Toast.makeText(getApplicationContext(),String.valueOf(mCurrentLocation.distanceTo(mLastLocation)),Toast.LENGTH_SHORT).show();
                if (mCurrentLocation.distanceTo(mLastLocation) > 0) {
                    My_lat=location.getLatitude();
                    My_long=location.getLongitude();
                    if(My_lat!=0) {
                        new StoreLatLong().execute();
                    }
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            Intent launchGoogleChrome = getPackageManager().getLaunchIntentForPackage("com.geekworkx.hellocab");
            startActivity(launchGoogleChrome);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("com.geekworkx.hellocab.Services.SensorService");
        sendBroadcast(broadcastIntent);

    }






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public class StoreLatLong extends AsyncTask<Void, Integer, String> {




        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)

                        .addFormDataPart("user_mobile",_PhoneNo)
                        .addFormDataPart("lattitude", String.valueOf(My_lat))
                        .addFormDataPart("longitude", String.valueOf(My_long))
                        .addFormDataPart("Tracking_type", "OnRide")
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_STORE_ALL_LATLNG)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        protected void onPostExecute(String file_url) {

        }

    }



}