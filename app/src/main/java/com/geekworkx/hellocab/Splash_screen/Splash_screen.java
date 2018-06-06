package com.geekworkx.hellocab.Splash_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Adapters.SmsActivity;
import com.geekworkx.hellocab.BuildConfig;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 * Created by parag on 30/08/16.
 */
public class Splash_screen extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    public static final int MULTIPLE_PERMISSIONS = 10;
    Boolean isInternetPresent = false;
    private SharedPreferences permissionStatus;
    SharedPreferences appPreferences;
    private ConnectionDetector cd;
    private static final String TAG = Splash_screen.class.getSimpleName();
    private PrefManager prefManager;
    private String UNIQUE_RIDER;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private GoogleApiClient mGoogleApiClient;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private LocationRequest mLocationRequest;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private double My_lat = 0, My_long = 0;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private boolean permissionsAllowd;
    private RelativeLayout Splash;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private DatabaseReference mDatabase;
    private String _PhoneNo;
    private TextView Gtcor;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cd = new ConnectionDetector(getApplicationContext());
        Gtcor=findViewById(R.id.gtcor);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        prefManager = new PrefManager(getApplicationContext());
        HashMap<String, String> user = prefManager.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Splash = (RelativeLayout) findViewById(R.id.splash);
        Splash.setOnClickListener(this);
        progressBar=findViewById(R.id.progressBarSplash);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cor_home_main);


        rebuildGoogleApiClient();
        //createLocationRequest();
        // TODO: This next IF statement temporarily deals with an issue where autoManage doesn't
        // call the onConnected callback after a Builder.build() when re-connecting after a
        // rotation change. Will remove when fixed.
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            onConnected(null);

        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkAndRequestPermissions();

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
    }



    protected synchronized void rebuildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and connection failed
        // callbacks should be returned, which Google APIs our app uses and which OAuth 2.0
        // scopes our app requests. When using enableAutoManage to register the failed connection
        // listener it will only be called back when auto-resolution attempts were not
        // successful or possible. A normal ConnectionFailedListener is also registered below to
        // notify the activity when it needs to stop making API calls.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        0 /* googleApiClientId used when auto-managing multiple googleApiClients */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this /* ConnectionCallbacks */)
                // Register a connection listener that will notify on disconnect (including ones
                // caused by calling disconnect on the GoogleApiClient).
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        googleApiClientConnectionStateChange(true);
                    }
                })

                .addApi(AppInvite.API)
                .addApi(LocationServices.API)
                // TODO(developer): Specify any additional API Scopes or APIs you need here.
                // The GoogleApiClient will ensure these APIs are available, and the Scopes
                // are approved before invoking the onConnected callbacks.
                .build();

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, true)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {}
                        });
    }


    private void googleApiClientConnectionStateChange(final boolean connected) {
        final Context appContext = this.getApplicationContext();

        // TODO(developer): Kill AsyncTasks, or threads using the GoogleApiClient.

        // Display Toast that isn't dependent on the current activity (in case of a rotation).
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, "Google Api Client has connected:" + connected,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
      

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            createLocationCallback();
                if (permissionsAllowd) {
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        if (My_lat == 0) {
                            startLocationUpdates();
                        } else {
                            new GetSettings().execute();
                        }

                    }
            }else{
                    checkAndRequestPermissions();
                }
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });

            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }


    private void checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);

        } else {
            permissionsAllowd = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if ( perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            &&perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        new FetchCordinates().execute();

                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");

                            explain("You need to give some mandatory permissions to continue");

                    }
                }
            }
            break;

        }


    }


    private void explain(String msg) {
        new android.support.v7.app.AlertDialog.Builder(Splash_screen.this)

                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();

    }
    private void launchHomeScreen() {

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
        Gtcor.setVisibility(View.GONE);

            if (prefManager.isLoggedIn()) {
                Intent i = new Intent(Splash_screen.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

            }else {
                Intent o = new Intent(Splash_screen.this, SmsActivity.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                startActivity(o);
                o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
            }
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates mState = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {
                            if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
                                startLocationUpdates();
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in ().
                            status.startResolutionForResult(Splash_screen.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // Indicate API calls to Google Play services APIs should be halted.
        googleApiClientConnectionStateChange(false);
        mGoogleApiClient.connect();
    }

    /**
     * Called when the Activity could not connect to Google Play services AND the auto manager
     * could not resolve the error automatically.
     *
     * @param result can be inspected to determine the cause of the failure
     */

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash:
                if (My_lat == 0) {
                    startLocationUpdates();
                } else {

                    new GetSettings().execute();

                }
                break;

            default:
                break;
        }
    }



    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((Splash_screen) getActivity()).onDialogDismissed();
        }
    }


    protected LocationRequest createLocationRequest() {
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private class FetchCordinates extends AsyncTask<String, Integer, String> {

        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {

            mVeggsterLocationListener = new VeggsterLocationListener();
            if (ActivityCompat.checkSelfPermission(Splash_screen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Splash_screen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {

                if (mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mVeggsterLocationListener);
                }

            } catch (SecurityException e) {

                e.printStackTrace();

            }
            Gtcor.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onCancelled() {
            out.println("Cancelled by user!");
            Gtcor.setVisibility(View.GONE);

        }

        @Override
        protected void onPostExecute(String result) {
            Gtcor.setVisibility(View.GONE);
            if (My_lat != 0) {
                permissionsAllowd = true;
           new GetSettings().execute();
            }else{
                new android.support.v7.app.AlertDialog.Builder(Splash_screen.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Location not found")
                        .setMessage("This may be for slow internet connection also!")
                        .setPositiveButton("Retry", null)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               new FetchCordinates().execute();
                            }
                        })
                     .show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (My_lat == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                try {

                    My_lat = location.getLatitude();
                    My_long = location.getLongitude();

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),"Unable to get Location"
                            , Toast.LENGTH_LONG).show();
                }

            }

        }

    }




    private Location mCurrentLocation;
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
            }
        };
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(mFusedLocationClient!=null) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }

        updateUI();

    }
    private void updateUI() {

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            My_lat=mCurrentLocation.getLatitude();
            My_long=mCurrentLocation.getLongitude();
            if (My_lat == 0) {
                startLocationUpdates();
            }else{
                launchHomeScreen();
            }


        }
    }

         @Override
         public void onBackPressed() {
             super.onBackPressed();
             finish();

         }


    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private int Online=0;
        private String New_Version;
        private int New_Version_Importance=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray Settings = jsonObj.getJSONArray("Settings");
                    JSONArray Version = jsonObj.getJSONArray("Version");

                    for (int i = 0; i < Settings.length(); i++) {
                        JSONObject c = Settings.getJSONObject(i);
                        Online = c.getInt("Service_Online");

                    }
                    for (int i = 0; i < Version.length(); i++) {
                        JSONObject c = Version.getJSONObject(i);
                        New_Version = c.getString("Version");
                        New_Version_Importance=c.getInt("Importance");

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                if (prefManager.getNewVersion() == null) {
                    prefManager.setNewVersion(New_Version);
                    if (Online == 1) {
                        launchHomeScreen();
                    }
                } else {
                    if (!prefManager.getNewVersion().matches(New_Version)) {
                        if (New_Version_Importance == 1) {
                            new android.support.v7.app.AlertDialog.Builder(Splash_screen.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle("Update HelloCab")
                                    .setMessage("A new version of hellocab is available!")
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            prefManager.deleteState();
                                            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        } else {
                            new android.support.v7.app.AlertDialog.Builder(Splash_screen.this)
                                    .setTitle("Update HelloCab")
                                    .setMessage("A new version of hellocab is available!")
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Online == 1) {
                                                launchHomeScreen();
                                            }
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();

                        }
                    } else {
                        if (Online == 1) {
                            launchHomeScreen();
                        }
                    }
                }

            }
        }
    }


}




