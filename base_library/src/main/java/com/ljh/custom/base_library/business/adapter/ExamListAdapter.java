package com.ljh.custom.base_library.business.adapter;

import com.ljh.custom.base_library.base.adapter.BaseRecyclerViewAdapter;
import com.ljh.custom.base_library.base.adapter.BaseViewHolder;
import com.ljh.custom.base_library.model.ExamItemModel;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 20:00
 */
public class ExamListAdapter extends BaseRecyclerViewAdapter<ExamItemModel, ExamListAdapter.ExamListViewHolder> {

    @Override
    public ExamListViewHolder createViewHolder(int viewType) {
        return new ExamListViewHolder(this, getOnItemClickListener(), getOnItemLongClickListener());
    }

    public class ExamListViewHolder extends BaseViewHolder<ExamItemModel> {

        private ExamListViewHolder(BaseRecyclerViewAdapter mBaseAdapter, OnItemClickListener mItemClickListener, OnItemLongClickListener mItemLongClickListener) {
            super(mBaseAdapter, mItemClickListener, mItemLongClickListener);
        }

        @Override
        protected void bindItemData(ExamItemModel object, int position) {
            // TODO: 2019/11/17
        }
    }
}
