package com.geekworkx.hellocab.Main_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Adapters.PrefUtil;
import com.geekworkx.hellocab.Adapters.SmsActivity;
import com.geekworkx.hellocab.ConnectionDetector;
import com.geekworkx.hellocab.CropImage.ImagePicker;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 29/12/17.
 */

public class Update_profile extends AppCompatActivity implements View.OnClickListener {


    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private static final String TAG = SmsActivity.class.getSimpleName();
    private ImageView ProfileImage;
    private EditText Name,_PhoneNo;
    private Button Logout;
    private Toolbar toolbar;
    private String Facebook_id="";
    private CallbackManager callbackManager;
    private PrefUtil prefUtil;
    private String Name_giver="";
    private String Email_giver="";
    private String filePath="";
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private boolean permissionsAllowd=false;
    public static final int MULTIPLE_PERMISSIONS = 10;
    private String _PhoneNo_giver="";
    private double My_lat=0,My_long=0;
    private static final int REQUEST_EXTERNAL_STORAGE = 201;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 200;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private File pic;
    private PrefManager pref;
    private String User_image,User_name;
    private TextInputLayout input_Name,input__PhoneNo,input_Email;
    private EditText Email,dStreetfirst,dCity,dState,dCountry,dPin;
    private String User_mobile="",User_address="",User_state="",User_country="",User_pin="",User_city="",User_Email="";
    private String filePath_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.profile_update);
        ProfileImage=findViewById(R.id.img_profil_update);
        Name=findViewById(R.id.editText_name);
        _PhoneNo=findViewById(R.id.editText_mobile);
        Logout=findViewById(R.id.logout);
        cd = new ConnectionDetector(getApplicationContext());
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo_giver = user.get(PrefManager.KEY_MOBILE);
        toolbar=findViewById(R.id.toolbar_profile);
        input_Email=findViewById(R.id.input_email);
        input__PhoneNo=findViewById(R.id.input_mobile);
        input_Name=findViewById(R.id.input_name);
        Email=findViewById(R.id.editText_email);
        dStreetfirst=findViewById(R.id.faddresso);
        dCity=findViewById(R.id.cityo);
        dState=findViewById(R.id.stateo);
        dCountry=findViewById(R.id.countryo);
        dPin=findViewById(R.id.pino);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolscreen));
        //getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefUtil=new PrefUtil(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Update_profile.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            }
        });
        if(_PhoneNo_giver!=null) {
            _PhoneNo.setText(_PhoneNo_giver);
            new GetCustomer().execute();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            ProfileImage.setOnClickListener(this);
            Logout.setOnClickListener(this);
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE).show();

        }
    }
    private class GetCustomer extends AsyncTask<Void, Void, Void> {


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
                    JSONArray parlours = jsonObj.getJSONArray("User");


                    // looping through All Contacts
                    if (_PhoneNo != null) {
                        for (int i = 0; i < parlours.length(); i++) {
                            JSONObject c = parlours.getJSONObject(i);
                            String relation = c.getString("Phone_No");
                            if (!relation.contains("null") ) {

                                if (relation.matches(_PhoneNo_giver)) {
                                    User_image = c.getString("Photo");
                                    User_name=c.getString("Name");
                                    User_mobile=c.getString("Phone_No");
                                    User_address=c.getString("Address");
                                    User_state=c.getString("State");
                                    User_country=c.getString("Country");
                                    User_pin=c.getString("Pin");
                                    User_city=c.getString("City");
                                    User_Email=c.getString("Email");


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
            //progressBar.setVisibility(View.GONE);


            if (_PhoneNo_giver != null && !_PhoneNo_giver.contains("null")) {
                if(User_name!=null) {
                    Name.setText(User_name);
                }
                if(User_Email!=null && !User_Email.contains("null") ) {
                    Email.setText(User_Email);
                }
                if(User_pin!=null && !User_pin.contains("null")) {
                    dPin.setText(User_pin);
                }

                if(User_address!=null && !User_address.contains("null")) {
                    dStreetfirst.setText(User_address);
                }
                if(User_state!=null && !User_state.contains("null")) {
                    dState.setText(User_state);
                }
                if(User_city!=null && !User_city.contains("null")) {
                    dCity.setText(User_city);
                }
                if(User_country!=null && !User_country.contains("null")) {
                    dCountry.setText(User_country);
                }

                if(User_image!=null && !TextUtils.isEmpty(User_image)) {
                    Picasso.Builder builder = new Picasso.Builder(Update_profile.this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(User_image).into(ProfileImage);
                }
            }


        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.logout:
                Name_giver=Name.getText().toString();
                _PhoneNo_giver=_PhoneNo.getText().toString();
                User_Email=Email.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

// onClick of button perform this simplest code.
                if (!User_Email.matches(emailPattern))
                {
                    User_Email="";
                    Email.setText("");
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();

                }

                User_address=dStreetfirst.getText().toString();
                User_city=dCity.getText().toString();
                User_country=dCountry.getText().toString();
                User_pin=dPin.getText().toString();
                User_state=dState.getText().toString();
                if(isValidPhoneNumber(_PhoneNo_giver)) {
                    new PostDataTOServer().execute();
                }else{
                    _PhoneNo.setError("Not a valid no");
                }
            break;
            case R.id.img_profil_update:
                if(verifyStoragePermissions()) {
                    pickImage();
                }else{
                    checkAndRequestPermissions();
                }
               break;

        }
    }
    public void pickImage() {
        ImagePicker.pickImage(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_PICK) {
            ImagePicker.beginCrop(this, resultCode, data);
        } else if (requestCode == ImagePicker.REQUEST_CROP) {

            Bitmap bitmap = ImagePicker.getImageCropped(this, resultCode, data,
                    ImagePicker.ResizeType.FIXED_SIZE, 300);

                if(bitmap!=null){
                    ProfileImage.setImageBitmap(bitmap);
                    if (((BitmapDrawable) ProfileImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) ProfileImage.getDrawable()).getBitmap();
                        final File mediaStorageDir = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                        File destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                        FileOutputStream fo;
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            filePath_ = destination.getPath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            //Log.d(SmsActivity.this, "bitmap picked: " + bitmap);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean verifyStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d(TAG,"Permission is granted");
            return true;
        }
    }

    private void checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);



        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);

        } else {
            permissionsAllowd = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if ( perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            &&perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                       pickImage();

                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");

                       checkAndRequestPermissions();
                    }
                }
            }
            break;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                pref.clearSession();
                Intent i=new Intent(Update_profile.this, GooglemapApp.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private static boolean isValidPhoneNumber(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 12 || phone.length()<12) {
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
    private class PostDataTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private String UNIQUE_MOBILE;

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
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            //String filename = filePath_.substring(filePath_.lastIndexOf("/") + 1);

            try {
                File sourceFile = new File(filePath_);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", Name_giver)
                        .addFormDataPart("mobile",  _PhoneNo_giver)
                        .addFormDataPart("email",User_Email)
                        .addFormDataPart("address",User_address)
                        .addFormDataPart("city", User_city)
                        .addFormDataPart("state",  User_state)
                        .addFormDataPart("country",User_country)
                        .addFormDataPart("pin",User_pin)
                        .addFormDataPart("my_lat", String.valueOf(My_lat))
                        .addFormDataPart("my_long", String.valueOf(My_long))
                        .addFormDataPart("image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))

                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_STORE_USER_PROFILE)
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
                    UNIQUE_MOBILE=user.getString("mobile");

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
                pref.createLogin(_PhoneNo.getText().toString());
                Intent i=new Intent(Update_profile.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
            } else {

                Snackbar.make(getWindow().getDecorView().getRootView(),"Please check mobile no.", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Update_profile.this, GooglemapApp.class);
        i.putExtra("my_lat", My_lat);
        i.putExtra("my_long", My_long);
        startActivity(i);
        finish();
    }
}
