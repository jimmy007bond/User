package com.geekworkx.hellocab.Ride_later;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.hellocab.Adapters.CustomMapFragment;
import com.geekworkx.hellocab.Adapters.DirectionsJSONParser;
import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.Rate;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.google.android.gms.maps.model.JointType.ROUND;


/**
 * Created by parag on 19/01/18.
 */

public class Ride_later_address extends AppCompatActivity implements
        OnMapReadyCallback,PlaceSelectionListener,View.OnClickListener {
    private static final String TAG = Ride_later_address.class.getSimpleName();
       private Toolbar toolbar;
       private TextView From_address,To_address;
       private GoogleMap googleMap;
    private Button Confirm_booking;
    private PrefManager pref;
    private String _PhoneNo;
    private CustomMapFragment mapFragment;
    private double My_lat=0,My_long=0;
    private double To_lat=0,To_long=0;
    private Button My_location;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(25.00000, 91.00000), new LatLng(27.99999, 91.99999));
    private static final int REQUEST_PICK_TO = 10015;
    private static final int REQUEST_PICK_FROM = 10016;
    private Place place_;
    private Marker markerd;
    private String Unique_ID;
    private ProgressBar progressBar;
    private HorizontalScrollView choose;
    private String strAdd;
    private String Date_later,Time_later;
    private SimpleDateFormat format_;
    private Marker markerf;
    private String ot;
    private Marker marker;
    private Boolean outstation=false;
    private boolean Multiple=false;
    private int Minimum_KM_Distance_for_Outstation=25;
    private ArrayList<Rate>mVehcle_option=new ArrayList<Rate>();
    private Button Choose_1,Choose_2,Choose_3,Choose_4;
    private String Car_choosen="MINI";
    private String mobileIp;
    private double Distance=0;
    private Boolean Out=false;
    private String _drop;
    private String _pick;
    private ArrayList<LatLng> markerPoints=new ArrayList<LatLng>();
    private PolylineOptions lineOptions = null;
    private Polyline polylineFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_later_address);
        toolbar=findViewById(R.id.toolbar_later);
        From_address=findViewById(R.id.et_myLocation_later);
        To_address=findViewById(R.id.et_destLocation_later);
        My_location=findViewById(R.id.myLocationCustomButton_later);
        Confirm_booking=findViewById(R.id.confirm_ride_later);
        progressBar=findViewById(R.id.progressBarlater);
        choose=findViewById(R.id.choose_car_later);
        Choose_1=findViewById(R.id.car_choosen_1);
        Choose_2=findViewById(R.id.car_choosen_2);
        Choose_3=findViewById(R.id.car_choosen_3);
        Choose_4=findViewById(R.id.car_choosen_4);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle("Ride Later");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.top));

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Unique_ID=i.getStringExtra("Unique_ID");
        Multiple=i.getBooleanExtra("multiple",false);
        Time_later=i.getStringExtra("time");
        Date_later=i.getStringExtra("Days");
        _drop=i.getStringExtra("drop");
        _pick=i.getStringExtra("pick");

        mapFragment = (CustomMapFragment) getFragmentManager()
                .findFragmentById(R.id.map_later);

        mapFragment.getMapAsync(Ride_later_address.this);




        choose.setVisibility(View.GONE);
        My_location.setOnClickListener(this);
        From_address.setOnClickListener(this);
        To_address.setOnClickListener(this);
        Confirm_booking.setOnClickListener(this);
        Choose_1.setOnClickListener(this);
        Choose_2.setOnClickListener(this);
        Choose_3.setOnClickListener(this);
        Choose_4.setOnClickListener(this);
        format_ = new SimpleDateFormat("MMM dd");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Ride_later_address.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        });

        mobileIp = getMobileIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }

    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return null;
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return  Formatter.formatIpAddress(ip);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (My_lat != 0) {
            if(TextUtils.isEmpty(From_address.getText().toString())) {
                From_address.setText(getCompleteAddressString(My_lat, My_long));
            }


        }
        if(_pick!=null){
            From_address.setText(_pick);
        }
        if(_drop!=null){
            choose.setVisibility(View.GONE);
            To_address.setText(_drop);
            Confirm_booking.setText("Check Fares");
        }

        if(outstation){
            Confirm_booking.setText("Check Fares");
        }
        new GetSettings().execute();
    }



    @Override
    public void onMapReady(final GoogleMap map) {
        if (map == null) {
            return;
        } else {
            if (googleMap == null) {
                googleMap = map;



                if (My_lat != 0) {
                    From_address.setText(getCompleteAddressString(My_lat,My_long));
                    CameraPosition googlePlex;
                    googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(17) // Sets the zoom
                            .build(); // Crea
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                }



                if(_pick==null) {
                    if (markerf != null) {
                        markerf.setPosition(new LatLng(My_lat, My_long));
                    } else {
                        markerf = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(My_lat, My_long))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));

                    }
                }else{
                    if (markerf != null) {
                        markerf.setPosition(getLocationFromAddress(this,_pick));
                    } else {
                        markerf = googleMap.addMarker(new MarkerOptions()
                                .position(getLocationFromAddress(this,_pick))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));

                    }
                }


                if(_drop!=null) {
                    if (markerd != null) {
                        markerd.setPosition(getLocationFromAddress(this,_drop));
                    } else {
                        markerd = googleMap.addMarker(new MarkerOptions()
                                .position(getLocationFromAddress(this,_drop))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_red)));

                    }


                }
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);



            }
        }
    }


    public void SetZoomGot(final Marker markeruser,final Marker markerCar) {
        if ( googleMap!=null) {
            ArrayList<Marker> markerAll = new ArrayList<Marker>();
            if (markeruser != null && markerCar != null  ) {
                markerAll.add(markeruser);
                markerAll.add(markerCar);
                if (markerAll.size() != 0) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 240);
                    googleMap.animateCamera(cu);
                    markerPoints.clear();
                    markerPoints.add(markeruser.getPosition());
                    markerPoints.add(markerCar.getPosition());

                    if (markerPoints.size() == 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        new DownloadTask().execute(url);


                    }


                }

            }
        }
        else{
            mapFragment.getMapAsync(Ride_later_address.this);

        }
    }
    @Override
    public void onClick(View view) {
        final AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        switch (view.getId()) {
            case R.id.myLocationCustomButton_later:
                if (My_lat != 0) {
                    if (markerf != null) {
                        markerf.setPosition(new LatLng(My_lat, My_long));
                    } else {
                        markerf = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(My_lat, My_long))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));

                    }

                    CameraPosition googlePlex;
                    googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(17) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            // Set the camera tilt
                            .build(); // Crea
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                }
                break;
            case R.id.et_myLocation_later:

                _pick=null;

                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .setFilter(typeFilter)
                            .build(Ride_later_address.this);
                    startActivityForResult(intent, REQUEST_PICK_FROM);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.et_destLocation_later:
                _drop=null;
                if(!TextUtils.isEmpty(From_address.getText().toString())) {
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder
                                (PlaceAutocomplete.MODE_OVERLAY)
                                .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                                .setFilter(typeFilter)
                                .build(Ride_later_address.this);
                        startActivityForResult(intent, REQUEST_PICK_TO);
                    } catch (GooglePlayServicesRepairableException |
                            GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter pick up address",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.confirm_ride_later:
                if(_drop==null) {
                    if (Car_choosen != null) {
                        if (!TextUtils.isEmpty(To_address.getText().toString()) && !TextUtils.isEmpty(From_address.getText().toString())) {
                            new PostFavourite().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter addresses", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select a vehicle", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (!TextUtils.isEmpty(To_address.getText().toString()) && !TextUtils.isEmpty(From_address.getText().toString())) {
                        outstation = true;
                        new PostOutstation().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter addresses", Toast.LENGTH_SHORT).show();

                    }
                }

                break;

            case R.id.car_choosen_1:
                String[] pars = Choose_1.getText().toString().split("\\@");
                Car_choosen=pars[0];
                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                Choose_2.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_3.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_4.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                break;
            case R.id.car_choosen_2:
                String[] pars2 = Choose_2.getText().toString().split("\\@");
                Car_choosen=pars2[0];
                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_2.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                Choose_3.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_4.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));

                break;
            case R.id.car_choosen_3:
                String[] pars3 = Choose_3.getText().toString().split("\\@");
                Car_choosen=pars3[0];
                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_2.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_3.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                Choose_4.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));

                break;
            case R.id.car_choosen_4:
                String[] pars4 = Choose_4.getText().toString().split("\\@");
                Car_choosen=pars4[0];
                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_2.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_3.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.regular));
                Choose_4.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));

                break;

        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        place=place_;
    }

    @Override
    public void onError(Status status) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        if(result!=null) {
            switch (requestCode) {
                //checking for our ColorSelectorActivity using request code

                case REQUEST_PICK_TO:
                    Place place1 = PlaceAutocomplete.getPlace(this, result);
                    this.onPlaceSelected(place1);
                    if (place1 != null) {
                        if (!TextUtils.isEmpty(place1.getAddress())) {
                            LatLng queriedLocation = place1.getLatLng();
                            if (markerd != null) {
                                markerd.setPosition(queriedLocation);
                            } else {
                                markerd = googleMap.addMarker(new MarkerOptions()
                                        .position(queriedLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_red)));

                            }
                            if(markerf!=null && markerd!=null) {
                                SetZoomGot(markerf, markerd);
                            }

                            To_lat = queriedLocation.latitude;
                            To_long = queriedLocation.longitude;
                            To_address.setText(place1.getAddress());
                            String Location=String.valueOf(To_lat)+" "+String.valueOf(To_long);
                            new PostDropLocation().execute(Location);

                        }


                    }
                    break;
                case REQUEST_PICK_FROM:
                    //check whether result comes with RESULT_OK (That mean no problem in result)
                    //Place place1 = PlaceAutocomplete.getPlace(this, result);
                    Place place = PlaceAutocomplete.getPlace(this, result);
                    this.onPlaceSelected(place);
                    if (place != null) {
                        if (!TextUtils.isEmpty(place.getAddress())) {
                            LatLng queriedLocation = place.getLatLng();
                            From_address.setText(place.getAddress());
                            My_lat=queriedLocation.latitude;
                            My_long=queriedLocation.longitude;
                            if (markerf != null) {
                                markerf.setPosition(new LatLng(My_lat, My_long));
                            } else {
                                markerf = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));

                            }

                        }


                    }
                    break;

                case RESULT_CANCELED:
                    recreate();

                    break;
                case PlaceAutocomplete.RESULT_ERROR:
                    Status status = PlaceAutocomplete.getStatus(this, result);
                    recreate();
                    // TODO: Handle the error.
                    Log.e("Tag", status.getStatusMessage());
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, result);
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }else {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private class PostDropLocation  extends AsyncTask<String, Integer, String> {


        private boolean success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String Locate=args[0];
            return uploadFile(Locate);
        }

        private String uploadFile(String locate) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("latlng", locate)
                        .build();

                Request request = new Request.Builder()
                        .url("http://139.59.38.160/OHO/APP/checkLocation.php?latlng="+locate)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                if(res.contains("True")){
                    success = true;

                }else{
                    success=false;
                }

                android.util.Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                android.util.Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                android.util.Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        protected void onPostExecute(String file_url) {
            if (success) {
                outstation=false;
                if(_drop==null) {
                    choose.setVisibility(View.VISIBLE);
                }
            } else {
                choose.setVisibility(View.GONE);
                if(Multiple){

                        new android.support.v7.app.AlertDialog.Builder(Ride_later_address.this)
                                .setTitle("You can travel upto "+Minimum_KM_Distance_for_Outstation+"Kilometers per ride while booking for Multiple Ride Later service")
                                .setMessage("Change drop location")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Distance=0f;
                                        To_long=0;
                                        To_lat=0;
                                        To_address.setText("");
                                        dialog.cancel();
                                    }
                                })

                                .show();

                }else {
                    Confirm_booking.setText("Check Fares");
                        new android.support.v7.app.AlertDialog.Builder(Ride_later_address.this)
                                .setTitle("Book HelloCab Outstation")
                                .setCancelable(false)
                                .setMessage("Drop location is outside the limits. Continue booking Outstation instead?")
                                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = getDirectionsUrl(new LatLng(My_lat,My_long),new LatLng(To_lat,To_long));
                                        new DownloadTask().execute(url);
                                        outstation = true;
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Change drop location", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        To_address.setText("");
                                        To_lat = 0;
                                        To_long = 0;
                                        dialog.cancel();
                                    }
                                })
                                .show();


                }

            }

        }

    }
    
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                Log.w(" address", strReturnedAddress.toString());
            } else {
                Log.w(" address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(" loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private class PostFavourite extends AsyncTask<Void, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            My_lat=getLocationFromAddress(Ride_later_address.this,From_address.getText().toString()).latitude;
            My_long=getLocationFromAddress(Ride_later_address.this,From_address.getText().toString()).longitude;
            To_lat=getLocationFromAddress(Ride_later_address.this,To_address.getText().toString()).latitude;
            To_long=getLocationFromAddress(Ride_later_address.this,To_address.getText().toString()).longitude;

        }

        protected String doInBackground(Void... strings) {
            // Some long-running task like downloading an image.

            return uploadFile();

        }
        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("day",Date_later)
                        .addFormDataPart("time",Time_later)
                        .addFormDataPart("from",From_address.getText().toString())
                        .addFormDataPart("to",To_address.getText().toString())
                        .addFormDataPart("vehicle",Car_choosen)
                        .addFormDataPart("to_lat", String.valueOf(To_lat))
                        .addFormDataPart("to_long", String.valueOf(To_long))
                        .addFormDataPart("from_lat", String.valueOf(My_lat))
                        .addFormDataPart("from_long", String.valueOf(My_long))
                        .addFormDataPart("IP", mobileIp)
                        .addFormDataPart("distance", String.valueOf(Distance))
                        .addFormDataPart("outstation", String.valueOf(outstation))
                        .addFormDataPart("return", String.valueOf(false))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_LATER_RIDE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;
                    String[]pars_=pars[1].split("false,");
                    JSONObject jObj = new JSONObject("{".concat(pars_[1]));
                    JSONObject user = jObj.getJSONObject("user");
                    Unique_ID=user.getString("Unique_Ride_Code");

                }else {
                    success=false;

                }
                Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }


        protected void onPostExecute(String result) {



            if(success){
                progressBar.setVisibility(ProgressBar.GONE);
                if(Distance>Minimum_KM_Distance_for_Outstation){

                                    Intent o = new Intent(Ride_later_address.this, Ride_outstation.class);
                                    o.putExtra("mylat", My_lat);
                                    o.putExtra("mylong", My_long);
                                    o.putExtra("from_address",From_address.getText().toString());
                                    o.putExtra("to_address",To_address.getText().toString());
                                    o.putExtra("date_later",Date_later);
                                    o.putExtra("time_later",Time_later);
                                    o.putExtra("distance",Distance);
                                    o.putExtra("Unique_ID",Unique_ID);
                                    startActivity(o);
                                    finish();


                }else {
                    if (My_lat != 0) {
                        Intent o = new Intent(Ride_later_address.this, GooglemapApp.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        startActivity(o);
                        finish();
                    }
                }
            }
        }
    }

    private class PostOutstation extends AsyncTask<Void, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            My_lat=getLocationFromAddress(Ride_later_address.this,From_address.getText().toString()).latitude;
            My_long=getLocationFromAddress(Ride_later_address.this,From_address.getText().toString()).longitude;
            To_lat=getLocationFromAddress(Ride_later_address.this,To_address.getText().toString()).latitude;
            To_long=getLocationFromAddress(Ride_later_address.this,To_address.getText().toString()).longitude;
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected String doInBackground(Void... strings) {
            // Some long-running task like downloading an image.

            return uploadFile();

        }
        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("day",Date_later)
                        .addFormDataPart("time",Time_later)
                        .addFormDataPart("vehicle",Car_choosen)
                        .addFormDataPart("from",From_address.getText().toString())
                        .addFormDataPart("to",To_address.getText().toString())
                        .addFormDataPart("to_lat", String.valueOf(To_lat))
                        .addFormDataPart("to_long", String.valueOf(To_long))
                        .addFormDataPart("from_lat", String.valueOf(My_lat))
                        .addFormDataPart("from_long", String.valueOf(My_long))
                        .addFormDataPart("IP", mobileIp)
                        .addFormDataPart("distance", String.valueOf(Distance))
                        .addFormDataPart("outstation", String.valueOf(outstation))
                        .addFormDataPart("return", String.valueOf(false))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_LATER_RIDE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;
                    String[]pars_=pars[1].split("false,");
                    JSONObject jObj = new JSONObject("{".concat(pars_[1]));
                    JSONObject user = jObj.getJSONObject("user");
                    Unique_ID=user.getString("Unique_Ride_Code");

                }else {
                    success=false;

                }
                Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }


        protected void onPostExecute(String result) {


            if(success){
                progressBar.setVisibility(ProgressBar.GONE);
                    Intent o = new Intent(Ride_later_address.this, Ride_outstation.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    o.putExtra("from_address",From_address.getText().toString());
                    o.putExtra("to_address",To_address.getText().toString());
                    o.putExtra("date_later",Date_later);
                    o.putExtra("time_later",Time_later);
                    o.putExtra("distance",Distance);
                    o.putExtra("Unique_ID",Unique_ID);
                    startActivity(o);
                    finish();


            }
        }
    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {


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
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");


                    // looping through All Contacts
                    if (_PhoneNo != null) {
                        for (int i = 0; i < Settings_default.length(); i++) {
                            JSONObject c = Settings_default.getJSONObject(i);

                            Minimum_KM_Distance_for_Outstation  = c.getInt("Minimum_KM_Distance_for_Outstation");
                        }

                        for (int i = 0; i < Settings_Vehicle_Rate.length(); i++) {
                            JSONObject c = Settings_Vehicle_Rate.getJSONObject(i);

                            Rate item=new Rate();
                            item.setVehicle_Type(c.getString("Vehicle_Type"));
                            item.setHourly_Rate(c.getInt("Hourly_Rate"));
                            item.setMinimum_Rate(c.getInt("Minimum_Rate"));
                            mVehcle_option.add(item);
                        }


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
            progressBar.setVisibility(ProgressBar.GONE);
           if(mVehcle_option.size()>=4){

                   Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0)+"@"+(mVehcle_option.get(0).getHourly_Rate(0)+(Math.round(mVehcle_option.get(0).getMinimum_Rate(0))))+" Rs");
                   Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1)+"@"+(mVehcle_option.get(1).getHourly_Rate(1)+(Math.round(mVehcle_option.get(1).getMinimum_Rate(1))))+" Rs");
                   Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2)+"@"+(mVehcle_option.get(2).getHourly_Rate(2)+(Math.round(mVehcle_option.get(2).getMinimum_Rate(2))))+" Rs");
                   Choose_4.setText(mVehcle_option.get(3).getVehicle_Type(3)+"@"+(mVehcle_option.get(3).getHourly_Rate(3)+(Math.round(mVehcle_option.get(3).getMinimum_Rate(3))))+" Rs");


           }else if(mVehcle_option.size()>=3){

               Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0)+"@"+(mVehcle_option.get(0).getHourly_Rate(0)+(Math.round(mVehcle_option.get(0).getMinimum_Rate(0))))+" Rs");
               Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1)+"@"+(mVehcle_option.get(1).getHourly_Rate(1)+(Math.round(mVehcle_option.get(1).getMinimum_Rate(1))))+" Rs");
               Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2)+"@"+(mVehcle_option.get(2).getHourly_Rate(2)+(Math.round(mVehcle_option.get(2).getMinimum_Rate(2))))+" Rs");
               Choose_4.setVisibility(View.GONE);

           }else if(mVehcle_option.size()>=2){

               Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0)+"@"+(mVehcle_option.get(0).getHourly_Rate(0)+(Math.round(mVehcle_option.get(0).getMinimum_Rate(0))))+" Rs");
               Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1)+"@"+(mVehcle_option.get(1).getHourly_Rate(1)+(Math.round(mVehcle_option.get(1).getMinimum_Rate(1))))+" Rs");
               Choose_3.setVisibility(View.GONE);
               Choose_4.setVisibility(View.GONE);

           }else if(mVehcle_option.size()>1){

               Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0)+"@"+(mVehcle_option.get(0).getHourly_Rate(0)+(Math.round(mVehcle_option.get(0).getMinimum_Rate(0))))+" Rs");
               Choose_2.setVisibility(View.GONE);
               Choose_3.setVisibility(View.GONE);
               Choose_4.setVisibility(View.GONE);

           }else{
               choose.setVisibility(View.GONE);
           }


           if(markerf!=null && markerd!=null){
               SetZoomGot(markerf,markerd);
           }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_white, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh1:
                recreate();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){



        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String duration = "";
            Random rand = new Random();


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        String pars = (String) point.get("distance");
                        String[] pars1 = pars.split(" ");
                        Distance = Float.parseFloat(pars1[0]);
                        break;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        break;
                    }

                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.startCap(new SquareCap());
                lineOptions.endCap(new SquareCap());
                lineOptions.jointType(ROUND);
                lineOptions.color(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));


            }
            if (Distance<Minimum_KM_Distance_for_Outstation) {
                choose.setVisibility(View.VISIBLE);
            }else if(Distance>Minimum_KM_Distance_for_Outstation){
                choose.setVisibility(View.GONE);
            }
            if (lineOptions != null) {
                    polylineFinal = googleMap.addPolyline(lineOptions);
            }


            if(outstation){
                new PostOutstation().execute();
            }
            }

    }
}
