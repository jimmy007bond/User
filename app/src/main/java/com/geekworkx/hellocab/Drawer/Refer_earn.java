package com.geekworkx.hellocab.Drawer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;
import com.google.android.gms.appinvite.AppInviteInvitation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by parag on 28/02/18.
 */

public class Refer_earn extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Refer_earn.class.getSimpleName();
    private static final int REQUEST_INVITED = 101;
    private Toolbar toolbar;
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private ProgressBar progressBar;
    private String Refer_code;
    private EditText Refer_text;
    private ImageView WhatsApp,Messenger,Email,Message,Twitter,Facebook;
    private Button Refer;
    private String strShareMessage;
    private ImageView Gplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_earn);

        progressBar=findViewById(R.id.progressBarRefer);
        Refer_text=findViewById(R.id.referalCode);

        WhatsApp=findViewById(R.id.whatsapp);
        Messenger=findViewById(R.id.messenger);
        Email=findViewById(R.id.email);
        Message=findViewById(R.id.message);
        Twitter=findViewById(R.id.twitter);
        Facebook=findViewById(R.id.facebok);
        Refer=findViewById(R.id.buttonRefer);
        Gplus=findViewById(R.id.gplus);

        toolbar = (Toolbar) findViewById(R.id.toolbar_later_refer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Refer_earn.this, GooglemapApp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                //Bungee.card(Ride_later_tabs.this);
            }
        });


        WhatsApp.setOnClickListener(this);
        Message.setOnClickListener(this);
        Messenger.setOnClickListener(this);
        Email.setOnClickListener(this);
        Twitter.setOnClickListener(this);
        Facebook.setOnClickListener(this);
        Refer.setOnClickListener(this);
        Gplus.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetCustomer().execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.whatsapp:
               sendAppMsg(v);
                break;
            case R.id.messenger:
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
                break;
            case R.id.message:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","Share HelloCab");

                smsIntent.putExtra("sms_body",strShareMessage);
                startActivity(smsIntent);
                break;
            case R.id.email:
                sendEmail(strShareMessage);
                break;
            case R.id.twitter:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = " message you want to share..";
                intent.setPackage("com.twitter.android");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(intent);
                } else {

                    Toast.makeText(this, "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.facebok:
                sendAppFacebook(v);
               break;

            case R.id.buttonRefer:
                onInviteClicked();
                break;
            case R.id.gplus:
                onInviteClicked();
                break;
            default:
                break;

        }
    }

    public void sendAppGplus(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = strShareMessage;
        // change with required  application package

        intent.setPackage("com.google.android.apps.plus");
        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            startActivity(intent);
        } else {

            Toast.makeText(this, "App not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void sendAppFacebook(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = strShareMessage;
        // change with required  application package

        intent.setPackage("com.facebook.katana");
        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            startActivity(Intent.createChooser(intent, text));
        } else {

            Toast.makeText(this, "App not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void sendAppMsg(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = strShareMessage;
        // change with required  application package

        intent.setPackage("com.whatsapp");
        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);//
            startActivity(Intent.createChooser(intent, text));
        } else {

            Toast.makeText(this, "App not found", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Send mail or SMS")
                .setMessage("HelloCab")
                .setDeepLink(Uri.parse("hellocab://use/"+"="+_PhoneNo+":"+Refer_code))
                .setCustomImage(Uri.parse("http://139.59.38.160/LiftMe/uploads/contri_logo.png"))
                .setCallToActionText("Join")
                .build();
        startActivityForResult(intent, REQUEST_INVITED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            //checking for our ColorSelectorActivity using request code

            case REQUEST_INVITED:
                if (resultCode == RESULT_OK) {

                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);


                    if (ids.length < 1) {
                        Toast.makeText(getApplicationContext(), "Please invite friends to join your community", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_SHORT).show();

                        finish();


                    }

                    break;
                }

            default:
                break;
        }

    }
    public void sendEmail(String s){

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("text/plain");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"bidliftassam@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "ERROR");
        i.putExtra(Intent.EXTRA_TEXT   , s);
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

    }
    private class GetCustomer extends AsyncTask<Void, Void, Void> {

        private int User_refernce_code_coupon_Amt;

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
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Settings_default = jsonObj.getJSONArray("Settings");

                    for (int i = 0; i < Settings_default.length(); i++) {
                        JSONObject c = Settings_default.getJSONObject(i);
                        User_refernce_code_coupon_Amt  = c.getInt("User_refernce_code_coupon_Amt");
                    }


                    for (int i = 0; i < User.length(); i++) {
                        JSONObject c = User.getJSONObject(i);
                        String relation = c.getString("Phone_No");
                        if (!relation.contains("null")) {

                            if (relation.matches(_PhoneNo)) {
                                Refer_code=c.getString("Reference_Code");

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
            progressBar.setVisibility(View.GONE);
            if(Refer_code!=null){
                Refer_text.setText(Refer_code);
                strShareMessage = "\nI have Hellocab coupon worth \u20B9"+User_refernce_code_coupon_Amt +" for you. Sign up with my code "+Refer_code+
                " to avail the coupon and ride!"+ "https://play.google.com/store/apps/details?id=" + getPackageName();


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
        Intent i=new Intent(Refer_earn.this, GooglemapApp.class);
        i.putExtra("my_lat", My_lat);
        i.putExtra("my_long", My_long);
        startActivity(i);
        finish();
    }
    }
