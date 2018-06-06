package com.geekworkx.hellocab.Drawer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.geekworkx.hellocab.Model.Contacts;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.uniquestudio.library.CircleCheckBox;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 28/02/18.
 */

public class Sosadapter extends RecyclerView.Adapter<Sosadapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Contacts> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private String Drop_at;
    private String Pick_at;
    private int Tag=0;


    public Sosadapter(Context aContext, ArrayList<Contacts> mItems) {
        this.mItems = mItems;
        layoutInflater = LayoutInflater.from(aContext);
        this.mContext=aContext;

    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setMobile(String _phone) {
        _PhoneNo=_phone;
    }
    public void setMyLat(double my_lat, double my_long) {
        My_lat=my_lat;
        My_long=my_long;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private EditText Sosname,Soscontacts;
        private ImageView Sosdelete;
        private CircleCheckBox Share;
        public ViewHolder(View itemView) {
            super(itemView);

            Sosname=itemView.findViewById(R.id.sosname);
            Soscontacts=itemView.findViewById(R.id.soscontact);
            Sosdelete=itemView.findViewById(R.id.sosdel);
            Share=itemView.findViewById(R.id.sosshare);


        }

    }

    @Override
    public Sosadapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sosrv, viewGroup, false);
        Sosadapter.ViewHolder viewHolder = new Sosadapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Sosadapter.ViewHolder viewHolder, final int position) {
        final Contacts album_pos = mItems.get(position);
        if (album_pos.getName(position)!=null && !TextUtils.isEmpty(album_pos.getName(position))) {
            viewHolder.Sosname.setText(album_pos.getName(position));

        }
        if (album_pos.getContacts(position)!=null && !TextUtils.isEmpty(album_pos.getContacts(position))) {
            viewHolder.Soscontacts.setText(album_pos.getContacts(position));

        }


        viewHolder.Sosdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(mContext)
                        .setTitle("Delete contact")
                        .setMessage(album_pos.getName(position))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            new PostDeleteContact().execute(album_pos.getName(position),album_pos.getContacts(position));
                            dialog.cancel();
                            }
                        })
                        .show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private class PostDeleteContact extends AsyncTask<String, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator

        }

        protected String doInBackground(String... strings) {
            // Some long-running task like downloading an image.
            String name=strings[0];
            String contact=strings[1];
            return uploadFile(name,contact);

        }
        private String uploadFile(String name, String contact) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("name",name)
                        .addFormDataPart("contact",contact)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.CONTACT_DELETE)
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

            if(success){
                Intent o = new Intent(mContext, Emergency_contacts.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                mContext.startActivity(o);
                //Bungee.slideUp(mContext);
            }


        }
    }
}





