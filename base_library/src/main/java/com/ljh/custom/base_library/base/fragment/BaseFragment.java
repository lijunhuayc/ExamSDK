package com.ljh.custom.base_library.base.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.custom.base_library.base.activity.BaseActivity;
import com.ljh.custom.base_library.domain.MainThreadScheduler;
import com.ljh.custom.base_library.utils.Timber;
import com.ljh.custom.cooldialog.CoolLoadingDialog;

import java.util.List;

/**
 * Desc: 不需要上报 PageInEvent/PageOutEvent 事件的Fragment 可继承此抽象类[比如非业务页面图片查看页面等等]
 * Created by ${junhua.li} on 2017/06/15 10:22.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseFragment extends Fragment {
    protected String TAG = "BaseFragment-TAG";
    protected BaseActivity mActivity = null;
    protected Application mApplication = null;
    private View rootView = null; // Fragment的根View
    protected Handler mHandler = new Handler();
//    protected Unbinder mUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
        mApplication = mActivity.getApplication();
        TAG = mActivity.getClass().getSimpleName() + "-" + this.getClass().getSimpleName();
        Log.d("life_" + TAG, "onAttach(Context) ...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("life_" + TAG, "onCreate() ...");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("life_" + TAG, "onCreateView() ...");
        rootView = inflater.inflate(initLayout(), container, false);
//        mUnBinder = ButterKnife.bind(this, rootView);
//        ARouter.getInstance().inject(this);
        mCoolLoadingDialog = new CoolLoadingDialog.Builder(getContext()).setMessage("努力加载中").setCancelable(false).create();
        init();
        return rootView;
    }

    /**
     * @return return the Fragment's root view
     */
    public View getRootView() {
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("life_" + TAG, "onActivityCreated() ...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("life_" + TAG, "onStart() ...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("life_" + TAG, "onResume() ...");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("life_" + TAG, "setUserVisibleHint: isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("life_" + TAG, "onHiddenChanged: hidden = " + hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("life_" + TAG, "onPause() ...");
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
//        mUnBinder.unbind();
        super.onDestroyView();
        Log.d("life_" + TAG, "onDestoryView() ...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("life_" + TAG, "onDestory() ...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("life_" + TAG, "onDetach() ...");
    }

    protected abstract String getPageName();

    protected abstract int initLayout();

    protected abstract void init();

    /**
     * @return boolean 默认不消费
     * @Description: (fragment是否消费返回键, 子类需要消费则重写此方法并返回true)
     */
    public boolean isBackPressed() {
        return false;
    }

    /**
     * API21 +
     */
    public boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * API23 +
     */
    public boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected int getColorResource(int id) {
        if (isMarshmallow()) {
            return getResources().getColor(id, mActivity.getTheme());
        } else {
            return getResources().getColor(id);
        }
    }

    protected Drawable getDrawableResource(int id) {
        if (isMarshmallow()) {
            return getResources().getDrawable(id, mActivity.getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }

    private CoolLoadingDialog mCoolLoadingDialog;

    protected void showProgressView() {
        if (getContext() == null) {
            return;
        }
        if (!mCoolLoadingDialog.isShowing()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mCoolLoadingDialog.show();
            } else {
                MainThreadScheduler.getInstance().post(() -> mCoolLoadingDialog.show());
            }
        }
    }

    protected void hideProgressView() {
        if (null != mCoolLoadingDialog) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mCoolLoadingDialog.cancel();
            } else {
                MainThreadScheduler.getInstance().post(() -> mCoolLoadingDialog.cancel());
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult: requestCode=%s, resultCode=%s, data=%s", requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (null != fragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    protected boolean isVisible(View view) {
        return null != view && view.getVisibility() == View.VISIBLE;
    }

    protected void showView(View view) {
        if (!isVisible(view)) {
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void hideView(View view) {
        if (isVisible(view)) {
            view.setVisibility(View.GONE);
        }
    }
}
