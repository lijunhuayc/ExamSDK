package com.ljh.custom.base_library.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.custom.base_library.data_report.ReportDataService;

/**
 * Desc: [将setUserVisibleHint和onHiddenChanged抽象封装]
 * Desc: 延迟加载数据可使用此抽象类使用 lazyLoad方法延迟加载数据并使用isLazyLoaded标记延迟加载成功
 * Desc: 需要上报 PageInEvent/PageOutEvent 事件的Fragment 可继承此抽象类
 * Created by ${junhua.li} on 2017/06/19 16:33.
 * Email: lijunhuayc@sina.com
 */
public abstract class LazyBaseFragment extends BaseFragment {
    private boolean hasInit;     // onCreatedView完成
    private boolean isVisible;   // Fragment 是否可见
    private boolean isLazyLoaded;   // 数据是否已加载

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        hasInit = true;
        loadData();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            if (hasInit) {
                onVisible();//onCreateView后才回调
            }
            loadData();
        } else {
            isVisible = false;
            if (hasInit) {
                onInvisible();//onCreateView后才回调
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            isVisible = true;
            if (hasInit) {
                onVisible();
            }
            loadData();
        } else {
            isVisible = false;
            if (hasInit) {
                onInvisible();
            }
        }
    }

    private void loadData() {
        if (!hasInit || !isVisible || isLazyLoaded) {
            return;//初始化完成并且可见并且从未加载过数据时才开始加载数据
        }
        Log.d("life_" + TAG, "loadData execute ...");
        lazyLoad();
    }

    public void setLazyLoaded(boolean lazyLoaded) {
        isLazyLoaded = lazyLoaded;
    }

    public boolean isLazyLoaded() {
        return isLazyLoaded;
    }

    //所有初始化完成后才调用以下接口

    /**
     * ViewPager 或者 FragmentManager 切换 Fragment 时触发, 与 onResume 无关
     */
    protected void onVisible() {//可见
//        ReportDataService.getInstance().pageInEvent(this.getClass().getSimpleName(), getPageName());
    }

    protected void onInvisible() {//不可见
//        ReportDataService.getInstance().pageOutEvent(this.getClass().getSimpleName(), getPageName());
    }

    /**
     * UI初始化绑定后并且可见时 加载数据
     */
    protected abstract void lazyLoad();  //延迟加载方法
}
