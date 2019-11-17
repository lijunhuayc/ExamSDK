package com.ljh.custom.base_library.business;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.base.adapter.BaseRecyclerViewAdapter;
import com.ljh.custom.base_library.base.fragment.LazyBaseFragment;
import com.ljh.custom.base_library.business.adapter.ExamListAdapter;
import com.ljh.custom.base_library.utils.MyToast;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 19:55
 */
public class ExamListFragment extends LazyBaseFragment {
    RecyclerView mRecyclerView;
    BaseRecyclerViewAdapter mAdapter;
    int type;//1 在线考试, 2 模拟考试

    @Override
    protected int initLayout() {
        return R.layout.library_fragment_exam_list;
    }

    @Override
    protected void init() {
        if (null != getArguments()) {
            type = getArguments().getInt("exam_type");
        } else {
            MyToast.showToast("参数异常");
            mActivity.finish();
        }
        mRecyclerView = getView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new ExamListAdapter();
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // TODO: 2019/11/17  检查考试权限等等
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {

    }

    private void getExamListData() {

    }
}
