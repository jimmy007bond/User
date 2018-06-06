package com.geekworkx.hellocab.Ride_later;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.Image_Adapter;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.User;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by parag on 27/01/18.
 */

public class Ride_later_dates extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView laterRv;
    private ArrayList<User> mItems=new ArrayList<User>();
    private static final String TAG = Ride_later_dates.class.getSimpleName();
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private Button Datesadd;
    private Toolbar toolbar;
    private ConnectionDetector cd;
    private TextView no_rides;

    public Ride_later_dates() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
        Bundle args = getArguments();
        My_lat = args.getDouble("my_lat");
        My_long = args.getDouble("my_long");
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.later_dates, container, false);
        laterRv=V.findViewById(R.id.laterRv);
        no_rides=V.findViewById(R.id.no_rides);
        progressBar = V.findViewById(R.id.progressBar21);
        mItems.clear();
        new GetlaterDate().execute();


       // laterRv=V.findViewById(R.id.laterRv);
        Datesadd=V.findViewById(R.id.buttondates);
       
        return  V;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
    @Override
    public void onResume() {
        super.onResume();
      
        Datesadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(getActivity(), Ride_later_ok.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                startActivity(o);
                getActivity().finish();
                //Bungee.slideLeft(getActivity());
            }
        });
    }


    private class GetlaterDate extends AsyncTask<Void, Void, Void> {

        private String User_mobile;

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
                    JSONArray later = jsonObj.getJSONArray("Book_Ride_Later");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");



                    // looping through All Contacts
                    if (_PhoneNo != null) {
                        int relation=0;
                        for (int i = 0; i < User.length(); i++) {

                            JSONObject d = User.getJSONObject(i);
                            if (_PhoneNo.contains(d.getString("Phone_No"))) {
                                relation = d.getInt("ID");
                            }
                        }

                        for (int i = 0; i < later.length(); i++) {
                            JSONObject c = later.getJSONObject(i);
                            if (relation!=0 ) {

                                if (relation== c.getInt("User_ID") && !TextUtils.isEmpty(c.getString("From_Address"))) {
                                    User item=new User();
                                    item.setDate(c.getString("Start_Date"));
                                    item.setTime(c.getString("Start_time"));
                                    item.setFrom(c.getString("From_Address"));
                                    item.setTo(c.getString("To_Address"));
                                    item.setOTP(c.getInt("OTP"));
                                    item.setUnique_id(c.getString("Unique_Ride_Code"));
                                    item.setTo_Lat(c.getDouble("To_Latitude"));
                                    item.setTo_Long(c.getDouble("To_Longitude"));
                                        for (int j = 0; j < Settings_Vehicle_Rate.length(); j++) {
                                            JSONObject d = Settings_Vehicle_Rate.getJSONObject(j);
                                            if(d.getInt("ID")==c.getInt("Vehicle_ID")) {
                                                item.setVehicle_choosen(d.getString("Vehicle_Type"));
                                            }

                                        }

                                        //item.setVehicle_choosen(c.getString("Vehicle_ID"));
                                    if(!c.isNull("Cost")) {
                                        item.set_exp(c.getInt("Cost"));
                                    }
                                    mItems.add(item);

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
            progressBar.setVisibility(View.GONE);
            if (mItems.size() != 0) {
                laterRv.setVisibility(View.VISIBLE);
                no_rides.setVisibility(View.GONE);
                Image_Adapter sAdapter = new Image_Adapter(getActivity(), mItems);
                sAdapter.notifyDataSetChanged();
                sAdapter.setMobile(_PhoneNo);
                sAdapter.setMyLat(My_lat,My_long);
                laterRv.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                laterRv.setLayoutManager(mLayoutManager);
            }else{
                laterRv.setVisibility(View.GONE);
               no_rides.setVisibility(View.VISIBLE);

            }
        }
    }


}
