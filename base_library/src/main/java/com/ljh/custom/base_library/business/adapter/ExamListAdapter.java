package com.ljh.custom.base_library.business.adapter;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.base.adapter.BaseRecyclerViewAdapter;
import com.ljh.custom.base_library.base.adapter.BaseViewHolder;
import com.ljh.custom.base_library.model.ExamItemModel;
import com.ljh.custom.base_library.utils.FrescoUtils;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 20:00
 */
public class ExamListAdapter extends BaseRecyclerViewAdapter<ExamItemModel, ExamListAdapter.ExamListViewHolder> {
    @Override
    public int getItemLayout() {
        return R.layout.library_item_exam_item_view;
    }

    @Override
    public ExamListViewHolder createViewHolder(int viewType) {
        return new ExamListViewHolder(this, getOnItemClickListener(), getOnItemLongClickListener());
    }

    public class ExamListViewHolder extends BaseViewHolder<ExamItemModel> {
        SimpleDraweeView mSimpleDraweeView;
        TextView nameTv;

        private ExamListViewHolder(BaseRecyclerViewAdapter mBaseAdapter, OnItemClickListener mItemClickListener, OnItemLongClickListener mItemLongClickListener) {
            super(mBaseAdapter, mItemClickListener, mItemLongClickListener);
        }

        @Override
        protected void findView(View itemLayoutRootView) {
            super.findView(itemLayoutRootView);
            mSimpleDraweeView = getView(R.id.simpleDraweeView);
            nameTv = getView(R.id.nameTv);
        }

        @Override
        protected void bindItemData(ExamItemModel object, int position) {
            FrescoUtils.setDraweeViewUri(mSimpleDraweeView, object.getBackgroundUrl());
            nameTv.setText(object.getTitle());
        }
    }
}
