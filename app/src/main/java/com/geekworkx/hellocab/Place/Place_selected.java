package com.geekworkx.hellocab.Place;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parag on 18/02/18.
 */

public class Place_selected extends AppCompatActivity implements View.OnClickListener{

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
    private ArrayList<favs>mItems=new ArrayList<favs>();
    private Toolbar toolbar;
    private double My_lat=0,My_long=0;
    private TextView Place1,Place2;
    private String Drop_at;
    private TextView Home,Work,Other;
    private String home,work,other;
    private ProgressBar progressBar;
    private RelativeLayout HomeFav,WorkFav,OtherFav;
    private ImageView Heart_home1,Heart_home2,Heart_work1,Heart_work2,Heart_other1,Heart_other2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeoDataClient = Places.getGeoDataClient(this, null);

        setContentView(R.layout.place_select);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        toolbar = (Toolbar) findViewById(R.id.toolbar_place);
        Home=findViewById(R.id.place_home);
        Work=findViewById(R.id.place_work);
        Other=findViewById(R.id.place_other);
        HomeFav=findViewById(R.id.favHome);
        WorkFav=findViewById(R.id.favWork);
        OtherFav=findViewById(R.id.favOther);
        Heart_home1=findViewById(R.id.heart_home_like);
        Heart_home2=findViewById(R.id.heart_home_empty);
        Heart_work1=findViewById(R.id.heart_work_like);
        Heart_work2=findViewById(R.id.heart_work_empty);
        Heart_other1=findViewById(R.id.heart_other_like);
        Heart_other2=findViewById(R.id.heart_other_empty);
        progressBar=findViewById(R.id.progressBarselect);
       setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        Place1=findViewById(R.id.place1);
        Place2=findViewById(R.id.place2);
        Place1.setText("Select ");
        Place2.setText("Pickup location");
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Drop_at=i.getStringExtra("drop");
        Button clearButton = (Button) findViewById(R.id.button_clear);
        laterRv = (RecyclerView) findViewById(R.id.placeRv);
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
                Intent i=new Intent(Place_selected.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                //Bungee.card(Place_selected.this);
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
                new RecyclerTouchListener(Place_selected.this, laterRv,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            public boolean Me;

                            @Override
                            public void onClick(View view,int position) {

                                if (mItems.size() != 0) {
                                    Intent i = new Intent(Place_selected.this, GooglemapApp.class);
                                    i.putExtra("my_lat", My_lat);
                                    i.putExtra("my_long", My_long);
                                    i.putExtra("pick", mItems.get(position).getFrom(position));
                                    if(Drop_at!=null) {
                                        i.putExtra("drop", Drop_at);
                                    }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.place_home:
                if(home!=null){
                    Intent i = new Intent(Place_selected.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("pick", home);
                    if(Drop_at!=null) {
                        i.putExtra("drop", Drop_at);
                    }
                    startActivity(i);finish();
                }

                break;
            case R.id.place_work:
                if(work!=null){
                    Intent i = new Intent(Place_selected.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("pick", work);
                    if(Drop_at!=null) {
                        i.putExtra("drop", Drop_at);
                    }
                    startActivity(i);finish();
                }

                break;
            case R.id.place_other:
                if(other!=null){
                    Intent i = new Intent(Place_selected.this, GooglemapApp.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("pick", other);
                    if(Drop_at!=null) {
                        i.putExtra("drop", Drop_at);
                    }
                    startActivity(i);finish();
                }
                break;
            default:break;
        }
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

                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Past_From_address");
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
                            item.setFrom(c.getString("From_Address"));
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
                CardArrayAdapter sAdapter = new CardArrayAdapter(Place_selected.this, mItems);
                sAdapter.notifyDataSetChanged();
                sAdapter.setDrop(Drop_at);
                sAdapter.setTag(1);
                laterRv.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Place_selected.this);
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
                String Address=place.getAddress().toString();
                Intent i = new Intent(Place_selected.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                i.putExtra("pick", Address);
                if(Drop_at!=null) {
                    i.putExtra("drop", Drop_at);
                }
                startActivity(i);finish();
                //Bungee.split(Place_selected.this);


                Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

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

    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            Intent o = new Intent(Place_selected.this, GooglemapApp.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
            //Bungee.slideLeft(Ride_later_tabs.this);
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Press Back again to Exit.", Snackbar.LENGTH_LONG).show();

            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            },  1000);

        }

    }
}
