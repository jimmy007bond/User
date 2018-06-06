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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.Image_Adapter;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.User;
import com.geekworkx.hellocab.Place.RecyclerTouchListener;
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
 * Created by parag on 18/02/18.
 */

public class PastFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView laterRv;
    private ArrayList<User> mItems=new ArrayList<User>();
    private static final String TAG = Ride_later_dates.class.getSimpleName();
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private ConnectionDetector cd;
    private TextView no_rides;

    public PastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.layout_past, container, false);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        laterRv=V.findViewById(R.id.pastRv);
        no_rides=V.findViewById(R.id.no_past);
        progressBar = V.findViewById(R.id.progressBarpast);
        return  V;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
    @Override
    public void onResume() {
        super.onResume();
        mItems.clear();
        new GetpastDate().execute();
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

                    JSONArray driver = jsonObj.getJSONArray("Driver_Details");

                    // looping through All Contacts
                    if (_PhoneNo != null) {

                           JSONArray rides = jsonObj.getJSONArray("Book_Ride_Past");
                        JSONArray User = jsonObj.getJSONArray("User");

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

                        for (int i = 0; i < rides.length(); i++) {
                            JSONObject c = rides.getJSONObject(i);

                            if (!c.isNull("User_ID") && !c.isNull("Driver_ID")) {

                                for (int j = 0; j < driver.length(); j++) {
                                    JSONObject d = driver.getJSONObject(j);
                                        if (c.getInt("User_ID")==User_ID && d.getInt("ID")==c.getInt("Driver_ID")) {
                                            User item = new User();
                                            item.setDate(c.getString("Start_Date"));
                                            item.setTime(c.getString("Start_time"));
                                            item.setFrom(c.getString("From_Address"));
                                            item.setTo(c.getString("To_Address"));
                                            item.setSnapshot(c.getString("Map_Snapshot"));
                                            if(d.getInt("ID")==c.getInt("Driver_ID")) {
                                                item.setDriverImage(d.getString("Photo"));
                                            }
                                            item.setCost(c.getString("Cost"));
                                            item.setUnique_id(c.getString("Unique_Ride_Code"));
                                            mItems.add(item);


                                    }
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
                laterRv.addOnItemTouchListener(
                        new RecyclerTouchListener(getActivity(), laterRv,
                                new RecyclerTouchListener.OnTouchActionListener() {
                                    public boolean Me;

                                    @Override
                                    public void onClick(View view, final int position) {

                                        if (mItems.size() != 0) {
                                            Intent i = new Intent(getActivity(), PastRides.class);
                                            i.putExtra("my_lat", My_lat);
                                            i.putExtra("my_long", My_long);
                                            i.putExtra("UNIQUEID", mItems.get(position).getUnique_id(position));
                                            startActivity(i);
                                            getActivity().finish();
                                        }

                                    }

                                    @Override
                                    public void onRightSwipe(View view, int position) {

                                    }

                                    @Override
                                    public void onLeftSwipe(View view, int position) {

                                    }
                                }));
            }else{
                laterRv.setVisibility(View.GONE);
                no_rides.setVisibility(View.VISIBLE);

            }
        }
    }


}
