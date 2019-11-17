package com.ljh.custom.base_library.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.custom.base_library.utils.Timber;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: RecyclerView.Adapter 基类
 * Created by ${junhua.li} on 2016/03/23 10:46.
 * Email: lijunhuayc@sina.com
 */
public abstract class BaseRecyclerViewAdapter<ITEM, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {
    protected static final int ITEM_TYPE_HEADER = -1;
    protected static final int ITEM_TYPE_NORMAL = -2;
    protected static final int ITEM_TYPE_FOOTER = -3;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private List<ITEM> mDatas = new ArrayList<>();
    private View itemLayoutRootView;
    private int pageTotal = 30;   //分页数据一页的数据量, 各列表依据自己接口设置此值
    private boolean headerIsEnable = false;
    private int headerLayoutId;
    protected boolean footerIsEnable = false;
    protected int footerLayoutId;

    public void setHeaderLayoutId(int pHeaderLayoutId) {
        this.headerLayoutId = pHeaderLayoutId;
        this.headerIsEnable = pHeaderLayoutId > 0;
    }

    public int getHeaderLayoutId() {
        return headerLayoutId;
    }

    public boolean isHeaderIsEnable() {
        return headerIsEnable;
    }

    public void setFooterLayoutId(int pFooterLayoutId) {
        this.footerLayoutId = pFooterLayoutId;
        this.footerIsEnable = pFooterLayoutId > 0;
    }

    public int getFooterLayoutId() {
        return footerLayoutId;
    }

    public boolean isFooterIsEnable() {
        return footerIsEnable;
    }

    /**
     * 设置一页数据量
     *
     * @param pageTotal
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public BaseRecyclerViewAdapter(List<ITEM> mDatas, Context mContext) {
        this.mContext = mContext;
        bindDatas(mDatas);
    }

    public Context getContext() {
        return mContext;
    }

    public void bindDatas(List<ITEM> itemList) {
        int count = this.mDatas.size();
        int itemCount = getItemCount();
        this.mDatas.clear();
        if (null != itemList && !itemList.isEmpty()) {
            this.mDatas.addAll(itemList);
            //itemList 非空
            if (count == 0) {
                if (isHeaderIsEnable()) {
                    this.notifyItemRangeInserted(1, this.mDatas.size());
                } else {
                    this.notifyItemRangeInserted(0, this.mDatas.size());
                }
            } else {
                if (isHeaderIsEnable()) {
                    if (count < itemList.size()) {
                        this.notifyItemRangeChanged(1, count);
                        this.notifyItemRangeInserted(1 + count, itemList.size() - count);
                    } else {
                        this.notifyItemRangeChanged(1, itemList.size());
                        this.notifyItemRangeRemoved(1 + itemList.size(), count - itemList.size());
                    }
                } else {
                    if (count < itemList.size()) {
                        this.notifyItemRangeChanged(0, count);
                        this.notifyItemRangeInserted(count, itemList.size() - count);
                    } else {
                        this.notifyItemRangeChanged(0, itemList.size());
                        this.notifyItemRangeRemoved(itemList.size(), count - itemList.size());
                    }
                }
            }
        } else {
            //itemList 空
            if (isHeaderIsEnable() && isFooterIsEnable()) {
                this.notifyItemRangeRemoved(1, itemCount - 2);
            } else if (isHeaderIsEnable()) {
                this.notifyItemRangeRemoved(1, itemCount - 1);
            } else if (isFooterIsEnable()) {
                this.notifyItemRangeRemoved(0, itemCount - 1);
            } else {
                this.notifyItemRangeRemoved(0, itemCount);
            }
        }
    }

    /**
     * 只清空数据，不刷新
     */
    public void clearDatas() {
        int count = this.mDatas.size();
        if (null != this.mDatas) {
            this.mDatas.clear();
        } else {
            this.mDatas = new ArrayList<>();
        }
        if (isHeaderIsEnable()) {
            this.notifyItemRangeRemoved(1, count);
        } else {
            this.notifyItemRangeRemoved(0, count);
        }
    }

    /**
     * 添加数据到结尾
     *
     * @param itemList
     */
    public void appendDatas2End(List<ITEM> itemList) {
        if (null != itemList) {
            int start = this.mDatas.size();
            this.mDatas.addAll(itemList);
            if (headerIsEnable) {
                start += 1; //有 HeaderView 操作位置需要 +1
            }
            this.notifyItemRangeInserted(start, itemList.size());
        } else {
            Timber.d("appendDatas2End: itemList is null.");
        }
    }

    /**
     * 添加数据到结尾
     *
     * @param itemModel
     */
    public void appendModel2End(ITEM itemModel) {
        if (null != itemModel) {
            int start = this.mDatas.size();
            this.mDatas.add(itemModel);
            if (headerIsEnable) {
                start += 1; //有 HeaderView 操作位置需要 +1
            }
            this.notifyItemRangeInserted(start, 1);
        } else {
            Timber.d("appendModel2End: itemModel is null.");
        }
    }

    /**
     * 添加数据到开头
     *
     * @param itemList
     */
    public void appendDatas2Head(List<ITEM> itemList) {
        if (null != itemList) {
            itemList.addAll(this.mDatas);
            bindDatas(itemList);
        }
    }

    public List<ITEM> getDatas() {
        return this.mDatas;
    }

    public void setDatas(List<ITEM> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            itemLayoutRootView = LayoutInflater.from(parent.getContext()).inflate(getHeaderLayoutId(), parent, false);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            itemLayoutRootView = LayoutInflater.from(parent.getContext()).inflate(getFooterLayoutId(), parent, false);
        } else if (viewType == ITEM_TYPE_NORMAL) {
            itemLayoutRootView = LayoutInflater.from(parent.getContext()).inflate(getItemLayout(), parent, false);
        } else {
            itemLayoutRootView = LayoutInflater.from(parent.getContext()).inflate(getItemLayout(viewType), parent, false);
        }
        return createViewHolder(viewType);
    }

    public int getItemLayout() {
        return 0;
    }

    public int getItemLayout(int viewType) {
        return 0;
    }

    public abstract VH createViewHolder(int viewType);

    /**
     * Item是否分组悬浮(通过 ItemDecoration 做分组悬浮时使用,可在 onDraw 中通过此方法判断当前Item是否需要绘制悬浮)
     *
     * @param position
     * @return 默认不需要做分组悬浮
     */
    public boolean isPinnedSuspendPosition(int position) {
        return false;
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        if (null == payloads || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.bindItemPartialData(position, payloads);// TO-DO: 2018/7/13 0013 局部刷新
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_HEADER) {
            holder.bindItemData(null, -1);//Header&Footer上的数据不来源于正常的列表数据源
        } else if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            holder.bindItemData(null, -2);//Header&Footer上的数据不来源于正常的列表数据源
        } else {
            int realPosition = position;
            if (headerIsEnable) {
                realPosition -= 1;//添加了 HeaderView 后 HeaderView为position0, 真正的数据从position1开始，这里对position减1作为下标取数据源(realPosition仅对取数据源有效)
            }
            holder.bindItemData(this.mDatas.get(realPosition), realPosition);
        }
    }

    /**
     * {@link #getItemViewType(int)}
     * {@link #getItemCount()}
     */
    @Override
    public int getItemViewType(int position) {
        if (headerIsEnable && position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (footerIsEnable && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        } else {
            int dataPosition = position;
            if (headerIsEnable) {
                dataPosition -= 1;//添加了 HeaderView 后 HeaderView为position0, 真正的数据从position1开始，这里对position减1作为下标取数据源
            }
            return getItemNormalContentType(dataPosition);//自定义ItemType, ItemLayout, ItemViewHolder
        }
    }

    /**
     * ITEM_NORMAL_CONTENT_TYPE
     *
     * @param dataPosition {@link #getDatas()} 下标
     * @return 自定义内容区ITEM 类型
     */
    public int getItemNormalContentType(int dataPosition) {
        return ITEM_TYPE_NORMAL;
    }

    /**
     * 是否有更多可以被加载
     *
     * @return
     */
    public boolean enableLoadMore() {
        if (this.mDatas.size() >= pageTotal) {
            return true;
        }
        return false;
    }

    public void setData(ITEM item, int position) {
        mDatas.set(position, item);
        notifyItemChanged(position);
    }

    public void deleteData(int position) {
        if (mDatas.size() > position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mDatas.size() - position);
        }
    }

    @Override
    public int getItemCount() {
        int size = this.mDatas.size();
        if (headerIsEnable) {
            size++;
        }
        if (footerIsEnable) {
            size++;
        }
        return size;
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

    protected View getItemLayoutRootView() {
        return itemLayoutRootView;
    }

    public static abstract class OnItemClickListener {
        public abstract void onItemClick(View itemView, int position);

        public void onHeaderItemClick(View itemView) {
        }
    }

    public static abstract class OnItemLongClickListener {
        public abstract boolean onItemLongClick(View itemView, int position);

        public void onHeaderItemLongClick(View itemView) {
        }
    }
}
