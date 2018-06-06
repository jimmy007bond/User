package com.geekworkx.hellocab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekworkx.hellocab.Model.User;
import com.geekworkx.hellocab.R;
import com.geekworkx.hellocab.Ride_later.Confirm_later_booking;
import com.geekworkx.hellocab.Ride_later.Ride_later_ok;
import com.geekworkx.hellocab.Ride_later.Ride_later_tabs;
import com.geekworkx.hellocab.URLS.Config_URL;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;



/**
 * Created by parag on 22/09/16.
 */
public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<User> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String Mobile;
    private double My_lat=0,My_long=0;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Image_Adapter(Context aContext, ArrayList<User> mItems) {
        this.mItems = mItems;
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

    public void setMobile(String mobile) {
        Mobile=mobile;
    }

    public void setMyLat(double my_lat, double my_long) {
        My_lat=my_lat;
        My_long=my_long;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView laterDate,laterTime;
        private EditText inputFrom, inputTo;
        private TextInputLayout inputFromA, inputFromT;
        private Button laterEdit,laterCancel;
        private LinearLayout Bottom1,Bottom2;
        private ImageView DriverImage;
        private TextView Ride_cost;

     public ViewHolder(View itemView) {
            super(itemView);

         laterDate=itemView.findViewById(R.id.ride_date_later);
         laterTime=itemView.findViewById(R.id.ride_time_later);
         inputFrom=itemView.findViewById(R.id.input_from_address);
         inputTo=itemView.findViewById(R.id.input_to_address);
         inputFromA=itemView.findViewById(R.id.input_from);
         inputFromT=itemView.findViewById(R.id.input_to);
         laterEdit=itemView.findViewById(R.id.ride_later_edit);
         laterCancel=itemView.findViewById(R.id.ride_later_cancel);
         Bottom1=itemView.findViewById(R.id.bottom1);
         Bottom2=itemView.findViewById(R.id.bottom2);
         DriverImage=itemView.findViewById(R.id.ride_driver);
         Ride_cost=itemView.findViewById(R.id.ride_cost);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.later_rv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final User album_pos = mItems.get(position);
        DecimalFormat dft=new DecimalFormat("0.00");
        Calendar cal = Calendar.getInstance();
        if(album_pos.getSnapshot(position)!=null && !TextUtils.isEmpty(album_pos.getSnapshot(position))){
            viewHolder.Bottom1.setVisibility(View.GONE);
            viewHolder.Bottom2.setVisibility(View.VISIBLE);
            viewHolder.inputFromA.setVisibility(View.VISIBLE);
            viewHolder.inputFromT.setVisibility(View.VISIBLE);
            Picasso.Builder builder1 = new Picasso.Builder(mContext);
            builder1.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
            builder1.build().load(album_pos.getDriverImage(position)).into(viewHolder.DriverImage);
            if(album_pos.getCost(position)!=null && !TextUtils.isEmpty(album_pos.getCost(position))){
                viewHolder.Ride_cost.setText("\u20B9 " +dft.format(Double.parseDouble(album_pos.getCost(position))));
                viewHolder.Ride_cost.setVisibility(View.VISIBLE);

            }
        }else{
            viewHolder.Bottom1.setVisibility(View.VISIBLE);
            viewHolder.Bottom2.setVisibility(View.GONE);
            viewHolder.inputFromA.setVisibility(View.VISIBLE);
            viewHolder.inputFromT.setVisibility(View.VISIBLE);

            viewHolder.Ride_cost.setVisibility(View.GONE);
            if(album_pos.get_exp(position)!=0 &&album_pos.get_exp(position)>1000){
                viewHolder.laterEdit.setText("Info");
                viewHolder.Ride_cost.setVisibility(View.GONE);
                viewHolder.Bottom2.setVisibility(View.VISIBLE);
                viewHolder.DriverImage.setVisibility(View.GONE);
                Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_outstation);
                viewHolder.DriverImage.setImageBitmap(bm);
            }
        }
        String date_ = parseDateToddMMyyyy(album_pos.getDate(position));
        if (!TextUtils.isEmpty(album_pos.getFrom(position))) {
            viewHolder.inputFrom.setText(album_pos.getFrom(position));

        }
        if (!TextUtils.isEmpty(album_pos.getTo(position))) {
            viewHolder.inputTo.setText(album_pos.getTo(position));

        }
        if (!TextUtils.isEmpty(album_pos.getDate(position))) {
            viewHolder.laterDate.setText("On "+date_);

        }
        if (!TextUtils.isEmpty(album_pos.getTo(position))) {
            try {
                Date date = format.parse(album_pos.getTime(position));
                cal.setTime(date);
                viewHolder.laterTime.setText("At "+sdf.format(cal.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

      viewHolder.laterEdit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(album_pos.get_exp(position)!=0 &&album_pos.get_exp(position)>1000){
                  double dista=com.google.maps.android.SphericalUtil.computeDistanceBetween(new LatLng(My_lat,My_long),new LatLng(album_pos.getTo_Lat(position),album_pos.getTo_Long(position)))/1000;
                  BigDecimal number = new BigDecimal(dista);
                  float myFloat = number.floatValue();
                  Intent o2 = new Intent(mContext, Confirm_later_booking.class);
                  o2.putExtra("Unique_ID",album_pos.getUnique_id(position));
                  o2.putExtra("distance", myFloat );
                  o2.putExtra("vehicle",album_pos.getVehicle_choosen(position));
                  o2.putExtra("my_lat", My_lat);
                  o2.putExtra("my_long", My_long);
                  o2.putExtra("where", "adapter");
                  o2.putExtra("otp", album_pos.getOTP(position));
                  mContext.startActivity(o2);
                  ((Activity) mContext).finish();
              }else {
                  Intent o = new Intent(mContext, Ride_later_ok.class);
                  o.putExtra("mylat", My_lat);
                  o.putExtra("mylong", My_long);
                  o.putExtra("date", album_pos.getDate(position));
                  o.putExtra("Unique_ID", album_pos.getUnique_id(position));
                  o.putExtra("from", "adapter");
                  o.putExtra("otp", album_pos.getOTP(position));
                  mContext.startActivity(o);
                  ((Activity) mContext).finish();

              }

          }
      });
        viewHolder.laterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.support.v7.app.AlertDialog.Builder(mContext)
                        .setTitle("Delete Ride")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new PostFavourite().execute(album_pos.getTime(position),album_pos.getDate(position));
                                removeAt(position);
                                if(mItems.size()==0){
                                    Intent o = new Intent(mContext, Ride_later_ok.class);
                                    o.putExtra("mylat", My_lat);
                                    o.putExtra("mylong", My_long);
                                    mContext.startActivity(o);
                                    ((Activity)mContext).finish();
                                }
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
        });

    }

    private class PostFavourite extends AsyncTask<String, Integer, String> {
        private boolean success;

        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator

        }

        protected String doInBackground(String... strings) {
            // Some long-running task like downloading an image.
            String time=strings[0];
            String date=strings[1];
            return uploadFile(time,date);

        }
        private String uploadFile(String time, String date) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",Mobile)
                        .addFormDataPart("time",time)
                        .addFormDataPart("date",date)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.ADD_LATER_RIDE_DELETE)
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
                Intent o = new Intent(mContext, Ride_later_tabs.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                mContext.startActivity(o);
                //Bungee.slideUp(mContext);
            }


        }
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());

    }
    }





