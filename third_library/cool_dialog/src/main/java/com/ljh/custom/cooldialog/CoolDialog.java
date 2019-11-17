package com.ljh.custom.cooldialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Desc: 通用标准 Dialog
 * Created by Junhua.Li
 * Date: 2018/04/28 11:54
 */
public class CoolDialog extends Dialog {
    private static final String TAG = "CoolDialog";
    private View root;
    private LinearLayout coolDialogTitleLL;
    //    private ImageView mIconView;
    private TextView mTitleView;
    private FrameLayout coolDialogContentFL;
    private TextView mMessageView;
    private FrameLayout coolDialogCustomContentFL;
    private LinearLayout coolDialogButtonLL;
    private TextView coolDialogNegativeBT;
    private TextView coolDialogPositiveBT;
    private Params params = new Params();

    private CoolDialog(Context context) {
        this(context, R.style.CoolDialog_Theme);
    }

    private CoolDialog(Context context, int themeResId) {
        super(context, themeResId);
        root = View.inflate(getContext(), R.layout.cool_dialog_layout, null);//必须提前加载View
    }

    private void setDialogUI() {
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            int margin = dp2px(getContext(), 56);
            params.width = getContext().getResources().getDisplayMetrics().widthPixels - margin * 2;
            getWindow().setAttributes(params);
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
        coolDialogTitleLL = root.findViewById(R.id.coolDialogTitleLL);
//        mIconView = root.findViewById(R.id.coolDialogIconIV);
        mTitleView = root.findViewById(R.id.coolDialogTitleTV);
        coolDialogContentFL = root.findViewById(R.id.coolDialogContentFL);
        mMessageView = root.findViewById(R.id.coolDialogMessageTV);
        coolDialogCustomContentFL = root.findViewById(R.id.coolDialogCustomContentFL);
        coolDialogButtonLL = root.findViewById(R.id.coolDialogButtonLL);
        coolDialogNegativeBT = root.findViewById(R.id.coolDialogNegativeBT);
        coolDialogPositiveBT = root.findViewById(R.id.coolDialogPositiveBT);
        applyParams(params);
    }

    private void setParams(Params params) {
        this.params = params;
    }

    private void applyParams(Params params) {
        //Builder方式设置, mIconId的优先级更高
//        if (params.mIconId != 0) {
//            setIcon(params.mIconId);
//        } else if (null != params.mIcon) {
//            setIcon(params.mIcon);
//        }
        if (!TextUtils.isEmpty(params.mTitle)) {
            setTitle(params.mTitle);
        }

        if (params.mMessageIconId != 0) {
            setMessageIcon(params.mMessageIconId);
        } else if (null != params.mMessageIcon) {
            setMessageIcon(params.mMessageIcon);
        }
        if (!TextUtils.isEmpty(params.mMessage)) {
            setMessage(params.mMessage);
        }

        if (params.mBackgroundColor >= 0) {
            root.setBackgroundColor(params.mBackgroundColor);
        }

        setNegativeButton(params.mNegativeButtonText, params.mNegativeButtonListener);
        setPositiveButton(params.mPositiveButtonText, params.mPositiveButtonListener);
        setCustomView(params.mCustomView);
    }

//    public void setIcon(Drawable mIcon) {
//        params.mIcon = mIcon;
//        if (null != mIconView) {
//            mIconView.setImageDrawable(mIcon);
//            if (null != mIcon) {
//                mIconView.setVisibility(View.VISIBLE);
//            } else {
//                mIconView.setVisibility(View.GONE);
//            }
//        }
//        resetTitleArea();
//    }
//
//    public void setIcon(int mIconId) {
//        params.mIconId = mIconId;
//        if (null != mIconView) {
//            mIconView.setImageResource(mIconId);
//            if (0 != mIconId) {
//                mIconView.setVisibility(View.VISIBLE);
//            } else {
//                mIconView.setVisibility(View.GONE);
//            }
//        }
//        resetTitleArea();
//        return this;
//    }

    /**
     * 供链式调用使用
     *
     * @param mTitle
     * @return
     */
    public CoolDialog setTitleCool(CharSequence mTitle) {
        setTitle(mTitle);
        return this;
    }

    @Override
    public void setTitle(CharSequence mTitle) {
        params.mTitle = mTitle;
        if (null != mTitleView) {
            mTitleView.setText(mTitle);
            if (!TextUtils.isEmpty(mTitle)) {
                mTitleView.setVisibility(View.VISIBLE);
            } else {
                mTitleView.setVisibility(View.GONE);
            }
        }
        resetTitleArea();
    }

    private CoolDialog resetTitleArea() {
        if (null != coolDialogTitleLL) {
//            if (params.mIconId != 0 || null != params.mIcon || !TextUtils.isEmpty(params.mTitle)) {
            if (!TextUtils.isEmpty(params.mTitle)) {
                coolDialogTitleLL.setVisibility(View.VISIBLE);
            } else {
                coolDialogTitleLL.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public CoolDialog setMessageIcon(Drawable drawable) {
        params.mMessageIcon = drawable;
        if (null != mMessageView) {
            if (null != drawable) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            mMessageView.setCompoundDrawables(null, drawable, null, null);
        }
        resetMessageArea();
        return this;
    }

    public CoolDialog setMessageIcon(int mIconId) {
        params.mMessageIconId = mIconId;
        if (null != mMessageView) {
            Drawable drawable = getContext().getResources().getDrawable(mIconId);
            if (null != drawable) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            mMessageView.setCompoundDrawables(null, drawable, null, null);
        }
        resetMessageArea();
        return this;
    }

    public CoolDialog setMessage(CharSequence mMessage) {
        params.mMessage = mMessage;
        if (null != mMessageView) {
            mMessageView.setText(mMessage);
        }
        resetMessageArea();
        return this;
    }

    public CoolDialog setMessageGravity(int gravity) {
        if (null != mMessageView) {
            mMessageView.setGravity(gravity);
        }
        return this;
    }

    private void resetMessageArea() {
        if (null != coolDialogContentFL) {
            if (params.mMessageIconId != 0 || null != params.mMessageIcon || !TextUtils.isEmpty(params.mMessage)) {
                coolDialogContentFL.setVisibility(View.VISIBLE);
            } else {
                coolDialogContentFL.setVisibility(View.GONE);
            }
        }
    }

    public CoolDialog setNegativeButton(CharSequence text, OnClickListener listener) {
        params.mNegativeButtonText = text;
        params.mNegativeButtonListener = listener;
        if (null != coolDialogNegativeBT) {
            coolDialogNegativeBT.setText(text);
            coolDialogNegativeBT.setOnClickListener(v -> {
                if (params.mAutoCancel) {
                    cancel();
                }
                if (null != listener) {
                    listener.onClick(CoolDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
            });
        }
        resetButtonArea();
        return this;
    }

    public CoolDialog setPositiveButton(CharSequence text, OnClickListener listener) {
        params.mPositiveButtonText = text;
        params.mPositiveButtonListener = listener;
        if (null != coolDialogPositiveBT) {
            coolDialogPositiveBT.setText(text);
            coolDialogPositiveBT.setOnClickListener(v -> {
                if (params.mAutoCancel) {
                    cancel();
                }
                if (null != listener) {
                    listener.onClick(CoolDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
            });
        }
        resetButtonArea();
        return this;
    }

    private void resetButtonArea() {
        if (null != coolDialogButtonLL && null != coolDialogNegativeBT && null != coolDialogPositiveBT) {
            LinearLayout.LayoutParams positiveBtLP = (LinearLayout.LayoutParams) coolDialogPositiveBT.getLayoutParams();
            if (!TextUtils.isEmpty(params.mNegativeButtonText) && !TextUtils.isEmpty(params.mPositiveButtonText)) {
                coolDialogButtonLL.setVisibility(View.VISIBLE);
                coolDialogNegativeBT.setVisibility(View.VISIBLE);
                coolDialogNegativeBT.setBackgroundResource(R.drawable.cool_dialog_negative_bt_drawable);
                coolDialogPositiveBT.setVisibility(View.VISIBLE);
                coolDialogPositiveBT.setBackgroundResource(R.drawable.cool_dialog_positive_bt_drawable);
                positiveBtLP.leftMargin = dp2px(getContext(), 16);
                coolDialogPositiveBT.setLayoutParams(positiveBtLP);
            } else if (!TextUtils.isEmpty(params.mNegativeButtonText) && TextUtils.isEmpty(params.mPositiveButtonText)) {
                coolDialogButtonLL.setVisibility(View.VISIBLE);
                coolDialogNegativeBT.setVisibility(View.VISIBLE);
//                coolDialogNegativeBT.setBackgroundResource(R.drawable.cool_dialog_negative_bt_only_drawable);
                coolDialogPositiveBT.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(params.mNegativeButtonText) && !TextUtils.isEmpty(params.mPositiveButtonText)) {
                coolDialogButtonLL.setVisibility(View.VISIBLE);
                coolDialogNegativeBT.setVisibility(View.GONE);
                coolDialogPositiveBT.setVisibility(View.VISIBLE);
                positiveBtLP.leftMargin = 0;
                coolDialogPositiveBT.setLayoutParams(positiveBtLP);
//                coolDialogPositiveBT.setBackgroundResource(R.drawable.cool_dialog_positive_bt_only_drawable);
            } else if (TextUtils.isEmpty(params.mNegativeButtonText) && TextUtils.isEmpty(params.mPositiveButtonText)) {
                coolDialogButtonLL.setVisibility(View.GONE);
            }
        }
    }

    public CoolDialog setCustomView(View mCustomView) {
        params.mCustomView = mCustomView;
        if (null != mCustomView) {
            if (null != coolDialogCustomContentFL) {
                coolDialogCustomContentFL.setVisibility(View.VISIBLE);
                coolDialogCustomContentFL.removeAllViews();
                coolDialogCustomContentFL.addView(mCustomView);
            }
            if (null != coolDialogContentFL) {
                coolDialogContentFL.setVisibility(View.GONE);
            }
        } else {
            if (null != coolDialogCustomContentFL) {
                coolDialogCustomContentFL.setVisibility(View.GONE);
                coolDialogCustomContentFL.removeAllViews();
            }
            if (null != coolDialogContentFL) {
                coolDialogContentFL.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    private static class Params {
        private Context mContext;
        //        private int mIconId = 0;
//        private Drawable mIcon;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private int mMessageIconId = 0;
        private int mBackgroundColor = -1;
        private Drawable mMessageIcon;
        private CharSequence mPositiveButtonText;
        private DialogInterface.OnClickListener mPositiveButtonListener;
        private CharSequence mNegativeButtonText;
        private DialogInterface.OnClickListener mNegativeButtonListener;
        private boolean mAutoCancel = true;//点击按钮后是否调用cancel()
        private boolean mCancelable = true;
        private DialogInterface.OnCancelListener mOnCancelListener;
        private DialogInterface.OnDismissListener mOnDismissListener;
        private View mCustomView;
    }

    public static class Builder {
        public Builder(Context mContext) {
            params.mContext = mContext;
        }

        private Params params = new Params();

//        public Builder setIcon(int mIconId) {
//            params.mIconId = mIconId;
//            return this;
//        }
//
//        public Builder setIcon(Drawable mIcon) {
//            params.mIcon = mIcon;
//            return this;
//        }

        public Builder setTitle(CharSequence mTitle) {
            params.mTitle = mTitle;
            return this;
        }

        public Builder setMessage(CharSequence mMessage) {
            params.mMessage = mMessage;
            return this;
        }

        public Builder setMessageIcon(Drawable mMessageIcon) {
            params.mMessageIcon = mMessageIcon;
            return this;
        }

        public Builder setMessageIcon(int mMessageIconId) {
            params.mMessageIconId = mMessageIconId;
            return this;
        }

        public Builder setPositiveButton(CharSequence mPositiveButtonText, OnClickListener listener) {
            params.mPositiveButtonText = mPositiveButtonText;
            params.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButtonText(CharSequence mPositiveButtonText) {
            params.mPositiveButtonText = mPositiveButtonText;
            return this;
        }

        public Builder setPositiveButtonListener(OnClickListener listener) {
            params.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence mNegativeButtonText, OnClickListener listener) {
            params.mNegativeButtonText = mNegativeButtonText;
            params.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButtonText(CharSequence mNegativeButtonText) {
            params.mNegativeButtonText = mNegativeButtonText;
            return this;
        }

        public Builder setNegativeButtonListener(OnClickListener listener) {
            params.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setAutoCancel(boolean mAutoCancel) {
            params.mAutoCancel = mAutoCancel;
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

        public Builder setCustomViewLayoutResId(int mCustomViewLayoutResId) {
            params.mCustomView = View.inflate(params.mContext, mCustomViewLayoutResId, null);
            return this;
        }

        public Builder setCustomView(View mCustomView) {
            params.mCustomView = mCustomView;
            return this;
        }

        public Builder setBackgroundColor(int pBackgroundColor) {
            params.mBackgroundColor = pBackgroundColor;
            return this;
        }

        public CoolDialog create() {
            CoolDialog dialog = new CoolDialog(params.mContext);
            dialog.setContentView(dialog.root);
            dialog.setParams(params);
            dialog.setCancelable(params.mCancelable);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(params.mOnCancelListener);
            dialog.setOnDismissListener(params.mOnDismissListener);
            return dialog;
        }

        public CoolDialog show() {
            CoolDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    /**
     * 提供链式调用
     *
     * @param cancel
     */
    public CoolDialog setCanceledOnTouchOutsideCool(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        super.show();
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
