package com.geekworkx.hellocab.Place;

/**
 * Created by parag on 18/02/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekworkx.hellocab.Model.favs;
import com.geekworkx.hellocab.R;

import java.util.ArrayList;

public class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<favs> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String Mobile;
    private double My_lat=0,My_long=0;
    private String Drop_at;
    private String Pick_at;
    private int Tag=0;


    public CardArrayAdapter(Context aContext, ArrayList<favs> mItems) {
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

    public void setDrop(String drop_at) {
        Drop_at=drop_at;
    }

    public void setPick(String pick_at) {
        Pick_at=pick_at;
    }

    public void setTag(int d) {
        Tag=d;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView laterPlace;
        private ImageView Heart1, Heart2;
        public ViewHolder(View itemView) {
            super(itemView);

            laterPlace=itemView.findViewById(R.id.place_selected);
            Heart1=itemView.findViewById(R.id.heart_hate);
            Heart2=itemView.findViewById(R.id.heart_like);


        }

    }

    @Override
    public CardArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_card, viewGroup, false);
        CardArrayAdapter.ViewHolder viewHolder = new CardArrayAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CardArrayAdapter.ViewHolder viewHolder, final int position) {
        final favs album_pos = mItems.get(position);
        if (!TextUtils.isEmpty(album_pos.getFrom(position))) {
            viewHolder.laterPlace.setText(album_pos.getFrom(position));

        }

        viewHolder.Heart2.setVisibility(View.GONE);
        viewHolder.Heart1.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

}





