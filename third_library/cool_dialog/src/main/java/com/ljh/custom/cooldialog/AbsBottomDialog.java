package com.ljh.custom.cooldialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Desc:底部弹窗基类
 * Created by JiaKun.Yang
 * Date: 2018/06/19 11:02
 */
public abstract class AbsBottomDialog extends Dialog {
    protected Context mContext;

    public AbsBottomDialog(Context context) {
        this(context, R.style.library_bottom_slide_dialog);
    }

    public AbsBottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initView();
    }

    public AbsBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    private void initView() {
        refreshTheme();
        setContentView(buildContentView(mContext));
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract View buildContentView(Context context);

    /**
     *
     */
    private void refreshTheme() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.library_bottom_slide_dialog);
        setCanceledOnTouchOutside(true);
    }
}
