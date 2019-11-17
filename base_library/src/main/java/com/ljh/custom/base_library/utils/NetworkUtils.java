package com.ljh.custom.base_library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import io.reactivex.annotations.Nullable;

/**
 * Desc: 网络验证工具类
 * Created by ${junhua.li} on 2017/06/14 17:12.
 * Email: lijunhuayc@sina.com
 */
public class NetworkUtils {
    /*
     * 检测网络是否连接
     * */
    public static boolean isNetworkAvailable(@Nullable Context mContext) {
        if (null == mContext) {
            Timber.d("NetworkUtils.isNetworkAvailable: mContext is null");
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWIFI(Context mContext) {
        NetworkInfo info = getNetworkInfo(mContext);
        return null != info && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile(Context mContext) {
        NetworkInfo info = getNetworkInfo(mContext);
        return null != info && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    private static NetworkInfo getNetworkInfo(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    @Deprecated
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Timber.d("get native ip address: exception = %s", ex.toString());
            return null;
        }
        return null;
    }

    @Deprecated
    private String getLocalIPAddress() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return "null";
    }
}
