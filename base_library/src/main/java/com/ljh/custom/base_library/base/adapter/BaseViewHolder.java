package com.ljh.custom.base_library.base.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

//import butterknife.ButterKnife;

/**
 * Desc: RecyclerView.ViewHolder 基类
 * Created by ${junhua.li} on 2016/03/23 10:51.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseViewHolder<ITEM> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private BaseRecyclerViewAdapter mBaseAdapter;
    private BaseRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;
    private BaseRecyclerViewAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private View clickView; //需要点击事件的View
    protected Context mContext;

    public BaseViewHolder(BaseRecyclerViewAdapter mBaseAdapter) {
        super(mBaseAdapter.getItemLayoutRootView());
        this.mBaseAdapter = mBaseAdapter;
        this.mContext = mBaseAdapter.getContext();
        bindView(itemView);
        findView(itemView);
    }

    public BaseViewHolder(BaseRecyclerViewAdapter mBaseAdapter, BaseRecyclerViewAdapter.OnItemClickListener mItemClickListener, BaseRecyclerViewAdapter.OnItemLongClickListener mItemLongClickListener) {
        this(mBaseAdapter);
        this.mOnItemClickListener = mItemClickListener;
        this.mOnItemLongClickListener = mItemLongClickListener;
        addClickEvent();
    }

    public BaseViewHolder(BaseRecyclerViewAdapter mBaseAdapter, BaseRecyclerViewAdapter.OnItemClickListener mItemClickListener) {
        this(mBaseAdapter, mItemClickListener, null);
    }

    /**
     * be call after this method {@link #bindView(View) and {@link #findView(View)}}
     * reset target clickEventView
     *
     * @param eventView target
     * @see #bindView(View)
     * @see #findView(View)
     */
    protected void setClickEventView(View eventView) {
        this.clickView = eventView;
        addClickEvent();
    }

    private void addClickEvent() {
        if (null != clickView) {
            clickView.setOnClickListener(this);
            clickView.setOnLongClickListener(this);
            itemView.setOnClickListener(null);
        } else {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
    }

    void setBaseAdapter(BaseRecyclerViewAdapter baseAdapter) {
        this.mBaseAdapter = baseAdapter;
    }

    protected BaseRecyclerViewAdapter getBaseAdapter() {
        return mBaseAdapter;
    }

    public Context getContext() {
        return mContext;
    }

    public int getColor(@ColorRes int id) {
        return mContext.getResources().getColor(id);
    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
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

    protected <T extends View> T getView(@IdRes int id) {
        return itemView.findViewById(id);
    }

    /**
     * init item child's view
     */
    protected void findView(View itemLayoutRootView) {
    }

    /**
     * if the item is header or footer, this object is null.
     *
     * @param object   ""
     * @param position 针对数据源的下标, 有 HeaderView 时此position 会比 Adapter 中的position 值小1
     *                 {@link BaseRecyclerViewAdapter#onBindViewHolder}
     */
    protected abstract void bindItemData(final ITEM object, int position);

    /**
     * 局部刷新
     *
     * @param position
     * @param payloads
     */
    protected void bindItemPartialData(int position, List payloads) {
    }

    /**
     * If you use the third-party libraries for example 'ButterKnife', you must init the third-party libraries here.
     * if not, you need to init item child's view at 'findView' {@link #findView(View)}.
     * 使用ButterKnife等第三方View绑定框架时实现此方法并调用绑定的方法。 如果不使用，则在findView中初始化Item Views。
     *
     * @param itemLayoutRootView ""
     */
    protected void bindView(View itemLayoutRootView) {
//        ButterKnife.bind(this, itemLayoutRootView);//这是示例。为了解耦，需要自己引入ButterKnife并实现此方法
    }

    @Override
    public void onClick(View view) {
        if (null != mOnItemClickListener) {
            int position = getLayoutPosition();
            if (null != mBaseAdapter && mBaseAdapter.isHeaderIsEnable()) {
                position -= 1;
                if (position == -1) {
                    mOnItemClickListener.onHeaderItemClick(view);
                    return;
                }
            }
            mOnItemClickListener.onItemClick(view, position);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mOnItemLongClickListener) {
            int position = getLayoutPosition();
            if (null != mBaseAdapter && mBaseAdapter.isHeaderIsEnable()) {
                position -= 1;
                if (position == -1) {
                    mOnItemLongClickListener.onHeaderItemLongClick(view);
                    return true;
                }
            }
            mOnItemLongClickListener.onItemLongClick(view, position);
            return true;
        }
        return false;
    }

    protected int getItemCount() {
        if (null != mBaseAdapter) {
            return mBaseAdapter.getDatas().size();
        } else {
            return 0;
        }
    }
}
