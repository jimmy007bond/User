package com.geekworkx.hellocab.Adapters;

/**
 * Created by parag on 05/11/17.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
public class CustomMapFragment extends MapFragment {

    private View mOriginalView;
    private MapWrapperLayout1 mMapWrapperLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalView = super.onCreateView(inflater, container, savedInstanceState);

        mMapWrapperLayout = new MapWrapperLayout1(getActivity());
        mMapWrapperLayout.addView(mOriginalView);

        return mMapWrapperLayout;
    }

    @Override
    public View getView() {
        return mOriginalView;
    }

    public void setOnDragListener(MapWrapperLayout1.OnDragListener onDragListener,boolean pick) {
        if(pick) {
            mMapWrapperLayout.setOnDragListener(onDragListener);
        }
    }
}
