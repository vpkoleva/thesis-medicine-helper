package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Handles screen motion events.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Do not allow swiping to switch between pages
        return false;
    }
}
