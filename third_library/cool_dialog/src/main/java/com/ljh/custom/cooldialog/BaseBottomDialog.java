package com.ljh.custom.cooldialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Desc:底部全屏宽度dialog弹窗
 * Created by JiaKun.Yang
 * Date: 2018/06/14 15:09
 */
public abstract class BaseBottomDialog extends DialogFragment {
    //重点部分
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog = new Dialog(getActivity(), getStyleResId());
        alertDialog.setCanceledOnTouchOutside(true);//点击面板外部可以dismiss面板
        View v = LayoutInflater.from(getActivity()).inflate(getLayoutResId(), null);
        initView(v);
        alertDialog.setContentView(v);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        return alertDialog;
    }

    public abstract int getLayoutResId();

    public abstract void initView(View v);

    public int getStyleResId() {
        return R.style.library_bottom_sheet_style;
    }
}
