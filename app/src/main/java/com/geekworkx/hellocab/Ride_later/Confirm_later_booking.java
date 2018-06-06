package com.geekworkx.hellocab.Ride_later;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.Rate;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 07/03/18.
 */

public class Confirm_later_booking extends AppCompatActivity {

    private Toolbar toolbar;
    private PrefManager pref;
    private String _PhoneNo;
    private TextView Leaveon,RetyrnOn;
    private EditText From_address,To_address;
    private TextView Rtrip,Total_cost;
    private Button Fare_details;
    private TextView Driver_days_cost,Driver_cost,Total_km_cost,Total_km,Total_fare,Taxes,Fare_cancellation;
    private LinearLayout Fare_hide;
    private Button Personal,Coupon,Confirm;
    private double My_lat=0,My_long=0;
    private String Unique_ID;
    private double Distance=0;
    private String From_address_,To_address_,Time_late,Date_late;
    private ProgressBar progressBar;
    private float cost;
    private String Vehicle;
    private TextView Partners,Wifi;
    private TextView Seater;
    private ImageView Car_image;
    private TextView Car;
    private TextView Car_cost;
    private String Where;
    private int Outstation_Driver_Allowance_Per_Day=0;
    private ArrayList<String>mTaxes=new ArrayList<String>();
    private double Total_Taxes=0;
    private TextView Tax_name;
    private double To_lat=0,To_long=0;
    private boolean _return=false;
    private LinearLayout L22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.later_car);
        toolbar = findViewById(R.id.toolbar_later_car);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        L22=findViewById(R.id.l22);
        Leaveon=findViewById(R.id.lvon);
        RetyrnOn=findViewById(R.id.rtnon);
        From_address=findViewById(R.id.input_from_address_out);
        To_address=findViewById(R.id.input_to_address_out);
        Rtrip=findViewById(R.id.text_days);
        Total_cost=findViewById(R.id.total_cost);
        Fare_details=findViewById(R.id.fare_details);
        Driver_days_cost=findViewById(R.id.total_days);
        Driver_cost=findViewById(R.id.total_days_driver);
        Total_km_cost=findViewById(R.id.total_km);
        Total_km=findViewById(R.id.total_km_cost);
        Taxes=findViewById(R.id.total_taxes);
        Total_fare=findViewById(R.id.total_fare);
        Fare_hide=findViewById(R.id.price_details);
        Fare_cancellation=findViewById(R.id.fare_cancellation);
        Personal=findViewById(R.id.personal);
        Coupon=findViewById(R.id.coupon_add);
        Confirm=findViewById(R.id.confirm_ri);
        Partners=findViewById(R.id.partners);
        Car=findViewById(R.id.car_icon);
        Wifi=findViewById(R.id.wifi);
        Seater=findViewById(R.id.seater);
        Car_image=findViewById(R.id.car_image);
        Car_cost=findViewById(R.id.car_cost);
        progressBar=findViewById(R.id.progressout);
        Tax_name=findViewById(R.id.tax_name);

        Intent i = getIntent();
        Unique_ID=i.getStringExtra("Unique_ID");
        Distance=i.getDoubleExtra("distance",0);
        Vehicle=i.getStringExtra("vehicle");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        Where=i.getStringExtra("where");
        _return=i.getBooleanExtra("round_trip",false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Where==null) {
                    Intent i = new Intent(Confirm_later_booking.this, Ride_outstation.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent o = new Intent(Confirm_later_booking.this, Ride_later_tabs.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    startActivity(o);
                    finish();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Where!=null){
            Confirm.setVisibility(View.GONE);
        }
        new GetSettings().execute();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostFavourite().execute();
            }
        });
    }

    private class PostFavourite extends AsyncTask<Void, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
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
                        .addFormDataPart("otp",Unique_ID)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("vehicle",Vehicle)
                        .addFormDataPart("cost", String.valueOf(cost))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_LATER_OUTSTATION)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


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

            progressBar.setVisibility(ProgressBar.GONE);

            if(success){
                    Intent o = new Intent(Confirm_later_booking.this, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
            }
        }
    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private static final String TAG = "Simple";
        private String Date_later,Time_later,Return_date,Return_time,From,To;
        private ArrayList<Rate> mVehcle_option=new ArrayList<Rate>();
        private float Total_cost_=0;
        private Date date1;
        private Date date;
        private int diff=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaxes.clear();
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
                    JSONArray Booking_Later = jsonObj.getJSONArray("Book_Ride_Later");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");
                    JSONArray Taxes = jsonObj.getJSONArray("TAXES");
                    JSONArray gari = jsonObj.getJSONArray("Vehicle_detail");

                    int relation=0;
                    for (int i = 0; i < User.length(); i++) {
                        JSONObject d = User.getJSONObject(i);
                        if (_PhoneNo.contains(d.getString("Phone_No"))) {
                            relation = d.getInt("ID");
                        }
                    }


                    for (int i = 0; i < Booking_Later.length(); i++) {
                        JSONObject c = Booking_Later.getJSONObject(i);


                        if (relation== c.getInt("User_ID") && c.getString("Unique_Ride_Code").contains(Unique_ID)) {
                            Date_later = c.getString("Start_Date");
                            Time_later = c.getString("Start_time");
                            From=c.getString("From_Address");
                            To=c.getString("To_Address");

                            My_lat=c.getDouble("From_Latitude");
                            My_long=c.getDouble("From_Longitude");
                            To_lat=c.getDouble("To_Latitude");
                            To_long=c.getDouble("To_Longitude");

                                if(c.isNull("Return_date") &&c.isNull("Return_time")) {
                                    Return_date = null;
                                    Return_time = null;
                                }else{
                                    Return_date = c.getString("Return_date");
                                    Return_time = c.getString("Return_time");
                                }
                                if(Where!=null) {
                                    for (int j = 0; j < Settings_Vehicle_Rate.length(); j++) {
                                        JSONObject d = Settings_Vehicle_Rate.getJSONObject(j);

                                        if (!c.isNull("Vehicle_ID") && d.getInt("ID") == c.getInt("Vehicle_ID")) {
                                            Vehicle = (d.getString("Vehicle_Type"));
                                            break;
                                        }
                                    }
                                }

                                if(Where!=null){
                                    Distance=c.getDouble("Distance_Travel");
                                }


                        }

                    }



                    for (int i = 0; i < Settings_Vehicle_Rate.length(); i++) {
                        JSONObject c = Settings_Vehicle_Rate.getJSONObject(i);
                        if(c.getString("Vehicle_Type").contains(Vehicle)) {
                            Rate item = new Rate();
                            item.setVehicle_Type(c.getString("Vehicle_Type"));
                            item.setHourly_Rate(c.getInt("Hourly_Rate"));
                            item.setMinimum_Rate(c.getInt("Minimum_Rate"));
                            mVehcle_option.add(item);
                        }
                    }

                    for (int i = 0; i < Taxes.length(); i++) {
                        JSONObject c = Taxes.getJSONObject(i);
                        Total_Taxes=Total_Taxes+c.getDouble("Tax_Percentage");
                        mTaxes.add(c.getString("Tax_Name"));
                    }
                    for (int i = 0; i < Settings_default.length(); i++) {
                        JSONObject c = Settings_default.getJSONObject(i);

                        Outstation_Driver_Allowance_Per_Day = c.getInt("Outstation_Driver_Allowance_Per_Day");
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
            if(From!=null){
                From_address.setText(From);
            }
            if(To!=null){
                To_address.setText(To);
            }

            if(mTaxes.size()!=0){
                Tax_name.setText(TextUtils.join(" + ", mTaxes));

            }

            Calendar cal = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            try {
                if(Date_later!=null) {
                     date = format.parse(Date_later + Time_later);

                    cal.setTime(date);
                }
                if(Return_date!=null) {
                    date1 = format.parse(Return_date+Return_time);
                    cal1.setTime(date1);
                     diff =(int) (date1.getTime() - date.getTime())/(24*60*60*1000);
                }

                if(_return) {
                    if (Distance != 0) {
                        if (diff == 1) {
                            Rtrip.setText("24-hour round trip of about " + Math.round(Distance * 2) + "Km");
                        } else if (diff == 0) {
                            Rtrip.setText(Math.round((int) (date1.getTime() - date.getTime()) / (24 * 60 * 60 * 1000)) + "-hour round trip of about " + Math.round(Distance * 2) + "Km");
                        } else {
                            Rtrip.setText(Math.round(diff) + "-days round trip of about " + Math.round(Distance * 2) + "Km");
                        }
                    } else {

                    }
                }else{
                    Rtrip.setText("One way trip of about " + Math.round(Distance ) + "Km");
                }
                if(Vehicle.contains("MINI")) {
                    Partners.setVisibility(View.VISIBLE);
                    Wifi.setVisibility(View.VISIBLE);
                    Seater.setText("4 Seater-Indica,Micra,Ritz");
                    Car_image.setBackgroundResource(R.mipmap.ic_mini);
                    Car.setText("MINI");
                    Car_cost.setText("\u20B9"+mVehcle_option.get(0).getMinimum_Rate(0)+"/km");
                    if(_return) {
                        Total_cost_ = Math.round((2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day) * diff);
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(2 * Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)));
                    }else{
                        Total_cost_ = Math.round((Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day));
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(Distance * mVehcle_option.get(0).getMinimum_Rate(0)));

                    }
                }
                    else if(Vehicle.contains("SUV")) {
                    Partners.setVisibility(View.VISIBLE);
                    Wifi.setVisibility(View.VISIBLE);
                    Seater.setText("4 Seater-SUMO,SAFARI,BOLERO");
                    Car_image.setBackgroundResource(R.mipmap.ic_suv);
                    Car.setText("SUV");
                    Car_cost.setText("\u20B9"+mVehcle_option.get(0).getMinimum_Rate(0)+"/km");
                    if(_return) {
                        Total_cost_ = Math.round((2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day) * diff);
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(2 * Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)));
                    }else{
                        Total_cost_ = Math.round((Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day));
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(Distance * mVehcle_option.get(0).getMinimum_Rate(0)));

                    }  }
                else if(Vehicle.contains("SEDAN")) {
                    Partners.setVisibility(View.VISIBLE);
                    Wifi.setVisibility(View.VISIBLE);
                    Seater.setText("4 Seater-Dzire,Etios,Sunny");
                    Car_image.setBackgroundResource(R.mipmap.ic_sedan);
                    Car.setText("SEDAN");
                    Car_cost.setText("\u20B9"+mVehcle_option.get(0).getMinimum_Rate(0)+"/km");
                    if(_return) {
                        Total_cost_ = Math.round((2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day) * diff);
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(2 * Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(2 * Distance * mVehcle_option.get(0).getMinimum_Rate(0)));
                    }else{
                        Total_cost_ = Math.round((Distance * mVehcle_option.get(0).getMinimum_Rate(0)) + (Outstation_Driver_Allowance_Per_Day));
                        cost = Math.round(Total_cost_ + (Total_cost_ * (Total_Taxes / 100)));
                        Total_km_cost.setText(Distance + "x" + "\u20B9" + mVehcle_option.get(0).getMinimum_Rate(0) + "/Km");
                        Total_km.setText("\u20B9" + Math.round(Distance * mVehcle_option.get(0).getMinimum_Rate(0)));

                    } }
                if(_return) {
                    if(Date_later!=null) {
                        Leaveon.setText(dateFormat.format(cal.getTime())+","+sdf.format(cal.getTime()));
                    }
                    if(Return_date!=null) {
                        RetyrnOn.setText(dateFormat.format(cal1.getTime())+","+sdf.format(cal1.getTime()));
                    }
                }else{
                    if(Date_later!=null) {
                        Leaveon.setText(dateFormat.format(cal.getTime())+","+sdf.format(cal.getTime()));
                    }
                    L22.setVisibility(View.GONE);
                }



                if(_return) {
                    Driver_days_cost.setText("\u20B9" + Outstation_Driver_Allowance_Per_Day + "x" + diff + "days");
                    Driver_cost.setText("\u20B9" + Math.round(diff * Outstation_Driver_Allowance_Per_Day));
                }else{
                    Driver_days_cost.setText("\u20B9" + Outstation_Driver_Allowance_Per_Day + "x" + "1" + "day");
                    Driver_cost.setText("\u20B9" + Math.round( Outstation_Driver_Allowance_Per_Day));

                }
                Taxes.setText("\u20B9"+Math.round(Total_cost_*(Total_Taxes/100)));
                Total_cost.setText("\u20B9"+Math.round(cost));
                Total_fare.setText("\u20B9"+Math.round(cost));
                Fare_cancellation.setText("Free cancellation till "+ dateFormat.format(cal.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_support, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_support:
                recreate();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Where==null) {
            Intent i = new Intent(Confirm_later_booking.this, Ride_outstation.class);
            startActivity(i);
            finish();
        }else{
            Intent o = new Intent(Confirm_later_booking.this, Ride_later_tabs.class);
            o.putExtra("mylat", My_lat);
            o.putExtra("mylong", My_long);
            startActivity(o);
            finish();

        }
    }
}
