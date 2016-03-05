package com.njit.buddy.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author toyknight 2/7/2016.
 */
public class BuddyScrollView extends ScrollView {

    private BuddyScrollListener scroll_listener = null;

    public BuddyScrollView(Context context) {
        super(context);
    }

    public BuddyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BuddyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollListener(BuddyScrollListener scroll_listener) {
        this.scroll_listener = scroll_listener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int old_x, int old_y) {
        View view = getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));
        if (diff == 0) { // if diff is zero, then the bottom has been reached
            fireBottomReachedEvent();
        }
        super.onScrollChanged(x, y, old_x, old_y);
    }

    private void fireBottomReachedEvent() {
        if (scroll_listener != null) {
            scroll_listener.onBottomReached();
        }
    }

}
