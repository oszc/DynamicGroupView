package com.zc.dynamicgroupview.app.switcher;

/**
 * 9/10/14  4:04 PM
 * Created by JustinZhang.
 */
public interface SwitcherListener {
    enum Side{
        left,right
    }
   void onSwitch(Side side);
}
