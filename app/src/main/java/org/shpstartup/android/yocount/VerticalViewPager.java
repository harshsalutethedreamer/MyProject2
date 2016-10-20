package org.shpstartup.android.yocount;

import android.content.Context;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by harshgupta on 23/09/16.
 */
public class VerticalViewPager extends ViewPager {

    private int mSlop,mMinFlingVelocity,mMaxFlingVelocity;
    public float initialX,initialY,finalX,finalY;
    private String TAG=VerticalViewPager.class.getSimpleName();
    private VelocityTracker mVelocityTracker = null;
    private Context vcontext;

    public VerticalViewPager(Context context) {
        super(context);
        vcontext=context;
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        vcontext=context;
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        private float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);



            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                //view.setTranslationX(1);
                view.setScaleX(1);
                view.setScaleY(1);
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
                view.setTranslationX(-1 * view.getWidth() * position);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                view.setTranslationX(-1 * view.getWidth() * position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }

            ViewConfiguration vc = ViewConfiguration.get(view.getContext());
            mSlop = vc.getScaledTouchSlop();
            mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
            mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        int index = ev.getActionIndex();
        int pointerId = ev.getPointerId(index);
        ViewConfiguration vc = ViewConfiguration.get(vcontext);
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                }
                initialX = ev.getX();
                initialY=ev.getY();
                Log.d(TAG,"MOVING DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"ACTION MOVE");
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"ACTION UP");
                finalX=ev.getX();
                finalY=ev.getY();
        }
        float deltaX=initialX-finalX;
        float deltaY=finalY-initialY;
        float deltaY2=initialY-finalY;
        float velocityX,velocityY;
        try {
            velocityX = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
            velocityY = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
        }catch (Exception e){
            velocityX = 130;
            velocityY = 130;
        }
        try {
            int mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
            if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(velocityX) > mMinFlingVelocity) {
                Log.d(TAG, "horizontal Action");
                return false;
            } else {
                Log.d(TAG, "vertical Action");
                return super.onTouchEvent(swapXY(ev));
            }
        }catch (Exception e){
            return true;
        }
    }

}
