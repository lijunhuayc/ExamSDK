package com.ljh.custom.base_library.business;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ljh.custom.base_library.ExamSDK;
import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.base.adapter.BaseRecyclerViewAdapter;
import com.ljh.custom.base_library.base.fragment.LazyBaseFragment;
import com.ljh.custom.base_library.business.adapter.ExamListAdapter;
import com.ljh.custom.base_library.data_source.net.ExamAPIService;
import com.ljh.custom.base_library.data_source.net.retrofit.RetrofitUtils;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.ExamItemModel;
import com.ljh.custom.base_library.utils.MyToast;
import com.ljh.custom.base_library.utils.Timber;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import retrofit2.Call;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2019/11/17 19:55
 */
public class ExamListFragment extends LazyBaseFragment {
    SmartRefreshLayout smartRefreshLayout;
    RecyclerView mRecyclerView;
    BaseRecyclerViewAdapter mAdapter;
    int mCurrentPage = 1;
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
                ExamItemModel model = (ExamItemModel) mAdapter.getDatas().get(position);
                if (model.isShop()) {

                }

                String id = model.getId();
                // TODO: 2019/11/17  检查考试权限等等
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        smartRefreshLayout = getView(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getExamListData(++mCurrentPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getExamListData(1);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        getExamListData(1);
    }

    private void getExamListData(int currentPage) {
        showProgressView();
        RetrofitUtils.getInstance().request(ExamAPIService.class, new RetrofitUtils.RetrofitCallback<ExamAPIService, List<ExamItemModel>>() {
            @Override
            public void onSuccess(List<ExamItemModel> model) {
                Timber.d("model = %s", model);
                // TODO: 2019/11/17 接口不通 & 选择企业的数据在哪里？ & 暂停


                mCurrentPage = currentPage;
            }

            @Override
            public void onError(int pStatus, String pMessage) {
                Timber.d("pStatus = %s, pMessage = %s", pStatus, pMessage);
            }

            @Override
            public Call<BaseResult<List<ExamItemModel>>> getAPI(ExamAPIService pT) {
                return pT.findExamList(currentPage, 30, ExamSDK.getUserModel().getId());
            }

            @Override
            public void onFinish() {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                hideProgressView();
            }
        });
    }
}
