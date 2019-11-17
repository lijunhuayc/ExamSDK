package com.ljh.custom.base_library.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljh.custom.base_library.receiver.NetWorkStateReceiver;
import com.ljh.custom.base_library.utils.MyToast;

/**
 * Desc:
 * Created by ${junhua.li} on 2017/06/15 16:21.
 * Email: lijunhuayc@sina.com
 */
public class BaseTopActivity extends AppCompatActivity implements NetWorkStateReceiver.NetChangeObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetWorkStateReceiver.registerNetStateObserver(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public boolean isNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    @Override
    protected void onDestroy() {
        NetWorkStateReceiver.unRegisterNetStateObserver(this);
        super.onDestroy();
    }

    @Override
    public void onConnect(NetWorkStateReceiver.NetType netType) {
        switch (netType) {
            case TYPE_WIFI:
                MyToast.showToast("WIFI 已连接");
                break;
            default:
                MyToast.showToast("移动网络 已连接");
                break;
        }
    }

    @Override
    public void onDisConnect() {
        MyToast.showToast("网络断开");
    }
}
