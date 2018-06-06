package com.geekworkx.hellocab.Adapters;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.hellocab.BuildConfig;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Drawer.Emergency_contacts;
import com.geekworkx.hellocab.Drawer.Refer_earn;
import com.geekworkx.hellocab.Drawer.Support;
import com.geekworkx.hellocab.FCM.NotificationUtils;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Main_activity.Success;
import com.geekworkx.hellocab.Main_activity.Update_profile;
import com.geekworkx.hellocab.Model.Book;
import com.geekworkx.hellocab.Model.Promo;
import com.geekworkx.hellocab.Model.Rate;
import com.geekworkx.hellocab.Model.Ride;
import com.geekworkx.hellocab.Place.Place_drop;
import com.geekworkx.hellocab.Place.Place_selected;
import com.geekworkx.hellocab.Place.RecyclerTouchListener;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.Ride_later.Ride_later_ok;
import com.geekworkx.hellocab.Ride_later.Ride_later_tabs;
import com.geekworkx.hellocab.Services.SensorService;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.github.andreilisun.swipedismissdialog.OnSwipeDismissListener;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDirection;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.view.View.FOCUS_LEFT;
import static com.google.android.gms.maps.model.JointType.ROUND;
import static java.lang.System.out;

/**
 * Created by parag on 31/12/16.
 */
public class GooglemapApp extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        View.OnClickListener,Animation.AnimationListener{
    private ArrayList<Rate>mVehcle_option=new ArrayList<Rate>();
    private static final int ALARM_REQUEST_CODE = 10009;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = GooglemapApp.class.getSimpleName();
    private String Unique_id;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView txtName;
    private ImageView Edit_profile;
    private CircleImageView profile_image;
    private Toolbar toolbar;
    private String User_image;
    private String User_name;
    private String USER;
    private int navItemIndex = 0;
    private GoogleMap googleMap;
    private RelativeLayout rlMapLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regId;
    double My_lat, My_long;
    private ProgressBar progressBar;
    private final int REQUEST_LOCATION = 200;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private String strAdd;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final int M_MAX_ENTRIES = 5;
    private CameraPosition mCameraPosition;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String resultText;
    private String mLastUpdateTime="";
    private double MyLat = 0, MyLong = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(25.00000, 91.00000), new LatLng(27.99999, 91.99999));
    private static final int REQUEST_PICK_FROM = 10014;
    private static final int REQUEST_PICK_TO = 10015;
    private ImageView myLocationButton;
    private android.support.design.widget.FloatingActionButton Fab;
    private FirebaseDataListener mFirebaseDataChanged;
    private FirebaseDataListener_s mFirebaseDataChanged_s;
    private boolean clicK = false;
    private Marker marker;
    private List<LatLng> mPathPolygonPoints=new ArrayList<LatLng>() ;
    private ArrayList<String> _mTags=new ArrayList<String>() ;
    private LinearLayout Ride_later,Ride_now;
    private DatabaseReference mDatabase;
    private TextView My_address,Destination_address;
    private String User_from, User_to;
    private double User_From_lat = 0, User_From_long = 0, User_To_lat = 0, User_To_long = 0;
    private LinearLayout L1,L2;
    private TextView Mini_time,Micro_time,Sedan_time,Suv_time,Pickup_time,Amb_time;
    private ImageView Mini_image,Micro_image,Sedan_image,Suv_image,Pickup_image,Amb_image;
    private String Car_type;
    private DotProgressBar pmini,pmicro,psuv,psedan,pmuv,pamb;
    private Marker markerd;
    private boolean Map_clicked=false;
    private int mIndexCurrentPoint;
    private HorizontalScrollView H12;
    private LinearLayout H13;
    private LinearLayout rideButtons;
    private LinearLayout SearchingLL;
    private Button Cancel_ride;
    private boolean My_loc=false;
    private Book data_book=new Book();
    private LinearLayout L12;
    private TextView Drop,Pick;
    private boolean Markers_created;
    private boolean Got_amb=false,Got_suv=false,Got_pickup=false,Got_mini=false,Got_sedan=false,Got_micro=false;
    private boolean adMarker=false;
    private ArrayList<Marker>markers=new ArrayList<Marker>();
    private ArrayList<Ride>details=new ArrayList<Ride>();
    private ArrayList<Promo>mPromos=new ArrayList<Promo>();
    private LinearLayout Lmini,Lmicro,Lsedan,Lsuv,Lpick,Lamb;
    private float distance;
    private static final float POLYLINE_WIDTH = 8;
    private ImageView Heart_d1,Heart_d2,Heart_t1,Heart_t2;
    private AlertDialog alertDialog1;
    private String Favourite;
    private boolean ride_later=false;
    private int imageParentWidth = -1;
    private int imageParentHeight = -1;
    private int imageHeight = -1;
    private int centerX = -1;
    private int centerY = -1;
    private ArrayList<String>fcmDriver=new ArrayList<String>();
    private Animation expandIn;
    private Animation myAnim;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final LatLng[] ROUTE = new LatLng[2];
    private ImageView IMarker;
    private Animation animFadein,animFadeout;
    private Animation animslideIn,animslideup;
    private LatLng midLatLng;
    private AppBarLayout AppBar;
    private FrameLayout FFv;
    private TextView ExpnadText;
    private RelativeLayout AllRl;
    private String OTP;
    private TextView Ride_otp,Ride_driver_name,Ride_car_no,Ride_car_type,Ride_car_no1;
    private ImageView Ride_driver_image,Ride_car_image;
    private TextView Ride_driver_rating;
    private CustomMapFragment mapFragment;
    private Button Confirm_booking;
    private PrefManager pref;
    private String _phoneNo;
    private double From_lat=0.0,From_long=0.0,To_lat=0.0,To_long=0.0;
    private String Driver_phone;
    private String Driver_name,Driver_image,Vehicle_no,Vehicle_image,Vehicle_type;
    private boolean isInternetPresent=false;
    private ConnectionDetector cd;
    private String Driver_identity;
    private RelativeLayout R1;
    private ArrayList<LatLng> markerPoints;
    private String WHO;
    private String UNIQUE_RIDE;
    private Button Call_driver,Track_driver;
    private boolean Track=false;
    private HorizontalScrollView ChooseCar;
    private Button Choose_1,Choose_2,Choose_3,Choose_4;
    private String From_address,To_address;
    private String User_phone;
    private String User_accept;
    private AnimatorSet mAnimatorSet;
    private static final float CAMERA_OBLIQUE_ZOOM = 14;
    private static final float CAMERA_OBLIQUE_TILT = 60;
    private static final long CAMERA_HEADING_CHANGE_RATE = 5;
    private double Driver_lat=0,Driver_long=0;
    private ImageView CenterMarker;
    private ImageView mySOS;
    private boolean Redraw=false;
    private String Driver_Start;
    private String Car_;
    private boolean first = false;
    private boolean book_ride=false;
    private String Pick_up,Drop_at;
    private String Driver_stop;
    private Boolean ask=false,wait=false,go=false,stop=false;
    private String con;
    private boolean drop=false;
    private Marker markerCar;
    private BottomNavigationView bottomNavigationView;
    private TextView Text1,Text2;
    private PolylineOptions lineOptions = null;
    private Polyline polylineFinal;
    private RelativeLayout Choose_car_below1;
    private LinearLayout Choose_car_below2;
    private LinearLayout Choose_car_below3;
    private boolean animate=false;
    private float angleDeg=0;
    private ImageView MyStar;
    private boolean cancel=false;
    List<Ride>list = new ArrayList<Ride>();
    private boolean drawn=false;
    private double KM=0;
    private double Driver_rating=0;
    private boolean running=false;
    private String formattedDate;
    private Button Cancel_again;
    private Animation animslideD;
    private Animation animslideU;
    private String User_done;
    private int Minimum_KM_Distance_for_Outstation=0;
    private int User_ID=0;
    private int Driver_ID=0;
    private boolean new_driver=false;
    private String mobileIp;
    private ArrayList<String>Emergencycontacts=new ArrayList<String>();
    private String mobileno;
    private String Discount_value;
    private ArrayList<String>Online_Driver=new ArrayList<String>();
    private double Distance=0;
    private int Minimum_rate=0,Hourly_rate=0;
    private double Estimated_cost=0;
    private boolean Service_start=false;
    private SensorService mSensorService;
    private Intent mServiceIntent;
    private String Tracking_type;
    private int Minimum_Distance_in_Meter_for_Tracking=0;
    private double COST=0;
    private boolean got=false;
    DecimalFormat dft=new DecimalFormat("0.00");
    DecimalFormat dfto=new DecimalFormat("0.000000");
    private List<LatLng> mPathPolygonPoint=new ArrayList<LatLng>() ;
    private float bearing=0;
    private boolean _reached=false;
    private boolean _userRef=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.dialogmap);
        mySOS=findViewById(R.id.mySOS);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Text1=findViewById(R.id.textView101);
        Text2=findViewById(R.id.textView102);
        AllRl=findViewById(R.id.ride_detail);
        Ride_otp=findViewById(R.id.textotp);
        ChooseCar=findViewById(R.id.choose_car);
        Choose_car_below1=findViewById(R.id.relativeLayout2);
        Choose_car_below2=findViewById(R.id.lov1);
        Choose_car_below3=findViewById(R.id.con);
        Ride_driver_image=findViewById(R.id.driver_image);
        Ride_driver_name=findViewById(R.id.driver_name);
        Ride_car_image=findViewById(R.id.driver_car);
        Ride_car_no1=findViewById(R.id.driver_car_no1);
        Ride_car_no=findViewById(R.id.driver_car_no);
        Ride_car_type=findViewById(R.id.driver_car_type);
        Ride_driver_rating=findViewById(R.id.driver_star);
        Confirm_booking=findViewById(R.id.confirm_ride);
        CenterMarker=findViewById(R.id.it);
        Choose_1=findViewById(R.id.car_choosen_1);
        Choose_2=findViewById(R.id.car_choosen_2);
        Choose_3=findViewById(R.id.car_choosen_3);
        Choose_4=findViewById(R.id.car_choosen_4);
        Call_driver=findViewById(R.id.call_driver);
        Track_driver=findViewById(R.id.track_driver);
        FFv=findViewById(R.id.ffv);
        ExpnadText=findViewById(R.id.expandText);
        AppBar=findViewById(R.id.app_bar_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cor_home_main);
        H12=findViewById(R.id.l12);
        H13=findViewById(R.id.l13);
        Drop=findViewById(R.id.drop_at);
        Pick=findViewById(R.id.pick_from);
        rideButtons=findViewById(R.id.linearLayout);
        SearchingLL=findViewById(R.id.ride_searching);
        Cancel_ride=findViewById(R.id.cancel_ride);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name_profile);
        MyStar = navHeader.findViewById(R.id.my_star);
        Edit_profile = (ImageView) navHeader.findViewById(R.id.edit_profile);
        profile_image = (CircleImageView) navHeader.findViewById(R.id.img_profile);
        Lmini=findViewById(R.id.lmini);
        Lmicro=findViewById(R.id.lmicro);
        Lsedan=findViewById(R.id.lsedan);
        Lpick=findViewById(R.id.lpick);
        Lamb=findViewById(R.id.lamb);
        Lsuv=findViewById(R.id.lsuv);
        L12=findViewById(R.id.l1);
        Heart_d1=findViewById(R.id.heart_01);
        Heart_d2=findViewById(R.id.heart_02);
        Heart_t1=findViewById(R.id.heart_11);
        Heart_t2=findViewById(R.id.heart_12);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpNavigationView();
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _phoneNo = user.get(PrefManager.KEY_MOBILE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3_map);
        rlMapLayout = (RelativeLayout) findViewById(R.id.rlMapLayout);
        Ride_later=findViewById(R.id.ride_later);
        Ride_now=findViewById(R.id.ride_now);
        myLocationButton =  findViewById(R.id.myLocationCustomButton);
        expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in);
        Mini_time=findViewById(R.id.mini_time);
        Mini_image=findViewById(R.id.car_mini);
        Micro_time=findViewById(R.id.micro_time);
        Micro_image=findViewById(R.id.car_micro);
        Sedan_time=findViewById(R.id.sedan_time);
        Sedan_image=findViewById(R.id.car_sedan);
        Suv_time=findViewById(R.id.suv_time);
        Suv_image=findViewById(R.id.car_suv);
        Pickup_time=findViewById(R.id.muv_time);
        Pickup_image=findViewById(R.id.car_muv);
        Amb_time=findViewById(R.id.ambulance_time);
        Amb_image=findViewById(R.id.car_amb);
        pmini=findViewById(R.id.dot_progress_bar_mini);
        pmicro=findViewById(R.id.dot_progress_bar_micro);
        psedan=findViewById(R.id.dot_progress_bar_sedan);
        pmuv=findViewById(R.id.dot_progress_bar_muv);
        psuv=findViewById(R.id.dot_progress_bar_suv);
        pamb=findViewById(R.id.dot_progress_bar_amb);
        L1=findViewById(R.id.l1);
        L2=findViewById(R.id.l2);
        cd = new ConnectionDetector(getApplicationContext());
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        mapFragment = (CustomMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(GooglemapApp.this);
        rebuildGoogleApiClient();
        // TODO: This next IF statement temporarily deals with an issue where autoManage doesn't
        // call the onConnected callback after a Builder.build() when re-connecting after a
        // rotation change. Will remove when fixed.
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            onConnected(null);
        }


        drawer.closeDrawers();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseDataChanged = new FirebaseDataListener();
        mFirebaseDataChanged_s = new FirebaseDataListener_s();


        My_address=findViewById(R.id.et_myLocation_Address);
        Destination_address=findViewById(R.id.et_destLocation_Address);
        Cancel_again=findViewById(R.id.cancel_again);
        Ride_later.setOnClickListener(this);
        Ride_now.setOnClickListener(this);
        Edit_profile.setOnClickListener(this);
        Heart_d1.setOnClickListener(this);
        Heart_d2.setOnClickListener(this);
        Heart_t1.setOnClickListener(this);
        Heart_t2.setOnClickListener(this);
        My_address.setOnClickListener(this);
        Destination_address.setOnClickListener(this);
        Mini_image.setOnClickListener(this);
        Micro_image.setOnClickListener(this);
        Sedan_image.setOnClickListener(this);
        Amb_image.setOnClickListener(this);
        Suv_image.setOnClickListener(this);
        Pickup_image.setOnClickListener(this);
        Cancel_ride.setOnClickListener(this);
        mySOS.setOnClickListener(this);
        myLocationButton.setOnClickListener(this);
        Call_driver.setOnClickListener(this);
        Cancel_again.setOnClickListener(this);
        Choose_1.setOnClickListener(this);
        Choose_2.setOnClickListener(this);
        Choose_3.setOnClickListener(this);
        Choose_4.setOnClickListener(this);


        setColor(Call_driver);
        setColor(Track_driver);
        setColor(Confirm_booking);
        setColor(Cancel_ride);
        setColor(Cancel_again);
        Track_driver.setOnClickListener(this);
        Confirm_booking.setOnClickListener(this);

        displayFirebaseRegId();
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animslideIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        animslideup = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        animslideD = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down1);
        animslideU = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up1);
        FFv.setVisibility(View.VISIBLE);
        ExpnadText.setVisibility(View.GONE);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        Pick_up=i.getStringExtra("pick");
        Drop_at = i.getStringExtra("drop");
        _reached=i.getBooleanExtra("reached",false);
        myLocationButton.setAnimation(expandIn);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GooglemapApp.this);
        mRequestingLocationUpdates = false;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(GooglemapApp.this);
        mSettingsClient = LocationServices.getSettingsClient(GooglemapApp.this);
        Lmicro.setAnimation(expandIn);
        Lpick.setAnimation(expandIn);
        Lmini.setAnimation(expandIn);
        Lsuv.setAnimation(expandIn);
        Lsedan.setAnimation(expandIn);
        Lamb.setAnimation(expandIn);
        Heart_d1.setAnimation(myAnim);
        Heart_t1.setAnimation(myAnim);
        mySOS.setAnimation(expandIn);
        mSensorService = new SensorService(GooglemapApp.this);
        mServiceIntent = new Intent(GooglemapApp.this, mSensorService.getClass());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_item1:
                                if(Discount_value!=null){
                                  Toast.makeText(getApplicationContext(),"Already applied",Toast.LENGTH_SHORT).show();
                                }else {
                                    new GetPromos().execute();
                                }
                                break;


                            case R.id.action_item3:
                                open_share();
                                break;
                        }

                        return true;
                    }
                });

        mobileIp = getMobileIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            public Snackbar snackbar;

            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config_URL.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config_URL.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config_URL.PUSH_NOTIFICATION)) {

                      snackbar = Snackbar
                            .make(coordinatorLayout, "Your ride has reached location", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                }
            }
        };

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


    private void setColor(Button b){
        SpannableString text = new SpannableString(b.getText());
        // make "Clicks" (characters 0 to 5) Red
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);
        // make "Here" (characters 6 to 10) Blue
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.top)), 6, 10, 0);
        // shove our styled text into the Button
        b.setText(text, TextView.BufferType.SPANNABLE);
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        if (map == null) {
            return;
        } else {
            googleMap = map;



            if (My_lat != 0) {
                CameraPosition googlePlex;
                googlePlex = CameraPosition.builder()
                        .target(new LatLng(My_lat, My_long))
                        .zoom(18) // Sets the zoom
                        .build(); // Crea
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

            }else{
                startLocationUpdates();
            }

            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setTrafficEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {
                @Override
                public boolean onMarkerClick(Marker arg0) {
                    return true;
                }
            });

            mapFragment.setOnDragListener(new MapWrapperLayout1.OnDragListener() {
                @Override
                public void onDrag(MotionEvent motionEvent) {
                    if(OTP==null && !drop && !drawer.isActivated()) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                clicK=true;
                                stopLocationUpdates();
                                AppBar.setAnimation(animslideup);
                                AppBar.setVisibility(View.GONE);
                                rideButtons.setAnimation(animslideD);
                                rideButtons.setVisibility(View.GONE);
                                myLocationButton.setAnimation(animFadein);
                                H13.setAnimation(animFadeout);
                                H13.setVisibility(View.GONE);
                                FFv.setAnimation(animFadeout);
                                FFv.setVisibility(View.GONE);
                                myLocationButton.setVisibility(View.GONE);
                                midLatLng = googleMap.getCameraPosition().target;
                                googleMap.setLatLngBoundsForCameraTarget(BOUNDS_MOUNTAIN_VIEW);
                                User_From_lat = midLatLng.latitude;
                                User_From_long = midLatLng.longitude;
                                ExpnadText.setText(getCompleteAddressString(midLatLng.latitude, midLatLng.longitude));
                                ExpnadText.setVisibility(View.VISIBLE);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                midLatLng = googleMap.getCameraPosition().target;
                                googleMap.setLatLngBoundsForCameraTarget(BOUNDS_MOUNTAIN_VIEW);
                                ExpnadText.setVisibility(View.VISIBLE);
                                FFv.setVisibility(View.GONE);
                                break;
                            case MotionEvent.ACTION_UP:
                                clicK=false;
                                AppBar.setVisibility(View.VISIBLE);
                                AppBar.setAnimation(animslideIn);
                                rideButtons.setAnimation(animslideU);
                                rideButtons.setVisibility(View.VISIBLE);
                                myLocationButton.setVisibility(View.VISIBLE);
                                myLocationButton.setAnimation(animFadein);
                                H13.setAnimation(animFadein);
                                H13.setVisibility(View.VISIBLE);
                                ExpnadText.setVisibility(View.GONE);
                                FFv.setAnimation(animFadein);
                                FFv.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(ExpnadText.getText().toString())) {
                                    My_address.setText(ExpnadText.getText().toString());
                                }
                                L1.bringToFront();
                                L2.invalidate();
                                Pick.setVisibility(View.VISIBLE);
                                Drop.setVisibility(View.GONE);
                                L1.setAlpha((float) 1.0);
                                L2.setAlpha((float) 0.7);
                                My_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                                My_address.setGravity(Gravity.CENTER);
                                Destination_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
                                Destination_address.setGravity(Gravity.BOTTOM);
                                My_loc = true;
                                User_From_lat = midLatLng.latitude;
                                User_From_long = midLatLng.longitude;
                                break;

                        }
                    }
                }
            }, true);

            getLocationPermission();

        }
    }

    private void updateCameraBearing(GoogleMap googleMap, float bearing) {
        if ( googleMap == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        googleMap.getCameraPosition() // current Camera
                )
                .bearing(bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }else{

            startLocationUpdat();
        }

    }

    private class FetchCordinates extends AsyncTask<String, Integer, String> {

        public FetchCordinates.VeggsterLocationListener mVeggsterLocationListener;
        private ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {

            mVeggsterLocationListener = new FetchCordinates.VeggsterLocationListener();
            if (ActivityCompat.checkSelfPermission(GooglemapApp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GooglemapApp.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {

                if (mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mVeggsterLocationListener);
                }

            } catch (SecurityException e) {

                e.printStackTrace();

            }
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onCancelled() {
            out.println("Cancelled by user!");
            progressBar.setVisibility(View.GONE);


        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);


            if (My_lat != 0) {


                if(!ride_later) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(My_lat, My_long)) // Sets the new camera position
                            .zoom(18) // Sets the zoom
                            .build(); // Crea
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position));
                    if (marker != null) {
                        marker.setPosition(new LatLng(My_lat, My_long));

                        User_From_lat = My_lat;
                        User_From_long = My_long;
                    } else {
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(My_lat, My_long))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));
                        User_From_lat = My_lat;
                        User_From_long = My_long;

                    }

                }else {
                    ride_later=false;
                    Intent o = new Intent(GooglemapApp.this, Ride_later_tabs.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    startActivity(o);
                }
            }else{

                new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                        .setIcon(R.drawable.logo)
                        .setTitle("Location not found")
                        .setMessage("This may be for slow internet connection also!")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new FetchCordinates().execute();
                            }
                        })
                        .show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (My_lat == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                try {

                    My_lat = location.getLatitude();
                    My_long = location.getLongitude();


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),"Unable to get Location"
                            , Toast.LENGTH_LONG).show();
                }

            }

        }

    }
    @Override
    public void onLocationChanged(Location location) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent  ) {

           if (!animate || !clicK) {
                if (location != null) {
                    mCurrentLocation = location;
                    bearing = location.getBearing();
                    My_lat = mCurrentLocation.getLatitude();
                    My_long = mCurrentLocation.getLongitude();
                    if (TextUtils.isEmpty(My_address.getText().toString())) {
                        My_address.setText(getCompleteAddressString(My_lat, My_long));
                    }

                    if (!first && !drop && googleMap != null) {
                        first = true;
                        CameraPosition googlePlex;
                        googlePlex = CameraPosition.builder()
                                .target(new LatLng(My_lat, My_long))
                                .zoom(18) // Sets the zoom
                                .build(); // Crea
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                    }

                    if (UNIQUE_RIDE != null) {


                        if (go) {
                            if (!isMyServiceRunning(mSensorService.getClass())) {
                                startService(mServiceIntent);
                            }
                        }



                    }
                }
                if (drop) {
                    markerPoints = new ArrayList<LatLng>();
                    markerPoints.clear();
                    if (User_From_lat != 0 && User_To_lat != 0) {
                        markerPoints.add(new LatLng(User_From_lat, User_From_long));

                        markerPoints.add(new LatLng(User_To_lat, User_To_long));

                        if (markerPoints.size() == 2) {
                            LatLng origin = markerPoints.get(0);
                            LatLng dest = markerPoints.get(1);
                            String url = getDirectionsUrl(origin, dest);
                            new DownloadTask().execute(url);
                            SetZoomDrop(markerPoints);
                        }
                    }
                }

            }
        }else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    private class FirebaseDataListener_s implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount() != 0) {
                if (UNIQUE_RIDE == null) {
                    Got_amb = false;
                    Got_suv = false;
                    Got_pickup = false;
                    Got_mini = false;
                    Got_sedan = false;
                    Got_micro = false;
                    Online_Driver.clear();
                    for(int i=0;i<markers.size();i++){
                        markers.get(i).setVisible(false);
                    }

                        Map<String, Object> objectMap = null;

                        objectMap = (HashMap<String, Object>)
                                dataSnapshot.getValue();
                        assert objectMap != null;

                        for (Object obj : objectMap.values()) {
                            if (obj instanceof Map) {

                                Map<String, Object> mapObj = (Map<String, Object>) obj;
                                if (mapObj.get("First_Latitude") != null && mapObj.get("First_Longitude") != null
                                        && mapObj.get("Driver_Phone_no") != null && mapObj.get("Driver_Vehicle") != null
                                        && mapObj.get("Offline") != null && mapObj.get("OnRide") != null) {

                                    String off = mapObj.get("Offline").toString();
                                    String ride = mapObj.get("OnRide").toString();
                                    if (off.contains("NO") && ride.contains("NO")) {
                                        double dist = SphericalUtil.computeDistanceBetween(new LatLng(My_lat, My_long), new LatLng(Double.parseDouble((String) mapObj.get("First_Latitude")), Double.parseDouble((String) mapObj.get("First_Longitude")))) / 1000;
                                        Online_Driver.add(mapObj.get("Driver_Phone_no").toString());
                                        if (mapObj.get("Driver_Vehicle").toString().contains("MINI") && !Got_mini) {
                                            if (dist < 2) {
                                                Lmini.setVisibility(View.VISIBLE);
                                                Mini_time.setVisibility(View.VISIBLE);
                                                pmini.setVisibility(View.GONE);
                                                Mini_time.setText("2 Min");
                                                Got_mini = true;

                                            } else if (dist > 2 && dist < 5 && !Got_mini) {
                                                Lmini.setVisibility(View.VISIBLE);
                                                Mini_time.setVisibility(View.VISIBLE);
                                                pmini.setVisibility(View.GONE);
                                                Mini_time.setText("5 Min");
                                                Got_mini = true;

                                            } else if (dist > 5 && !Got_mini) {
                                                Lmini.setVisibility(View.VISIBLE);
                                                Mini_time.setVisibility(View.VISIBLE);
                                                pmini.setVisibility(View.GONE);
                                                Mini_time.setText("No Cab");
                                                Got_mini = false;
                                            }

                                        } else if (!Got_mini) {

                                            Lmini.setVisibility(View.GONE);
                                        }
                                        if (mapObj.get("Driver_Vehicle").toString().contains("MICRO") && !Got_micro) {

                                            if (dist < 2) {
                                                Lmicro.setVisibility(View.VISIBLE);
                                                Micro_time.setVisibility(View.VISIBLE);
                                                pmicro.setVisibility(View.GONE);
                                                Micro_time.setText("2 Min");
                                                Got_micro = true;

                                            } else if (dist > 2 && dist < 5 && !Got_micro) {
                                                Lmicro.setVisibility(View.VISIBLE);
                                                Micro_time.setVisibility(View.VISIBLE);
                                                pmicro.setVisibility(View.GONE);
                                                Micro_time.setText("5 Min");
                                                Got_micro = true;

                                            } else if (dist > 5 && !Got_micro) {
                                                Lmicro.setVisibility(View.VISIBLE);
                                                Micro_time.setVisibility(View.VISIBLE);
                                                pmicro.setVisibility(View.GONE);
                                                Micro_time.setText("No Cab");
                                                Got_micro = false;
                                            }

                                        } else if (!Got_micro) {
                                            Lmicro.setVisibility(View.GONE);
                                        }
                                        if (mapObj.get("Driver_Vehicle").toString().contains("SEDAN") && !Got_sedan) {

                                            if (dist < 2) {
                                                Lsedan.setVisibility(View.VISIBLE);
                                                Sedan_time.setVisibility(View.VISIBLE);
                                                psedan.setVisibility(View.GONE);
                                                Sedan_time.setText("2 Min");
                                                Got_sedan = true;

                                            } else if (dist > 2 && dist < 5 && !Got_sedan) {
                                                Lsedan.setVisibility(View.VISIBLE);
                                                Sedan_time.setVisibility(View.VISIBLE);
                                                psedan.setVisibility(View.GONE);
                                                Sedan_time.setText("5 Min");
                                                Got_sedan = true;

                                            } else if (dist > 5 && !Got_sedan) {
                                                Lsedan.setVisibility(View.VISIBLE);
                                                Sedan_time.setVisibility(View.VISIBLE);
                                                psedan.setVisibility(View.GONE);
                                                Sedan_time.setText("No Cab");
                                                Got_sedan = false;
                                            }

                                        } else if (!Got_sedan) {
                                            Lsedan.setVisibility(View.GONE);
                                        }
                                        if (mapObj.get("Driver_Vehicle").toString().contains("SUV") && !Got_suv) {

                                            if (dist < 2) {
                                                Lsuv.setVisibility(View.VISIBLE);
                                                Suv_time.setVisibility(View.VISIBLE);
                                                psuv.setVisibility(View.GONE);
                                                Suv_time.setText("2 Min");
                                                Got_suv = true;

                                            } else if (dist > 2 && dist < 5 && !Got_suv) {
                                                Lsuv.setVisibility(View.VISIBLE);
                                                Suv_time.setVisibility(View.VISIBLE);
                                                psuv.setVisibility(View.GONE);
                                                Suv_time.setText("5 Min");
                                                Got_suv = true;

                                            } else if (dist > 5 && !Got_sedan) {
                                                Lsuv.setVisibility(View.VISIBLE);
                                                Suv_time.setVisibility(View.VISIBLE);
                                                psuv.setVisibility(View.GONE);
                                                Suv_time.setText("No Cab");
                                                Got_suv = false;
                                            }

                                        } else if (!Got_suv) {
                                            Lsuv.setVisibility(View.GONE);
                                        }

                                        if (mapObj.get("Driver_Vehicle").toString().contains("PICKUP") && !Got_pickup) {

                                            if (dist < 2) {
                                                Lpick.setVisibility(View.VISIBLE);
                                                Pickup_time.setVisibility(View.VISIBLE);
                                                pmuv.setVisibility(View.GONE);
                                                Pickup_time.setText("2 Min");
                                                Got_pickup = true;

                                            } else if (dist > 2 && dist < 5 && !Got_pickup) {
                                                Lpick.setVisibility(View.VISIBLE);
                                                Pickup_time.setVisibility(View.VISIBLE);
                                                pmuv.setVisibility(View.GONE);
                                                Pickup_time.setText("5 Min");
                                                Got_pickup = true;

                                            } else if (dist > 5 && !Got_pickup) {
                                                Lpick.setVisibility(View.VISIBLE);
                                                Pickup_time.setVisibility(View.VISIBLE);
                                                pmuv.setVisibility(View.GONE);
                                                Pickup_time.setText("No Cab");
                                                Got_pickup = false;
                                            }

                                        } else if (!Got_pickup) {
                                            Lpick.setVisibility(View.GONE);
                                        }

                                        if (mapObj.get("Driver_Vehicle").toString().contains("AMBULANCE") && !Got_amb) {

                                            if (dist < 2) {
                                                Lamb.setVisibility(View.VISIBLE);
                                                Amb_time.setVisibility(View.VISIBLE);
                                                pamb.setVisibility(View.GONE);
                                                Amb_time.setText("2 Min");
                                                Got_amb = true;

                                            } else if (dist > 2 && dist < 5 && !Got_amb) {
                                                Lamb.setVisibility(View.VISIBLE);
                                                Amb_time.setVisibility(View.VISIBLE);
                                                pamb.setVisibility(View.GONE);
                                                Amb_time.setText("5 Min");
                                                Got_amb = true;

                                            } else if (dist > 5 && !Got_amb) {
                                                Lamb.setVisibility(View.VISIBLE);
                                                Amb_time.setVisibility(View.VISIBLE);
                                                pamb.setVisibility(View.GONE);
                                                Amb_time.setText("No Cab");
                                                Got_amb = false;

                                            }
                                        } else if (!Got_amb) {
                                            Lamb.setVisibility(View.GONE);
                                        }
                                        String mobi = mapObj.get("Driver_Phone_no").toString();
                                        mPathPolygonPoint.add(new LatLng(Double.parseDouble((String) mapObj.get("First_Latitude")), Double.parseDouble((String) mapObj.get("First_Longitude"))));
                                        if (markers.size() != 0 && googleMap != null && mPathPolygonPoint.size() != 0 ) {
                                            for (int i = 0; i < markers.size(); i++) {
                                                if (markers.get(i).getTitle().contains(mobi)) {
                                                    if(markers.get(i).getPosition().longitude!=0.0) {
                                                        Location temp = new Location("this");
                                                        temp.setLatitude(mPathPolygonPoint.get(0).latitude);
                                                        temp.setLongitude(mPathPolygonPoint.get(0).longitude);
                                                        Location temp1 = new Location("thiss");
                                                        temp1.setLatitude(markers.get(i).getPosition().latitude);
                                                        temp1.setLongitude(markers.get(i).getPosition().longitude);
                                                        if(temp.distanceTo(temp1)>10) {
                                                           startAnimationFromBackgroundThread(markers.get(i), temp);
                                                            //animateCarMoveAbc(markers.get(i),markers.get(i).getPosition(),mPathPolygonPoint.get(0),mapObj.get("Driver_Vehicle").toString());
                                                        }else {
                                                            markers.get(i).setAlpha(1.0f);
                                                            markers.get(i).setPosition(mPathPolygonPoint.get(0));
                                                        }
                                                    }else {
                                                        markers.get(i).setAlpha(1.0f);
                                                        markers.get(i).setPosition(mPathPolygonPoint.get(0));
                                                    }
                                                    markers.get(i).setVisible(true);
                                                    markers.get(i).hideInfoWindow();
                                                    mPathPolygonPoint.clear();

                                                }
                                            }
                                        }
                                    }
                                }
                                    }
                                }

                }else{
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener());

                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    public void startAnimationFromBackgroundThread(final Marker mmarker, final Location latLng1) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // this runs on a background thread
                Log.v(TAG, "Worker thread id:" + Thread.currentThread().getId());
               runOnUiThread(new Runnable() {
                    public Bitmap mMarkerIcon;

                    @Override
                    public void run() {
                        Log.v(TAG, "Animation thread id:" + Thread.currentThread().getId());
                        animateMarker(latLng1,mmarker);
                    }
                });
            }
        });
    }

    public static void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(500); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override public void onAnimationUpdate(ValueAnimator animation) {

                    try {
                                float v = animation.getAnimatedFraction();
                                LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                                marker.setPosition(newPosition);
                                marker.setAnchor(.5f, .5f);
                                marker.setRotation((float) (com.google.maps.android.SphericalUtil.computeHeading(marker.getPosition(), new LatLng(destination.getLatitude(), destination.getLongitude()))));
                                marker.setAlpha((float) animation.getAnimatedValue());


                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }


            });

            valueAnimator.start();
        }
    }
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }
    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    private void startLocationUpdat() {
        // Begin by checking if the device has the necessary location settings.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());

        updateUI();

    }
    private void updateUI() {

        updateLocationUI();
    }

    private void updateLocationUI() {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (mCurrentLocation != null && !clicK) {
                My_lat = mCurrentLocation.getLatitude();
                My_long = mCurrentLocation.getLongitude();
                if (TextUtils.isEmpty(My_address.getText().toString())) {
                    My_address.setText(getCompleteAddressString(My_lat, My_long));
                }

                if (!first && !drop) {
                    first = true;
                    CameraPosition googlePlex;
                    googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(18) // Sets the zoom
                            .build(); // Crea
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                }
                if (UNIQUE_RIDE != null) {

                    mDatabase.child("Accepted_Ride").child(con).child("UserLat").setValue(My_lat);
                    mDatabase.child("Accepted_Ride").child(con).child("UserLong").setValue(My_long);

                    if (OTP == null) {
                        new GetDriver().execute();
                    }

                    if (go) {

                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }

                    }


                    if (wait) {
                        if (OTP == null) {
                            new GetDriver().execute();
                        }
                    }

                    if (Driver_Start != null) {
                        if (Driver_Start.contains("YES") ) {
                            Cancel_ride.setAnimation(animslideD);
                            SearchingLL.setAnimation(animslideD);
                            Cancel_ride.setVisibility(View.GONE);
                            SearchingLL.setVisibility(View.GONE);
                            ChooseCar.setVisibility(View.GONE);
                            Choose_car_below2.setVisibility(View.GONE);
                            Choose_car_below3.setVisibility(View.GONE);
                            Choose_car_below3.setEnabled(false);
                            Choose_car_below3.setEnabled(false);
                            Cancel_again.setClickable(false);
                            bottomNavigationView.setVisibility(View.VISIBLE);
                            bottomNavigationView.bringToFront();
                            mySOS.setVisibility(View.VISIBLE);
                            AllRl.setAnimation(animslideU);
                            AllRl.setVisibility(View.VISIBLE);
                            Ride_otp.setVisibility(View.VISIBLE);
                            Choose_car_below1.setVisibility(View.VISIBLE);
                            FFv.setAnimation(animFadeout);
                            FFv.setVisibility(View.GONE);
                            CenterMarker.setVisibility(View.GONE);
                            Text1.setText("Enjoy ");
                            Text2.setText("the Ride");
                            wait = false;
                            got = false;
                            stop = false;
                            drop = false;
                            go = true;
                            ask = false;
                            if (googleMap != null) {
                                googleMap.setTrafficEnabled(true);
                            }
                            if (Minimum_rate != 0 && Hourly_rate != 0) {
                                mDatabase.child("Accepted_Ride").child(con).child("Hourly_rate").setValue(Hourly_rate);
                                mDatabase.child("Accepted_Ride").child(con).child("Minimum_rate").setValue(Minimum_rate);
                                mDatabase.child("Accepted_Ride").child(con).child("Distance").setValue(dft.format(Distance));
                                mDatabase.child("Accepted_Ride").child(con).child("Estimated_fare").setValue(dft.format(Minimum_rate+Hourly_rate*Distance));
                            }else{
                                new GetSettings().execute();
                            }
                        } else {
                            stopService(mServiceIntent);

                        }


                    }else{
                        if (User_accept != null) {
                            CenterMarker.setVisibility(View.GONE);
                            H13.setAnimation(animFadeout);
                            H13.setVisibility(View.GONE);
                            ChooseCar.setVisibility(View.GONE);
                            Choose_car_below2.setVisibility(View.VISIBLE);
                            Choose_car_below3.setAnimation(animslideD);
                            Choose_car_below3.setVisibility(View.GONE);
                            Choose_car_below3.setEnabled(false);
                            Choose_car_below3.setEnabled(false);
                            Cancel_again.setClickable(false);
                            bottomNavigationView.setAnimation(animslideD);
                            bottomNavigationView.setVisibility(View.GONE);
                            FFv.setAnimation(animFadeout);
                            FFv.setVisibility(View.GONE);

                        }
                    }


                }

            }
            if(drop){
                markerPoints = new ArrayList<LatLng>();
                markerPoints.clear();
                if (User_From_lat != 0 && User_To_lat != 0 ) {
                    markerPoints.add(new LatLng(User_From_lat, User_From_long));

                    markerPoints.add(new LatLng(User_To_lat, User_To_long));

                    if (markerPoints.size() == 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        new DownloadTask().execute(url);
                    }
                }
            }

        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if(_reached){
                final Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Your ride has rechaed location", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recreate();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).setVisible(false);
            }
            mDatabase.child("Driver_Online").addValueEventListener(new FirebaseDataListener_s());

            if(Pick_up!=null){
                User_from=Pick_up;
                LatLng l1=getLocationFromAddress(GooglemapApp.this,Pick_up);
                if(l1!=null) {
                    User_From_lat = l1.latitude;
                    User_From_long = l1.longitude;
                }
                My_address.setText(Pick_up);

            }
            if(Drop_at!=null){
                drop=true;
                User_to=Drop_at;
                LatLng l2=getLocationFromAddress(GooglemapApp.this,Drop_at);
                if(l2!=null) {
                    User_To_lat = l2.latitude;
                    User_To_long = l2.longitude;
                    Destination_address.setText(Drop_at);
                    mapFragment.setOnDragListener(null, false);
                    CenterMarker.setVisibility(View.GONE);
                    markerPoints = new ArrayList<LatLng>();
                    markerPoints.clear();
                    if (User_From_lat != 0) {
                        markerPoints.add(new LatLng(User_From_lat, User_From_long));
                    }
                    if (User_To_lat != 0) {
                        markerPoints.add(l2);
                    }

                    if (markerPoints.size() == 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        new DownloadTask().execute(url);
                        SetZoomDrop(markerPoints);

                    }
   }
            }else{
                CenterMarker.setVisibility(View.VISIBLE);
            }

            if(My_lat==0) {
                if (checkPermissions()) {

                    if (mGoogleApiClient.isConnected()) {
                        startLocationUpdates();

                    } else {
                        startLocationUpdat();


                    }
                }
            } else if (!checkPermissions()) {
                requestPermissions();
            }

            if(!TextUtils.isEmpty(My_address.getText().toString())){
                Heart_d1.setVisibility(View.VISIBLE);
                Heart_d2.setVisibility(View.GONE);
            }else {
                Heart_d1.setVisibility(View.GONE);
                Heart_d2.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(Destination_address.getText().toString())){
                Heart_t1.setVisibility(View.VISIBLE);
                Heart_t2.setVisibility(View.GONE);
            }else{
                Heart_t1.setVisibility(View.GONE);
                Heart_t2.setVisibility(View.GONE);
            }


            if (_phoneNo != null) {
                new GetSettings().execute();
            }


            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.REGISTRATION_COMPLETE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.PUSH_NOTIFICATION));
            NotificationUtils.clearNotifications(getApplicationContext());



        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    private void open_share() {
        final String strShareMessage = "\nLet me recommend you this application\n\n"+ "https://play.google.com/store/apps/details?id=" + getPackageName();
        final Dialog dialog = new Dialog(GooglemapApp.this);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        ImageView WhatsApp,Messenger,Email,Message,Facebook;
        LayoutInflater inflater = getLayoutInflater();
        dialog.setContentView(R.layout.share_data);;
        HorizontalScrollView hview = (HorizontalScrollView) dialog.findViewById(R.id.choose_share);
        hview.setSmoothScrollingEnabled(true);
        hview.fullScroll(HorizontalScrollView.FOCUS_RIGHT/FOCUS_LEFT);
        WhatsApp=dialog.findViewById(R.id.whatsapps);
        Messenger=dialog.findViewById(R.id.messengers);
        Email=dialog.findViewById(R.id.emails);
        Facebook=dialog.findViewById(R.id.faceboks);
        Message=dialog.findViewById(R.id.messages);


        WhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = strShareMessage;
                // change with required  application package

                intent.setPackage("com.whatsapp");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(intent, text));
                } else {

                    Toast.makeText(getApplicationContext(), "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
                dialog.cancel();
            }
        });
        Messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent
                        .putExtra(Intent.EXTRA_TEXT,
                                strShareMessage);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("text/plain");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"hellocab@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "ERROR");
                i.putExtra(Intent.EXTRA_TEXT   , strShareMessage);
                try {
                    startActivity(Intent.createChooser(i, "Email us.."));
                } catch (android.content.ActivityNotFoundException ex) {

                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView().getRootView(),"There are no email clients installed.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                dialog.cancel();
            }
        });
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = strShareMessage;
                // change with required  application package

                intent.setPackage("com.facebook.katana");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(intent, text));
                } else {

                    Toast.makeText(getApplicationContext(), "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
                dialog.cancel();
            }
        });
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","Share HelloCab");

                smsIntent.putExtra("sms_body",strShareMessage);
                startActivity(smsIntent);
                dialog.cancel();
            }

        });
        // Create the alert dialog

        dialog.show();

    }

    private class GetCodes extends AsyncTask<String, Void, Void> {


        private String _ref;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);

            _ref=arg0[0];

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");

                    if (_phoneNo != null) {
                        if (!TextUtils.isEmpty(arg0[0])) {
                            for (int i = 0; i < User.length(); i++) {
                                JSONObject c = User.getJSONObject(i);
                                if (!_phoneNo.matches(c.getString("Phone_No"))) {
                                    String relation = c.getString("Reference_Code");
                                    if (!relation.contains("null")) {
                                        if (relation.matches(_ref)) {
                                            for (int j = 0; j < Settings_default.length(); j++) {
                                                JSONObject d = Settings_default.getJSONObject(j);
                                                Discount_value = String.valueOf(d.getInt("User_refernce_code_coupon_Amt"));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }


                    }

                    // looping through All Contacts

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
            if(Discount_value!=null) {
                mDatabase.child("Accepted_Ride").child(con).child("Coupon_code").setValue(_ref);
                mDatabase.child("Accepted_Ride").child(con).child("Coupon_value").setValue(Discount_value);
                new PostDataTOServer().execute(_ref);
            }else{
                Snackbar.make(coordinatorLayout,"Please add another Coupon", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    private class PostDataTOServer  extends AsyncTask<String, Integer, String> {


        private boolean success;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            return uploadFile(args[0]);
        }

        private String uploadFile(String arg) {
            // TODO Auto-generated method stub
            String res = null;



            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_phoneNo)
                        .addFormDataPart("coupon", arg)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_COUPON_ADD)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


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
            progressBar.setVisibility(View.GONE);
            if (success) {
                Snackbar.make(coordinatorLayout,"Coupon added", Snackbar.LENGTH_SHORT).show();

            } else {

                Snackbar.make(coordinatorLayout,"Please check coupon", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    private class GetPromos extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            mPromos.clear();

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

                    JSONArray Promos = jsonObj.getJSONArray("Promo_codes");
                    JSONArray User = jsonObj.getJSONArray("User");
                    // looping through All Contacts
                    if (_phoneNo != null) {

                        for (int i = 0; i < Promos.length(); i++) {
                            JSONObject c = Promos.getJSONObject(i);

                            Promo item=new Promo();
                            item.setPromo_Code(c.getString("Promo_Code"));
                            item.setDiscount_Value(c.getDouble("Discount_Value"));
                            item.setDrop_Location(c.getString("Drop_Location"));
                            item.setApp_Invitation(c.getString("App_Invitation"));
                            item.setApplicable_Place(c.getString("Applicable_Place"));
                            mPromos.add(item);
                        }


                    }


                        for (int i = 0; i < User.length(); i++) {
                            JSONObject c = User.getJSONObject(i);
                            String relation = c.getString("Phone_No");
                            if (!relation.contains("null")) {
                                if (relation.matches(_phoneNo)) {
                                    if (!c.isNull("User_Referrence_Code") && !TextUtils.isEmpty(c.getString("User_Referrence_Code"))) {
                                        _userRef = true;
                                        break;
                                    }
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
            SwipeDismissDialog.Builder builder = new SwipeDismissDialog.Builder(GooglemapApp.this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.apply_coupon,null);

            // Set the custom layout as alert dialog view
            builder.setView(dialogView);


            TextView text11 = (TextView) dialogView.findViewById(R.id.textView11);
            Button btn_positive = (Button) dialogView.findViewById(R.id.home_sav);
            Button btn_negative = (Button) dialogView.findViewById(R.id.home_can);
            final EditText app_coupon = (EditText) dialogView.findViewById(R.id.app_coupon);
            RecyclerView Promos=(RecyclerView) dialogView.findViewById(R.id.promoRv);
            //new GetPromos().execute();

            if(mPromos.size()!=0){
                Promos.setVisibility(View.VISIBLE);
                Promo_Adapter sAdapter = new Promo_Adapter(GooglemapApp.this, mPromos);
                sAdapter.notifyDataSetChanged();
                Promos.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(GooglemapApp.this);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                Promos.setLayoutManager(mLayoutManager);
            }else{
                Promos.setVisibility(View.GONE);
            }

            if(!_userRef) {
                text11.setVisibility(View.GONE);
                app_coupon.setHint("Please insert Coupon code");
                app_coupon.setFocusableInTouchMode(true);
                btn_negative.setVisibility(View.VISIBLE);

            }else{
                text11.setVisibility(View.GONE);
                if(mPromos.size()!=0) {
                    app_coupon.setHint("No coupons available");
                }else{
                    app_coupon.setHint("Please select a Coupon code");
                }
                app_coupon.setFocusableInTouchMode(false);
                btn_negative.setVisibility(View.GONE);
            }
            // Create the alert dialog
            final SwipeDismissDialog dialog = builder.setOnSwipeDismissListener(new OnSwipeDismissListener() {
                @Override
                public void onSwipeDismiss(View view, SwipeDismissDirection direction) {
                    Toast.makeText(getApplicationContext(),"No coupon applied",Toast.LENGTH_SHORT).show();
                }
            })
                    .build();

            // Set positive/yes button click listener
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(app_coupon.getText().toString())) {
                        new GetCodes().execute(app_coupon.getText().toString());
                    }
                    dialog.cancel();
                }
            });

            // Set negative/no button click listener
            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Dismiss the alert dialog
                    dialog.cancel();
                }
            });


            Promos.addOnItemTouchListener(
                    new RecyclerTouchListener(GooglemapApp.this, Promos,
                            new RecyclerTouchListener.OnTouchActionListener() {


                                @Override
                                public void onClick(View view, final int position) {
                                    if(Discount_value==null) {

                                        Discount_value = dft.format(mPromos.get(position).getDiscount_Value(position));
                                        app_coupon.setText(mPromos.get(position).getPromo_Code(position));
                                        mDatabase.child("Accepted_Ride").child(con).child("Coupon_code").setValue(mPromos.get(position).getPromo_Code(position));
                                        mDatabase.child("Accepted_Ride").child(con).child("Coupon_value").setValue(Discount_value);

                                    }else{
                                        Toast.makeText(getApplicationContext(),"Coupon alreday applied",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onRightSwipe(View view, int position) {

                                }

                                @Override
                                public void onLeftSwipe(View view, int position) {

                                }
                            }));

            dialog.show();

        }
    }

    @Override
    public void onClick(View view) {
        final AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        switch (view.getId()) {

            case R.id.confirm_ride:
                if(markers.size()!=0){
                    for(int i=0;i<markers.size();i++){
                        markers.get(i).setVisible(false);
                    }
                }
                ask=false;
                wait=true;
                go=false;
                stop=false;
                got=false;
                AllRl.setAnimation(animslideD);
                AllRl.setVisibility(View.VISIBLE);
                AllRl.setAnimation(animslideU);
                ChooseCar.setAnimation(animslideD);
                ChooseCar.setVisibility(View.GONE);
                Choose_car_below2.setVisibility(View.VISIBLE);
                Choose_car_below3.setAnimation(animslideD);
                Choose_car_below3.setVisibility(View.GONE);
                Choose_car_below3.setEnabled(false);
                Choose_car_below3.setEnabled(false);
                Cancel_again.setClickable(false);
                Choose_car_below1.setVisibility(View.VISIBLE);
                bottomNavigationView.setAnimation(animslideD);
                bottomNavigationView.setVisibility(View.GONE);
                Cancel_ride.setVisibility(View.GONE);
                SearchingLL.setVisibility(View.GONE);
                mDatabase.child("Accepted_Ride").child(con).child("UserAccept").setValue("YES");

                break;
            case R.id.car_choosen_1:
                new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                        .setTitle("Change of vehicle ")
                        .setMessage("Get "+Choose_1.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] pars1 = Choose_1.getText().toString().split("\\@");
                                Car_type=pars1[0];
                                new PostAnotherCardata().execute(Car_type);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


                break;
            case R.id.car_choosen_2:
                new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                        .setTitle("Change of vehicle ")
                        .setMessage("Get "+Choose_2.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] pars2 = Choose_2.getText().toString().split("\\@");
                                Car_type=pars2[0];

                                new PostAnotherCardata().execute(Car_type);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.car_choosen_3:
                new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                        .setTitle("Change of vehicle ")
                        .setMessage("Get "+Choose_3.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] pars3 = Choose_3.getText().toString().split("\\@");
                                Car_type=pars3[0];

                                new PostAnotherCardata().execute(Car_type);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.car_choosen_4:
                new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                        .setTitle("Change of vehicle ")
                        .setMessage("Get "+Choose_4.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] pars4 = Choose_4.getText().toString().split("\\@");
                                Car_type=pars4[0];

                                if (UNIQUE_RIDE != null) {
                                    String[] pars = UNIQUE_RIDE.split("\\.");
                                    con = TextUtils.join("", pars);
                                    mDatabase.child("Accepted_Ride").child(con).removeValue();
                                }
                                new PostAnotherCardata().execute(Car_type);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.ride_later:
                stopLocationUpdates();
                if(_phoneNo==null){
                    Intent o = new Intent(this, SmsActivity.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    startActivity(o);
                    finish();
                }else{
                    if(My_lat!=0) {
                        Intent o = new Intent(GooglemapApp.this, Ride_later_ok.class);
                        if(My_lat!=0) {
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
                            startActivity(o);
                        }else{
                            new FetchCordinates().execute();
                        }
                    }else{
                        ride_later=true;
                        new FetchCordinates().execute();
                    }
                }
                break;
            case R.id.ride_now:
                ask=true;
                wait=false;
                go=false;
                stop=false;
                got=false;

                if(_phoneNo==null){
                    Intent o = new Intent(this, SmsActivity.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    startActivity(o);
                    finish();
                }else{

                    if(!TextUtils.isEmpty(Destination_address.getText().toString())){
                        if(Car_type!=null) {
                            new PostBookdata().execute();


                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Please select a vehicle above!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Please input drop location!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        L1.bringToFront();
                        L2.invalidate();
                    }
                }

                break;

            case R.id.myLocationCustomButton:
                clicK = true;
                if (My_lat != 0) {

                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(My_lat, My_long)) // Sets the new camera position
                            .zoom(18) // Sets the zoom
                            .build(); // Crea
                    GooglemapApp.this.googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position));
                    clicK = false;
                    new StoreLatLong().execute();

                }
                break;

            case R.id.et_myLocation_Address:
                L1.bringToFront();
                L2.invalidate();
                Pick.setVisibility(View.VISIBLE);
                Drop.setVisibility(View.GONE);
                L1.setAlpha((float) 1.0);
                L2.setAlpha((float) 0.7);
                My_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                My_address.setGravity(Gravity.CENTER);
                Destination_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
                Destination_address.setGravity(Gravity.BOTTOM);
                My_loc=true;
                Intent o2 = new Intent(GooglemapApp.this, Place_selected.class);
                o2.putExtra("mylat", My_lat);
                o2.putExtra("mylong", My_long);
                if(Drop_at!=null) {
                    o2.putExtra("drop", Drop_at);
                }
                startActivity(o2);
                finish();
                break;

            case R.id.et_destLocation_Address:
                My_loc=false;
                L2.bringToFront();
                L1.invalidate();
                Pick.setText(My_address.getText().toString());
                L1.setAlpha((float) 0.7);
                L2.setAlpha((float) 1.0);
                Pick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
                Destination_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                Destination_address.setGravity(Gravity.CENTER);
                Drop.setVisibility(View.VISIBLE);

                Intent o1 = new Intent(GooglemapApp.this, Place_drop.class);
                o1.putExtra("mylat", My_lat);
                o1.putExtra("mylong", My_long);
                if(Pick_up!=null) {
                    o1.putExtra("pick", Pick_up);
                }else{
                    o1.putExtra("pick", My_address.getText().toString());
                }
                startActivity(o1);
                finish();
                break;

            case R.id.edit_profile:
                if(_phoneNo==null){
                    Edit_profile.setVisibility(View.GONE);
                }else {

                    Intent o = new Intent(this, Update_profile.class);
                    o.putExtra("mylat", My_lat);
                    o.putExtra("mylong", My_long);
                    startActivity(o);
                    finish();
                }
                break;

            case R.id.car_mini:
                Mini_image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Micro_image.clearColorFilter();
                Sedan_image.clearColorFilter();
                Suv_image.clearColorFilter();
                Pickup_image.clearColorFilter();
                Amb_image.clearColorFilter();
                Car_type="MINI";
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
                Lmini.setVisibility(View.VISIBLE);
                break;
            case R.id.car_micro:
                Micro_image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Mini_image.clearColorFilter();
                Sedan_image.clearColorFilter();
                Suv_image.clearColorFilter();
                Pickup_image.clearColorFilter();
                Amb_image.clearColorFilter();
                Car_type="MICRO";
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
                break;
            case R.id.car_sedan:
                Sedan_image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Mini_image.clearColorFilter();
                Micro_image.clearColorFilter();
                Suv_image.clearColorFilter();
                Pickup_image.clearColorFilter();
                Amb_image.clearColorFilter();
                Car_type="SEDAN";
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
                break;
            case R.id.car_suv:
                Suv_image .setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Mini_image.clearColorFilter();
                Micro_image.clearColorFilter();
                Sedan_image.clearColorFilter();
                Pickup_image.clearColorFilter();
                Amb_image.clearColorFilter();
                Car_type="SUV";
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
               // Lmini.setVisibility(View.GONE);
              //  Lmicro.setVisibility(View.GONE);
               // Lsedan.setVisibility(View.GONE);
               // Lpick.setVisibility(View.GONE);
                Lsuv.setVisibility(View.VISIBLE);
                break;
            case R.id.car_muv:
                Pickup_image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Mini_image.clearColorFilter();
                Micro_image.clearColorFilter();
                Sedan_image.clearColorFilter();
                Suv_image.clearColorFilter();
                Amb_image.clearColorFilter();
                Car_type="PICKUP";
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
                break;

            case R.id.car_amb:
                Amb_image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Mini_image.clearColorFilter();
                Micro_image.clearColorFilter();
                Sedan_image.clearColorFilter();
                Suv_image.clearColorFilter();
                Pickup_image.clearColorFilter();
                Car_type="AMBULANCE";
                if(markers.size()!=0){
                    for(int i=0;i<markers.size();i++){
                        if(!markers.get(i).getSnippet().contains(Car_type)) {
                            markers.get(i).setVisible(false);
                        }
                    }
                }
                Got_amb = false;
                Got_suv = false;
                Got_pickup = false;
                Got_mini = false;
                Got_sedan = false;
                Got_micro = false;
                break;
            case R.id.cancel_again:
                new PostDeleteData().execute();
                break;
            case R.id.cancel_ride:
                ask=false;
                wait=false;
                go=false;
                stop=false;
                animate=false;
                Drop_at=null;
                cancel=true;
                if(polylineFinal!=null){
                    polylineFinal.remove();
                    googleMap.clear();
                }
                if(marker!=null){
                    marker.setVisible(false);
                }
                if(markerd!=null){
                    markerd.setVisible(false);
                }

                new PostDeleteRide().execute();
                break;

            case R.id.heart_01:
                if(!TextUtils.isEmpty(My_address.getText().toString())) {
                    open_home(My_address.getText().toString());
                    Heart_d2.setVisibility(View.VISIBLE);
                    Heart_d1.setVisibility(View.GONE);
                }
                break;
            case R.id.heart_11:
                if(!TextUtils.isEmpty(Destination_address.getText().toString())) {
                    open_home(Destination_address.getText().toString());
                    Heart_t2.setVisibility(View.VISIBLE);
                    Heart_t1.setVisibility(View.GONE);
                    break;
                }


            case R.id.call_driver:
                if(Driver_phone!=null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Driver_phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.track_driver:
                if (UNIQUE_RIDE != null) {
                    if(Driver_lat!=0) {
                        CameraPosition googlePlex;
                        googlePlex = CameraPosition.builder()
                                .target(new LatLng(Driver_lat, Driver_long))
                                .zoom(18) // Sets the zoom
                                .build(); // Crea
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                    }

                }
                break;
            case R.id.mySOS:
                //takeScreenshot();
                new GetEmergencyContacts().execute();
                break;
        }

    }
    private class GetEmergencyContacts extends AsyncTask<Void, Void, Void> {


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

                    JSONArray User = jsonObj.getJSONArray("User");
                    for (int i = 0; i < User.length(); i++) {
                        JSONObject c = User.getJSONObject(i);
                        String relation = c.getString("Phone_No");
                        if (!relation.contains("null")) {

                            if (relation.matches(_phoneNo)) {
                                User_ID=c.getInt("ID");
                            }

                        }
                    }

                    JSONArray Emergency = jsonObj.getJSONArray("Emergency_contacts");

                    for (int i = 0; i < Emergency.length(); i++) {
                        JSONObject c = Emergency.getJSONObject(i);
                        if (!c.isNull("User_ID") && c.getInt("User_ID")==User_ID) {

                            Emergencycontacts.add(c.getString("Contact_Phone_No"));
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
            if(Emergencycontacts.size()!=0){
                for(int i=0;i<Emergencycontacts.size();i++) {
                    new PostEmergencyMsg().execute(Emergencycontacts.get(i));
                }
            }else{
                Intent o1 = new Intent(GooglemapApp.this, Emergency_contacts.class);
                o1.putExtra("mylat", My_lat);
                o1.putExtra("mylong", My_long);
                startActivity(o1);
            }

        }
    }
    private class PostEmergencyMsg  extends AsyncTask<String, Integer, String> {


        private boolean success;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();


        }

        protected String doInBackground(String... args) {

            mobileno=args[0];
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_phoneNo)
                        .addFormDataPart("emobile", mobileno)
                        .addFormDataPart("unique_ride",UNIQUE_RIDE)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_EMERGENCY_MOBILE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


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
                Snackbar.make(getWindow().getDecorView().getRootView(),"Informed mobile no."+mobileno, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(),"Please check mobile no.", Snackbar.LENGTH_LONG).show();

            }

        }


    }
    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private int Online=0 ,New_Version_Importance=0;
        private String New_Version;

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
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");
                    JSONArray Version = jsonObj.getJSONArray("Version");


                    // looping through All Contacts
                    if (_phoneNo != null) {
                        for (int i = 0; i < Settings_default.length(); i++) {
                            JSONObject c = Settings_default.getJSONObject(i);
                            Online = c.getInt("Service_Online");
                            Minimum_KM_Distance_for_Outstation  = c.getInt("Minimum_KM_Distance_for_Outstation");
                        }

                        for (int i = 0; i < Version.length(); i++) {
                            JSONObject c = Version.getJSONObject(i);
                            New_Version = c.getString("Version");
                            New_Version_Importance=c.getInt("Importance");

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

            if(!pref.getNewVersion().matches(New_Version)){
                if(New_Version_Importance==1){
                    new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Update HelloCab")
                            .setMessage("A new version of hellocab is available!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pref.deleteState();
                                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            })
                            .show();
                }else{
                    new android.support.v7.app.AlertDialog.Builder(GooglemapApp.this)
                            .setTitle("Update HelloCab")
                            .setMessage("A new version of hellocab is available!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Online==1){
                                        new GetCustomer().execute();
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                }
            }else{
                if(Online==1){

                    new GetCustomer().execute();

                }
            }

            //mDatabase.child("Driver_Online").addValueEventListener(new FirebaseDataListener_s());

        }
    }



    private class GetCustomer extends AsyncTask<Void, Void, Void> {

        private String User_mobile;
        private double User_review=0;
        private double _dRiview=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            details.clear();
            markers.clear();
            mPromos.clear();
            ask = false;
            wait = false;
            got=false;
            go = false;
            stop = false;
            first=true;
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

                    JSONArray drivers = jsonObj.getJSONArray("Driver_Details");
                    JSONArray rides = jsonObj.getJSONArray("Book_Ride_Now_User");
                    JSONArray gari = jsonObj.getJSONArray("Vehicle_detail");
                    JSONArray User = jsonObj.getJSONArray("User");
                    for (int i = 0; i < User.length(); i++) {
                        JSONObject c = User.getJSONObject(i);
                        String relation = c.getString("Phone_No");
                        if (!relation.contains("null")) {

                            if (relation.matches(_phoneNo)) {
                                User_mobile = c.getString("Phone_No");
                                User_image = c.getString("Photo");
                                User_name = c.getString("Name");
                                User_ID=c.getInt("ID");
                                User_review = c.getDouble("Rating");
                                break;
                            }

                        }
                    }

                    for (int i = 0; i < drivers.length(); i++) {
                        JSONObject c = drivers.getJSONObject(i);
                        if(!c.isNull("Owner_ID")&&!c.isNull("Driving_License_Photo")) {
                            Ride item = new Ride();
                            item.setMobile(c.getString("Phone_No"));
                            item.setDriver_ID(c.getInt("ID"));
                            Driver_ID=c.getInt("ID");
                            for (int j = 0; j < gari.length(); j++) {
                                JSONObject d = gari.getJSONObject(j);
                                if (!d.isNull("Driver_ID") && d.getInt("Driver_ID") == Driver_ID) {
                                    item.setVehicle(d.getString("Vehicle_Type"));
                                }
                            }
                            details.add(item);
                        }

                    }

                    for (int i = 0; i < rides.length(); i++) {
                        JSONObject c = rides.getJSONObject(i);
                        if (!c.isNull("User_ID") && c.getInt("User_ID")==User_ID) {
                            UNIQUE_RIDE = c.getString("Unique_Ride_Code");
                            User_from=c.getString("From_Address");
                            User_to=c.getString("To_Address");
                            User_From_lat=c.getDouble("From_Latitude");
                            User_From_long=c.getDouble("From_Longitude");
                            User_To_lat=c.getDouble("To_Latitude");
                            User_To_long=c.getDouble("To_Longitude");
                            _dRiview=c.getDouble("User_Rating_By_Driver");
                            break;
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
            if (User_name != null && User_mobile!=null  && !TextUtils.isEmpty(User_name)) {
                if(User_review!=0){
                    MyStar.setVisibility(View.VISIBLE);
                    txtName.setText(String.valueOf( User_review));
                }else {
                    MyStar.setVisibility(View.GONE);
                    txtName.setText(User_name);
                }
            } else {
                MyStar.setVisibility(View.GONE);
                txtName.setText(User_mobile);
            }
            if (User_image != null && !TextUtils.isEmpty(User_image)) {
                Picasso.Builder builder = new Picasso.Builder(GooglemapApp.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(User_image).into(profile_image);
            }

            if (UNIQUE_RIDE != null) {
                H12.setAnimation(animFadeout);
                H12.setVisibility(View.GONE);
                rideButtons.setVisibility(View.GONE);
                if(_dRiview==0) {
                    Cancel_ride.setVisibility(View.VISIBLE);
                    Cancel_ride.setVisibility(View.VISIBLE);
                    SearchingLL.setVisibility(View.VISIBLE);
                }
                mapFragment.setOnDragListener(null,false);
                CenterMarker.setVisibility(View.GONE);
                FFv.setAnimation(animFadeout);
                FFv.setVisibility(View.GONE);
                String[] pars = UNIQUE_RIDE.split("\\.");
                con = TextUtils.join("", pars);
                markerPoints = new ArrayList<LatLng>();
                markerPoints.clear();
                Destination_address.setText(User_to);
                My_address.setText(User_from);
                ask = true;
                wait = false;
                got=false;
                go = false;
                stop = false;
                first=true;
                CameraPosition googlePlex;
                googlePlex = CameraPosition.builder()
                        .target(new LatLng(User_From_lat,User_From_long))
                        .zoom(18) // Sets the zoom
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                for(int i=0;i<markers.size();i++){
                    markers.get(i).setVisible(false);
                }

                markerPoints.clear();
                markerPoints.add(new LatLng(User_From_lat,User_From_long));
                markerPoints.add(new LatLng(User_To_lat, User_To_long));

                    if (markerPoints.size() == 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        new DownloadTask().execute(url);

                    }
            } else {
                Cancel_ride.setAnimation(animslideD);
                SearchingLL.setAnimation(animslideD);
                Cancel_ride.setVisibility(View.GONE);
                SearchingLL.setVisibility(View.GONE);
                rideButtons.setAnimation(animslideU);
                rideButtons.setVisibility(View.VISIBLE);
                FFv.setAnimation(animFadein);
                FFv.setVisibility(View.VISIBLE);
                H12.setAnimation(animFadein);
                H12.setVisibility(View.VISIBLE);
                if (details.size() != 0) {
                    for (int i = 0; i < details.size(); i++) {
                        if(details.get(i).getVehicle(i)!=null) {
                            markers.add(createMarker(details.get(i).getLat(i), details.get(i).getLong(i), details.get(i).getMobile(i), details.get(i).getVehicle(i)));
                        }
                    }
                        }
            }
        }
    }
    private class GetDriver extends AsyncTask<Void, Void, Void> {


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
                    JSONArray driver_details = jsonObj.getJSONArray("Driver_Details");

                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Now");
                    JSONArray User = jsonObj.getJSONArray("User");
                    for (int i = 0; i < User.length(); i++) {
                        JSONObject c = User.getJSONObject(i);
                        String relation = c.getString("Phone_No");
                        if (!relation.contains("null")) {

                            if (relation.matches(_phoneNo)) {
                                User_ID=c.getInt("ID");
                                break;
                            }

                        }
                    }

                    for (int i = 0; i < ride.length(); i++) {
                        JSONObject c = ride.getJSONObject(i);
                        if(!c.isNull("User_ID")){
                            int relation = c.getInt("User_ID");
                            if(UNIQUE_RIDE!=null) {
                                if (relation == User_ID && c.getString("Unique_Ride_Code").contains(UNIQUE_RIDE)) {
                                    OTP = c.getString("OTP");
                                    From_address = c.getString("From_Address");
                                    To_address = c.getString("To_Address");
                                    if (!c.isNull("Driver_ID")) {
                                        Driver_ID = c.getInt("Driver_ID");
                                        Distance = c.getDouble("Distance_Travel");
                                        UNIQUE_RIDE = c.getString("Unique_Ride_Code");
                                        User_from=c.getString("From_Address");
                                        User_to=c.getString("To_Address");
                                        User_From_lat=c.getDouble("From_Latitude");
                                        User_From_long=c.getDouble("From_Longitude");
                                        User_To_lat=c.getDouble("To_Latitude");
                                        User_To_long=c.getDouble("To_Longitude");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");
                    //JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");
                    for (int i = 0; i < Settings_Vehicle_Rate.length(); i++) {
                        JSONObject c = Settings_Vehicle_Rate.getJSONObject(i);

                        Rate item=new Rate();
                        item.setVehicle_Type(c.getString("Vehicle_Type"));
                        item.setHourly_Rate(c.getInt("Hourly_Rate"));
                        item.setMinimum_Rate(c.getInt("Minimum_Rate"));
                        mVehcle_option.add(item);
                    }


                    JSONArray gari = jsonObj.getJSONArray("Vehicle_detail");
                    for (int i = 0; i < driver_details.length(); i++) {
                        JSONObject c = driver_details.getJSONObject(i);
                        String relation = c.getString("Phone_No");

                        if(!c.isNull("Owner_ID")&&!c.isNull("Driving_License_Photo")) {
                            if (c.getInt("ID")==Driver_ID) {
                                Driver_name = c.getString("Name");
                                Driver_image = c.getString("Photo");
                                Driver_phone=c.getString("Phone_No");
                                if(!c.isNull("Rating")) {
                                    Driver_rating = c.getDouble("Rating");
                                }
                                for (int j = 0; j < gari.length(); j++) {
                                    JSONObject d = gari.getJSONObject(j);

                                    if (!d.isNull("Driver_ID") && d.getInt("Driver_ID") == Driver_ID) {
                                        Vehicle_no = d.getString("Vehicle_No");
                                        Vehicle_image = (d.getString("Vehicle_Photo_1"));
                                        Vehicle_type = (d.getString("Vehicle_Type"));
                                        Car_type=d.getString("Vehicle_Type");

                                        break;
                                    }
                                }
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

            if (Vehicle_type != null ) {
                Ride_car_no1.setText(getLastTwo(Vehicle_no)+"-");
                Ride_car_no.setText(getLastThree(Vehicle_no));
                Ride_car_type.setText(Vehicle_type);
                Ride_driver_name.setText(Driver_name);
                Ride_otp.setText("OTP: "+OTP);
                    if (Vehicle_type.contains("SEDAN")) {
                        if (markerCar != null) {
                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));
                            markerCar.setTag("Driver");

                        } else {
                            markerCar = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Driver_lat, Driver_long))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            markerCar.setTag("Driver");
                        }

                    } else if (Vehicle_type.contains("PICKUP")) {
                        if (markerCar != null) {
                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck));
                            markerCar.setTag("Driver");
                        } else {
                            markerCar = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Driver_lat, Driver_long))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck)));
                            markerCar.setTag("Driver");
                        }
                    } else if (Vehicle_type.contains("SUV")) {
                        if (markerCar != null) {
                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));
                            markerCar.setTag("Driver");
                        } else {
                            markerCar = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Driver_lat, Driver_long))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            markerCar.setTag("Driver");
                        }
                    } else if (Vehicle_type.contains("MINI")) {
                        if (markerCar != null) {
                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));
                            markerCar.setTag("Driver");

                        } else {
                            markerCar = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Driver_lat, Driver_long))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            markerCar.setTag("Driver");
                        }
                    } else if (Vehicle_type.contains("MICRO")) {
                        if (markerCar != null) {
                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));
                            markerCar.showInfoWindow();
                            markerCar.setTag("Driver");
                        } else {
                            markerCar = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Driver_lat, Driver_long))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            markerCar.setTag("Driver");
                        }
                    }



                if (mVehcle_option.size() < 2) {

                    Choose_1.setVisibility(View.VISIBLE);
                    Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");


                } else if (mVehcle_option.size() < 3) {
                    Choose_1.setVisibility(View.VISIBLE);
                    Choose_2.setVisibility(View.VISIBLE);
                    Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                    Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");


                } else if (mVehcle_option.size() < 4) {
                    Choose_1.setVisibility(View.VISIBLE);
                    Choose_2.setVisibility(View.VISIBLE);
                    Choose_3.setVisibility(View.VISIBLE);
                    Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                    Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");
                    Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2) + " @ " + Math.round(Distance * mVehcle_option.get(2).getHourly_Rate(2) + mVehcle_option.get(2).getMinimum_Rate(2)) + "Rs");

                } else if (mVehcle_option.size() < 5) {
                    Choose_1.setVisibility(View.VISIBLE);
                    Choose_2.setVisibility(View.VISIBLE);
                    Choose_3.setVisibility(View.VISIBLE);
                    Choose_4.setVisibility(View.VISIBLE);
                    Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                    Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");
                    Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2) + " @ " + Math.round(Distance * mVehcle_option.get(2).getHourly_Rate(2) + mVehcle_option.get(2).getMinimum_Rate(2)) + "Rs");
                    Choose_4.setText(mVehcle_option.get(3).getVehicle_Type(3) + " @ " + Math.round(Distance * mVehcle_option.get(3).getHourly_Rate(3) + mVehcle_option.get(3).getMinimum_Rate(3)) + "Rs");
                }
                for (int i = 0; i < mVehcle_option.size(); i++) {
                    if (mVehcle_option.get(i).getVehicle_Type(i).contains(Vehicle_type)) {
                        if (Choose_1.getText().toString().contains(Vehicle_type)) {
                            Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.driver_type));
                            Choose_1.setTextColor(Color.WHITE);
                            Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                            Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                            Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                        } else if (Choose_2.getText().toString().contains(Vehicle_type)) {
                            Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                            Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.driver_type));
                            Choose_2.setTextColor(Color.WHITE);
                            Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                            Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                        } else if (Choose_3.getText().toString().contains(Vehicle_type)) {
                            Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                            Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                            Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.driver_type));
                            Choose_3.setTextColor(Color.WHITE);
                            Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                        } else if (Choose_4.getText().toString().contains(Vehicle_type)) {
                            Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                            Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                            Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                            Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.driver_type));
                            Choose_4.setTextColor(Color.WHITE);
                        }

                        Minimum_rate = (mVehcle_option.get(i).getMinimum_Rate(i));
                        Hourly_rate = (mVehcle_option.get(i).getHourly_Rate(i));

                    }
                }


                if(Driver_rating!=0) {
                    Ride_driver_rating.setText(String.valueOf(Driver_rating));
                }else{
                    Ride_driver_rating.setText("Not rated");
                    Ride_driver_rating.setTextSize(8f);
                }
                Picasso.Builder builder = new Picasso.Builder(GooglemapApp.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(Vehicle_image).into(Ride_car_image);
                Picasso.Builder builder1 = new Picasso.Builder(GooglemapApp.this);
                builder1.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(Driver_image).into(Ride_driver_image);
                if(User_accept!=null){
                    Choose_car_below3.setVisibility(View.GONE);
                    Choose_car_below3.setEnabled(false);
                    Choose_car_below3.setEnabled(false);
                    Cancel_again.setClickable(false);

                }
                if(UNIQUE_RIDE!=null) {
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    con = TextUtils.join("", pars);
                    if (Minimum_rate != 0 && Hourly_rate != 0) {
                        mDatabase.child("Accepted_Ride").child(con).child("Hourly_rate").setValue(Hourly_rate);
                        mDatabase.child("Accepted_Ride").child(con).child("Minimum_rate").setValue(Minimum_rate);
                        mDatabase.child("Accepted_Ride").child(con).child("Distance").setValue(dft.format(Distance));
                        mDatabase.child("Accepted_Ride").child(con).child("Estimated_fare").setValue(dft.format(Minimum_rate + Distance * Hourly_rate));
                    }

                }
            }

        }

    }
    private class PostBookdata  extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("mobile", _phoneNo)
                        .addFormDataPart("from_", My_address.getText().toString())
                        .addFormDataPart("to_", Destination_address.getText().toString())
                        .addFormDataPart("from_lat",String.valueOf(User_From_lat))
                        .addFormDataPart("from_long",String.valueOf(User_From_long))
                        .addFormDataPart("to_lat",String.valueOf(User_To_lat))
                        .addFormDataPart("to_long",String.valueOf(User_To_long))
                        .addFormDataPart("vehicle",Car_type)
                        .addFormDataPart("distance", String.valueOf(Distance))
                        .addFormDataPart("IP",mobileIp)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.USER_BOOKING_RIDE)
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
                    UNIQUE_RIDE=user.getString("unique_id");

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
                mVehcle_option.clear();
                new getFCM().execute(Car_type);

            } else {
                Snackbar.make(GooglemapApp.this.getWindow().getDecorView().getRootView(), "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

            }

        }

    }

    class getFCM extends AsyncTask<String, Void, Void> {

        private String _Car;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg0) {

            _Car=arg0[0];

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray drivers = jsonObj.getJSONArray("Driver_Details");
                    JSONArray vehicles = jsonObj.getJSONArray("Vehicle_detail");


                    for (int j = 0; j < drivers.length(); j++) {
                        JSONObject d = drivers.getJSONObject(j);
                        if(!d.isNull("Owner_ID") ) {
                            for(int k=0;k<Online_Driver.size();k++) {
                                if(Online_Driver.get(k).contains(d.getString("Phone_No"))) {
                                    for (int i = 0; i < vehicles.length(); i++) {
                                        JSONObject c = vehicles.getJSONObject(i);
                                        if(c.getString("Vehicle_Type").contains(_Car)) {
                                            if(!c.isNull("Driver_ID")) {
                                                int identity = c.getInt("Driver_ID");
                                                int driver_identity = d.getInt("ID");
                                                if (identity == driver_identity) {
                                                    fcmDriver.add(d.getString("Firebase_Token"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    JSONArray Settings_Vehicle_Rate = jsonObj.getJSONArray("Vehicle_Rate");
                    for (int i = 0; i < Settings_Vehicle_Rate.length(); i++) {
                        JSONObject c = Settings_Vehicle_Rate.getJSONObject(i);

                        Rate item=new Rate();
                        item.setVehicle_Type(c.getString("Vehicle_Type"));
                        item.setHourly_Rate(c.getInt("Hourly_Rate"));
                        item.setMinimum_Rate(c.getInt("Minimum_Rate"));
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
                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView().getRootView(), "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recreate();
                                    }
                                });
                        snackbar.setActionTextColor(Color.YELLOW);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(fcmDriver.size()!=0){
                for (int i = 0; i < fcmDriver.size(); i++) {
                    new CreateFCM().execute(fcmDriver.get(i));
                }

            }
            if (UNIQUE_RIDE != null) {
                String[] pars = UNIQUE_RIDE.split("\\.");
                con = TextUtils.join("", pars);
                if(_Car!=null) {
                    if (mVehcle_option.size() < 2) {

                        Choose_1.setVisibility(View.VISIBLE);
                        Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");


                    } else if (mVehcle_option.size() < 3) {
                        Choose_1.setVisibility(View.VISIBLE);
                        Choose_2.setVisibility(View.VISIBLE);
                        Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                        Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");


                    } else if (mVehcle_option.size() < 4) {
                        Choose_1.setVisibility(View.VISIBLE);
                        Choose_2.setVisibility(View.VISIBLE);
                        Choose_3.setVisibility(View.VISIBLE);
                        Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                        Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");
                        Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2) + " @ " + Math.round(Distance * mVehcle_option.get(2).getHourly_Rate(2) + mVehcle_option.get(2).getMinimum_Rate(2)) + "Rs");

                    } else if (mVehcle_option.size() < 5) {
                        Choose_1.setVisibility(View.VISIBLE);
                        Choose_2.setVisibility(View.VISIBLE);
                        Choose_3.setVisibility(View.VISIBLE);
                        Choose_4.setVisibility(View.VISIBLE);
                        Choose_1.setText(mVehcle_option.get(0).getVehicle_Type(0) + " @ " + Math.round(Distance * mVehcle_option.get(0).getHourly_Rate(0) + mVehcle_option.get(0).getMinimum_Rate(0)) + "Rs");
                        Choose_2.setText(mVehcle_option.get(1).getVehicle_Type(1) + " @ " + Math.round(Distance * mVehcle_option.get(1).getHourly_Rate(1) + mVehcle_option.get(1).getMinimum_Rate(1)) + "Rs");
                        Choose_3.setText(mVehcle_option.get(2).getVehicle_Type(2) + " @ " + Math.round(Distance * mVehcle_option.get(2).getHourly_Rate(2) + mVehcle_option.get(2).getMinimum_Rate(2)) + "Rs");
                        Choose_4.setText(mVehcle_option.get(3).getVehicle_Type(3) + " @ " + Math.round(Distance * mVehcle_option.get(3).getHourly_Rate(3) + mVehcle_option.get(3).getMinimum_Rate(3)) + "Rs");
                    }
                    for (int i = 0; i < mVehcle_option.size(); i++) {
                        if (mVehcle_option.get(i).getVehicle_Type(i).contains(_Car)) {
                            if (Choose_1.getText().toString().contains(_Car)) {
                                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.driver_type));
                                Choose_1.setTextColor(Color.WHITE);
                                Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                                Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                                Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                            } else if (Choose_2.getText().toString().contains(_Car)) {
                                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                                Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.driver_type));
                                Choose_2.setTextColor(Color.WHITE);
                                Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                                Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                            } else if (Choose_3.getText().toString().contains(_Car)) {
                                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                                Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                                Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.driver_type));
                                Choose_3.setTextColor(Color.WHITE);
                                Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                            } else if (Choose_4.getText().toString().contains(_Car)) {
                                Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                                Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                                Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                                Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.driver_type));
                                Choose_4.setTextColor(Color.WHITE);
                            }


                            Minimum_rate=(mVehcle_option.get(i).getMinimum_Rate(i)) ;
                            Hourly_rate=(mVehcle_option.get(i).getHourly_Rate(i)) ;
                            if(mVehcle_option.get(i).getVehicle_Type(i).contains(_Car)){
                                //mDatabase.child("Ride_Request").child(con).child("Estimated_fare").setValue(Math.round(Distance*mVehcle_option.get(i).getHourly_Rate(i)+mVehcle_option.get(i).getMinimum_Rate(i)));
                                Estimated_cost=Math.round(Distance*mVehcle_option.get(i).getHourly_Rate(i)+mVehcle_option.get(i).getMinimum_Rate(i));
                            }
                        }
                    }

                    if(User_From_lat!=0) {
                        mDatabase.child("Ride_Request").child(con).child("User_Latitude").setValue(dfto.format(User_From_lat));
                        mDatabase.child("Ride_Request").child(con).child("User_Longitude").setValue(dfto.format(User_From_long));
                        mDatabase.child("Ride_Request").child(con).child("Book_From_Latitude").setValue(dfto.format(User_From_lat));
                        mDatabase.child("Ride_Request").child(con).child("Book_From_Longitude").setValue(dfto.format(User_From_long));
                    }else{
                        mDatabase.child("Ride_Request").child(con).child("User_Latitude").setValue(dfto.format(My_lat));
                        mDatabase.child("Ride_Request").child(con).child("User_Longitude").setValue(dfto.format(My_long));
                        mDatabase.child("Ride_Request").child(con).child("Book_From_Latitude").setValue(dfto.format(User_From_lat));
                        mDatabase.child("Ride_Request").child(con).child("Book_From_Longitude").setValue(dfto.format(User_From_long));
                    }
                    mDatabase.child("Ride_Request").child(con).child("UserMobile").setValue(_phoneNo);
                    mDatabase.child("Ride_Request").child(con).child("Vehicle").setValue(_Car);
                    mDatabase.child("Ride_Request").child(con).child("Book_To_Latitude").setValue(dfto.format(User_To_lat));
                    mDatabase.child("Ride_Request").child(con).child("Book_To_Longitude").setValue(dfto.format(User_To_long));
                    mDatabase.child("Ride_Request").child(con).child("Distance").setValue(dft.format(Distance));
                    mDatabase.child("Ride_Request").child(con).child("Estimated_fare").setValue(dft.format(Estimated_cost));
                    H12.setAnimation(animFadeout);
                    H12.setVisibility(View.GONE);
                    rideButtons.setVisibility(View.GONE);
                    Cancel_ride.setVisibility(View.VISIBLE);
                    Cancel_ride.setEnabled(true);
                    SearchingLL.setVisibility(View.VISIBLE);
                    mapFragment.setOnDragListener(null,false);
                    CenterMarker.setVisibility(View.GONE);
                    FFv.setAnimation(animFadeout);
                    FFv.setVisibility(View.GONE);
                    if(mVehcle_option.size()!=0) {
                        for (int i = 0; i < mVehcle_option.size(); i++) {
                            if (mVehcle_option.get(i).getVehicle_Type(i).contains(_Car)) {
                                if(Choose_1.getText().toString().contains(_Car)) {
                                    Choose_1.setBackgroundColor(Choose_1.getContext().getResources().getColor(R.color.main));
                                }else if(Choose_2.getText().toString().contains(_Car)) {
                                    Choose_2.setBackgroundColor(Choose_2.getContext().getResources().getColor(R.color.main));
                                }else  if(Choose_3.getText().toString().contains(_Car)) {
                                    Choose_3.setBackgroundColor(Choose_3.getContext().getResources().getColor(R.color.main));
                                }else if(Choose_4.getText().toString().contains(_Car)) {
                                    Choose_4.setBackgroundColor(Choose_4.getContext().getResources().getColor(R.color.main));
                                }

                                Minimum_rate=(mVehcle_option.get(i).getMinimum_Rate(i)) ;
                                Hourly_rate=(mVehcle_option.get(i).getHourly_Rate(i)) ;
                                if(mVehcle_option.get(i).getVehicle_Type(i).contains(_Car)){
                                    mDatabase.child("Ride_Request").child(con).child("Estimated_fare").setValue(Math.round(Distance*mVehcle_option.get(i).getHourly_Rate(i)+mVehcle_option.get(i).getMinimum_Rate(i)));
                                    Estimated_cost=Math.round(Distance*mVehcle_option.get(i).getHourly_Rate(i)+mVehcle_option.get(i).getMinimum_Rate(i));
                                    if(Distance!=0){
                                        mDatabase.child("Ride_Request").child(con).child("Distance").setValue(roundTwoDecimals(Distance));
                                        mDatabase.child("Ride_Request").child(con).child("Estimated_fare").setValue(Estimated_cost);
                                    }
                                }
                            }
                        }
                        ask=true;
                        wait=false;
                        go=false;
                        stop=false;
                        drop=false;
                        got=false;
                        Driver_phone=null;
                    }
                    //mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener());

                }else{
                    Toast.makeText(getApplicationContext(),"Please selcet vehicle type",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    class CreateFCM extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String fcm=args[0];
            return uploadFile(fcm);
        }

        private String uploadFile(String fcm) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Request for ride")
                        .addFormDataPart("message",My_lat+"|"+My_long+"|"+Car_type)
                        .addFormDataPart("push_type", "individual")
                        .addFormDataPart("regId", fcm)
                        .addFormDataPart("include_image","FALSE")
                        .addFormDataPart("image","")
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.FCM_SENT)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
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
            //progressBar.setVisibility(View.GONE);

        }

    }



    private class PostAnotherCardata  extends AsyncTask<String, Integer, String> {


        private boolean success;
        private String Car_type_;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Car_type_=args[0];
            return uploadFile(Car_type_);
        }

        private String uploadFile(String Car_type_) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile", _phoneNo)
                        .addFormDataPart("unique_id",UNIQUE_RIDE)
                        .addFormDataPart("vehicle",Car_type_)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.BOOKING_RIDE_ANOTHER_CAR)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;
                  /*  String[]pars_=pars[1].split("false,");
                    JSONObject jObj = new JSONObject("{".concat(pars_[1]));
                    JSONObject user = jObj.getJSONObject("user");
                    UNIQUE_RIDE=user.getString("unique_id");*/

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
                if (UNIQUE_RIDE != null) {
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).removeValue();
                }
                Driver_phone=null;
                Car_type=Car_type_;
                AllRl.setAnimation(animslideD);
                AllRl.setVisibility(View.GONE);
                new getFCM().execute(Car_type_);
            } else {
                Snackbar.make(GooglemapApp.this.getWindow().getDecorView().getRootView(), "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

            }

        }

    }
    private class PostDeleteRide  extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("mobile", _phoneNo)
                        .addFormDataPart("unique_id", UNIQUE_RIDE)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.DELETE_RIDE_ALL)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;

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
                if (UNIQUE_RIDE != null) {
                    ask=false;
                    wait=false;
                    go=false;
                    stop=false;
                    animate=false;
                    Drop_at=null;
                    cancel=true;
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).removeValue();
                    mDatabase.child("Ride_Request").child(con).removeValue();
                    if(Driver_phone!=null) {
                        mDatabase.child("Driver_Online").child(Driver_phone).child("OnRide").setValue("NO");
                    }
                   recreate();
                }
            } else {
                Snackbar.make(GooglemapApp.this.getWindow().getDecorView().getRootView(), "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

            }

        }

    }
    private class PostDeleteData  extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("mobile", _phoneNo)
                        .addFormDataPart("unique_id", UNIQUE_RIDE)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.DELETE_RIDE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;
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
                    ask=false;
                    wait=false;
                    go=false;
                    stop=false;
                    animate=false;
                    Drop_at=null;
                    cancel=true;
                if (UNIQUE_RIDE != null) {
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).removeValue();
                    if(Driver_phone!=null) {
                        mDatabase.child("Driver_Online").child(Driver_phone).child("OnRide").setValue("NO");
                    }
                    UNIQUE_RIDE=null;
                }

                recreate();

            } else {
                Snackbar.make(GooglemapApp.this.getWindow().getDecorView().getRootView(), "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

            }

        }

    }
    private class FirebaseDataListener implements ValueEventListener {

        private String _paid;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (UNIQUE_RIDE != null) {
                if (dataSnapshot.getChildrenCount() != 0) {

                    Driver_Start = (String) dataSnapshot.child("START").getValue();
                    _paid = (String) dataSnapshot.child("Paid").getValue();
                    User_accept = (String) dataSnapshot.child("UserAccept").getValue();
                    if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                            dataSnapshot.child("Driver_First_Longitude").getValue() != null ) {

                        Driver_lat = Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue());
                        Driver_long = Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue());

                                mPathPolygonPoint.clear();
                                if(!animate){
                                mPathPolygonPoint.add(new LatLng(Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue()), Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue())));
                                //mPathPolygonPoint.add(new LatLng(Double.parseDouble((String) dataSnapshot.child("Driver_Second_Latitude").getValue()), Double.parseDouble((String) dataSnapshot.child("Driver_Second_Longitude").getValue())));

                    }}

                    if (Driver_Start != null) {
                        if (Driver_Start.contains("YES") ) {
                            if(!drawn) {
                                Cancel_ride.setAnimation(animslideD);
                                SearchingLL.setAnimation(animslideD);
                                Cancel_ride.setVisibility(View.GONE);
                                SearchingLL.setVisibility(View.GONE);
                                ChooseCar.setVisibility(View.GONE);
                                Choose_car_below2.setVisibility(View.GONE);
                                Choose_car_below3.setVisibility(View.GONE);
                                Choose_car_below3.setEnabled(false);
                                Cancel_again.setClickable(false);
                                Confirm_booking.setClickable(false);
                                bottomNavigationView.setVisibility(View.VISIBLE);
                                bottomNavigationView.bringToFront();
                                mySOS.setVisibility(View.VISIBLE);
                                AllRl.setAnimation(animslideU);
                                AllRl.setVisibility(View.VISIBLE);
                                Ride_otp.setVisibility(View.VISIBLE);
                                Choose_car_below1.setVisibility(View.VISIBLE);
                                FFv.setAnimation(animFadeout);
                                FFv.setVisibility(View.GONE);
                                CenterMarker.setVisibility(View.GONE);
                                Text1.setText("Enjoy ");
                                Text2.setText("the Ride");
                                wait = false;
                                got = false;
                                stop = false;
                                drop = false;
                                go = true;
                                ask = false;
                                drawn=true;
                                if (googleMap != null) {
                                    googleMap.setTrafficEnabled(true);
                                }

                                if (Minimum_rate != 0 && Hourly_rate != 0) {
                                    mDatabase.child("Accepted_Ride").child(con).child("Hourly_rate").setValue(Hourly_rate);
                                    mDatabase.child("Accepted_Ride").child(con).child("Minimum_rate").setValue(Minimum_rate);
                                    mDatabase.child("Accepted_Ride").child(con).child("Distance").setValue(dft.format(Distance));
                                    mDatabase.child("Accepted_Ride").child(con).child("Estimated_fare").setValue(dft.format(Minimum_rate + Hourly_rate * Distance));
                                } else {
                                    new GetSettings().execute();
                                }
                            }
                        } else {
                            stopService(mServiceIntent);
                            wait = false;
                            got = false;
                            stop = true;
                            drop = false;
                            go = false;
                            ask = false;
                        }


                    }else{
                        if (User_accept != null) {
                            CenterMarker.setVisibility(View.GONE);
                            H13.setAnimation(animFadeout);
                            H13.setVisibility(View.GONE);
                            ChooseCar.setVisibility(View.GONE);
                            Choose_car_below2.setVisibility(View.VISIBLE);
                            Choose_car_below3.setAnimation(animslideD);
                            Choose_car_below3.setVisibility(View.GONE);
                            Choose_car_below3.setEnabled(false);
                            Choose_car_below3.setEnabled(false);
                            Cancel_again.setClickable(false);
                            bottomNavigationView.setAnimation(animslideD);
                            bottomNavigationView.setVisibility(View.GONE);
                            FFv.setAnimation(animFadeout);
                            FFv.setVisibility(View.GONE);

                        }
                    }

                    if (_paid != null) {
                        if (_paid.contains("YES") ) {
                            stopService(mServiceIntent);
                            mDatabase.child("Accepted_Ride").child(con).child("START").setValue("Done");
                            Intent i = new Intent(GooglemapApp.this, Success.class);
                            i.putExtra("my_lat", My_lat);
                            i.putExtra("my_long", My_long);
                            i.putExtra("COST", COST);
                            i.putExtra("KM", KM);
                            i.putExtra("UNIQUEID", UNIQUE_RIDE);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            UNIQUE_RIDE=null;
                            finish();
                        }


                    }
                        if (ask) {

                            Cancel_ride.setAnimation(animslideD);
                            SearchingLL.setAnimation(animslideD);
                            Cancel_ride.setVisibility(View.GONE);
                            SearchingLL.setVisibility(View.GONE);
                            rideButtons.setAnimation(animslideD);
                            rideButtons.setVisibility(View.GONE);
                            FFv.setAnimation(animFadeout);
                            FFv.setVisibility(View.GONE);
                            H12.setAnimation(animFadeout);
                            H12.setVisibility(View.GONE);
                            CenterMarker.setVisibility(View.GONE);
                            AllRl.setAnimation(animslideU);
                            AllRl.setVisibility(View.VISIBLE);
                            Ride_otp.setVisibility(View.VISIBLE);
                            Choose_car_below1.setVisibility(View.VISIBLE);
                            Choose_car_below3.setVisibility(View.VISIBLE);
                            Choose_car_below3.setEnabled(true);
                            Choose_car_below3.setEnabled(true);
                            Cancel_again.setClickable(true);
                            if (Driver_phone == null) {
                                Driver_phone = (String) dataSnapshot.child("DriverMobile").getValue();
                                new GetDriver().execute();

                            } else {
                                Driver_phone = (String) dataSnapshot.child("DriverMobile").getValue();
                                ask = false;
                                got = true;
                                wait = false;
                                go = false;
                                stop = false;
                            }
                        }
                        if (got) {
                            CenterMarker.setVisibility(View.GONE);
                            AllRl.setVisibility(View.VISIBLE);
                            AllRl.setAnimation(animslideU);
                            Ride_otp.setVisibility(View.VISIBLE);
                            ChooseCar.setVisibility(View.GONE);
                            Choose_car_below3.setVisibility(View.VISIBLE);
                            Choose_car_below3.setEnabled(true);
                            Choose_car_below3.setEnabled(true);
                            Cancel_again.setClickable(true);
                            Choose_car_below2.setVisibility(View.GONE);
                            bottomNavigationView.setAnimation(animslideD);
                            bottomNavigationView.setVisibility(View.GONE);
                            if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                    dataSnapshot.child("Driver_First_Longitude").getValue() != null &&
                                    dataSnapshot.child("User_Latitude").getValue() != null &&
                                    dataSnapshot.child("User_Longitude").getValue() != null &&
                                    dataSnapshot.child("Vehicle_Choosen").getValue() != null) {
                                Driver_lat = Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue());
                                Driver_long = Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue());
                                User_From_lat = Double.parseDouble((String) dataSnapshot.child("User_Latitude").getValue());
                                User_From_long = Double.parseDouble((String) dataSnapshot.child("User_Longitude").getValue());

                                if (OTP == null) {
                                    new GetDriver().execute();

                                }
                                for (int i = 0; i < markers.size(); i++) {
                                    markers.get(i).setVisible(false);
                                }

                                if (googleMap != null) {
                                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(GooglemapApp.this);
                                    googleMap.setInfoWindowAdapter(adapter);
                                }


                                        markerPoints.add(new LatLng(User_From_lat, User_From_long));
                                        markerPoints.add(new LatLng(User_To_lat, User_To_long));
                                        if (marker != null) {
                                            marker.setPosition(new LatLng(User_From_lat, User_From_long));
                                            marker.hideInfoWindow();

                                        }
                                        if (markerCar != null) {
                                            markerCar.setPosition(new LatLng(Driver_lat, Driver_long));
                                            markerCar.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));
                                            //markerCar.showInfoWindow();
                                            //markerCar.setTag("Driver");

                                        } else {
                                            markerCar = googleMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(Driver_lat, Driver_long))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                            markerCar.showInfoWindow();
                                            markerCar.setTag("Driver");
                                        }

                                        if (markerPoints.size() == 2) {
                                            LatLng origin = markerPoints.get(0);
                                            LatLng dest = markerPoints.get(1);
                                            String url = getDirectionsUrl(origin, dest);
                                            new DownloadTask().execute(url);

                                        }
                                        SetZoomGot(marker, markerCar);

                                if(User_accept!=null) {
                                    wait = true;
                                    got = false;
                                    stop = false;
                                    drop = false;
                                    go = false;
                                    ask = false;

                                }
                                    }

                        }

                        if (wait) {
                                wait = true;
                                got = false;
                                stop = false;
                                drop = false;
                                go = false;
                                ask = false;
                                if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                        dataSnapshot.child("Driver_First_Longitude").getValue() != null &&
                                        dataSnapshot.child("User_Latitude").getValue() != null &&
                                        dataSnapshot.child("User_Longitude").getValue() != null) {

                                    User_From_lat = Double.parseDouble((String) dataSnapshot.child("User_Latitude").getValue());
                                    User_From_long = Double.parseDouble((String) dataSnapshot.child("User_Longitude").getValue());

                                    if (OTP == null) {
                                        new GetDriver().execute();

                                    }
                                   if (marker != null) {
                                        marker.setPosition(new LatLng(User_From_lat, User_From_long));

                                    }
                                    if (markerCar != null && mPathPolygonPoint.size()!=0) {
                                        if(markerCar.getPosition()!=null) {
                                            markerCar.hideInfoWindow();
                                            animate = true;
                                            animateCarMoveA(markerCar, markerCar.getPosition(), mPathPolygonPoint.get(0), Vehicle_type);
                                        }else{
                                            markerCar.setPosition(mPathPolygonPoint.get(0));
                                        }
                                    }
                                    markerPoints.clear();
                                    markerPoints.add(new LatLng(Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue()), Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue())));
                                    markerPoints.add(new LatLng(User_To_lat, User_To_long));

                                    if(marker!=null && markerCar!=null) {
                                        markerCar.showInfoWindow();
                                      if (markerPoints.size() == 2) {
                                          LatLng origin = markerPoints.get(0);
                                          LatLng dest = markerPoints.get(1);
                                          String url = getDirectionsUrl(origin, dest);
                                          new DownloadTask().execute(url);

                                      }
                                      SetZoomGot(marker, markerCar);

                                  }
                                }


                        }
                        if (go) {
                            wait = false;
                            got = false;
                            stop = false;
                            drop = false;
                            go = true;
                            ask = false;
                            if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                    dataSnapshot.child("Driver_First_Longitude").getValue() != null) {
                                Vehicle_type = (String) dataSnapshot.child("Vehicle_Choosen").getValue();
                                Driver_stop = (String) dataSnapshot.child("START").getValue();
                                User_done = (String) dataSnapshot.child("USERDONE").getValue();

                                if (OTP == null) {
                                    new GetDriver().execute();

                                }
                                 if (marker != null) {
                                    marker.setVisible(false);

                                }
                                if (markerCar != null && mPathPolygonPoint.size()!=0) {
                                    if(markerCar.getPosition()!=null) {
                                        markerCar.hideInfoWindow();
                                            animate = true;
                                            animateCarMove(markerCar, markerCar.getPosition(), mPathPolygonPoint.get(0), Vehicle_type);
                                    }else{
                                        markerCar.setPosition(mPathPolygonPoint.get(0));
                                    }
                                }
                                if (Driver_stop != null && Driver_stop.contains("NO")) {
                                    if (dataSnapshot.child("Cost").getValue() != null) {
                                        COST = Double.parseDouble((String) dataSnapshot.child("Cost").getValue());
                                    }

                                    stop = true;
                                }

                            }
                        }

                } else {

                    Car_type = null;
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    protected Marker createMarker(double latitude, double longitude, String mobile,String vehicle_type) {
        Marker Marker_;

        MarkerOptions markerOptions = new MarkerOptions();


        markerOptions.position(new LatLng(latitude,longitude));

        Marker_=googleMap.addMarker(markerOptions);
        Marker_.setTag(mobile);
        Marker_.setTitle(mobile);
        Marker_.setSnippet(vehicle_type);

        if (vehicle_type.contains("SEDAN")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        } else if (vehicle_type.contains("PICKUP")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck));

        } else if (vehicle_type.contains("SUV")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_su));

        } else if (vehicle_type.contains("MINI")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        } else if (vehicle_type.contains("MICRO")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        } else if (vehicle_type.contains("AMBULANCE")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        }
        Marker_.setDraggable(false);
        Marker_.hideInfoWindow();
        Marker_.setVisible(false);

        return Marker_;
    }
    protected Marker createMarker1(double latitude, double longitude, String mobile) {
        Marker Marker_;

        MarkerOptions markerOptions = new MarkerOptions();

// Setting latitude and longitude for the marker
        markerOptions.position(new LatLng(latitude,longitude));

        Marker_=googleMap.addMarker(markerOptions);
        Marker_.setTitle(mobile);
        Marker_.setVisible(false);

// Adding marker on the Google Map
        return Marker_;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Indicate API calls to Google Play services APIs should be halted.
        googleApiClientConnectionStateChange(false);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {

            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((GooglemapApp) getActivity()).onDialogDismissed();
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    private void setUpNavigationView() {

        //Setting Navigation View Item Selected Listener to handle the bean click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            // This method will trigger on bean Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which bean was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        drawer.closeDrawers();
                        Redraw=true;
                        //recreate();
                        break;

                    case R.id.nav_reminder:
                        drawer.closeDrawers();
                        if (pref.isLoggedIn()) {
                            stopLocationUpdates();
                            Intent o = new Intent(GooglemapApp.this, Ride_later_tabs.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
                            startActivity(o);


                        }else {
                            Intent o = new Intent(GooglemapApp.this, SmsActivity.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
                            startActivity(o);

                        }
                        break;
                    case R.id.nav_refer:
                        drawer.closeDrawers();
                        if (pref.isLoggedIn()) {
                            stopLocationUpdates();
                            Intent o2 = new Intent(GooglemapApp.this, Refer_earn.class);
                            o2.putExtra("mylat", My_lat);
                            o2.putExtra("mylong", My_long);
                            startActivity(o2);
                            finish();

                        }else {
                            Intent o = new Intent(GooglemapApp.this, SmsActivity.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
                            startActivity(o);
                            finish();
                        }
                        break;
                    case R.id.nav_rate:
                        drawer.closeDrawers();
                        if (pref.isLoggedIn()) {
                            stopLocationUpdates();
                            Intent o1 = new Intent(GooglemapApp.this, Emergency_contacts.class);
                            o1.putExtra("mylat", My_lat);
                            o1.putExtra("mylong", My_long);
                            startActivity(o1);
                            finish();

                        }else {
                            Intent o = new Intent(GooglemapApp.this, SmsActivity.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
                            startActivity(o);
                            finish();
                        }
                        break;

                    case R.id.logout:
                        stopLocationUpdates();
                        Intent p = new Intent(GooglemapApp.this, Support.class);
                        p.putExtra("mylat", My_lat);
                        p.putExtra("mylong", My_long);
                        startActivity(p);
                        drawer.closeDrawers();
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the bean is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }




    class StoreLatLong extends AsyncTask<Void, Integer, String> {




        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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

                        .addFormDataPart("mobile",_phoneNo)
                        .addFormDataPart("user_lat", String.valueOf(My_lat))
                        .addFormDataPart("user_long", String.valueOf(My_long))
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_STORE_USER_LATLNG)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
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
            progressBar.setVisibility(View.GONE);
        }

    }
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;

                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notification, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_notify:
                /*Redraw=true;
                ask=false;
                wait=false;
                go=false;
                stop=false;
                animate=false;
                first=false;
                drop=false;
                Car_type=null;
                for(int i=0;i<markers.size();i++){
                    markers.get(i).setVisible(false);
                }
                restartActivity(GooglemapApp.this);*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        if (animation == animFadein) {
            Toast.makeText(getApplicationContext(), "Animation Stopped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config_URL.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, " reg id: " + regId);

        if (regId!=null) {
            if (_phoneNo != null) {
                new Update_FCM().execute();
            }
        }
    }

    class Update_FCM extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("mobile", _phoneNo)
                        .addFormDataPart("token", regId)


                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_FCM)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
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

        }

    }


    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }




    protected synchronized void rebuildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and connection failed
        // callbacks should be returned, which Google APIs our app uses and which OAuth 2.0
        // scopes our app requests. When using enableAutoManage to register the failed connection
        // listener it will only be called back when auto-resolution attempts were not
        // successful or possible. A normal ConnectionFailedListener is also registered below to
        // notify the activity when it needs to stop making API calls.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        0 /* googleApiClientId used when auto-managing multiple googleApiClients */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this /* ConnectionCallbacks */)
                // Register a connection listener that will notify on disconnect (including ones
                // caused by calling disconnect on the GoogleApiClient).
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        googleApiClientConnectionStateChange(true);
                    }
                })


                .addApi(LocationServices.API)
                // TODO(developer): Specify any additional API Scopes or APIs you need here.
                // The GoogleApiClient will ensure these APIs are available, and the Scopes
                // are approved before invoking the onConnected callbacks.
                .build();

    }


    private void googleApiClientConnectionStateChange(final boolean connected) {
        final Context appContext = this.getApplicationContext();

        // TODO(developer): Kill AsyncTasks, or threads using the GoogleApiClient.

        // Display Toast that isn't dependent on the current activity (in case of a rotation).
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, "Google Api Client has connected:" + connected,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        imageParentWidth = coordinatorLayout.getWidth();
        imageParentHeight = coordinatorLayout.getHeight();

        centerX = imageParentWidth / 2;
        centerY = (imageParentHeight / 2) + (imageHeight / 2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //stopLocationUpdates();
        //mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    public String getLastTwo(String str) {

        return str.length() < 4 ? str : str.substring(0, 4);
    }
    public String getLastThree(String myString) {

        if( myString.length() >= 4)
            return myString.substring(myString.length()-4);
        else
            return myString;
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void shareItpic() {

        String shareBody = "https://play.google.com/store/apps/details?id=com.share.contri";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey lets try OHO");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    private void open_home(final String  address) {

        SwipeDismissDialog.Builder builder = new SwipeDismissDialog.Builder(GooglemapApp.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.home_selected,null);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);


        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialogView.findViewById(R.id.home_save);
        Button btn_negative = (Button) dialogView.findViewById(R.id.home_cancel);

        TextView Home_address=dialogView.findViewById(R.id.home_address);

        Home_address.setText(address);

        RadioGroup choose=dialogView.findViewById(R.id.radioGroup);

        choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radioButton1:
                        Favourite="HOME";
                        break;
                    case R.id.radioButton2:
                        Favourite="WORK";
                        break;
                    case R.id.radioButton3:
                        Favourite="OTHER";
                        break;

                }
            }
        });

        // Create the alert dialog
        final SwipeDismissDialog dialog = builder.setOnSwipeDismissListener(new OnSwipeDismissListener() {
            @Override
            public void onSwipeDismiss(View view, SwipeDismissDirection direction) {
                Heart_d2.setVisibility(View.GONE);
                Heart_t2.setVisibility(View.GONE);
                Heart_d1.setVisibility(View.VISIBLE);
                Heart_t1.setVisibility(View.VISIBLE);
            }
        })
                .build()
                ;

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();
                new PostFavourite().execute(address);
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();
                Heart_d2.setVisibility(View.GONE);
                Heart_t2.setVisibility(View.GONE);
                Heart_d1.setVisibility(View.VISIBLE);
                Heart_t1.setVisibility(View.VISIBLE);

            }
        });

        // Set cancel/neutral button click listener

        // Display the custom alert dialog on interface
        dialog.show();

    }

    private class PostFavourite extends AsyncTask<String, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected String doInBackground(String... strings) {
            // Some long-running task like downloading an image.
            String Address = strings[0];
            return uploadFile(Address);

        }
        private String uploadFile(String address) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_phoneNo)
                        .addFormDataPart("fav",Favourite)
                        .addFormDataPart("address",address)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_FAVOURITE)
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

            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }



    public LatLng getCenterCoordinate(ArrayList<LatLng> listLatLng){
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(listLatLng.get(0));
        b.include(listLatLng.get(1));
        LatLngBounds bounds = b.build();
        return bounds.getCenter();
    }
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }else {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){



        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String duration = "";
            Random rand = new Random();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            String pars = (String) point.get("distance");
                            String[] pars1 = pars.split(" ");
                            if (Drop_at != null) {
                                Distance = Float.parseFloat(pars1[0]);
                            }
                            continue;
                        } else if (j == 1) {
                            duration = (String) point.get("duration");
                            if (wait) {
                                if (markerCar != null) {
                                    markerCar.setTitle(duration);
                                    markerCar.showInfoWindow();

                                }
                            }

                            continue;
                        }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(POLYLINE_WIDTH);
                lineOptions.startCap(new SquareCap());
                lineOptions.endCap(new SquareCap());
                lineOptions.jointType(ROUND);
                lineOptions.color(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) ));
            }
            if(lineOptions!=null) {
                if(polylineFinal==null) {
                    polylineFinal = googleMap.addPolyline(lineOptions);
                }
            }


        }
    }
    public void SetZoomDrop(ArrayList<LatLng> listLatLng) {
        if ( googleMap!=null) {
            ArrayList<Marker> markerAll = new ArrayList<Marker>();
            if (listLatLng != null && listLatLng.size() > 1) {
                if (marker != null) {
                    marker.setPosition(listLatLng.get(0));
                    markerAll.add(marker);

                } else {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(listLatLng.get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));
                    markerAll.add(marker);

                }

                if (markerd != null) {
                    markerd.setPosition(listLatLng.get(1));
                    markerAll.add(markerd);
                } else {
                    markerd = googleMap.addMarker(new MarkerOptions()
                            .position(listLatLng.get(1))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_red)));
                    markerAll.add(markerd);
                }



                if(markerAll.size()!=0) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 120);
                    googleMap.animateCamera(cu);
                }
            }

        }else{
            mapFragment.getMapAsync(GooglemapApp.this);

        }

    }
    public double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public void SetZoomGot(final Marker markeruser,final Marker markerCar) {
        if ( googleMap!=null) {
            ArrayList<Marker> markerAll = new ArrayList<Marker>();
            if (markeruser != null && markerCar != null) {
                markerAll.add(markeruser);
                markerAll.add(markerCar);
                if (markerAll.size() != 0) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 240);
                    googleMap.animateCamera(cu);
                    markerPoints.clear();
                    markerPoints.add(markeruser.getPosition());

                    markerPoints.add(markerCar.getPosition());

                    if (markerPoints.size() == 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);
                        String url = getDirectionsUrl(origin, dest);
                        new DownloadTask().execute(url);


                    }


                }

            }
        }
        else{
            mapFragment.getMapAsync(GooglemapApp.this);

        }
    }
    public void SetZoomlevel(final ArrayList<LatLng> listLatLng) {
        if ( googleMap!=null) {
            ArrayList<Marker> markerAll = new ArrayList<Marker>();
            if (listLatLng != null && listLatLng.size() > 1) {


                if (wait) {
                    if (marker != null) {
                        marker.setPosition(listLatLng.get(0));

                    } else {
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(listLatLng.get(0))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_green)));

                    }

                    if (markerCar != null) {
                        markerCar.setPosition(listLatLng.get(1));
                    }
                }
                for(int i=0;i<listLatLng.size();i++) {
                    markerAll.add(createMarker1(listLatLng.get(i).latitude, listLatLng.get(i).longitude, "null"));
                }
                if(markerAll.size()!=0) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
                    googleMap.animateCamera(cu);
                }

            }

        }else{
            mapFragment.getMapAsync(GooglemapApp.this);

        }
    }




    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                Log.w(" address", strReturnedAddress.toString());
            } else {
                Log.w(" address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(" loction address", "Canont get Address!");
        }
        return strAdd;
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(GooglemapApp.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(GooglemapApp.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates mState = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.

                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(GooglemapApp.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    if(mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                    }else{
                        startLocationUpdat();
                    }
                }
            }
            break;
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    if(mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                    }else{
                        startLocationUpdat();
                    }

                } else {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
            break;
        }


    }
    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(0.1f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
            }
        };
    }

    public void onMarkerClick(final Marker wmarker) {

        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                wmarker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    wmarker.showInfoWindow();
                }

            }
        });

    }
    private void animateCarMove(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng, final String vehicle) {
        final Bitmap[] mMarkerIcon = new Bitmap[1];
        if (vehicle != null) {
            animate=true;
            final Handler handler = new Handler();
            final long startTime = SystemClock.uptimeMillis();
            final Interpolator interpolator = new AccelerateInterpolator();
            if(go) {
                float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng, endLatLng));
                float direction = (angleDeg1 > 0) ? 180 : 0;
                Location location = new Location("Test");
                location.setLatitude(beginLatLng.latitude);
                location.setLongitude(beginLatLng.longitude);
                Location location1 = new Location("Test");
                location1.setLatitude(endLatLng.latitude);
                location1.setLongitude(endLatLng.longitude);
                bearing = location.bearingTo(location1);
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(endLatLng)
                        .bearing(direction)
                        .zoom(18)
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            }
            handler.post(new Runnable() {


                @Override
                public void run() {
                    if (!clicK){
                        if(go){
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                        }else {
                            if (vehicle.contains("SEDAN")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("PICKUP")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_truck);
                            } else if (vehicle.contains("SUV")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_su);
                            } else if (vehicle.contains("MINI")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("MICRO")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("AMBULANCE")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            }
                        }

                        long elapsed = SystemClock.uptimeMillis() - startTime;
                        float t = interpolator.getInterpolation((float) elapsed / 3000);
                        double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                        double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                        if (Math.abs(lngDelta) > 180) {
                            lngDelta -= Math.signum(lngDelta) * 360;
                        }
                        double lng = lngDelta * t + beginLatLng.longitude;
                        float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(anmarker.getPosition(),new LatLng(lat,lng)));
                        Matrix matrix = new Matrix();
                        float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,endLatLng));
                        float direction = (angleDeg1 > 0) ? angleDeg+180 : angleDeg;
                        matrix.postRotate(direction);
                        if(googleMap != null) {
                            anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            anmarker.setPosition(new LatLng(lat, lng));
                            anmarker.setAnchor(.5f, .5f);
                            anmarker.setVisible(true);

                        }

                        if (t < 1.0) {
                            handler.postDelayed(this, 16);
                        } else {
                            //matrix.postRotate(180);
                            //anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            animate = false;
                            //anmarker.setPosition(endLatLng);

                        }
                    }else{
                        handler.removeCallbacks(this);
                    }
                }
            });
        }
    }

    private void animateCarMoveA(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng, final String vehicle) {
        final Bitmap[] mMarkerIcon = new Bitmap[1];
        if (vehicle != null) {
            animate=true;
            final Handler handler = new Handler();
            final long startTime = SystemClock.uptimeMillis();
            final Interpolator interpolator = new AccelerateInterpolator();
            handler.post(new Runnable() {


                @Override
                public void run() {
                    if (!clicK){

                            if (vehicle.contains("SEDAN")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("PICKUP")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_truck);
                            } else if (vehicle.contains("SUV")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_su);
                            } else if (vehicle.contains("MINI")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("MICRO")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            } else if (vehicle.contains("AMBULANCE")) {
                                mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                            }


                        long elapsed = SystemClock.uptimeMillis() - startTime;
                        float t = interpolator.getInterpolation((float) elapsed / 3000);
                        double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                        double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                        if (Math.abs(lngDelta) > 180) {
                            lngDelta -= Math.signum(lngDelta) * 360;
                        }
                        double lng = lngDelta * t + beginLatLng.longitude;
                        float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(anmarker.getPosition(),new LatLng(lat,lng)));
                        Matrix matrix = new Matrix();
                        matrix.postRotate(angleDeg);
                        if(googleMap != null) {
                            anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            anmarker.setPosition(new LatLng(lat, lng));
                            anmarker.setAnchor(.5f, .5f);
                            anmarker.setVisible(true);

                        }
                        if (t < 1.0) {
                            handler.postDelayed(this, 16);
                        } else {
                            //matrix.postRotate(180);
                            //anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            animate = false;
                            //anmarker.setPosition(endLatLng);

                        }
                    }else{
                        handler.removeCallbacks(this);
                    }
                }
            });
        }
    }


    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Press Back again to Exit.", Toast.LENGTH_SHORT).show();

            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    finish();
                }
            },  1000);

        }

    }
}
