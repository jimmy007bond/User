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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
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

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by parag on 06/03/18.
 */

public class LeaveOn extends Fragment {

    private ConnectionDetector cd;
    private PrefManager pref;
    private String _PhoneNo;
    private RecyclerView laterRv;
    private TextView textDate;
    private String Time_selected;
    private String Unique_ID;
    private String Date_later;
    private ArrayList<Date_> mItems=new ArrayList<Date_>();
    private TimePicker time_choose;
    private String Date_selected;
    private Button LeaveO;
    private int Pager;

    public LeaveOn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
        Bundle args = getArguments();
        Unique_ID = args.getString("Unique_ID");
        Pager=args.getInt("pager");
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.leave_on, container, false);
        laterRv=V.findViewById(R.id.leaveRv);
        textDate=V.findViewById(R.id.textDate);
        time_choose=V.findViewById(R.id.timePicker_out);
        LeaveO=V.findViewById(R.id.btn_leave);
        return  V;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
    @Override
    public void onResume() {
        super.onResume();
        time_choose.setIs24HourView(true);
        time_choose.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                if(minute<10) {
                    Time_selected = hourOfDay + ":0" + minute;
                }else {
                    Time_selected = hourOfDay + ":" + minute;
                }


            }
        });

        LeaveO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Date_selected!=null && Time_selected!=null){
                  new PostFavourite().execute(Date_selected,Time_selected);
                } else{
                    Toast.makeText(getApplicationContext(),"Please select time",Toast.LENGTH_SHORT).show();
                }

            }
        });
        mItems.clear();
        new GetSettings().execute();

    }

    private class PostFavourite extends AsyncTask<String, Integer, String> {
        private boolean success;
        private String commaSeparatedValues;

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
                        .addFormDataPart("otp", String.valueOf(Unique_ID))
                        .addFormDataPart("return", String.valueOf(true))
                        .addFormDataPart("pager", String.valueOf(false))
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
                o.putExtra("Unique_ID",Unique_ID);
                startActivity(o);
                getActivity().finish();
            }


        }
    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private static final String TAG = "Simple";

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
                    // looping through All Contacts
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
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM,yyyy");
            final Calendar cal = Calendar.getInstance();
            try {

                if(Date_later!=null){
                    Date date = format.parse(Date_later);
                    cal.setTime(date);
                    textDate.setText(dateFormat.format(cal.getTime()));
                    for(int i=0;i<8;i++){
                        SimpleDateFormat df = new SimpleDateFormat("dd");
                        cal.add(Calendar.DATE,i);
                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        Date_ item=new Date_();
                        String gg=format.format(cal.getTime());
                        switch (day) {
                            case Calendar.SUNDAY:

                                item.setDate("SUN");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.MONDAY:
                                item.setDate("MON");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.TUESDAY:
                                item.setDate("TUE");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                                // etc.
                            case Calendar.WEDNESDAY:
                                item.setDate("WED");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.THURSDAY:
                                item.setDate("THR");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            case Calendar.FRIDAY:
                                item.setDate("FRI");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;
                            // etc.
                            case Calendar.SATURDAY:
                                item.setDate("SAT");
                                item.setTime(df.format(cal.getTime()).toString());
                                if(format.parse(Date_later).compareTo(format.parse(format.format(cal.getTime())))==0){
                                    item.setSelected(true);
                                }
                                item.setDate_selected(gg);
                                mItems.add(item);
                                break;

                                default:
                                    break;

                        }
                        cal.setTime(date);
                    }
                    laterRv.setVisibility(View.VISIBLE);
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
                                                Date date = null;
                                                try {
                                                    date = format.parse(Date_selected);
                                                    cal.setTime(date);
                                                    textDate.setText(dateFormat.format(cal.getTime()));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

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
