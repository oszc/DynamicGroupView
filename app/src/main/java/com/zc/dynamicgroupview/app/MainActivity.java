package com.zc.dynamicgroupview.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zc.dynamicgroupview.app.choice.OnScaleableItemClick;
import com.zc.dynamicgroupview.app.choice.ScaleableRadioLayout;
import com.zc.dynamicgroupview.app.choice.ScaleableRadioLayoutBaseAdapter;
import com.zc.dynamicgroupview.app.input.AMInputView;
import com.zc.dynamicgroupview.app.switcher.Switcher;
import com.zc.dynamicgroupview.app.switcher.SwitcherListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 特定用途，单选题，
 */
public class MainActivity extends ActionBarActivity implements OnScaleableItemClick, SwitcherListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    @InjectView(R.id.srl)
    ScaleableRadioLayout mSrl;
    @InjectView(R.id.switcher)
    Switcher mSwitcher;
    @InjectView(R.id.amiv)
    AMInputView mAmiv;
    @InjectView(R.id.bt_add_view)
    Button mBtAddView;
    @InjectView(R.id.bt_get_result)
    Button mBtGetResult;

    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mData = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            mData.add(i + "");
        }

        mSrl.setAdapter(mAdapter);

        mSrl.setOnItemClickListener(this);
        mSwitcher.setSwitchListener(this);

        mAmiv.setCorrespondView(mBtAddView);
        mBtAddView.setOnClickListener(this);
        mBtGetResult.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ScaleableRadioLayoutBaseAdapter<String> mAdapter = new ScaleableRadioLayoutBaseAdapter<String>() {
        @Override
        public int count() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public String getName(int position) {
            return mData.get(position);
        }
    };

    @Override
    public void onItemClick(List<Integer> position) {
        Log.e(TAG, "click: " + position);
    }

    @Override
    public void onSwitch(Side side) {
        switch (side) {

            case left:
                Log.e(TAG, "switch to left");
                break;
            case right:
                Log.e(TAG, "switch to right");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_view:
                mAmiv.addSubView();
                break;
            case R.id.bt_get_result:
                Log.e(TAG,mAmiv.getResult()+"");
                break;
        }
    }
}
