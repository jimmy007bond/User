package com.geekworkx.hellocab.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.geekworkx.hellocab.Model.Promo;
import com.geekworkx.hellocab.R;

import java.util.ArrayList;



/**
 * Created by parag on 22/09/16.
 */
public class Promo_Adapter extends RecyclerView.Adapter<Promo_Adapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Promo> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;


    public Promo_Adapter(Context aContext, ArrayList<Promo> mItems) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {


        private LinearLayout promoll;
        private CheckBox Select;
       

        public ViewHolder(View itemView) {
            super(itemView);
            promoll=itemView.findViewById(R.id.promoLL);
            Select=itemView.findViewById(R.id.select_promo);

           
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.promorv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Promo album_pos = mItems.get(position);
        if(album_pos.getPromo_Code(position)!=null && !TextUtils.isEmpty(album_pos.getPromo_Code(position))){
            viewHolder.Select.setText(album_pos.getPromo_Code(position));

        }else{
            viewHolder.Select.setVisibility(View.GONE);

        }

       viewHolder.promoll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               viewHolder.Select.setChecked(true);
           }
       });

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }



}





