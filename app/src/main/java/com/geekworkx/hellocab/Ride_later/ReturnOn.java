package com.geekworkx.hellocab.Ride_later;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.Date_;
import com.geekworkx.hellocab.Place.RecyclerTouchListener;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by parag on 06/03/18.
 */

public class ReturnOn extends Fragment {

    private ConnectionDetector cd;
    private PrefManager pref;
    private String _PhoneNo;
    private RecyclerView laterRv;
    private TextView Hr12,Hr24;
    private String Time_selected;
    private String Unique_ID;
    private String Date_later,Time_later;
    private ArrayList<Date_> mItems=new ArrayList<Date_>();
    private RadioButton hr12,hr24,hrPick;
    private String Date_selected;
    private TextView textDate;
    public ReturnOn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
        Bundle args = getArguments();
        Unique_ID = args.getString("Unique_ID");
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.return_on, container, false);
        laterRv=V.findViewById(R.id.returnRv);
        Hr12=V.findViewById(R.id.text_hr12);
        Hr24=V.findViewById(R.id.text_hr24);
        hr12=V.findViewById(R.id.hr12);
        hr24=V.findViewById(R.id.hr24);
        hrPick=V.findViewById(R.id.pick_choose_date);
        textDate=V.findViewById(R.id.textDate);
        return  V;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
    @Override
    public void onResume() {
        super.onResume();

        hr12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    laterRv.setVisibility(View.GONE);
                    textDate.setVisibility(View.GONE);

                    if(hr24.isChecked()) {
                        hr24.setChecked(false);
                    }
                    if(hrPick.isChecked()) {
                        hrPick.setChecked(false);
                    }
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-ddHH:mm");
                        Calendar cal1 = Calendar.getInstance();
                        try {

                            if(Date_later!=null) {
                                Date date = form.parse(Date_later+Time_later);
                                cal1.setTime(date);
                                cal1.add(Calendar.HOUR,12);
                                Date_selected=format.format(cal1.getTime());
                                Time_selected=parseFormat.format(cal1.getTime());
                                if(Date_selected!=null && Time_selected!=null){
                                    new PostFavourite().execute(Date_selected,Time_selected);
                                } else{
                                    Toast.makeText(getApplicationContext(),"Please select time",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                }
            }
        });
        hr24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    laterRv.setVisibility(View.GONE);
                    textDate.setVisibility(View.GONE);

                    if(hr12.isChecked()) {
                        hr12.setChecked(false);
                    }
                    if(hrPick.isChecked()) {
                        hrPick.setChecked(false);
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-ddHH:mm");
                    Calendar cal1 = Calendar.getInstance();
                    try {

                        if(Date_later!=null) {
                            Date date = form.parse(Date_later+Time_later);
                            cal1.setTime(date);
                            cal1.add(Calendar.HOUR,24);
                            Date_selected=format.format(cal1.getTime());
                            Time_selected=parseFormat.format(cal1.getTime());
                            if(Date_selected!=null && Time_selected!=null){
                                new PostFavourite().execute(Date_selected,Time_selected);
                            } else{
                                Toast.makeText(getApplicationContext(),"Please select time",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        hrPick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(hr24.isChecked()) {
                        hr24.setChecked(false);
                    }
                    if(hr12.isChecked()) {
                        hr12.setChecked(false);
                    }

                    laterRv.setVisibility(View.VISIBLE);
                    textDate.setVisibility(View.VISIBLE);

                }
            }
        });
        mItems.clear();
        new GetSettings().execute();

    }

    private class PostFavourite extends AsyncTask<String, Integer, String> {
        private boolean success;


        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator

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
                Intent o = new Intent(getApplicationContext(), Ride_outstation.class);
                o.putExtra("otp",Unique_ID);
                startActivity(o);
                getActivity().finish();
            }


        }
    }
    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private static final String TAG = "Simple";
        private String Return_date,Return_time;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                            if(c.getString("Is_Roudtrip").contains("1")){
                                if(c.getString("Return_date").contains("null") &&c.getString("Return_time").contains("null")) {
                                    Return_date = null;
                                    Return_time = null;
                                }else{
                                    Return_date = c.getString("Return_date");
                                    Return_time = c.getString("Return_time");
                                }
                            }

                        }
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM,yyyy");
            SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            Calendar cal = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            try {

                if(Date_later!=null){
                    Date date = parseFormat.parse(Date_later+Time_later);
                    Date date1 = parseFormat.parse(Date_later+Time_later);
                    cal1.setTime(date1);
                    cal.setTime(date);
                    cal2.setTime(date);
                    textDate.setText(dateFormat1.format(cal.getTime()));
                    cal.add(Calendar.HOUR,12);
                    Hr12.setHint(dateFormat.format(cal.getTime())+","+sdf.format(cal.getTime()));
                    cal1.add(Calendar.HOUR,24);
                    Hr24.setHint(dateFormat.format(cal1.getTime())+","+sdf.format(cal1.getTime()));
                    for(int i=0;i<8;i++){
                        SimpleDateFormat df = new SimpleDateFormat("dd");
                        cal2.add(Calendar.DATE,i);
                        int day = cal2.get(Calendar.DAY_OF_WEEK);
                        Date_ item=new Date_();
                        String gg=format.format(cal2.getTime());
                        switch (day) {
                            case Calendar.SUNDAY:

                                item.setDate("SUN");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.MONDAY:
                                item.setDate("MON");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.TUESDAY:
                                item.setDate("TUE");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            // etc.
                            case Calendar.WEDNESDAY:
                                item.setDate("WED");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.THURSDAY:
                                item.setDate("THR");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.FRIDAY:
                                item.setDate("FRI");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            // etc.
                            case Calendar.SATURDAY:
                                item.setDate("SAT");
                                item.setTime(df.format(cal2.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal2.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;

                            default:
                                break;

                        }
                        cal2.setTime(date);
                    }
                    daterv sAdapter = new daterv(getActivity(), mItems);
                    sAdapter.notifyDataSetChanged();
                    laterRv.setAdapter(sAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    laterRv.setLayoutManager(mLayoutManager);

                    laterRv.addOnItemTouchListener(
                            new RecyclerTouchListener(getActivity(), laterRv,
                                    new RecyclerTouchListener.OnTouchActionListener() {
                                        public boolean Me;

                                        @Override
                                        public void onClick(View view, final int position) {

                                            if (mItems.size() != 0) {
                                                Date_selected=mItems.get(position).getDate_selected(position);
                                                new PostFavourite().execute(Date_selected,Time_later);
                                            }
                                        }

                                        @Override
                                        public void onRightSwipe(View view, int position) {

                                        }

                                        @Override
                                        public void onLeftSwipe(View view, int position) {

                                        }
                                    }));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }
}
