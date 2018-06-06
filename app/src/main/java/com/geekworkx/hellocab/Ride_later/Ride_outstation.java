package com.geekworkx.hellocab.Ride_later;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by parag on 06/03/18.
 */

public class Ride_outstation extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = Ride_outstation.class.getSimpleName();
    private PrefManager pref;
    private String _PhoneNo;
    private Toolbar toolbar;
    private EditText From_address;
    private EditText To_address;
    private RadioGroup Rdg;
    private RadioButton Oneway,Returnway;
    private TextView LeaveOn,RetyrnOn,Rtrip;
    private LinearLayout Vmini,Vsedan,Vsuv;
    private double My_lat=0,My_long=0;
    private String From_address_,To_address_;
    private boolean _return=true;
    private String Date_later,Time_later;
    private String Date_add;
    private double Distance;
    private String Unique_ID;
    private String Date_late,Time_late;
    private String Return_date,Return_time;
    private String Date_selected;
    private String Time_selected;
    private String Vehicle;
    private int Mini_minimum,Sedan_minimum,Suv_minimum;
    private LinearLayout L22;
    private TextView Mini_default_price,Sedan_default_price,Suv_default_price;
    private ProgressBar progressBar;
    private TextView Tax_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outstation);
        toolbar=findViewById(R.id.toolbar_later_out);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        From_address=findViewById(R.id.input_from_address_out);
        To_address=findViewById(R.id.input_to_address_out);
        L22=findViewById(R.id.l22);
        progressBar=findViewById(R.id.progressBar3);
        Mini_default_price=findViewById(R.id.mini_minimum);
        Sedan_default_price=findViewById(R.id.sedan_minimum);
        Suv_default_price=findViewById(R.id.suv_minimum);
        Rdg=findViewById(R.id.radioOut);
        Oneway=findViewById(R.id.radioButtonOne);
        Returnway=findViewById(R.id.radioButtonTwo);
        Returnway.setChecked(true);
        LeaveOn=findViewById(R.id.lvon);
        RetyrnOn=findViewById(R.id.rtnon);
        Rtrip=findViewById(R.id.rtrip);

        Vmini=findViewById(R.id.vmini);
        Vsedan=findViewById(R.id.vsedan);
        Vsuv=findViewById(R.id.vsuv);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Ride_outstation.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        });

        LeaveOn.setOnClickListener(this);
        RetyrnOn.setOnClickListener(this);
        Vmini.setOnClickListener(this);
        Vsedan.setOnClickListener(this);
        Vsuv.setOnClickListener(this);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        From_address_=i.getStringExtra("from_address");
        To_address_=i.getStringExtra("to_address");
        Date_late=i.getStringExtra("date_late");
        Time_late=i.getStringExtra("time_late");
        Distance=i.getDoubleExtra("distance",0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        Unique_ID=i.getStringExtra("Unique_ID");
        new GetSettings().execute();
        Rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radioButtonOne:
                        _return=false;
                        Returnway.setChecked(false);
                        Oneway.setChecked(true);
                        L22.setVisibility(View.GONE);
                        LeaveOn.setClickable(false);
                        LeaveOn.setEnabled(false);
                        new GetSettings().execute();
                        break;
                    case R.id.radioButtonTwo:
                        _return=true;
                        Returnway.setChecked(true);
                        Oneway.setChecked(false);
                        L22.setVisibility(View.VISIBLE);
                        LeaveOn.setClickable(true);
                        LeaveOn.setEnabled(true);
                        new GetSettings().execute();
                        break;

                }
            }
        });



    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {

        private ArrayList<Rate> mVehcle_option=new ArrayList<Rate>();
        private ArrayList<String>mTaxes=new ArrayList<String>();
        private double Total_cost_=0;
        private float cost;
        private double Total_Taxes=0;
        private int Outstation_Driver_Allowance_Per_Day=0;

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

                    JSONArray Booking_Later = jsonObj.getJSONArray("Book_Ride_Later");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Master_Vehicle_Type");
                    JSONArray Taxes = jsonObj.getJSONArray("TAXES");
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");

                    int relation=0;

                    for (int i = 0; i < Taxes.length(); i++) {
                        JSONObject c = Taxes.getJSONObject(i);
                        Total_Taxes=Total_Taxes+c.getDouble("Tax_Percentage");
                        mTaxes.add(c.getString("Tax_Name"));
                    }
                    for (int i = 0; i < Settings_default.length(); i++) {
                        JSONObject c = Settings_default.getJSONObject(i);

                        Outstation_Driver_Allowance_Per_Day = c.getInt("Outstation_Driver_Allowance_Per_Day");
                    }
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
                            if(c.getString("Is_Roudtrip").contains("1")){
                                if(c.isNull("Return_date") &&c.isNull("Return_time")) {
                                    Return_date = null;
                                    Return_time = null;
                                }else{
                                    Return_date = c.getString("Return_date");
                                    Return_time = c.getString("Return_time");
                                }
                            }

                        }
                    }
                    for (int i = 0; i < Settings_Vehicle_Rate.length(); i++) {
                        JSONObject c = Settings_Vehicle_Rate.getJSONObject(i);

                        Rate item=new Rate();
                        item.setVehicle_Type(c.getString("Vehicle_Type"));
                        item.setMinimum_Rate(c.getInt("Minimum_Fare"));
                        mVehcle_option.add(item);
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
          if(Date_later!=null) {
              if (From_address_ != null) {
                  From_address.setText(From_address_);
              }
              if (To_address_ != null) {
                  To_address.setText(To_address_);
              }

              SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm");
              SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
              SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
              Calendar cal = Calendar.getInstance();
              Calendar cal1 = Calendar.getInstance();
              Calendar cal2 = Calendar.getInstance();
              try {
                  Date date = format.parse(Date_later+Time_later);
                  if(_return){
                  if(Return_date!=null) {
                      Date date2 = format.parse(Date_later+Time_later);
                      Date date3 = format.parse(Return_date+Return_time);
                      cal1.setTime(date2);
                      cal2.setTime(date3);
                      int diff=(int)  (date3.getTime() - date2.getTime())/(24*60*60*1000);


                      if (diff == 1) {
                          Rtrip.setText("24-hour round trip of about " + Math.round(Distance * 2 )+ "Km");
                            }else if(diff==0){
                          Rtrip.setText(Math.round((int)  (date3.getTime() - date2.getTime())/(60*60*1000))+"-hour round trip of about " + Math.round(Distance * 2 )+ "Km");
                      }else{
                          Rtrip.setText(Math.round(diff)+"-days round trip of about " + Math.round(Distance * 2 )+ "Km");
                      }
                  }else{
                      Rtrip.setText("24-hour round trip of about " + Math.round(Distance * 2 )+ "Km");
                  }
                  }else {
                      Rtrip.setText("one way trip of about " + Math.round(Distance )+ "Km");

                  }
                  cal.setTime(date);

                  if (date != null) {
                      LeaveOn.setText(dateFormat.format(cal.getTime()) + "," + sdf.format(cal.getTime()));
                  }
                  if (Return_date != null) {
                      cal.setTime(format.parse(Return_date+Return_time));
                  } else {
                      cal.add(Calendar.DATE, 1);
                  }

                  Date_add = String.valueOf(dateFormat.format(cal.getTime()));
                  RetyrnOn.setText(Date_add + "," + sdf.format(cal.getTime()));
                  Date_selected = format2.format(cal.getTime());
                  Time_selected = Time_later;

                  if(mVehcle_option.size()!=0){
                      for(int i=0;i<mVehcle_option.size();i++){
                          if(mVehcle_option.get(i).getVehicle_Type(i).contains("MINI")){
                              Vmini.setVisibility(View.VISIBLE);
                              if(!_return){
                                  Total_cost_ = Math.round((Distance * mVehcle_option.get(i).getMinimum_Rate(i)) + (Outstation_Driver_Allowance_Per_Day) );
                                  cost =Math.round(Total_cost_+(Total_cost_*(Total_Taxes/100)));
                                  Mini_default_price.setText("\u20B9" + String.valueOf(cost) );
                              }else {
                                  Mini_minimum=mVehcle_option.get(i).getMinimum_Rate(i);
                                  Mini_default_price.setText("\u20B9" + mVehcle_option.get(i).getMinimum_Rate(i) + "/km");
                              }
                          }
                          if(mVehcle_option.get(i).getVehicle_Type(i).contains("SEDAN")){
                              Vsedan.setVisibility(View.VISIBLE);
                              if(!_return){
                                  Total_cost_ = Math.round((Distance * mVehcle_option.get(i).getMinimum_Rate(i)) + (Outstation_Driver_Allowance_Per_Day) );
                                  cost =Math.round(Total_cost_+(Total_cost_*(Total_Taxes/100)));
                                  Sedan_default_price.setText("\u20B9" + String.valueOf(cost) );
                              }else {
                                  Sedan_minimum = mVehcle_option.get(i).getMinimum_Rate(i);
                                  Sedan_default_price.setText("\u20B9" + mVehcle_option.get(i).getMinimum_Rate(i) + "/km");
                              }
                          }
                          if(mVehcle_option.get(i).getVehicle_Type(i).contains("SUV")){
                              Vsuv.setVisibility(View.VISIBLE);
                              if(!_return){
                                  Total_cost_ = Math.round((Distance * mVehcle_option.get(i).getMinimum_Rate(i)) + (Outstation_Driver_Allowance_Per_Day) );
                                  cost =Math.round(Total_cost_+(Total_cost_*(Total_Taxes/100)));
                                  Suv_default_price.setText("\u20B9" + String.valueOf(cost) );
                              }else {
                                  Suv_minimum = mVehcle_option.get(i).getMinimum_Rate(i);
                                  Suv_default_price.setText("\u20B9" + mVehcle_option.get(i).getMinimum_Rate(i) + "/km");
                              }
                          }
                      }
                  }

              } catch (ParseException e) {
                  e.printStackTrace();
              }
          }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lvon:
                Intent o = new Intent(Ride_outstation.this, Return_by.class);
                o.putExtra("Unique_ID",Unique_ID);
                o.putExtra("pager",0);
                startActivity(o);
                break;

            case R.id.rtnon:
                Intent o1 = new Intent(Ride_outstation.this, Return_by.class);
                o1.putExtra("Unique_ID",Unique_ID);
                o1.putExtra("pager",1);
                startActivity(o1);
                break;
            case R.id.vmini:
                 Vehicle="MINI";
                 if(Date_selected!=null && Time_selected!=null) {
                     new PostFavourite().execute(Date_selected, Time_selected);
                 }
                break;
            case R.id.vsedan:
                Vehicle="SEDAN";
                if(Date_selected!=null && Time_selected!=null) {
                    new PostFavourite().execute(Date_selected, Time_selected);
                }
                break;
            case R.id.vsuv:
                Vehicle="SUV";
                if(Date_selected!=null && Time_selected!=null) {
                    new PostFavourite().execute(Date_selected, Time_selected);
                }
                break;
            default:
                break;
        }
    }

    private class PostFavourite extends AsyncTask<String, Integer, String> {
        private boolean success;


        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... strings) {
            // Some long-running task like downloading an image.
            String date_=strings[0];
            String time_=strings[1];
            return uploadFile(date_,time_);

        }
        private String uploadFile(String date_, String time_) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("time",time_)
                        .addFormDataPart("day", date_)
                        .addFormDataPart("otp", Unique_ID)
                        .addFormDataPart("return", String.valueOf(true))
                        .addFormDataPart("pager", String.valueOf(true))
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
                    //OTP= Integer.parseInt(user.getString("OTP"));

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
                progressBar.setVisibility(View.GONE);
                    Intent o2 = new Intent(Ride_outstation.this, Confirm_later_booking.class);
                    o2.putExtra("Unique_ID", Unique_ID);
                    o2.putExtra("distance", Distance);
                    o2.putExtra("vehicle", Vehicle);
                    o2.putExtra("my_lat", My_lat);
                    o2.putExtra("my_long", My_long);
                    o2.putExtra("round_trip", _return);
                    startActivity(o2);

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
