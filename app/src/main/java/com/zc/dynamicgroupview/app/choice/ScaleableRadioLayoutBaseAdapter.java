package com.zc.dynamicgroupview.app.choice;

/**
 * 9/5/14  3:14 PM
 * Created by JustinZhang.
 */
public interface ScaleableRadioLayoutBaseAdapter<T> {

    int count();
    T getItem(int position);
    String getName(int position);
}
