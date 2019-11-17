package com.ljh.custom.base_library.business.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.base.activity.BaseActivity;
import com.ljh.custom.base_library.base.fragment.BaseFragment;
import com.ljh.custom.base_library.business.ExamListFragment;

/**
 * Desc: 考试列表页面
 * Created by Junhua.Li
 * Date: 2019/11/17 19:37
 */
public class ExamListActivity extends BaseActivity {
    TextView mOnlineExamLabel;
    TextView mOnlineExamLine;
    TextView mSimulationExamLabel;
    TextView mSimulationExamLine;
    ViewPager mViewPager;
    FragmentPagerAdapter mPagerAdapter;

    @Override
    protected int initLayout() {
        return R.layout.library_activity_exam_list_layout;
    }

    @Override
    protected void initUI() {
        mOnlineExamLabel = findViewById(R.id.onlineExamLabel);
        mOnlineExamLine = getView(R.id.onlineExamLine);
        mSimulationExamLabel = getView(R.id.simulationExamLabel);
        mSimulationExamLine = getView(R.id.simulationExamLine);
        mViewPager = getView(R.id.viewPager);
    }

    @Override
    protected void onCreated() {
        mOnlineExamLabel.setOnClickListener(v -> {
            showView(mOnlineExamLine);
            hideView(mSimulationExamLine);
            mViewPager.setCurrentItem(0);
        });
        mSimulationExamLabel.setOnClickListener(v -> {
            hideView(mOnlineExamLine);
            showView(mSimulationExamLine);
            mViewPager.setCurrentItem(1);
        });
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int pI) {
                BaseFragment fragment = new ExamListFragment();
                Bundle bundle = new Bundle();
                if (pI == 1) {
                    bundle.putInt("exam_type", 1);
                } else {
                    bundle.putInt("exam_type", 2);
                }
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }
}
