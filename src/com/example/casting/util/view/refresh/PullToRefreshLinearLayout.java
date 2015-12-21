package com.example.casting.util.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {

	 /**
     * 构造方法
     * 
     * @param context context
     */
    public PullToRefreshLinearLayout(Context context) {
        this(context, null);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context, android.util.AttributeSet)
     */
    @Override
    protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
    	LinearLayout linearLayout = new LinearLayout(context);
        return linearLayout;
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullDown()
     */
    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullUp()
     */
    @Override
    protected boolean isReadyForPullUp() {
//        View scrollViewChild = mRefreshableView.getChildAt(0);
//        if (null != scrollViewChild) {
//            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
//        }
        
        return false;
    }
}
