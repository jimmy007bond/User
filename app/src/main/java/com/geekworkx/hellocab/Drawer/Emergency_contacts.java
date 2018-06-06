package com.geekworkx.hellocab.Drawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.geekworkx.hellocab.Adapters.GooglemapApp;
import com.geekworkx.hellocab.Main_activity.PrefManager;
import com.geekworkx.hellocab.Model.Contacts;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.geekworkx.hellocab.helper.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 28/02/18.
 */

public class Emergency_contacts extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Emergency_contacts.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView SOS;
    private static final int RESULT_PICK_CONTACT = 101;
    private double My_lat=0,My_long=0;
    private PrefManager pref;
    private String _PhoneNo;
    private String phoneNo  ;
    private String name;
    private Button addSos;
    private RecyclerView Sosrv;
    private ArrayList<Contacts> mItems=new ArrayList<Contacts>();
    private String mobileIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);

        toolbar = (Toolbar) findViewById(R.id.toolbar_later_sos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SOS=findViewById(R.id.sos_image);
        addSos=findViewById(R.id.buttonSos);
        Sosrv=findViewById(R.id.sosrv);
        addSos.setOnClickListener(this);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Emergency_contacts.this, GooglemapApp.class);
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
        mobileIp = getMobileIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }

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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonSos:
                pickContact(v);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mItems.clear();
        new GetContacts().execute();
    }

    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {

            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo=(cursor.getString(phoneIndex)).trim();
            name=(cursor.getString(nameIndex));
            new PostData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class PostData  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if(phoneNo.contains("+")) {
                String[] pars = phoneNo.split("\\+");
                phoneNo = pars[1];

            }

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
                        .addFormDataPart("giver_mobile",_PhoneNo)
                        .addFormDataPart("phone", phoneNo)
                        .addFormDataPart("names", name)
                        .addFormDataPart("IP", mobileIp)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_EMERGENCY)
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
                new android.support.v7.app.AlertDialog.Builder(Emergency_contacts.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Contact added")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                            }
                        })
                        .show();

            } else {

                Snackbar.make(getWindow().getDecorView().getRootView(),"Maximum 5 no can be added!", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {


        private JSONArray Contacts;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                    Contacts = jsonObj.getJSONArray("Contacts");
                    JSONArray User = jsonObj.getJSONArray("User");
                    int relation=0;
                    for (int i = 0; i < User.length(); i++) {

                        JSONObject d = User.getJSONObject(i);
                        if (_PhoneNo.contains(d.getString("Phone_No"))) {
                            relation = d.getInt("ID");
                        }
                    }


                        for (int j = 0; j < Contacts.length(); j++) {

                            JSONObject c = Contacts.getJSONObject(j);
                            if (relation==c.getInt("ID")) {
                                Contacts item = new Contacts();
                                item.setContacts(c.getString("Contact_Phone_No"));
                                item.setName(c.getString("Contact_Name"));
                                mItems.add(item);
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


            if (mItems.size() != 0) {
                Sosrv.setVisibility(View.VISIBLE);
                SOS.setVisibility(View.GONE);
                Sosadapter sAdapter = new Sosadapter(Emergency_contacts.this, mItems);
                sAdapter.notifyDataSetChanged();
                sAdapter.setMobile(_PhoneNo);
                sAdapter.setMyLat(My_lat,My_long);
                Sosrv.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Emergency_contacts.this);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                Sosrv.setLayoutManager(mLayoutManager);
                if(mItems.size()==5){
                    addSos.setVisibility(View.GONE);
                }else{
                    addSos.setVisibility(View.VISIBLE);
                }

            }else{
                Sosrv.setVisibility(View.GONE);
                SOS.setVisibility(View.VISIBLE);
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
        Intent i=new Intent(Emergency_contacts.this, GooglemapApp.class);
        i.putExtra("my_lat", My_lat);
        i.putExtra("my_long", My_long);
        startActivity(i);
        finish();
    }
}
