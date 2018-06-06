package com.geekworkx.hellocab.Ride_later;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.User;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by parag on 22/02/18.
 */

public class PastRides extends AppCompatActivity{
    private static final String TAG = PastRides.class.getSimpleName();
    private ImageView Snapshot,PastDriver,PastCar;
    private EditText Past_deriver_name,Past_you_rated,Past_car,Past_start_time,Past_end_time;
    private TextInputLayout Start_time,End_time;
    private EditText Past_review,Past_review_text;
    private RatingBar Rating;
    private Toolbar toolbar;
    private double My_lat=0,My_long=0;
    private PrefManager pref;
    private String _PhoneNo;
    private boolean isInternetPresent=false;
    private ConnectionDetector cd;
    private ProgressBar progressBar;
    private String UNIQUEID;
    private ArrayList<User> mItems=new ArrayList<User>();
    private TextView Tool1,Tool2;
    private TextView total_cost,total_km_cost,total_coupon,total_taxes,total_fare,tax_name;
    private LinearLayout C_applied;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_rides);
         total_cost = (TextView) findViewById(R.id.total_cost);
         total_km_cost = (TextView) findViewById(R.id.total_km_cost);
         total_coupon = (TextView) findViewById(R.id.total_coupon);
         total_taxes = (TextView) findViewById(R.id.total_taxes);
         total_fare = (TextView) findViewById(R.id.total_fare);
         tax_name = (TextView) findViewById(R.id.tax_name);
         C_applied = findViewById(R.id.c_applied);
        toolbar = (Toolbar) findViewById(R.id.toolbar_later_past);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cd = new ConnectionDetector(getApplicationContext());
        Snapshot=findViewById(R.id.past_snapshot);
        PastDriver=findViewById(R.id.past_driver);
        Start_time=findViewById(R.id.past_start);
        End_time=findViewById(R.id.past_end);
        PastCar=findViewById(R.id.past_car_image);
        Past_car=findViewById(R.id.past_car_detail);
        Past_deriver_name=findViewById(R.id.past_name_driver);
        Past_you_rated=findViewById(R.id.past_you_rated);
        Rating=findViewById(R.id.past_rate);
        Past_start_time=findViewById(R.id.past_start_time_text);
        Past_end_time=findViewById(R.id.past_end_time_text);
        Past_review=findViewById(R.id.past_review);
        Past_review_text=findViewById(R.id.past_review_text);
        progressBar=findViewById(R.id.progressBarpast);
        Tool1=findViewById(R.id.past101);
        Tool2=findViewById(R.id.past102);
        Tool1.setText("Your ride on ");

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        UNIQUEID=i.getStringExtra("UNIQUEID");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PastRides.this, Ride_later_tabs.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                //Bungee.card(Ride_later_tabs.this);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
           mItems.clear();
            mDatabase = FirebaseDatabase.getInstance().getReference();
           new GetpastDate().execute();
        }
    }

    private class GetpastDate extends AsyncTask<Void, Void, Void> {


        private int User_ID=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mItems.clear();

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

                    if (_PhoneNo != null && UNIQUEID!=null) {
                        JSONArray later1 = jsonObj.getJSONArray("Book_Ride_Past");
                        JSONArray profile = jsonObj.getJSONArray("Driver_Details");
                        JSONArray User = jsonObj.getJSONArray("User");
                        JSONArray gari = jsonObj.getJSONArray("Vehicle_detail");
                        for (int i = 0; i < User.length(); i++) {
                            JSONObject c = User.getJSONObject(i);
                            String relation = c.getString("Phone_No");
                            if (!relation.contains("null")) {

                                if (relation.matches(_PhoneNo)) {
                                    User_ID=c.getInt("ID");
                                    break;
                                }

                            }
                        }
                        for (int i = 0; i < later1.length(); i++) {
                            JSONObject c = later1.getJSONObject(i);
                           int relation = c.getInt("User_ID");
                            if (!c.isNull("User_ID")  ) {

                                for (int j = 0; j < profile.length(); j++) {
                                    JSONObject d = profile.getJSONObject(j);


                                    if (relation==User_ID && UNIQUEID.matches(c.getString("Unique_Ride_Code"))) {
                                        if (c.getInt("Driver_ID")==(d.getInt("ID"))) {
                                            User item = new User();
                                            item.setDate(c.getString("Booking_Date"));
                                            item.setTime(c.getString("Booking_Time"));
                                            item.setFrom(c.getString("From_Address"));
                                            item.setTo(c.getString("To_Address"));
                                            item.setSnapshot(c.getString("Map_Snapshot"));
                                            item.setDriverImage(d.getString("Photo"));
                                            item.setUnique_id(c.getString("Unique_Ride_Code"));
                                            for (int jk = 0; jk < gari.length(); jk++) {
                                                JSONObject e = gari.getJSONObject(jk);

                                                if (!e.isNull("Driver_ID") && d.getInt("ID") == c.getInt("Driver_ID")) {
                                                    item.setVehicle(e.getString("Vehicle_Type"));
                                                    break;
                                                }
                                            }
                                            item.setDrivername(d.getString("Name"));
                                            item.setDriverRate(c.getString("Driver_Rating_By_User"));
                                            item.setCost(c.getString("Cost"));
                                            item.setStart_time(c.getString("Start_time"));
                                            item.setEnd_time(c.getString("End_time"));
                                            item.setReview(c.getString("User_Review"));
                                            mItems.add(item);

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
            if (mItems.size() != 0) {
                if(mItems.get(0).getDate(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getDate(0))){
                    String date_ = parseDateToddMMyyyy(mItems.get(0).getDate(0));
                    Tool2.setText( date_);

                }
                if(mItems.get(0).getSnapshot(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getSnapshot(0))){
                    Picasso.Builder builder = new Picasso.Builder(PastRides.this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(mItems.get(0).getSnapshot(0)).into(Snapshot);
                }
                    if(mItems.get(0).getDriverImage(0)!=null &&
                            !TextUtils.isEmpty(mItems.get(0).getDriverImage(0))){
                        Picasso.Builder builder = new Picasso.Builder(PastRides.this);
                        builder.listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                        builder.build().load(mItems.get(0).getDriverImage(0)).into(PastDriver);
                    }
                if(mItems.get(0).getVehicle(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getVehicle(0))) {
                    if (mItems.get(0).getVehicle(0).contains("SEDAN")) {
                        Past_car.setText("SEDAN");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sedan));
                    } else if (mItems.get(0).getVehicle(0).contains("PICKUP")) {
                        Past_car.setText("PICK UP");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_muv));
                    } else if (mItems.get(0).getVehicle(0).contains("SUV")) {
                        Past_car.setText("SUV");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv));
                    } else if (mItems.get(0).getVehicle(0).contains("MINI")) {
                        Past_car.setText("MINI");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mini));
                    } else if (mItems.get(0).getVehicle(0).contains("MICRO")) {
                        Past_car.setText("MICRO");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_micro));
                    } else if (mItems.get(0).getVehicle(0).contains("AMBULANCE")) {
                        Past_car.setText("AMBULANCE");
                        PastCar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_amb));
                    }
                }
                if(mItems.get(0).getDrivername(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getDrivername(0))) {
                    Past_deriver_name.setText(mItems.get(0).getDrivername(0));
                }

                if(mItems.get(0).getDriverRate(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getDriverRate(0))) {
                    Past_you_rated.setText("You have rated");
                    Rating.setRating(Float.parseFloat(mItems.get(0).getDriverRate(0)));
                }else{
                    Past_you_rated.setText("You did not give any rating");
                }

                if(mItems.get(0).getStart_time(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getStart_time(0))) {
                    Past_start_time.setText(mItems.get(0).getStart_time(0));
                }
                if(mItems.get(0).getEnd_time(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getEnd_time(0))) {
                    Past_end_time.setText(mItems.get(0).getEnd_time(0));
                }

                if(mItems.get(0).getReview(0)!=null &&
                        !TextUtils.isEmpty(mItems.get(0).getReview(0))) {
                    Past_review.setText("You remarks");
                    Past_review_text.setText(mItems.get(0).getReview(0));
                }else{
                    Past_review.setText("You have not provided any remarks");
                    Past_review_text.setVisibility(View.GONE);
                }
                if (UNIQUEID != null) {
                    String[] pars = UNIQUEID.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_continue());
                }

            }else{

            }
        }
    }

    private class FirebaseDataListener_continue implements ValueEventListener {


        private double COST=0;
        private double Cost=0;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getChildrenCount()!=0) {

                if (dataSnapshot.child("Bill").getValue() != null &&
                        dataSnapshot.child("Tax_names").getValue() != null) {


                        COST=Double.parseDouble((String) dataSnapshot.child("Bill").getValue());
                        DecimalFormat dft=new DecimalFormat("0.00");


                        total_cost.setText("\u20B9 " + (dft.format(COST)));
                    if ((String) dataSnapshot.child("Cost").getValue() != null && (String) dataSnapshot.child("Estimated_fare").getValue() != null) {
                        if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) < Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                            Cost = Double.parseDouble((String) dataSnapshot.child("Cost").getValue());
                        } else if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) > Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                            Cost = Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue());
                        }
                    }
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


                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

            Intent o = new Intent(PastRides.this,Ride_later_tabs.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();

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
    }
