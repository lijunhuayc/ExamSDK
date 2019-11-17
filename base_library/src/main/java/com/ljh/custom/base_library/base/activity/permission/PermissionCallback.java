package com.ljh.custom.base_library.base.activity.permission;

/**
 * Desc:权限检查回调
 * Created by Junhua.Li
 * Date: 2018/06/19 09:54
 */
public interface PermissionCallback {
    void hasPermission();
    void noPermission(boolean shouldShowRequestPermissionRationale);
}
