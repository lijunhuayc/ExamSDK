package com.ljh.custom.base_library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ljh.custom.base_library.utils.Timber;

import java.util.ArrayList;

/**
 * Desc: 全局网络监听器
 * Created by ${junhua.li} on 2016/05/11 15:34.
 * Email: lijunhuayc@sina.com
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private static final String TAG = NetWorkStateReceiver.class.getSimpleName();
    private static final String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;

    /**
     * 储存所有的网络状态观察者集合
     */
    private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList<>();
    private static boolean networkAvailable = true;
    private NetType netType;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("NetWorkStateReceiver: action=%s", intent.getAction());
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNIF = connectivityManager.getActiveNetworkInfo();
        if (null != activeNIF && activeNIF.isConnectedOrConnecting()) {
            networkAvailable = true;
            switch (activeNIF.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    Timber.d("NetWorkStateReceiver: 您当前正在使用移动网络");
                    netType = NetType.TYPE_MOBILE;
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    Timber.d("NetWorkStateReceiver: 您当前正在使用WIFI网络");
                    netType = NetType.TYPE_WIFI;
                    break;
            }
        } else {
            Timber.d("NetWorkStateReceiver: 网络断开");
            networkAvailable = false;
        }
        notifyObserver();
    }

    /**
     * 添加/注册网络连接状态观察者
     *
     * @param observer
     */
    public static void registerNetStateObserver(NetChangeObserver observer) {
        if (netChangeObserverArrayList == null) {
            netChangeObserverArrayList = new ArrayList<>();
        }
        netChangeObserverArrayList.add(observer);
    }

    /**
     * 删除/注销网络连接状态观察者
     *
     * @param observer
     */
    public static void unRegisterNetStateObserver(NetChangeObserver observer) {
        if (netChangeObserverArrayList != null) {
            netChangeObserverArrayList.remove(observer);
        }
    }

    /**
     * 向所有的观察者发送通知：网络状态发生改变咯...
     */
    private void notifyObserver() {
        if (netChangeObserverArrayList != null && netChangeObserverArrayList.size() > 0) {
            Timber.d("NetWorkStateReceiver: 当前有 %s 个网络观察者", netChangeObserverArrayList.size());
            for (NetChangeObserver observer : netChangeObserverArrayList) {
                if (observer != null) {
                    if (networkAvailable) {
                        observer.onConnect(netType);
                    } else {
                        observer.onDisConnect();
                    }
                }
            }
        }
    }

    public interface NetChangeObserver {
        void onConnect(NetType netType);
        void onDisConnect();
    }

    public enum NetType {
        TYPE_WIFI, TYPE_MOBILE;
    }

}
