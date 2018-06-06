package com.geekworkx.hellocab.Main_activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.CustomMapFragment;
import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parag on 12/01/18.
 */

public class Confirm_booking  extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Confirm_booking.class.getSimpleName();
    private RelativeLayout AllRl;
    private String OTP;
    private TextView Ride_otp,Ride_driver_name,Ride_car_no,Ride_car_type;
    private ImageView Ride_driver_image,Ride_car_image;
    private RatingBar Ride_driver_rating;
    private CustomMapFragment mapFragment;
    private Button Confirm_booking;
    private Button Car_choosen,Change_car;
    private PrefManager pref;
    private String _PhoneNo;
    private double From_lat=0.0,From_long=0.0,To_lat=0.0,To_long=0.0;
    private String Driver_phone;
    private String Driver_name,Driver_image,Vehicle_no,Vehicle_image,Vehicle_type;
    private boolean isInternetPresent=false;
    private ConnectionDetector cd;
    private String Driver_identity;
    private RelativeLayout R1;
    private ArrayList<LatLng>Longs=new ArrayList<LatLng>();
    private ArrayList<LatLng> markerPoints;
    private String WHO;
    private boolean Booking=false;
    private String UNIQUE_RIDE;
    private LinearLayout Lv1;
    private Button Call_driver,Track_driver;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        setContentView(R.layout.confirm_booking);
        AllRl=findViewById(R.id.ride_detail);
        Ride_otp=findViewById(R.id.textotp);
        Ride_driver_image=findViewById(R.id.driver_image);
        Ride_driver_name=findViewById(R.id.driver_name);
        Ride_car_image=findViewById(R.id.driver_car);
        Ride_car_no=findViewById(R.id.driver_car_no);
        Ride_car_type=findViewById(R.id.driver_car_type);
        Ride_driver_rating=findViewById(R.id.ratingBar);
        Confirm_booking=findViewById(R.id.confirm_ride);
        Lv1=findViewById(R.id.lov1);
        Confirm_booking.setOnClickListener(this);
        Car_choosen=findViewById(R.id.car_choosen);
        Change_car=findViewById(R.id.another_car);
        Call_driver=findViewById(R.id.call_driver);
        Track_driver=findViewById(R.id.track_driver);
        Call_driver.setOnClickListener(this);
        Track_driver.setOnClickListener(this);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        cd = new ConnectionDetector(getApplicationContext());


        Intent i = getIntent();
        From_lat = i.getDoubleExtra("From_lat", 0);
        From_long = i.getDoubleExtra("From_long", 0);
        To_lat = i.getDoubleExtra("To_lat", 0);
        To_long = i.getDoubleExtra("To_long", 0);
        Driver_phone=i.getStringExtra("Driver_phone");
        UNIQUE_RIDE=i.getStringExtra("UNIQUERIDE");

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }







    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_ride:
                Booking=true;
                if (UNIQUE_RIDE != null) {
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accept").child(con).child("UserAccept").setValue("YES");

                }
                Lv1.setVisibility(View.VISIBLE);
                Confirm_booking.setVisibility(View.GONE);

                break;

            case R.id.call_driver:
                if(Driver_phone!=null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Driver_phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if(Booking){
                Confirm_booking.setVisibility(View.GONE);

            }


        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE).show();

        }
    }


    private class GetDriver extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_ALL_DATA);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray driver_login = jsonObj.getJSONArray("driver_login");
                    JSONArray driver_details = jsonObj.getJSONArray("driver_details");
                    JSONArray gari = jsonObj.getJSONArray("vehicle_details");
                    JSONArray ride = jsonObj.getJSONArray("book_ride");

                    // looping through All Contacts


                    for (int i = 0; i < ride.length(); i++) {
                        JSONObject c = ride.getJSONObject(i);
                        String relation = c.getString("mobile");


                        if (relation.matches(_PhoneNo)) {
                            OTP = c.getString("otp");

                        }
                    }
                    for (int i = 0; i < driver_login.length(); i++) {
                        JSONObject c = driver_login.getJSONObject(i);
                        String relation = c.getString("mobile");


                        if (relation.matches(Driver_phone)) {
                            Driver_name = c.getString("name");

                        }
                    }
                    for (int i = 0; i < driver_details.length(); i++) {
                        JSONObject c = driver_details.getJSONObject(i);
                        String relation = c.getString("Driver_phone");
                        if (c.getInt("identity")!=0 ) {

                            if (relation.contains(Driver_phone)) {
                                Driver_identity=c.getString("identity");
                                Driver_image = c.getString("Driver_image");
                                for (int j = 0; j < gari.length(); j++) {
                                    JSONObject d = gari.getJSONObject(j);
                                    String relation1 = d.getString("identity");
                                    if (relation1.contains(Driver_identity)) {


                                        Vehicle_no=d.getString("Vehicle_no");
                                        Vehicle_image=(d.getString("Vehicle_image_1"));
                                        Vehicle_type=(d.getString("Vehicle_type"));


                                    }
                                }
                            }

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

            if (Vehicle_no != null) {
                Car_choosen.setText(Vehicle_type+"@"+Math.round(20 ));
                Change_car.setText("Mini"+"@"+Math.round(15 ));
                AllRl.setVisibility(View.VISIBLE);
                Ride_car_no.setText(getLastThree(Vehicle_no));
                Ride_car_type.setText(Vehicle_type);
                Ride_driver_name.setText(Driver_name);
                Ride_otp.setText("OTP: "+OTP);
                Ride_driver_rating.setRating(4);
                Picasso.Builder builder = new Picasso.Builder(Confirm_booking.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(Vehicle_image).into(Ride_car_image);
                Picasso.Builder builder1 = new Picasso.Builder(Confirm_booking.this);
                builder1.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(Driver_image).into(Ride_driver_image);

            }

        }

    }
    public String getLastThree(String myString) {
        if(myString.length() >= 4)
            return myString.substring(myString.length()-4);
        else
            return myString;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (UNIQUE_RIDE != null) {
            String[] pars = UNIQUE_RIDE.split("\\.");
            String con = TextUtils.join("", pars);
            mDatabase.child("Accept").child(con).removeValue();
        }
        Intent i=new Intent(Confirm_booking.this, GooglemapApp.class);
        startActivity(i);
        finish();
    }
}
