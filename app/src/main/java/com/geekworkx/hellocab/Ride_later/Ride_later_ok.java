package com.geekworkx.hellocab.Ride_later;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Adapters.HeightWrappingViewPager;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.Elite;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.uniquestudio.library.CircleCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by parag on 19/01/18.
 */

public class Ride_later_ok extends AppCompatActivity
        implements  View.OnClickListener {
    private static final String TAG = Ride_later_ok.class.getSimpleName();
    private TimePicker time_choose;
    private CalendarView date_choose;
    private String time,date;
    private Button Cancel,Save;
    private PrefManager pref;
    private String _PhoneNo;
    private ConnectionDetector cd;
    private boolean isInternetPresent=false;
    private double My_lat=0,My_long=0;
    private HeightWrappingViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button Single,Multiple;
    private TimePicker time_multiple;
    private CircleCheckBox Mon,Tue,Wed,Thr,Fri,Sat,Sun,Scheme_1,Scheme_2,Scheme_3;
    private String Repeat_days;
    private TextView Txt20;
    private HorizontalScrollView h2;
    private String Repeat_day_1="";
    private String flag;
    private Button btnTime,btnDate,btnMul;
    private Calendar firstLimit,secondLimit;
    private SimpleDateFormat sdf;
    private Calendar c;
    private int dayOfWeek;
    private CalendarView date_multiple_start,date_multiple_end;
    private String Start_day,End_day;
    List<String> Monday = new ArrayList<String>();
    List<String> Tuesday = new ArrayList<String>();
    List<String> Wednesday = new ArrayList<String>();
    List<String> Thursday = new ArrayList<String>();
    List<String> Friday = new ArrayList<String>();
    List<String> Saturday = new ArrayList<String>();
    List<String> Sunday = new ArrayList<String>();
    List<String> TotalDays = new ArrayList<String>();
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private Date Start_date,End_date;
    private Calendar scal,ecal;
    private Button SDate,EDate;
    private String Unique_ID;
    private String from;
    private String time_multi;
    private String mobileIp;
    private String commaSeparatedValues;
    private ArrayList<String>myLaterRides=new ArrayList<String>();
    private int Single_day=0;
    private String Date_;
    private int OTPq=0;
    private TextView Scheme_time_1,Scheme_time_2,Scheme_time_3,Scheme_price_1,Scheme_price_2,Scheme_price_3,
            Scheme_name_1,Scheme_name_2,Scheme_name_3;
    private LinearLayout Plane_1,Plane_2,Plane_3;
    private String Desc1,Desc2,Desc3;
    private int _plan=0;
    private String _drop;
    private String _pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_later);
        time_choose=findViewById(R.id.timePicker_later);
        time_multiple=findViewById(R.id.timePicker_mul);
        date_choose=findViewById(R.id.date_picker_later);
        date_multiple_start=findViewById(R.id.date_multiple_start);
        date_multiple_end=findViewById(R.id.date_multiple_end);
        viewPager=findViewById(R.id.viewPagerLater);
        SDate=findViewById(R.id.btn_multiple_start);
        EDate=findViewById(R.id.btn_multiple_end);
        SDate.setOnClickListener(this);
        EDate.setOnClickListener(this);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        cd = new ConnectionDetector(getApplicationContext());
        Scheme_1= findViewById(R.id.month_check);
        Scheme_2=  findViewById(R.id.month_check_1);
        Scheme_3=  findViewById(R.id.month_check_2);
        Scheme_time_1= findViewById(R.id.month_time);
        Scheme_time_2=  findViewById(R.id.month_time_1);
        Scheme_time_3=  findViewById(R.id.month_time_2);
        Scheme_price_1= findViewById(R.id.month_price);
        Scheme_price_2=  findViewById(R.id.month_price_1);
        Scheme_price_3=  findViewById(R.id.month_price_2);
        Scheme_name_1=findViewById(R.id.month_plan);
        Scheme_name_2=findViewById(R.id.month_plan_1);
        Scheme_name_3=findViewById(R.id.month_plan_2);
        Plane_1=findViewById(R.id.plane1);
        Plane_2=findViewById(R.id.plane2);
        Plane_3=findViewById(R.id.plane3);
        Mon= findViewById(R.id.radioMon);
        Tue=  findViewById(R.id.radioTue);
        Wed=  findViewById(R.id.radioWed);
        Thr=  findViewById(R.id.radioThr);
        Fri=  findViewById(R.id.radioFri);
        Sat=  findViewById(R.id.radioSat);
        Sun=  findViewById(R.id.radioSun);

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        from=i.getStringExtra("from");
        OTPq=i.getIntExtra("otp",0);
        Date_=i.getStringExtra("date");
        _drop=i.getStringExtra("drop");
        _pick=i.getStringExtra("pick");
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);


        btnTime =  findViewById(R.id.btn_time);
        btnDate =  findViewById(R.id.btn_date);
        btnMul= findViewById(R.id.btn_mul);
        Single=findViewById(R.id.buttono);
        Multiple=findViewById(R.id.buttond);

        Single.setOnClickListener(this);
        Multiple.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnTime.setOnClickListener(this);


        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Monday.clear();
        Tuesday.clear();
        Wednesday.clear();
        Thursday.clear();
        Friday.clear();
        Saturday.clear();
        Sunday.clear();
        TotalDays.clear();
        if(!TextUtils.isEmpty(Repeat_day_1)){
            TotalDays.add(Repeat_day_1);
            viewPager.setCurrentItem(2);
        }

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
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            scal = Calendar.getInstance();
            ecal = Calendar.getInstance();
            if(from!=null){
                viewPager.setCurrentItem(2);
            }
            if(_drop!=null){
                viewPager.setCurrentItem(1,true);
            }
            time_choose.setIs24HourView(true);
            time_choose.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                if(minute<10) {
                    time = hourOfDay + ":0" + minute;
                }else {
                    time = hourOfDay + ":" + minute;
                }
            }
        });
            time_multiple.setIs24HourView(true);
            time_multiple.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    // display a toast with changed values of time picker
                    if(minute<10) {
                        time_multi = hourOfDay + ":0" + minute;
                    }else {
                        time_multi = hourOfDay + ":" + minute;
                    }
                }
            });

        date_choose.setFirstDayOfWeek(Calendar.SUNDAY);
            date_choose.setMinDate(System.currentTimeMillis() - 1000);

            date_choose.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                TotalDays.clear();
                Repeat_day_1="";
                if(month<10){
                    Repeat_day_1=year+"-"+"0"+(month+1)+"-"+dayOfMonth;
                }else {
                    Repeat_day_1 = year + "-" + (month + 1) + "-" + dayOfMonth;
                }
                TotalDays.add(Repeat_day_1);

            }
        });

            date_multiple_start.setFirstDayOfWeek(Calendar.SUNDAY);
            date_multiple_start.setMinDate(System.currentTimeMillis());
            date_multiple_start.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                    if(month<10){
                        Start_day=year+"-"+"0"+(month+1)+"-"+dayOfMonth;
                    }else {
                        Start_day = year + "-" + (month + 1) + "-" + dayOfMonth;
                    }


                        firstLimit = Calendar.getInstance();
                     Calendar thirdLimit = Calendar.getInstance();

                    try {
                        firstLimit.setTime(dateFormat.parse(Start_day));
                        thirdLimit.setTime(dateFormat.parse(Start_day));
                        thirdLimit.add(Calendar.MONTH,_plan);
                        secondLimit = Calendar.getInstance();
                        secondLimit.setTime(thirdLimit.getTime());
                        ecal.setTime(secondLimit.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    scal.setTime(firstLimit.getTime());


                }
            });

            date_multiple_end.setFirstDayOfWeek(Calendar.SUNDAY);
            date_multiple_end.setMinDate(System.currentTimeMillis());
            date_multiple_end.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                    End_day=year+"-"+(month+1)+"-"+dayOfMonth;

                        secondLimit = Calendar.getInstance();
                    try {
                        secondLimit.setTime(dateFormat.parse(End_day));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ecal.setTime(secondLimit.getTime());

                }
            });
            Scheme_1.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        _plan=3;
                        viewPager.setCurrentItem(4);
                    }
                }
            });
            Scheme_2.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        _plan=6;
                        viewPager.setCurrentItem(4);
                    }
                }
            });
            Scheme_3.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        _plan=12;
                        viewPager.setCurrentItem(4);
                    }
                }
            });

               Mon.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
                                Monday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());

                    }
                }
            });
            Tue.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
                                Tuesday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
                }
            });
            Wed.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){
                                Wednesday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
                }
            });
            Thr.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
                                Thursday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
                }
            });
            Fri.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
                                Friday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
                }
            });
            Sat.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
                                Saturday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
                }
            });
            Sun.setListener(new CircleCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isChecked) {
                    if (isChecked) {
                        while(!firstLimit.equals(secondLimit)){
                            firstLimit.add(Calendar.DATE, 1);
                            if(firstLimit.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                                Sunday.add(dateFormat.format(firstLimit.getTime()));
                            }

                        }
                        firstLimit.setTime(scal.getTime());
                    }
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

        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            snackbar.show();
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttono:
                 viewPager.setCurrentItem(1,true);
                break;
            case R.id.buttond:
                new GetPlans().execute();
                break;
            case R.id.btn_mul:
             if(Monday.size()!=0||Tuesday.size()!=0||Wednesday.size()!=0||
                Thursday.size()!=0||Friday.size()!=0||Saturday.size()!=0||Sunday.size()!=0){
                 TotalDays.addAll(Monday);
                 TotalDays.addAll(Tuesday);
                 TotalDays.addAll(Wednesday);
                 TotalDays.addAll(Thursday);
                 TotalDays.addAll(Friday);
                 TotalDays.addAll(Saturday);
                 TotalDays.addAll(Sunday);
                 if(time_multi!=null) {
                     Single_day=2;
                     new GetMultiple().execute();
                 }else{
                     Toast.makeText(getApplicationContext(),"Choose time please",Toast.LENGTH_SHORT).show();
                     viewPager.setCurrentItem(6);
                 }
             }
                break;

            case R.id.btn_time:
                if(time==null){
                    Toast.makeText(getApplicationContext(),"Choose time please",Toast.LENGTH_SHORT).show();

                }else{
                    if(from==null) {
                        commaSeparatedValues = TotalDays.get(0);
                        Intent o = new Intent(Ride_later_ok.this, Ride_later_address.class);
                        o.putExtra("mylat", My_lat);
                        o.putExtra("mylong", My_long);
                        o.putExtra("time", time);
                        o.putExtra("Days", commaSeparatedValues);
                        o.putExtra("Unique_ID", Unique_ID);
                        o.putExtra("multiple", false);
                        o.putExtra("drop", _drop);
                        o.putExtra("pick", _pick);
                        startActivity(o);
                        finish();
                    }else{
                        new PostTime().execute();
                    }
                }
                break;

            case R.id.btn_date:
                if( TotalDays.size()==0){
                    Toast.makeText(getApplicationContext(),"Choose date please ",Toast.LENGTH_SHORT).show();

                }else{
                        Single_day=1;
                        new GetSettings().execute();

                }
                break;


            case R.id.btn_multiple_start:
                viewPager.setCurrentItem(6);
                break;
            /*case R.id.btn_multiple_end:
                viewPager.setCurrentItem(6);
                break;*/

        }
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 7;
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
                    resId = R.id.f_pager;
                    break;
                case 1:
                    resId = R.id.t_pager;
                    break;
                case 2:
                    resId = R.id.s_pager;
                    break;
                case 3:
                    resId = R.id.subscripe;
                    break;
                case 4:
                    resId = R.id.start_date;
                    break;
                case 5:
                    resId = R.id.end_date;
                    break;
                case 6:
                    resId = R.id.f4_pager;
                    break;




            }
            return findViewById(resId);
        }
    }


    private class PostTime extends AsyncTask<Void, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            super.onPreExecute();
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
                        .addFormDataPart("otp", String.valueOf(OTPq))
                        .addFormDataPart("date", String.valueOf(Date_))
                        .addFormDataPart("time", time+":00")
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_LATER_CHANGE_TIME)
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

            if(success){
                Intent o = new Intent(Ride_later_ok.this, GooglemapApp.class);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                finish();
            }
        }
    }
    private class GetPlans extends AsyncTask<Void, Void, Void> {


        private boolean repeat=false;
        private ArrayList<Elite> mElite=new ArrayList<Elite>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

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


                    JSONArray ELITE = jsonObj.getJSONArray("hellocab_elite_plans");

                    if (_PhoneNo != null) {


                        for (int i = 0; i < 3; i++) {

                            JSONObject d = ELITE.getJSONObject(i);
                            Elite item=new Elite();
                            item.setPlane_Name(d.getString("Plan_Name"));
                            item.setAmount(d.getInt("Amount"));
                            item.setValidity_in_Days(d.getInt("Validity_in_Days"));
                            item.setDescription(d.getString("Description"));
                            mElite.add(item);
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

            if(mElite.size()!=0){
                if(mElite.size()>0) {
                    Plane_1.setVisibility(View.VISIBLE);
                    Scheme_price_1.setText("\u20B9 " + String.valueOf(mElite.get(0).getAmount(0)));
                    Scheme_time_1.setText(String.valueOf(Math.round(mElite.get(0).getValidity_in_Days(0) / 30))+" Months");
                    Scheme_name_1.setText(mElite.get(0).getPlane_Name(0));
                    Desc1=mElite.get(0).getDescription(0);
                }
                if(mElite.size()>1) {
                    Plane_2.setVisibility(View.VISIBLE);
                    Scheme_price_2.setText("\u20B9 " + String.valueOf(mElite.get(1).getAmount(1)));
                    Scheme_time_2.setText(String.valueOf(Math.round(mElite.get(1).getValidity_in_Days(1) / 30))+" Months");
                    Scheme_name_2.setText(mElite.get(1).getPlane_Name(1));
                    Desc2=mElite.get(1).getDescription(1);
                }
                if(mElite.size()>2) {
                    Plane_3.setVisibility(View.VISIBLE);
                    Scheme_price_3.setText("\u20B9 " + String.valueOf(mElite.get(2).getAmount(2)));
                    Scheme_time_3.setText(String.valueOf(Math.round(mElite.get(2).getValidity_in_Days(2) / 30))+" Months");
                    Scheme_name_3.setText(mElite.get(2).getPlane_Name(2));
                    Desc3=mElite.get(2).getDescription(2);
                }

            }else{
                Plane_1.setVisibility(View.GONE);
                Plane_2.setVisibility(View.GONE);
                Plane_3.setVisibility(View.GONE);
            }

            viewPager.setCurrentItem(3,true);


        }
    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private boolean repeat=false;
        private ArrayList<Elite> mElite=new ArrayList<Elite>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

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
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray ELITE = jsonObj.getJSONArray("hellocab_elite_plans");
                    JSONArray Booking_Later = jsonObj.getJSONArray("Book_Ride_Later");

                    // looping through All Contacts
                    if (_PhoneNo != null) {

                        int relation=0;
                        for (int i = 0; i < User.length(); i++) {

                            JSONObject d = User.getJSONObject(i);
                            if (_PhoneNo.contains(d.getString("Phone_No"))) {
                                relation = d.getInt("ID");
                            }
                        }

                        for (int i = 0; i < 3; i++) {

                            JSONObject d = ELITE.getJSONObject(i);
                            Elite item=new Elite();
                            item.setPlane_Name(d.getString("Plane_Name"));
                            item.setAmount(d.getInt("Amount"));
                            item.setValidity_in_Days(d.getInt("Validity_in_Day"));
                            item.setDescription(d.getString("Description"));
                            mElite.add(item);
                        }



                        for (int i = 0; i < Booking_Later.length(); i++) {
                            JSONObject c = Booking_Later.getJSONObject(i);
                            if(relation==c.getInt("User_ID"))
                            myLaterRides.add(c.getString("Start_Date"));
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

            if(mElite.size()!=0){
                if(mElite.size()>0) {
                    Plane_1.setVisibility(View.VISIBLE);
                    Scheme_price_1.setText("\\u20B9 " + mElite.get(0).getAmount(0));
                    Scheme_time_1.setText(Math.round(mElite.get(0).getValidity_in_Days(0) / 30));
                    Scheme_name_1.setText(mElite.get(0).getPlane_Name(0));
                }
                if(mElite.size()>1) {
                    Plane_2.setVisibility(View.VISIBLE);
                    Scheme_price_2.setText("\\u20B9 " + mElite.get(1).getAmount(1));
                    Scheme_time_2.setText(Math.round(mElite.get(1).getValidity_in_Days(1) / 30));
                    Scheme_name_2.setText(mElite.get(1).getPlane_Name(1));
                }
                if(mElite.size()>2) {
                    Plane_3.setVisibility(View.VISIBLE);
                    Scheme_price_3.setText("\\u20B9 " + mElite.get(2).getAmount(2));
                    Scheme_time_3.setText(Math.round(mElite.get(2).getValidity_in_Days(2) / 30));
                    Scheme_name_3.setText(mElite.get(3).getPlane_Name(3));
                }

            }else{
                Plane_1.setVisibility(View.GONE);
                Plane_2.setVisibility(View.GONE);
                Plane_3.setVisibility(View.GONE);
            }

            if(Single_day==1){
                if(myLaterRides.size()!=0){
                    for(int i=0;i<myLaterRides.size();i++){
                        if(myLaterRides.get(i).matches(TotalDays.get(0))){
                            repeat=true;
                            break;
                        }
                    }
                }else{
                    repeat=false;

                }

                if(!repeat){
                    viewPager.setCurrentItem(2, true);
                }else{

                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView().getRootView(), "Already ride scheduled on this day!", Snackbar.LENGTH_LONG)
                            .setAction("Check", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent o = new Intent(Ride_later_ok.this, Ride_later_tabs.class);
                                    o.putExtra("mylat", My_lat);
                                    o.putExtra("mylong", My_long);
                                    o.putExtra("pager",1);
                                    startActivity(o);
                                    finish();
                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();

                }
            }
        }
    }

    private class GetMultiple extends AsyncTask<Void, Void, Void> {


        private boolean repeat=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

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
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Booking_Later = jsonObj.getJSONArray("Book_Ride_Later");

                    // looping through All Contacts
                    if (_PhoneNo != null) {

                        int relation=0;
                        for (int i = 0; i < User.length(); i++) {

                            JSONObject d = User.getJSONObject(i);
                            if (_PhoneNo.contains(d.getString("Phone_No"))) {
                                relation = d.getInt("ID");
                            }
                        }


                        for (int i = 0; i < Booking_Later.length(); i++) {
                            JSONObject c = Booking_Later.getJSONObject(i);
                            if(relation==c.getInt("User_ID"))
                                myLaterRides.add(c.getString("Start_Date"));
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
              if(Single_day==2) {
                commaSeparatedValues = TextUtils.join(",", TotalDays);
                Intent o = new Intent(Ride_later_ok.this, Ride_later_address.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                o.putExtra("time", time_multi);
                o.putExtra("Days", commaSeparatedValues);
                o.putExtra("Unique_ID", Unique_ID);
                o.putExtra("multiple", true);
                startActivity(o);
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent o = new Intent(Ride_later_ok.this, GooglemapApp.class);
        o.putExtra("my_lat", My_lat);
        o.putExtra("my_long", My_long);
        startActivity(o);
        finish();
    }
}
