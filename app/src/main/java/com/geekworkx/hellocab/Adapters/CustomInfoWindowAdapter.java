package com.geekworkx.hellocab.Adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.geekworkx.hellocab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by parag on 03/02/18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.infowindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.info);
        tvTitle.setText(marker.getTitle());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
