package com.example.casting.login.start;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPagerScorller extends Scroller {
    private int mScrollDuration = 1500;// 滑动速度(此处是指滑动需要的时间，单位为毫秒)
    public ViewPagerScorller(Context context) {
        super(context);
    }
    public ViewPagerScorller(Context context, Interpolator interpolator) {
       super(context);
    }
    public ViewPagerScorller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
    public void setScrollDuration(int mScrollDuration)
    {
      this.mScrollDuration = mScrollDuration;	
    }
}