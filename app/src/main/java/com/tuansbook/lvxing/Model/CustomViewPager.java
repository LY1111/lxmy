package com.tuansbook.lvxing.Model;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/2/20.
 * 可以设置ViewPager是否滑动
 */
public class CustomViewPager extends ViewPager {

    private boolean canScroll = false; // 是否能滑动

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return canScroll && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return canScroll && super.onInterceptTouchEvent(event);
    }
}
