package com.ljh.custom.base_library.utils;

import android.app.Activity;

import com.google.common.collect.Lists;
import com.ljh.custom.base_library.data_report.ReportDataService;

import java.util.ArrayList;

/**
 * Desc: 简单记录activity栈, 退出APP时遍历finish activity而不杀进程, 下次启动速度可加快
 * Created by ${junhua.li} on 2018/06/26 09:37.
 * Email: lijunhuayc@sina.com
 */
public class LocalActivityManager {
    private static LocalActivityManager manager;
    private ArrayList<Activity> activityList = Lists.newArrayList();
    private static final int LOOKCAR_CAR_SERIES_OPEN_NUM = 3;//限制车系详情页打开的个数
    private static final String LOOKCAR_CAR_SERIES_ACTIVITY_NAME = "com.renxin.cheku.lookcar.activity.CarSeriesDetailsActivity";
    private volatile static int CAR_SERIES_COUNT = 0;

    private LocalActivityManager() {
    }

    public static final LocalActivityManager getInstance() {
        if (null == manager) {
            synchronized (LocalActivityManager.class) {
                if (null == manager) {
                    manager = new LocalActivityManager();
                }
            }
        }
        return manager;
    }

    public void add(Activity pActivity) {
        activityList.add(pActivity);
        restrictCarSeriesPageNum(pActivity);
    }

    private void restrictCarSeriesPageNum(Activity pActivity) {
        if (pActivity.getClass().getName().equals(LOOKCAR_CAR_SERIES_ACTIVITY_NAME)) {
            CAR_SERIES_COUNT++;
            if (CAR_SERIES_COUNT > LOOKCAR_CAR_SERIES_OPEN_NUM) {
                for (Activity activity1 : activityList) {
                    if (activity1.getClass().getName().equals(LOOKCAR_CAR_SERIES_ACTIVITY_NAME)) {
                        activity1.finish();//关闭最先打开的车系详情页
                        CAR_SERIES_COUNT--;
                        break;
                    }
                }
            }
        }
    }

    public void remove(Activity pActivity) {
        activityList.remove(pActivity);
        if (pActivity.getClass().getName().equals(LOOKCAR_CAR_SERIES_ACTIVITY_NAME)) {
            CAR_SERIES_COUNT--;
        }
    }

    public void finish(Activity pActivity) {
        if (!pActivity.isDestroyed()) {
            pActivity.finish();
        }
    }

    public boolean searchActivity(Class<? extends Activity> pClass) {
        for (Activity activity : activityList) {
            if (activity.getClass() == pClass) {
                return true;
            }
        }
        return false;
    }

    public void finishAll() {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            finish(activityList.get(i));
        }
    }

    /**
     * finish 指定之外的
     *
     * @param except
     */
    public void finishAll(Activity except) {
        if (null == except) {
            finishAll();
            return;
        }
        for (int i = activityList.size() - 1; i >= 0; i--) {
            if (except != activityList.get(i)) {
                finish(activityList.get(i));
            }
        }
    }

    /**
     * 退出APP 关闭页面，上报事件
     */
    public void exitApp() {
        ReportDataService.getInstance().reportExitEvent("exit_app");
        finishAll();
        System.exit(0);
    }
}
