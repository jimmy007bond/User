package com.geekworkx.hellocab.Drawer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 28/02/18.
 */

public class Support extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Support.class.getSimpleName();
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private CircleImageView Car,Driver;
    private TextView Dates,Times,Cost,Pick_another;
    private Button Search;
    private RecyclerView SupportRv;
    private EditText From_,To_;
    private String Vehicle;
    private String Driver_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support);

        progressBar = findViewById(R.id.progressBarSupport);
        Dates=findViewById(R.id.ride_date_support);
        Times=findViewById(R.id.ride_time_support);
        From_=findViewById(R.id.input_from_address_support);
        To_=findViewById(R.id.input_to_address_support);
        Driver=findViewById(R.id.ride_driver_support);
        Car=findViewById(R.id.ride_car_support);
        Cost=findViewById(R.id.ride_cost_support);
        SupportRv=findViewById(R.id.supportRv);
        Search=findViewById(R.id.btn_seq);
        Pick_another=findViewById(R.id.pick_another);
        toolbar = (Toolbar) findViewById(R.id.toolbar_later_support);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Support.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        });

        Pick_another.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_another:

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        new PostBookdata().execute();
    }


    private class PostBookdata  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private String Dates_,Times_,From_1,To_1;
        private String Cost_;

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
                        .addFormDataPart("mobile", _PhoneNo)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.GET_SUPPORT_RIDE)
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
                    Dates_=(user.getString("Date_"));
                    Times_=(user.getString("Time_"));
                    From_1=(user.getString("User_from"));
                    To_1=(user.getString("User_to"));
                    Vehicle=user.getString("Vehicle");
                    Driver_phone=user.getString("drivermobile");
                    Cost_=user.getString("cost");

                }else{
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

        protected void onPostExecute(String file_url) {
            if (success) {
                if (Driver_phone!= null) {
                    if (Vehicle.contains("SEDAN")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sedan));
                    } else if (Vehicle.contains("PICKUP")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_muv));
                    } else if (Vehicle.contains("SUV")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv));
                    } else if (Vehicle.contains("MINI")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mini));
                    } else if (Vehicle.contains("MICRO")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_micro));
                    } else if (Vehicle.contains("AMBULANCE")) {
                        Car.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_amb));
                    }

                    Dates.setText(parseDateToddMMyyyy(Dates_));
                    Times.setText(Times_);
                    From_.setText(From_1);
                    To_.setText(To_1);
                    Cost.setText(Cost_);

                   new GetpastDate().execute();
                }
            } else {

            }

        }
        private class GetpastDate extends AsyncTask<Void, Void, Void> {


            private String DriverImage;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(Config_URL.GET_STOP_BOOKING);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        JSONArray profile = jsonObj.getJSONArray("driver_details");

                        // looping through All Contacts
                        if (_PhoneNo != null) {

                                    for (int j = 0; j < profile.length(); j++) {
                                        JSONObject d = profile.getJSONObject(j);

                                        String relation=d.getString("Driver_phone");
                                        if (relation.matches(Driver_phone)) {
                                                DriverImage=d.getString("Driver_image");
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
                if (DriverImage != null) {
                    Picasso.Builder builder1 = new Picasso.Builder(Support.this);
                    builder1.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder1.build().load(DriverImage).into(Driver);

                }else{

                }
            }
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
}
