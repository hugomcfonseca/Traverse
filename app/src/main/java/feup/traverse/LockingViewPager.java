package feup.traverse;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by AmiguelS on 03/11/2015.
 */
public class LockingViewPager extends ViewPager {

    private boolean canSwipe = true;

    public LockingViewPager(Context context) {
        super(context);
    }

    public LockingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //if(canSwipe)
            return super.onInterceptTouchEvent(event);

        //return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if(canSwipe)
            return super.onTouchEvent(event);

        //return true;
    }

    public void setSwipe(boolean m){
        this.canSwipe = m;
    }

    public boolean getSwipe(){
        return this.canSwipe;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return !canSwipe;
    }
}
