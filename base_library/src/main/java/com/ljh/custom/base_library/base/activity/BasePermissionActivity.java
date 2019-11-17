package com.ljh.custom.base_library.base.activity;

import android.content.Intent;
import android.os.Looper;
import android.provider.Settings;

import com.ljh.custom.base_library.base.activity.permission.PermissionCallback;
import com.ljh.custom.base_library.domain.MainThreadScheduler;
import com.ljh.custom.base_library.utils.Timber;
import com.ljh.custom.cooldialog.CoolDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

/**
 * Desc:
 * Created by Junhua.Li
 * Date: 2018/06/15 17:03
 */
public class BasePermissionActivity extends BaseTopActivity /*implements PermissionImpl*/ {
    private int permissionRequestCode = 2098;
    private final static int REQUEST_CODE_OPEN_SETTINGS = 9897;
    private PermissionCallback mPermissionCallback;
    private String[] mCurrentPermissions;
    private String mCurrentDesc;
    private RxPermissions mRxPermissions = new RxPermissions(this);

    public void requestPermissions(String pPermission, String pDesc, PermissionCallback pPermissionCallback) {
        requestPermissions(new String[]{pPermission}, pDesc, pPermissionCallback);
    }

    /**
     * 统一封装权限请求方法
     *
     * @param pPermissions
     * @param pDesc
     * @param pPermissionCallback
     */
    public void requestPermissions(String[] pPermissions, String pDesc, PermissionCallback pPermissionCallback) {
        if (null == pPermissions || pPermissions.length == 0) {
            return;
        }
        this.mPermissionCallback = pPermissionCallback;
        this.mCurrentPermissions = pPermissions;
        this.mCurrentDesc = pDesc;
        Disposable subscribe = mRxPermissions
                .requestEachCombined(pPermissions)
                .subscribe(pPermission -> {
                    Timber.d("RxPermissions.requestEach: %s, %s, %s", pPermission.name, pPermission.granted, pPermission.shouldShowRequestPermissionRationale);
                    if (null != mPermissionCallback) {
                        if (pPermission.granted) {
                            mPermissionCallback.hasPermission();
                        } else if (pPermission.shouldShowRequestPermissionRationale) {
                            mPermissionCallback.noPermission(true);
                        } else {
                            //勾选了不再询问并拒绝后Dialog提示
                            CoolDialog.Builder builder = new CoolDialog.Builder(this)
                                    .setTitle("提示")
                                    .setMessage(pDesc)
                                    .setCancelable(false)
                                    .setNegativeButton("取消", (dialog1, which) -> {
                                        if (mPermissionCallback != null) {
                                            mPermissionCallback.noPermission(false);
                                            mPermissionCallback = null;
                                        }
                                    })
                                    .setPositiveButton("去设置", (dialog2, which) -> startAppSettings(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                builder.show();
                            } else {
                                MainThreadScheduler.getInstance().post(builder::show);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_SETTINGS) {
            requestPermissions(mCurrentPermissions, mCurrentDesc, mPermissionCallback);
        }
    }

//    /**
//     * Android M运行时权限请求封装
//     *
//     * @param permissionDes 权限描述
//     * @param runnable      请求权限回调
//     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
//     */
//    @Override
//    public void checkPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions) {
//        if (permissions.length == 0) return;
//        this.mPermissionCallback = runnable;
//        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)) {
//            if (mPermissionCallback != null) {
//                mPermissionCallback.hasPermission();
//                mPermissionCallback = null;
//            }
//        } else {
//            requestPermission(permissionDes, permissionRequestCode, permissions);
//        }
//    }
//
//    @Override
//    public boolean checkPermissionGranted(String[] permissions) {
//        boolean flag = true;
//        for (String p : permissions) {
//            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
//                flag = false;
//                break;
//            }
//        }
//        return flag;
//    }
//
//    @Override
//    public void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
//        if (shouldShowRequestPermissionRationale(permissions)) {
//            CoolDialog.Builder builder = new CoolDialog.Builder(this)
//                    .setTitle("提示")
//                    .setMessage(permissionDes)
//                    .setNegativeButton("取消", (dialog1, which) -> {
//                        if (mPermissionCallback != null) {
//                            mPermissionCallback.noPermission(true);
//                            mPermissionCallback = null;
//                        }
//                    })
//                    .setPositiveButton("确定", (dialog2, which)
//                            -> ActivityCompat.requestPermissions(this, permissions, requestCode));
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                builder.show();
//            } else {
//                MainThreadScheduler.getInstance().post(builder::show);
//            }
//        } else {
//            ActivityCompat.requestPermissions(this, permissions, requestCode);
//        }
//    }
//
//    @Override
//    public boolean shouldShowRequestPermissionRationale(String[] permissions) {
//        boolean flag = false;
//        for (String p : permissions) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
//                flag = true;
//                break;
//            }
//        }
//        return flag;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == permissionRequestCode) {
//            if (verifyPermissions(grantResults)) {
//                if (mPermissionCallback != null) {
//                    mPermissionCallback.hasPermission();
//                    mPermissionCallback = null;
//                }
//            } else {
//                if (mPermissionCallback != null) {
//                    mPermissionCallback.noPermission(false);
//                    mPermissionCallback = null;
//                }
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    @Override
//    public boolean verifyPermissions(int[] grantResults) {
//        if (grantResults.length < 1) {
//            return false;
//        }
//
//        for (int result : grantResults) {
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }

    protected void startAppSettings(String pAction) {
        Intent intent = new Intent(pAction);
//        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_OPEN_SETTINGS);
    }
}
