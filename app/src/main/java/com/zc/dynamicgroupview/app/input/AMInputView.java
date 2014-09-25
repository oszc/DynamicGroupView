package com.zc.dynamicgroupview.app.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.zc.dynamicgroupview.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 9/23/14  11:19 AM
 * Created by JustinZhang.
 * 可添加，可删除
 *
 * 添加（add(String content))
 * private 删除 (remove)
 * 获得数据(getData)
 * 去除删除功能(setRemoveable)
 * 2.005yDBYB0JB32a00842268d80Z6CCr
 *
 * 删除的时候和添加的时候，添加动画，从上至下，不仅仅是该控件，连AddView 这个控件也需要，所以addview这个控件只能放在
 *
 */
public class AMInputView extends LinearLayout{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<View> mViews;
    private int mTopMargin = 10;

    private View mCorrespondView = null;

    public AMInputView(Context context) {
        super(context);
        shareConstructor(context, null);
    }

    public AMInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shareConstructor(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AMInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shareConstructor(context, attrs);
    }

    private void shareConstructor(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        mContext = context;
        mViews = new ArrayList<View>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.addable_layout,this,true);
    }

    public void addSubView(){

        final View view = mInflater.inflate(R.layout.am_input_item,this,false);
        if(mViews.size()>0) {
            LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.topMargin = mTopMargin;
            view.setLayoutParams(params);

        }
        view.findViewById(R.id.rl_del).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
            }
        });
        addView(view);


    }

    public List<String> getResult(){
        List<String> result = new ArrayList<String>();
        if(mViews!=null && mViews.size()>0){
            for (int i = 0; i < mViews.size(); i++) {
                View view = mViews.get(i);
                if(view !=null) {
                    EditText et = (EditText) view.findViewById(R.id.et_input);
                    if(et !=null){
                        String text = et.getText().toString();
                       if(!TextUtils.isEmpty(text)){
                           result.add(text);
                       }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void addView(final View child) {
        mViews.add(child);

        child.setVisibility(View.INVISIBLE);


        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.display_translate);
        final Animation fadeAnim = AnimationUtils.loadAnimation(mContext, R.anim.display_fade);

      //  child.startAnimation(animation);
        if(mCorrespondView!=null){
            mCorrespondView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    child.startAnimation(fadeAnim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            child.startAnimation(fadeAnim);
        }

        fadeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                child.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                child.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        super.addView(child);

    }

    @Override
    public void removeView(final View view) {
        final Animation animationTranslate = AnimationUtils.loadAnimation(mContext, R.anim.disappear_translate);
        Animation animationFade = AnimationUtils.loadAnimation(mContext, R.anim.disappear_fade);
        if(mViews.contains(view)){

            final int indexOfDeleteView = mViews.indexOf(view);
            view.startAnimation(animationFade);

            animationFade.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    view.setVisibility(View.INVISIBLE);
                    for(int i = indexOfDeleteView+1; i < mViews.size(); i++){
                        mViews.get(i).startAnimation(animationTranslate);
                    }

                    if(mCorrespondView!=null){
                        mCorrespondView.startAnimation(animationTranslate);
                    }


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animationTranslate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mViews.remove(view);
                    AMInputView.super.removeView(view);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        }

    }

    @Override
    public void removeAllViews() {
        mViews.clear();
        super.removeAllViews();
    }

    public int getTopMargin() {
        return mTopMargin;
    }

    public void setTopMargin(int topMargin) {
        this.mTopMargin = topMargin;
    }

    public void setCorrespondView(View correspondView) {
        this.mCorrespondView = correspondView;
    }
}
