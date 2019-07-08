package com.frxs.order.listener;

import android.view.MotionEvent;

/**
 * Created by ewu on 2016/3/16.
 */
public interface MyOnTouchListener {
    public void pullupFromBottomEvent(MotionEvent event); //底部上拉事件
    public void pulldownFromTopEvent(MotionEvent event);  //顶部下拉事件
    public void scrollupEvent(MotionEvent event); //向上滚动事件
    public void scrolldownEvent(MotionEvent event); //向下滚动事件
}
