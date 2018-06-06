package com.geekworkx.hellocab.Ride_later;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekworkx.hellocab.Model.Date_;
import com.geekworkx.hellocab.R;

import java.util.ArrayList;



/**
 * Created by parag on 22/09/16.
 */
public class daterv extends RecyclerView.Adapter<daterv.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Date_> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String Mobile;
    private double My_lat=0,My_long=0;
    private int row_index=0;


    public daterv(Context aContext, ArrayList<Date_> mItems) {
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

  

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView laterDate,laterDay;
        private LinearLayout DD;
       
        public ViewHolder(View itemView) {
            super(itemView);

            laterDay=itemView.findViewById(R.id.day_);
            laterDate=itemView.findViewById(R.id.date_);
            DD=itemView.findViewById(R.id.ddd);
           
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaverv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Date_ album_pos = mItems.get(position);
   
        //String date_ = parseDateToddMMyyyy(album_pos.getDate(position));
        if (!TextUtils.isEmpty(album_pos.getDate(position))) {
            viewHolder.laterDay.setText(album_pos.getDate(position));

        }
        if (album_pos.getSelected(position)!=null && album_pos.getSelected(position)) {
            viewHolder.DD.setBackgroundResource(R.drawable.card_state_pressed);
            viewHolder.laterDay.setTextColor(Color.WHITE);
            viewHolder.laterDate.setTextColor(Color.WHITE);

        }


        if (!TextUtils.isEmpty(album_pos.getTime(position))) {
            viewHolder.laterDate.setText(album_pos.getTime(position));

        }

        viewHolder.DD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();

            }
        });

        if(row_index==position){
            viewHolder.DD.setBackgroundResource(R.drawable.card_state_pressed);
            viewHolder.laterDay.setTextColor(Color.WHITE);
            viewHolder.laterDate.setTextColor(Color.WHITE);

        }
        else
        {
            viewHolder.DD.setBackgroundColor(Color.WHITE);
            viewHolder.laterDay.setTextColor(Color.BLACK);
            viewHolder.laterDate.setTextColor(Color.BLACK);

        }



    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


}





