package com.geekworkx.hellocab.Place;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.favs;
import com.geekworkx.hellocab.Place.logger.Log;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.Ride_later.Ride_later_ok;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 18/02/18.
 */

public class Place_drop extends AppCompatActivity implements View.OnClickListener{

    protected GeoDataClient mGeoDataClient;

    private PlaceAutocompleteAdapter mAdapter;
    private static final String TAG = Place_selected.class.getSimpleName();
    private AutoCompleteTextView mAutocompleteView;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(25.00000, 91.00000), new LatLng(27.99999, 91.99999));
    private RecyclerView laterRv;
    private CardArrayAdapter cardArrayAdapter;
    private PrefManager pref;
    private String _PhoneNo;
    private ArrayList<favs> mItems=new ArrayList<favs>();
    private Toolbar toolbar;
    private double My_lat=0,My_long=0;
    private TextView Place1,Place2;
    private String Pick_at;
    private TextView Home,Work,Other;
    private String home,work,other;
    private RelativeLayout HomeFav,WorkFav,OtherFav;
    private ProgressBar progressBar;
    private String Location,Address;
    private ImageView Heart_home1,Heart_home2,Heart_work1,Heart_work2,Heart_other1,Heart_other2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeoDataClient = Places.getGeoDataClient(this, null);

        setContentView(R.layout.place_drop);
        Home=findViewById(R.id.place_home1);
        Work=findViewById(R.id.place_work1);
        Other=findViewById(R.id.place_other1);
        HomeFav=findViewById(R.id.favHome1);
        WorkFav=findViewById(R.id.favWork1);
        OtherFav=findViewById(R.id.favOther1);
        Heart_home1=findViewById(R.id.heart_home_like1);
        Heart_home2=findViewById(R.id.heart_home_empty1);
        Heart_work1=findViewById(R.id.heart_work_like1);
        Heart_work2=findViewById(R.id.heart_work_empty1);
        Heart_other1=findViewById(R.id.heart_other_like1);
        Heart_other2=findViewById(R.id.heart_other_empty1);
        progressBar=findViewById(R.id.progressBardrop);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        toolbar = (Toolbar) findViewById(R.id.toolbar_place_drop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places_drop);
        Place1=findViewById(R.id.placedrop1);
        Place2=findViewById(R.id.placedrop2);
        Place1.setText("Select ");
        Place2.setText("Drop location");
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Pick_at=i.getStringExtra("pick");
        Button clearButton = (Button) findViewById(R.id.button_clear_drop);
        laterRv = (RecyclerView) findViewById(R.id.dropRv);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteView.setAdapter(mAdapter);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Place_drop.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                //Bungee.card(Place_drop.this);
            }
        });

        Home.setOnClickListener(this);
        Work.setOnClickListener(this);
        Other.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mItems.clear();
        new GetDriver().execute();
        laterRv.addOnItemTouchListener(
                new RecyclerTouchListener(Place_drop.this, laterRv,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            public boolean Me;

                            @Override
                            public void onClick(View view, final int position) {

                                if (mItems.size() != 0) {
                                    Intent i = new Intent(Place_drop.this, GooglemapApp.class);
                                    i.putExtra("my_lat", My_lat);
                                    i.putExtra("my_long", My_long);
                                    i.putExtra("drop", mItems.get(position).getFrom(position));
                                    if(Pick_at!=null) {
                                        i.putExtra("pick", Pick_at);
                                    }
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                    finish();
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



    private class GetDriver extends AsyncTask<Void, Void, Void> {


        private JSONArray ride;

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

            android.util.Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Past_To_address");
                    JSONArray User = jsonObj.getJSONArray("User");
                    // looping through All Contacts
                    int relation=0;
                    for (int i = 0; i < User.length(); i++) {
                        JSONObject d = User.getJSONObject(i);
                        if (_PhoneNo.contains(d.getString("Phone_No"))) {
                            relation = d.getInt("ID");
                            if(d.getString("Favourite_Other_Address")!=null|| d.getString("Favourite_Work_Address")!=null
                                    || d.getString("Favorite_Home_Address")!=null){
                                home=d.getString("Favorite_Home_Address");
                                work=d.getString("Favourite_Work_Address");
                                other=d.getString("Favourite_Other_Address");

                            }
                        }
                    }

                    for (int i = 0; i < ride.length(); i++) {

                        JSONObject c = ride.getJSONObject(i);

                        if (relation== c.getInt("User_ID")) {
                            favs item=new favs();
                            item.setFrom(c.getString("To_Address"));
                            mItems.add(item);

                        }
                        if(i==2){
                            break;
                        }
                    }





                } catch (final JSONException e) {
                    android.util.Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                android.util.Log.e(TAG, "Couldn't get json from server.");
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
                CardArrayAdapter sAdapter = new CardArrayAdapter(Place_drop.this, mItems);
                sAdapter.notifyDataSetChanged();
                sAdapter.setPick(Pick_at);
                sAdapter.setTag(2);
                laterRv.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Place_drop.this);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                laterRv.setLayoutManager(mLayoutManager);


            }else{
                laterRv.setVisibility(View.GONE);
            }
            if (home!=null && !home.contains("null")) {
                Home.setText(home);
                Heart_home1.setVisibility(View.VISIBLE);
                Heart_home2.setVisibility(View.GONE);
            } else {
                HomeFav.setVisibility(View.GONE);
            }
            if (work!=null && !work.contains("null")) {
                Work.setText(work);
                Heart_work1.setVisibility(View.VISIBLE);
                Heart_work2.setVisibility(View.GONE);
            } else {
                WorkFav.setVisibility(View.GONE);
            }
            if (other!=null && !other.contains("null")) {
                Other.setText(other);
                Heart_other1.setVisibility(View.VISIBLE);
                Heart_other2.setVisibility(View.GONE);
            } else {
                OtherFav.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.place_home1:
                if(home!=null){
                    Intent i = new Intent(Place_drop.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("drop", home);
                    if(Pick_at!=null) {
                        i.putExtra("pick", Pick_at);
                    }
                    startActivity(i);finish();
                }

                break;
            case R.id.place_work1:
                if(work!=null){
                    Intent i = new Intent(Place_drop.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("drop", work);
                    if(Pick_at!=null) {
                        i.putExtra("pick", Pick_at);
                    }
                    startActivity(i);
                    finish();
                }

                break;
            case R.id.place_other1:
                if(other!=null){
                    Intent i = new Intent(Place_drop.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("drop", other);
                    if(Pick_at!=null) {
                        i.putExtra("pick", Pick_at);
                    }

                    startActivity(i);
                    finish();
                }
                break;
            default:break;
        }
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);


            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                Address=place.getAddress().toString();
                LatLng drop1=getLocationFromAddress(Place_drop.this,Address);
                Location=String.valueOf(drop1.latitude)+" "+String.valueOf(drop1.longitude);
                if(SphericalUtil.computeDistanceBetween(getLocationFromAddress(Place_drop.this,Pick_at),drop1)>30) {
                    new PostDropLocation().execute();
                }else{
                    open_short();
                }
                //new PostDropLocation().execute();
                Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    private void open_short() {
        new android.support.v7.app.AlertDialog.Builder(Place_drop.this)
                .setTitle("Book HelloCab ")
                .setCancelable(false)
                .setMessage("Drop location is too near from pick up location.Change drop location?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAutocompleteView.setError("Please select");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Change drop location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent o = new Intent(Place_drop.this, Place_drop.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        if(Pick_at!=null) {
                            o.putExtra("pick", Pick_at);
                        }
                        o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(o);
                        dialog.cancel();
                    }
                })
                .show();

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private class PostDropLocation  extends AsyncTask<Void, Integer, String> {


        private boolean success;

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
                        .addFormDataPart("latlng", Location)
                        .build();

                Request request = new Request.Builder()
                        .url("http://139.59.38.160/OHO/APP/checkLocation.php?latlng="+Location)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                if(res.contains("True")){
                    success = true;

                }else{
                    success=false;
                }

                android.util.Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                android.util.Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                android.util.Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        protected void onPostExecute(String file_url) {
            if (success) {
                Intent i = new Intent(Place_drop.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                i.putExtra("drop", Address);
                if(Pick_at!=null) {
                    i.putExtra("pick", Pick_at);
                }
                startActivity(i);
                finish();
            } else {
                new android.support.v7.app.AlertDialog.Builder(Place_drop.this)
                        .setTitle("Book HelloCab Outstation")
                        .setMessage("Drop location is outside the limits. Continue booking Outstation instead?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent o = new Intent(Place_drop.this, Ride_later_ok.class);
                                o.putExtra("my_lat", My_lat);
                                o.putExtra("my_long", My_long);
                                o.putExtra("drop", Address);
                                if(Pick_at!=null) {
                                    o.putExtra("pick", Pick_at);
                                }
                                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(o);
                                finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Change drop location", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent o = new Intent(Place_drop.this, Place_drop.class);
                                o.putExtra("my_lat", My_lat);
                                o.putExtra("my_long", My_long);
                                if(Pick_at!=null) {
                                    o.putExtra("pick", Pick_at);
                                }
                                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(o);
                                dialog.cancel();
                            }
                        })
                        .show();

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

