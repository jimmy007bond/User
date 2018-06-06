package com.geekworkx.hellocab.Main_activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Adapters.Remarks_rv;
import com.geekworkx.hellocab.Model.Book;
import com.geekworkx.hellocab.Model.Remarks;
import com.geekworkx.hellocab.MyViewPager;
import com.geekworkx.hellocab.Place.RecyclerTouchListener;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 20/09/17.
 */

public class Success extends AppCompatActivity implements  View.OnClickListener {

    private static final String TAG = Success.class.getSimpleName();
    private EditText Total_km;
    private EditText Total_m;
    private Button Yes;
    private float KM;
    private double COST;
    private String UNIQUEID;
    private PrefManager pref;
    private String USER;
    private String _PhoneNo;
    private String WHO;
    private Float Total_cost;
    private TextView Rate;
    private EditText receive;
    private RelativeLayout Rl;
    private CheckBox Yes_check,No_check;
    private TextView Total_length;
    private ProgressBar progressBar;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView Profile;
    private Button Submit;
    private String Rider_pic;
    private String confirmation;
    private String Reason_cancel;
    private RatingBar ratingBar_i;
    private String rating_i;
    private DatabaseReference mDatabase;
    private Book data_f = new Book();
    private String Paid;
    private String Rider_mobile;
    private TextView rrr;
    private String User_pic,User_mobile;
    private String Opp_mobile;
    //private FirebaseDataListener mFirebaseDataChanged;
    private String Rider_name,User_name;
    private float rate=0;
    private String Driver_mobile;
    private String Rate_user;
    private final int REQUEST_LOCATION = 200;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private boolean mResolvingError = false;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final int M_MAX_ENTRIES = 5;
    private CameraPosition mCameraPosition;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private double My_lat = 0, My_long = 0;
    private GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;
    private TextView Coupon,Bill_gen,Bill_pick,Bill_drop;
    private ImageView Bill_car;
    private RatingBar Bill_rate;
    private ImageView Bill_driver;
    private ImageView Bill_paid;
    private EditText What_liked;
    List<String> TotalDays = new ArrayList<String>();
    private String User_from,User_to,Car;
    private Button Stop;
    private TextView S1,S2;
    private Button Bill;
    private String _first,_second,_third,_fourth,_fifth;
    private RecyclerView _driverRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        Coupon=findViewById(R.id.coupon_applied);
        Bill_gen=findViewById(R.id.bill_generated);
        Bill_pick=findViewById(R.id.bill_pick);
        Bill_drop=findViewById(R.id.bill_drop);
        Bill_car=findViewById(R.id.bill_car);
        Bill_rate=findViewById(R.id.ratingBarbill);
        LayerDrawable stars = (LayerDrawable) Bill_rate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        Bill_driver=findViewById(R.id.driver_bill);
        Bill_paid=findViewById(R.id.bill_paid);
        What_liked=findViewById(R.id.what_liked);
        Stop=findViewById(R.id.success_ride);
        Submit=findViewById(R.id.bill_submit);
        Bill=findViewById(R.id.button_bill);
        Bill.setOnClickListener(this);
        S1=findViewById(R.id.s1);
        S2=findViewById(R.id.s2);
        S1.setText("Your");
        S2.setText(" Bill");
        toolbar = (Toolbar) findViewById(R.id.toolbar_success);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar=(ProgressBar)findViewById(R.id.progressBar3_last);
        viewPager = (MyViewPager) findViewById(R.id.rider_bill);
        Submit=(Button)findViewById(R.id.bill_submit);
        Intent i=getIntent();
        KM=i.getFloatExtra("KM",0);
        COST=(i.getDoubleExtra("COST",0));
        WHO=i.getStringExtra("WHO");
        UNIQUEID=i.getStringExtra("UNIQUEID");
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        _driverRv=findViewById(R.id._driver_remarks);

        Submit.setOnClickListener(this);
        Stop.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Success.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                //Bungee.card(Ride_later_tabs.this);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //mFirebaseDataChanged = new FirebaseDataListener();
        new GetRide().execute();
        Bill_rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Rate_user=String.valueOf(v);
                rate=v;
                new GetRide().execute();

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });


    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button_bill:
                if (UNIQUEID != null) {
                    String[] pars = UNIQUEID.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_continue());
                }
                break;


            case R.id.bill_submit:
                new PostConfirmation().execute();
                break;

            case R.id.success_ride:

                S1.setText("Your");
                S2.setText(" Review");
                viewPager.setCurrentItem(1);
                break;
            default:break;
        }
    }



    private class GetRide extends AsyncTask<Void, Void, Void>

    {


        private int Driver_ID=0;
        private ArrayList<Remarks>mRemarks=new ArrayList<Remarks>();
        private ArrayList<Remarks>mRemarks_rate=new ArrayList<Remarks>();
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


                    JSONArray profile = jsonObj.getJSONArray("Driver_Details");
                    JSONArray Ride_booked = jsonObj.getJSONArray("Book_Ride_past_driver");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Vehicle = jsonObj.getJSONArray("Vehicle_detail");

                    JSONArray _Rating = jsonObj.getJSONArray("rating_remarks");
                    // looping through All Contacts

                    if(UNIQUEID!=null) {



                        for (int x = 0; x < Ride_booked.length(); x++) {
                            JSONObject d = Ride_booked.getJSONObject(x);
                            if (d.getString("Unique_Ride_Code").matches(UNIQUEID)) {
                                for (int i = 0; i < profile.length(); i++) {
                                    JSONObject c = profile.getJSONObject(i);

                                    if (c.getInt("ID")==(d.getInt("Driver_ID"))) {
                                        User_pic = c.getString("Photo");
                                        User_name = c.getString("Name");
                                        Driver_mobile=_PhoneNo;
                                        Driver_mobile=c.getString("Phone_No");
                                        User_from=d.getString("From_Address");
                                        User_to=d.getString("To_Address");
                                        Driver_ID=d.getInt("Driver_ID");
                                    }

                                }

                                for (int i = 0; i < Vehicle.length(); i++) {
                                    JSONObject c = Vehicle.getJSONObject(i);
                                    if (!d.isNull("Driver_ID") && d.getInt("Driver_ID")==Driver_ID) {
                                        Car=c.getString("Vehicle_Type");
                                    }
                                }

                                for (int i = 0; i < _Rating.length(); i++) {
                                    JSONObject c = _Rating.getJSONObject(i);
                                    if (!c.isNull("Rating_limit")) {
                                        Remarks item=new Remarks();
                                        item.set_limit(c.getInt("Rating_limit"));
                                        item.set_comments(c.getString("Rating_comments"));
                                        mRemarks.add(item);
                                    }
                                }


                            }
                        }
                        if(Car==null){

                            for (int x = 0; x < Ride_booked.length(); x++) {
                                JSONObject d = Ride_booked.getJSONObject(x);
                                if (d.getString("Unique_Ride_Code").matches(UNIQUEID)) {

                                    for (int i = 0; i < profile.length(); i++) {
                                        JSONObject c = profile.getJSONObject(i);

                                        if (c.getInt("ID")==(d.getInt("Driver_ID"))) {
                                            User_pic = c.getString("Photo");
                                            User_name = c.getString("Name");
                                            Driver_mobile=_PhoneNo;
                                            Driver_mobile=c.getString("Phone_No");
                                            User_from=d.getString("From_Address");
                                            User_to=d.getString("To_Address");
                                            Driver_ID=d.getInt("Driver_ID");
                                        }

                                    }

                                    for (int i = 0; i < Vehicle.length(); i++) {
                                        JSONObject c = Vehicle.getJSONObject(i);
                                        if (!d.isNull("Driver_ID") && d.getInt("Driver_ID")==Driver_ID) {
                                            Car=c.getString("Vehicle_Type");
                                        }
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
            progressBar.setVisibility(View.GONE);
            if(User_pic!=null){
                Picasso.Builder builder = new Picasso.Builder(Success.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(User_pic).into(Bill_driver);

                Bill_pick.setText(User_from);
                Bill_drop.setText(User_to);
                if (Car.contains("SEDAN")) {
                   Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sedan));
                } else if (Car.contains("PICKUP")) {
                    Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_muv));
                } else if (Car.contains("SUV")) {
                    Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv));
                } else if (Car.contains("MINI")) {
                    Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mini));
                } else if (Car.contains("MICRO")) {
                    Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_micro));
                } else if (Car.contains("AMBULANCE")) {
                    Bill_car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_amb));
                }
                if (UNIQUEID != null) {
                    String[] pars = UNIQUEID.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener());
                }
            }

         if(mRemarks.size()!=0){
                if(rate<3.5) {
                    for (int i = 0; i < mRemarks.size(); i++) {
                        if (mRemarks.get(i).get_limit(i) == 1) {
                            Remarks item = new Remarks();
                            item.set_limit(mRemarks.get(i).get_limit(i));
                            item.set_comments(mRemarks.get(i).get_comments(i));
                            mRemarks_rate.add(item);
                        }
                    }
                }else{
                    for (int i = 0; i < mRemarks.size(); i++) {
                        if (mRemarks.get(i).get_limit(i) == 2) {
                            Remarks item = new Remarks();
                            item.set_limit(mRemarks.get(i).get_limit(i));
                            item.set_comments(mRemarks.get(i).get_comments(i));
                            mRemarks_rate.add(item);
                        }
                    }
                }
             if(mRemarks_rate.size()!=0) {
                 Remarks_rv sAdapter = new Remarks_rv(Success.this, mRemarks_rate);
                 sAdapter.notifyDataSetChanged();
                 _driverRv.setAdapter(sAdapter);
                 LinearLayoutManager mLayoutManager = new LinearLayoutManager(Success.this);
                 mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                 _driverRv.setLayoutManager(mLayoutManager);
             }

         }

            _driverRv.addOnItemTouchListener(
                    new RecyclerTouchListener(Success.this, _driverRv,
                            new RecyclerTouchListener.OnTouchActionListener() {
                                public boolean Me;

                                @Override
                                public void onClick(View view, final int position) {
                                    TotalDays.add(mRemarks.get(position).get_comments(position));
                                }

                                @Override
                                public void onRightSwipe(View view, int position) {

                                }

                                @Override
                                public void onLeftSwipe(View view, int position) {

                                }
                            }));


        }
    }

    private class FirebaseDataListener_continue implements ValueEventListener {


        private double Cost=0;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getChildrenCount()!=0) {

                if (dataSnapshot.child("Bill").getValue() != null &&
                        dataSnapshot.child("Tax_names").getValue() != null) {
                    if (!Success.this.isFinishing()) {
                        final Dialog dialog = new Dialog(Success.this);

                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.dialog_bill);


                        COST=Double.parseDouble((String) dataSnapshot.child("Bill").getValue());
                        DecimalFormat dft=new DecimalFormat("0.00");

                        TextView total_cost = (TextView) dialog.findViewById(R.id.total_cost);
                        TextView total_km_cost = (TextView) dialog.findViewById(R.id.total_km_cost);
                        TextView total_coupon = (TextView) dialog.findViewById(R.id.total_coupon);
                        TextView total_taxes = (TextView) dialog.findViewById(R.id.total_taxes);
                        TextView total_fare = (TextView) dialog.findViewById(R.id.total_fare);
                        TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                        LinearLayout C_applied = dialog.findViewById(R.id.c_applied);

                        if ((String) dataSnapshot.child("Cost").getValue() != null && (String) dataSnapshot.child("Estimated_fare").getValue() != null) {
                            if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) < Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                                Cost = Double.parseDouble((String) dataSnapshot.child("Cost").getValue());
                            } else if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) > Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                                Cost = Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue());
                            }
                        }
                        total_cost.setText("\u20B9 " + (dft.format(COST)));
                        total_km_cost.setText("\u20B9 " +dft.format(Cost));
                        if (dataSnapshot.child("Coupon_value").getValue() != null) {
                            total_coupon.setText("-" + "\u20B9 " +dft.format(Double.parseDouble( (String) dataSnapshot.child("Coupon_value").getValue())));
                        } else {
                            C_applied.setVisibility(View.GONE);
                        }
                        tax_name.setText((String) dataSnapshot.child("Tax_names").getValue());
                        double tx = Double.parseDouble((String) dataSnapshot.child("Bill").getValue()) * Double.parseDouble((String) dataSnapshot.child("Tax_percentage").getValue()) / 100;
                        total_taxes.setText("\u20B9 " + dft.format(tx));
                        total_fare.setText("\u20B9 " + dft.format(Double.parseDouble((String) dataSnapshot.child("Bill").getValue())));

                        dialog.show();

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    private class FirebaseDataListener implements ValueEventListener {



        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            COST=Double.parseDouble((String) dataSnapshot.child("Bill").getValue());
            DecimalFormat dft=new DecimalFormat("0.00");
            Bill_gen.setText(dft.format(COST));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.last1;
                    break;
                case 1:
                    resId = R.id.last2;
                    break;

            }
            return findViewById(resId);
        }
    }


    private class PostConfirmation extends AsyncTask<Void, Integer, String> {


        private String commaSeparatedValues="";
        private boolean success=false;

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
            if(TotalDays.size()!=0) {
                if (TotalDays.size() > 1) {
                    commaSeparatedValues = TextUtils.join(",", TotalDays);
                } else {
                    commaSeparatedValues = TotalDays.get(0);
                }
            }else{
                commaSeparatedValues = "Not Rated";
            }

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("unique_id",UNIQUEID)
                        .addFormDataPart("driver_mobile",Driver_mobile)
                        .addFormDataPart("user_mobile",_PhoneNo)
                        .addFormDataPart("review",commaSeparatedValues)
                        .addFormDataPart("user_rating",Rate_user)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.STOP_BOOKING)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars = res.split("error");
                if (pars[1].contains("false")) {
                    success = true;

                } else {
                    success = false;
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

        protected void onPostExecute(String file_url) {
            if (success) {
                String[] pars = UNIQUEID.split("\\.");
                String con = TextUtils.join("", pars);
                mDatabase.child("Accepted_Ride").child(con).child("USERDONE").setValue("DONE");
                Intent i=new Intent(Success.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewPager.setCurrentItem(0);
    }

}
