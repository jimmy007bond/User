package com.geekworkx.hellocab.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekworkx.hellocab.Model.Remarks;
import com.geekworkx.hellocab.R;
import com.uniquestudio.library.CircleCheckBox;

import java.util.ArrayList;

public class Remarks_rv extends RecyclerView.Adapter<Remarks_rv.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Remarks> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String Mobile;
    private double My_lat=0,My_long=0;
    private int row_index=0;


    public Remarks_rv(Context aContext, ArrayList<Remarks> mItems) {
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


        private TextView _remarks;
        private CircleCheckBox _first;

        public ViewHolder(View itemView) {
            super(itemView);

            _remarks=itemView.findViewById(R.id.textfirst);
            _first=itemView.findViewById(R.id.radiofirst);
    

        }

    }

    @Override
    public Remarks_rv.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.remarks_rv, viewGroup, false);
        Remarks_rv.ViewHolder viewHolder = new Remarks_rv.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Remarks_rv.ViewHolder viewHolder, final int position) {
        final Remarks album_pos = mItems.get(position);

        //String Remarks = parseDateToddMMyyyy(album_pos.getDate(position));

        if (album_pos.get_comments(position)!=null && !TextUtils.isEmpty(album_pos.get_comments(position))) {
            viewHolder._remarks.setText(album_pos.get_comments(position));


        }





    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


}





