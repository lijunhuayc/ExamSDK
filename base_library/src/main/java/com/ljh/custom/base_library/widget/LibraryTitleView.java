package com.ljh.custom.base_library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.utils.ScreenUtils;

/**
 * Desc: 通用TitleView
 * Created by Junhua.Li
 * Date: 2018/06/13 18:00
 */
public class LibraryTitleView extends RelativeLayout {
    RelativeLayout bar_left_rl;
    ImageView bar_left_back_iv;
    View bar_left_back_tips;
    RelativeLayout bar_center_rl;
    TextView bar_title_tv;
    RelativeLayout bar_right_rl;
    View bar_right_menu_tips;
    TextView bar_right_menu_tv;
    ImageView bar_right_menu_iv1;
    ImageView bar_right_menu_iv2;
    boolean mBackTipsEnable;
    int mBackIcon;
    String mTitleContent;
    int mTitleContentDrawableRight;
    int mTitleSize;
    int mTitleColor;
    String mMenuText;
    int mMenuTextColor;
    int mMenuTextDrawableLeft;
    int mMenuIcon1;
    int mMenuIcon2;
    int mHeight;
    OnClickListener mOnBackListener;
    OnMenuClickListener mOnMenuClickListener;

    public LibraryTitleView(Context context) {
        this(context, null);
    }

    public LibraryTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LibraryTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        initView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LibraryTitleView);
        int mBackgroundColor = a.getColor(R.styleable.LibraryTitleView_library_background_color, Color.parseColor("#FFFFFFFF"));
        setBackgroundColor(mBackgroundColor);
        mHeight = ScreenUtils.dp2px(context, 48);

        mBackTipsEnable = a.getBoolean(R.styleable.LibraryTitleView_library_back_tips_enable, false);
        mBackIcon = a.getResourceId(R.styleable.LibraryTitleView_library_back_icon, R.drawable.library_common_back_icon_brand);
        mTitleContent = a.getString(R.styleable.LibraryTitleView_library_title_content);
        mTitleContentDrawableRight = a.getResourceId(R.styleable.LibraryTitleView_library_title_content_drawable_right, 0);
        mTitleSize = a.getInt(R.styleable.LibraryTitleView_library_title_size, 18);
        mTitleColor = a.getColor(R.styleable.LibraryTitleView_library_title_color, Color.parseColor("#FF000000"));
        mMenuText = a.getString(R.styleable.LibraryTitleView_library_menu_text);
        mMenuTextColor = a.getColor(R.styleable.LibraryTitleView_library_menu_text_color, Color.parseColor("#FF000000"));
        mMenuTextDrawableLeft = a.getResourceId(R.styleable.LibraryTitleView_library_menu_text_drawable_left, 0);
        mMenuIcon1 = a.getResourceId(R.styleable.LibraryTitleView_library_menu_icon1, 0);
        mMenuIcon2 = a.getResourceId(R.styleable.LibraryTitleView_library_menu_icon2, 0);
        a.recycle();
        applyData();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.library_common_title_bar_layout, this);
        bar_left_rl = findViewById(R.id.bar_left_rl);
        bar_left_back_iv = findViewById(R.id.bar_left_back_iv);
        bar_left_back_tips = findViewById(R.id.bar_left_back_tips);
        bar_center_rl = findViewById(R.id.bar_center_rl);
        bar_title_tv = findViewById(R.id.bar_title_tv);
        bar_right_rl = findViewById(R.id.bar_right_rl);
        bar_right_menu_tips = findViewById(R.id.bar_right_menu_tips);
        bar_right_menu_tv = findViewById(R.id.bar_right_menu_tv);
        bar_right_menu_iv1 = findViewById(R.id.bar_right_menu_iv1);
        bar_right_menu_iv2 = findViewById(R.id.bar_right_menu_iv2);
    }

    private void applyData() {
        bar_left_rl.setOnClickListener(v -> {
            if (null != mOnBackListener) {
                mOnBackListener.onClick(v);
            } else {
                ((Activity) getContext()).onBackPressed();
            }
        });
        bar_right_menu_tv.setOnClickListener(v -> {
            if (null != mOnMenuClickListener) {
                mOnMenuClickListener.onMenuClick(v, 0);
            }
        });
        bar_right_menu_iv1.setOnClickListener(v -> {
            if (null != mOnMenuClickListener) {
                mOnMenuClickListener.onMenuClick(v, 1);
            }
        });
        bar_right_menu_iv2.setOnClickListener(v -> {
            if (null != mOnMenuClickListener) {
                mOnMenuClickListener.onMenuClick(v, 2);
            }
        });

        setBackTipsEnable(mBackTipsEnable);
        setBackIcon(mBackIcon);
        setTitleContent(mTitleContent);
        setTitleContentDrawableRight(mTitleContentDrawableRight);
        setTitleSize(mTitleSize);
        setTitleColor(mTitleColor);
        setMenuText(mMenuText);
        setMenuTextColor(mMenuTextColor);
        setMenuTextDrawableLeft(mMenuTextDrawableLeft);
        setMenuIcon1(mMenuIcon1);
        setMenuIcon2(mMenuIcon2);
    }

    public LibraryTitleView setOnBackListener(OnClickListener pBackListener) {
        this.mOnBackListener = pBackListener;
        return this;
    }

    public LibraryTitleView setBackTipsEnable(boolean pBackTipsEnable) {
        this.mBackTipsEnable = pBackTipsEnable;
        bar_left_back_tips.setVisibility(pBackTipsEnable ? VISIBLE : GONE);
        return this;
    }

    public LibraryTitleView setBackIcon(int pBackIcon) {
        this.mBackIcon = pBackIcon;
        bar_left_back_iv.setImageResource(pBackIcon);
        return this;
    }

    public LibraryTitleView setTitleContent(String pTitleContent) {
        this.mTitleContent = pTitleContent;
        bar_title_tv.setText(pTitleContent);
        return this;
    }

    public LibraryTitleView setTitleContentDrawableRight(int pTitleContentDrawableRight) {
        this.mTitleContentDrawableRight = pTitleContentDrawableRight;
        if (pTitleContentDrawableRight == 0) {
            bar_title_tv.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getContext().getResources().getDrawable(pTitleContentDrawableRight);
            if (null != drawable) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            bar_title_tv.setCompoundDrawables(null, null, drawable, null);
            bar_title_tv.setCompoundDrawablePadding(dp2px(getContext(), 8));
        }
        return this;
    }

    public LibraryTitleView setTitleColor(int pTitleColor) {
        this.mTitleColor = pTitleColor;
        bar_title_tv.setTextColor(pTitleColor);
        return this;
    }

    public LibraryTitleView setTitleSize(int pTitleSize) {
        this.mTitleSize = pTitleSize;
        bar_title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, pTitleSize);
        return this;
    }

    public LibraryTitleView setMenuText(String pMenuText) {
        this.mMenuText = pMenuText;
        bar_right_menu_tv.setText(pMenuText);
        if (TextUtils.isEmpty(pMenuText)) {
            bar_right_menu_tv.setVisibility(GONE);
        } else {
            bar_right_menu_tv.setVisibility(VISIBLE);
        }
        return this;
    }

    public String getMenuText() {
        return mMenuText;
    }

    public LibraryTitleView setMenuTextColor(int pMenuTextColor) {
        this.mMenuTextColor = pMenuTextColor;
        bar_right_menu_tv.setTextColor(pMenuTextColor);
        return this;
    }

    public LibraryTitleView setMenuTextDrawableLeft(int pMenuTextDrawableLeft) {
        this.mMenuTextDrawableLeft = pMenuTextDrawableLeft;
        if (pMenuTextDrawableLeft == 0) {
            bar_right_menu_tv.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getContext().getResources().getDrawable(pMenuTextDrawableLeft);
            if (null != drawable) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            bar_right_menu_tv.setCompoundDrawables(drawable, null, null, null);
            bar_right_menu_tv.setCompoundDrawablePadding(dp2px(getContext(), 8));
        }
        return this;
    }

    public LibraryTitleView setMenuIcon1(int pMenuIcon1) {
        this.mMenuIcon1 = pMenuIcon1;
        bar_right_menu_iv1.setImageResource(pMenuIcon1);
        if (pMenuIcon1 == 0) {
            bar_right_menu_iv1.setVisibility(GONE);
        } else {
            bar_right_menu_iv1.setVisibility(VISIBLE);
        }
        return this;
    }

    public LibraryTitleView setMenuIcon2(int pMenuIcon2) {
        this.mMenuIcon2 = pMenuIcon2;
        bar_right_menu_iv2.setImageResource(pMenuIcon2);
        if (pMenuIcon2 == 0) {
            bar_right_menu_iv2.setVisibility(GONE);
        } else {
            bar_right_menu_iv2.setVisibility(VISIBLE);
        }
        return this;
    }

    public void showMenuLayout() {
        this.bar_right_rl.setVisibility(VISIBLE);
    }

    public void hideMenuLayout() {
        this.bar_right_rl.setVisibility(INVISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    public void setOnMenuClickListener(OnMenuClickListener pOnMenuClickListener) {
        mOnMenuClickListener = pOnMenuClickListener;
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public interface OnMenuClickListener {
        /**
         * @param v
         * @param type 0-mMenuText, 1-mMenuIcon1, 2-mMenuIcon2
         */
        void onMenuClick(View v, int type);
    }
}
