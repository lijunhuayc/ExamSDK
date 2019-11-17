package com.ljh.custom.base_library.base.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.domain.MainThreadScheduler;
import com.ljh.custom.base_library.utils.LocalActivityManager;
import com.ljh.custom.base_library.utils.Timber;
import com.ljh.custom.cooldialog.CoolDialog;
import com.ljh.custom.cooldialog.CoolLoadingDialog;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Desc: 通用Activity基类, 所有业务Activity应该继承于此类或者此类的派生类
 * Created by ${junhua.li} on 2017/06/15 10:18.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseActivity extends BasePermissionActivity {
    protected String TAG = getClass().getSimpleName();
    public Application mApplication;
    public Context mContext;
    public LayoutInflater mLayoutInflater;
    public Handler mHandler = new Handler();
//    private Unbinder mUnBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Log.d("life_" + TAG, "onCreate()");
        LocalActivityManager.getInstance().add(this);
        mApplication = getApplication();
        mContext = this;
        mLayoutInflater = getLayoutInflater();
        setContentView();
//        mUnBinder = ButterKnife.bind(this);
//        ARouter.getInstance().inject(this);
//        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
//        mSwipeBackLayout.setEdgeTrackingEnabled(isSwipeBack() ? SwipeBackLayout.EDGE_LEFT : -1);
        mCoolLoadingDialog = new CoolLoadingDialog.Builder(this).setMessage("努力加载中").setCancelable(false).create();
        initUI();
        onCreated();
    }

    private void setContentView() {
        setContentView(initLayout());
    }

    protected abstract int initLayout();

    protected abstract void initUI();

    protected abstract void onCreated();

//    protected boolean isSwipeBack() {
//        return true;
//    }

    public interface KeyBoardVisibleChangeCallBack {
        void callBack(boolean visible, int height);
    }

    boolean sLastVisible = false;
    public KeyBoardVisibleChangeCallBack mChangeCallBack;
    public ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            final View decorView = getWindow().getDecorView();
            decorView.getWindowVisibleDisplayFrame(rect);
            int displayHeight = rect.bottom - rect.top;
            int height = decorView.getHeight();
            int keyboardHeight = height - displayHeight - rect.top;
            boolean visible = (double) displayHeight / height < 0.8;

            Log.d(TAG, "DecorView display height = " + displayHeight);
            Log.d(TAG, "DecorView height = " + height);
            Log.d(TAG, "softKeyboard keyboardHeight = " + keyboardHeight);
            Log.d(TAG, "softKeyboard visible = " + visible);

            if (visible != sLastVisible) {
                if (null != mChangeCallBack) {
                    mChangeCallBack.callBack(visible, keyboardHeight);
                }
            }
            sLastVisible = visible;
        }
    };

    public void removeSoftKeyBoardVisibleListener() {
        getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(listener);
    }

    public void addOnSoftKeyBoardVisibleListener(final KeyBoardVisibleChangeCallBack callBack) {
        mChangeCallBack = callBack;
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("life_" + TAG, "onNewIntent()");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("life_" + TAG, "onPostCreate()");
    }

    @Override
    protected void onStart() {
        Log.d("life_" + TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d("life_" + TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("life_" + TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("life_" + TAG, "onPause()");
        hideInputMethod();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("life_" + TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("life_" + TAG, "onDestroy()");
//        mUnBinder.unbind();
        mHandler.removeCallbacksAndMessages(null);
        LocalActivityManager.getInstance().remove(this);
        super.onDestroy();
    }

    private CoolLoadingDialog mCoolLoadingDialog;

    public void showProgressView(String message) {
        if (!mCoolLoadingDialog.isShowing()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mCoolLoadingDialog.setMessage(message);
                mCoolLoadingDialog.show();
            } else {
                MainThreadScheduler.getInstance().post(() -> {
                    mCoolLoadingDialog.setMessage(message);
                    mCoolLoadingDialog.show();
                });
            }
        }
    }

    /**
     * 显示加载进度条
     */
    public void showProgressView() {
        showProgressView("努力加载中");
    }

    /**
     * 隐藏加载进度条
     */
    public void hideProgressView() {
        if (null != mCoolLoadingDialog && !hasDestroyed()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mCoolLoadingDialog.cancel();
            } else {
                MainThreadScheduler.getInstance().post(() -> mCoolLoadingDialog.cancel());
            }
        }
    }

    /**
     * 显示错误提示 Dialog
     *
     * @param pTitle
     * @param pMessage
     * @param needFinish 是否需要关闭当前页面
     */
    public void showErrorDialog(String pTitle, String pMessage, boolean needFinish) {
        new CoolDialog.Builder(this)
                .setTitle(pTitle)
                .setMessage(pMessage)
                .setMessageIcon(R.drawable.library_icon_warning)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (needFinish) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 显示错误提示 Dialog
     *
     * @param pTitle
     * @param pMessage
     */
    public void showErrorDialog(String pTitle, String pMessage) {
        showErrorDialog(pTitle, pMessage, false);
    }

    /**
     * Tips: 多种状态时需要将各种状态都写上, 否则很容易混乱
     *
     * @param normalDrawable
     * @param pressedDrawable
     * @param selectedDrawable
     * @param unusableDrawable
     * @return
     * @see {@link android.graphics.drawable.AnimatedStateListDrawable}
     */
    public Drawable addStateListDrawable(int normalDrawable, int pressedDrawable, int selectedDrawable, int unusableDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();//API21+ 可以使用 AnimatedStateListDrawable
        if (normalDrawable > 0) {
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, getDrawableResource(normalDrawable));
        }
        if (pressedDrawable > 0) {
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, getDrawableResource(pressedDrawable));
        }
        return stateListDrawable;
    }

    /**
     * @return boolean
     * @Description: (是否前台运行)
     * @author (ljh) @date 2015年6月24日 下午3:06:00
     */
    public boolean isAppOnForeground() {
        //Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有虚拟按键
     *
     * @return
     */
    public boolean hasPermanentKey() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (hasBackKey && hasHomeKey) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 横竖屏切换会保留上一次的fragment在fragmentmanager中
     */
    public void clearAllCacheFragment(FragmentManager fmg) {
        List<Fragment> mExists = fmg.getFragments();
        FragmentTransaction ft = fmg.beginTransaction();
        if (null != mExists && !mExists.isEmpty()) {
            for (Fragment fg : mExists) {
                ft.remove(fg);
            }
        }
        ft.commit();
    }

    /**
     * 判断 Activity 是否销毁
     * API 17以下无 此方法, 所以重写此方法 API17以下用 isFinishing()替代
     * {@link #isFinishing()} {@link #isDestroyed()}
     */
    public boolean hasDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            return super.isFinishing();
        }
    }

    /**
     * @return void
     * @Description: 隐藏输入法
     * @author (ljh) @date 2016-4-28 下午3:20:57
     */
    public void showInputMethod(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideInputMethod();
                }
            }
        });
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        } catch (Exception e) {
        }
    }

    /**
     * @return void
     * @Description: 隐藏输入法
     * @author (ljh) @date 2016-4-28 下午3:20:57
     */
    public void hideInputMethod() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) { // 为true表示输入法处于打开状态
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);// 关闭输入法
            }
        } catch (Exception e) {
        }
    }

    public View inflateView(@LayoutRes int resource) {
        return View.inflate(mContext, resource, null);
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     */
    public static int getResourceId(String variableName, Class<?> c) {
        //test code: getResId("ic_test_" + position, R.drawable.class);
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getColorResource(@ColorRes int id) {
        if (isMarshmallow()) {
            return getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }

    public ColorStateList getColorStateListResource(@ColorRes int id) {
        if (isMarshmallow()) {
            return getColorStateList(id);
        } else {
            return getResources().getColorStateList(id);
        }
    }

    public Drawable getDrawableResource(@DrawableRes int id) {
        if (isLollipop()) {
            return getDrawable(id);
        } else {
            return getResources().getDrawable(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult: requestCode=%s, resultCode=%s, data=%s", requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
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
