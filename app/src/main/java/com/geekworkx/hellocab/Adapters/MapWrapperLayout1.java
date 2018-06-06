package com.geekworkx.hellocab.Adapters;

/**
 * Created by parag on 05/11/17.
 */
import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MapWrapperLayout1 extends FrameLayout {

    public interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
    }

    private OnDragListener mOnDragListener;

    public MapWrapperLayout1(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }
}
