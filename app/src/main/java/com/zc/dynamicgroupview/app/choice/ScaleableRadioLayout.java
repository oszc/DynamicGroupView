package com.zc.dynamicgroupview.app.choice;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zc.dynamicgroupview.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/5/14  2:50 PM
 * Created by JustinZhang.
 * 动态的添加radiobutton
 * <p/>
 * List<String > ??? onItemClick? to select? and position?
 * 单选题，多选题？？？
 * 注意，必须有限个数，因为容器位scrollview而非Listview,也不会重复创建！
 * 尽量包裹在ScrollView中
 */
public class ScaleableRadioLayout extends LinearLayout implements View.OnTouchListener {


    public static enum ChoiceMode {
        single, multiple
    }

    private static final String TAG = "ScaleableRadioLayout";
    private Context mContext;
    private LayoutInflater mInflater;
    private ScaleableRadioLayoutBaseAdapter mAdapter;

    private List<Rect> mChildRects; //记录每个list的位置

    private OnScaleableItemClick mOnItemClickListener;

    private ChoiceMode mChoiceMode;  //默认是single

    private List<Integer> mSelectedItems; //选中的位置

    public ScaleableRadioLayout(Context context) {
        super(context);
        shareConstructor(context, null);
    }

    public ScaleableRadioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        shareConstructor(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ScaleableRadioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shareConstructor(context, attrs);
    }

    private void shareConstructor(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScaleableRadioLayout, 0, 0);

            int choiceMode = 0;

            try {
                choiceMode = a.getInteger(R.styleable.ScaleableRadioLayout_choiceMode, 0);
            } finally {
                a.recycle();
            }

            if (choiceMode == 0) {
                mChoiceMode = ChoiceMode.single;
            } else {
                mChoiceMode = ChoiceMode.multiple;
            }

        } else {
            mChoiceMode = ChoiceMode.single;
        }

        mChildRects = new ArrayList<Rect>();
        mSelectedItems = new ArrayList<Integer>();
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.addable_layout, this, true);
        setOnTouchListener(this);

    }

    public void setAdapter(ScaleableRadioLayoutBaseAdapter adapter) {
        if (adapter == null) {
            return;
        }
        mAdapter = adapter;
        notifiyDatasetChanged();
    }

    private void notifiyDatasetChanged() {

        removeAllViews();
        for (int i = 0; i < mAdapter.count(); i++) {

            View view = mInflater.inflate(R.layout.item, this, false);
            String content = mAdapter.getName(i);

            TextView tv = (TextView) view.findViewById(R.id.tv_content);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_raido);

            if (mChoiceMode == ChoiceMode.multiple) {
                iv.setImageResource(R.drawable.ic_checkbox_normal);
            }

            tv.setText(content);
            addView(view);
        }

        post(new Runnable() {
            @Override
            public void run() {
                mChildRects.clear();
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    Rect r = new Rect();
                    view.getHitRect(r);
                    mChildRects.add(r);
                }
            }
        });
    }

    private static final long MAX_CLICK_DURATION = 200;
    private long moveIn = 0;
    private long moveOut = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Point selectedPoint = new Point((int) (event.getX()), (int) (event.getY()));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveIn = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                moveOut = System.currentTimeMillis();

                if ((moveOut - moveIn) < MAX_CLICK_DURATION) {
                    for (int j = 0; j < mChildRects.size(); j++) {
                        Integer i = j;
                        View view = getChildAt(i);
                        ImageView imageView = ((ImageView) view.findViewById(R.id.iv_raido));

                        if (mChildRects.get(i).contains(selectedPoint.x, selectedPoint.y)) {
                            //selected

                            if (mChoiceMode == ChoiceMode.single) {
                                //单选
                                mSelectedItems.clear();
                                mSelectedItems.add(i);
                              //  updateListener();
                                imageView.setImageResource(R.drawable.ic_raidon_selected);
                            } else if (mChoiceMode == ChoiceMode.multiple) {
                                if (isItemSelected(i)) {
                                    //多选 原有选中
                                    mSelectedItems.remove(i);
                                    imageView.setImageResource(R.drawable.ic_checkbox_normal);
                                } else {
                                    //多选 原未选中
                                    mSelectedItems.add(i);
                                    imageView.setImageResource(R.drawable.ic_checkbox_checked);
                                }
                            }
                            updateListener();
                        } else {
                            //not selected
                            if (mChoiceMode == ChoiceMode.single) {
                                imageView.setImageResource(R.drawable.ic_raido_normal);
                            } else if (mChoiceMode == ChoiceMode.multiple) {
                                //do nothing
                            }
                        }
                    }
                    requestLayout();
                }
                break;
        }
        return true;
    }

    private void updateListener() {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(mSelectedItems);
        }
    }

    public OnScaleableItemClick getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public void setOnItemClickListener(OnScaleableItemClick onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setmChoiceMode(ChoiceMode mChoiceMode) {
        this.mChoiceMode = mChoiceMode;
    }

    public boolean isItemSelected(Integer index) {
        return mSelectedItems.contains(index);
    }


}
