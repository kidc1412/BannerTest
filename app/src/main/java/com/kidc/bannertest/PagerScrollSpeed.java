package com.kidc.bannertest;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;



public class PagerScrollSpeed extends Scroller {

    public int getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    private int scrollSpeed = 450;

    public PagerScrollSpeed(Context context) {
        super(context);
    }

    public PagerScrollSpeed(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public PagerScrollSpeed(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, scrollSpeed);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, scrollSpeed);
    }
}
