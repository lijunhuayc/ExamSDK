package com.ljh.custom.cooldialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ljh.custom.cooldialog.widget.RotateLoadingWidget;

/**
 * Desc: 通用LoadingDialog
 * Created by Junhua.Li
 * Date: 2018/04/28 11:54
 */
public class CoolLoadingDialog extends Dialog {
    private static final String TAG = "CoolLoadingDialog";
    private View root;
    private RotateLoadingWidget mRotateLoadingWidget;
    private TextView mLoadingMessageTV;
    private Params params = new Params();

    private CoolLoadingDialog(Context context) {
        this(context, R.style.CoolDialog_Theme);
    }

    private CoolLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        root = View.inflate(getContext(), R.layout.cool_loading_dialog_layout, null);//必须提前加载View
    }

    private void setDialogUI() {
        Window window = getWindow();
        if (null != window) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } else {
            Log.d(TAG, "this window is null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogUI();
        initView();
    }

    private void initView() {
        mRotateLoadingWidget = root.findViewById(R.id.mRotateLoadingWidget);
        mRotateLoadingWidget.start();
        mLoadingMessageTV = root.findViewById(R.id.mLoadingMessageTV);
        applyParams(params);
    }

    private void setParams(Params params) {
        this.params = params;
    }

    private void applyParams(Params params) {
        setMessage(params.mMessage);
    }

    public void setMessage(CharSequence mMessage) {
        params.mMessage = mMessage;
        if (null != mLoadingMessageTV) {
            mLoadingMessageTV.setText(mMessage);
            if (TextUtils.isEmpty(mMessage)) {
                mLoadingMessageTV.setVisibility(View.GONE);
            } else {
                mLoadingMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }

    private static class Params {
        private Context mContext;
        private CharSequence mMessage;
        private boolean mCancelable = true;
        private OnCancelListener mOnCancelListener;
        private OnDismissListener mOnDismissListener;
    }

    public static class Builder {
        public Builder(Context mContext) {
            params.mContext = mContext;
        }

        private Params params = new Params();

        public Builder setMessage(CharSequence mMessage) {
            params.mMessage = mMessage;
            return this;
        }

        public Builder setCancelable(boolean mCancelable) {
            params.mCancelable = mCancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener listener) {
            params.mOnCancelListener = listener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener listener) {
            params.mOnDismissListener = listener;
            return this;
        }

        public CoolLoadingDialog create() {
            CoolLoadingDialog dialog = new CoolLoadingDialog(params.mContext);
            dialog.setContentView(dialog.root);
            dialog.setParams(params);
            dialog.setCancelable(params.mCancelable);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(params.mOnCancelListener);
            dialog.setOnDismissListener(params.mOnDismissListener);
            return dialog;
        }

        public CoolLoadingDialog show() {
            CoolLoadingDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
