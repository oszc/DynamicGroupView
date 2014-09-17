package com.zc.dynamicgroupview.app.switcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zc.dynamicgroupview.app.R;

/**
 * 9/10/14  3:26 PM
 * Created by JustinZhang.
 */
public class Switcher extends LinearLayout {

    @InjectView(R.id.rb_left)
    RadioButton mRbLeft;
    @InjectView(R.id.rb_right)
    RadioButton mRbRight;
    @InjectView(R.id.rg)
    RadioGroup mRg;
    private LayoutInflater mInflater;

    private SwitcherListener mListener;


    public Switcher(Context context) {
        super(context);
        shareConstructor(context, null);
    }

    public Switcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        shareConstructor(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Switcher(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shareConstructor(context, attrs);
    }

    private void shareConstructor(Context context, AttributeSet attrs) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.switch_layout, this, true);
        ButterKnife.inject(this);

        if(attrs != null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Switcher, 0, 0);
            try {
                String leftLabelText = a.getString(R.styleable.Switcher_leftLabelText);
                String rightLabelText = a.getString(R.styleable.Switcher_rightLabelText);
                Drawable leftLabelDrawable =a.getDrawable(R.styleable.Switcher_leftLabelBackGround);
                Drawable rightLabelDrawable = a.getDrawable(R.styleable.Switcher_rightLabelBackGround);

                /*
                int color = a.getColor(R.styleable.Switcher_labelColor, 0);
                if(color == 0){

                    ColorStateList colorStateList = a.getColorStateList(R.styleable.Switcher_labelColor);
                    if(colorStateList == null){
                        mRbLeft.setTextColor(Color.WHITE);
                        mRbRight.setTextColor(Color.WHITE);
                    }else{
                        mRbLeft.setTextColor(colorStateList);
                        mRbRight.setTextColor(colorStateList);
                    }

                }else{
                    mRbLeft.setTextColor(color);
                    mRbRight.setTextColor(color);
                }
                */

                if(leftLabelDrawable!=null){
                    mRbLeft.setBackgroundDrawable(leftLabelDrawable);
                }
                if(rightLabelDrawable!=null){
                    mRbRight.setBackgroundDrawable(rightLabelDrawable);
                }
                setLeftLabelText(leftLabelText);
                setRightLabelText(rightLabelText);
            } finally {
                a.recycle();
            }
        }


        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_left:
                        if(mListener!=null){
                            mListener.onSwitch(SwitcherListener.Side.left);
                        }
                        break;
                    case R.id.rb_right:
                        if(mListener!=null){
                            mListener.onSwitch(SwitcherListener.Side.right);
                        }
                        break;
                }
            }
        });
    }

    public void setLeftLabelText(String text){
        if(!TextUtils.isEmpty(text))
            mRbLeft.setText(text);
    }
    public void setRightLabelText(String text){
        if(!TextUtils.isEmpty(text))
            mRbRight.setText(text);
    }


    public void setSwitchListener(SwitcherListener mListener) {
        this.mListener = mListener;
    }
}
