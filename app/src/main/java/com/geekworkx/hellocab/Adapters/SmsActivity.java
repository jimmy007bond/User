package com.geekworkx.hellocab.Adapters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.MyViewPager;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.Total;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by parag on 13/01/17.
 */
public class SmsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int ALARM_REQUEST_CODE = 301;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private static final String TAG = SmsActivity.class.getSimpleName();
    private ImageView Profile_image;
    private EditText Name,_PhoneNo;
    private static String Name_giver="",Email_giver="",_PhoneNo_giver="",Home_giver="";
    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 200;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private Compress_image compress;
    private File pic;
    private Uri picUri;
    private String filePath;
    private Button First_button;
    private ProgressBar progressBar;
    private PrefManager pref;
    private Button Second_button,Third_button;
    private String otp;
    private EditText inputOtp;
    private LinearLayout Main_ui;
    private PrefUtil prefUtil;
    private ScrollView SV1;
    private String blockCharacters = "(~*#^|$%&!";
    private String Community_name="";
    private static final int REQUEST_INVITED=101;
    private Button Skip;
    private String USER;
    private ArrayList<Total> Community_ = new ArrayList<Total>();
    private ImageView Community_icon;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private boolean join;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";


    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;
    private Button mResendButton;
    private String mVerificationId;
    private Toolbar toolbar;
    private double My_long=0,My_lat=0;
    private EditText inputName;
    private String mobileIp="";
    private TextInputLayout l1,l2;
    private CheckBox TnC;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms);
        l1=findViewById(R.id.txtL);
        l2=findViewById(R.id.txtL1);
        TnC=findViewById(R.id.accept_tnc);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .smscor);
        viewPager =  findViewById(R.id.viewPagerVertical);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        inputName=findViewById(R.id.u_name);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        progressBar=(ProgressBar)findViewById(R.id.progress_rider);
        prefUtil=new PrefUtil(this);
        pref = new PrefManager(this);
        _PhoneNo= (EditText) findViewById(R.id.u_mobile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle("Login");
        otp=inputOtp.getText().toString();
        compress=new Compress_image(this);
        First_button= (Button) findViewById(R.id.first_button);
        First_button.setOnClickListener(this);
        Second_button= (Button) findViewById(R.id.second_butona);
        Second_button.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mResendButton = findViewById(R.id.button_resend);
        Intent i=getIntent();
        My_lat=i.getDoubleExtra("mylat",0);
        My_long=i.getDoubleExtra("mylong",0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SmsActivity.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        });

        mobileIp = getMobileIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }

        pref.createLogin("917002608241"); /// temporary to bypass login

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

                First_button.setOnClickListener(this);
                Second_button.setOnClickListener(this);
                if (pref.isLoggedIn()) {
                        Intent o = new Intent(SmsActivity.this, GooglemapApp.class);
                        o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        o.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        startActivity(o);
                        finish();
                }
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




        }else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE)
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.first_button:
                _PhoneNo_giver="+91"+_PhoneNo.getText().toString();
                Name_giver=inputName.getText().toString();

                if(TextUtils.isEmpty(_PhoneNo_giver)){
                    _PhoneNo.setError("Empty");
                }else if(isValidPhoneNumber(_PhoneNo_giver)){
                    if(!TnC.isChecked()){
                        Snackbar.make(coordinatorLayout,"Please accept Terms & Condition", Snackbar.LENGTH_SHORT).show();
                    }else {

                        pref.setIsWaitingForSms(true);
                        validateForm();
                        break;
                    }

                } else {
                    _PhoneNo.setError("Error!");

                }
            case R.id.second_butona:
                //verifyOtp();
                String code = inputOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    inputOtp.setError("Cannot be empty");
                    return;
                }else{
                    new OTPverify().execute(code);
                }

                break;

            case R.id.button_resend:
                new PostDataTOServer().execute();
                break;


        }
    }

    private void validateForm() {
        new PostDataTOServer().execute();
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
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
                    resId = R.id.first_pager;
                    break;
                case 1:
                    resId = R.id.second_pager_;
                    break;

            }
            return findViewById(resId);
        }
    }

    public boolean verifyStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }




    private static boolean isValidPhoneNumber(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;

    }
    class OTPverify  extends AsyncTask<String, Integer, String> {
        private boolean success;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
                    String Otp=args[0];
                    uploadFile(Otp);
            return Otp;
        }

        private String uploadFile(String Otp) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("giver_name",Name_giver)
                        .addFormDataPart("giver_mobile","91"+_PhoneNo.getText().toString())
                        .addFormDataPart("otp",Otp)
                        .addFormDataPart("my_lat", String.valueOf(My_lat))
                        .addFormDataPart("my_long", String.valueOf(My_long))
                        .addFormDataPart("IP", mobileIp)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_VERIFY_USER_OTP)
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

        protected void onPostExecute(final String file_url) {
            progressBar.setVisibility(View.GONE);
            if (success == true) {
                pref.createLogin("91"+_PhoneNo.getText().toString());
                Intent o = new Intent(SmsActivity.this, GooglemapApp.class);
                o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                finish();

            }else{

                Snackbar.make(getWindow().getDecorView().getRootView(), "Not a valid otp", Snackbar.LENGTH_LONG).show();

            }
        }

    }

    private class PostDataTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;


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
                        .addFormDataPart("giver_mobile","91"+_PhoneNo.getText().toString())
                        .addFormDataPart("IP", mobileIp)
                        .addFormDataPart("giver_name",Name_giver)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_REQUEST_MOBILE)
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

                viewPager.setCurrentItem(1);
            } else {

                Snackbar.make(getWindow().getDecorView().getRootView(),"Please check mobile no.", Snackbar.LENGTH_LONG).show();

            }

        }


    }


    @Override
    protected void onStop() {
        super.onStop();

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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }
}

