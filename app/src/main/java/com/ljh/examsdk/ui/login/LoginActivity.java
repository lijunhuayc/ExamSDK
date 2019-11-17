package com.ljh.examsdk.ui.login;

import android.Manifest;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.ljh.custom.base_library.ExamSDK;
import com.ljh.custom.base_library.base.activity.BaseActivity;
import com.ljh.custom.base_library.base.activity.permission.PermissionCallback;
import com.ljh.custom.base_library.business.activity.ExamActivity;
import com.ljh.custom.base_library.business.activity.ExamListActivity;
import com.ljh.custom.base_library.data_source.net.retrofit.RetrofitUtils;
import com.ljh.custom.base_library.model.BaseResult;
import com.ljh.custom.base_library.model.UserModel;
import com.ljh.custom.base_library.utils.MyToast;
import com.ljh.custom.base_library.utils.Timber;
import com.ljh.examsdk.R;
import com.ljh.examsdk.net.UserAPIService;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.loading)
    ProgressBar mLoading;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUI() {
    }

    @Override
    protected void onCreated() {
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
        requestPermissions(permissions, "", new PermissionCallback() {
            @Override
            public void hasPermission() {
                login();
            }

            @Override
            public void noPermission(boolean shouldShowRequestPermissionRationale) {
                MyToast.showToast("请在设置页面打开相机和SD卡权限");
            }
        });
    }

    private void login() {
        RetrofitUtils.getInstance().request(UserAPIService.class, new RetrofitUtils.RetrofitCallback<UserAPIService, UserModel>() {
            @Override
            public void onSuccess(UserModel model) {
                Timber.d(new Gson().toJson(model));
                ExamSDK.init(model);

                //
                startActivity(new Intent(LoginActivity.this, ExamListActivity.class));
            }

            @Override
            public void onError(int pStatus, String pMessage) {
                // TODO: 2019/11/17
            }

            @Override
            public Call<BaseResult<UserModel>> getAPI(UserAPIService pT) {
                return pT.login("15184459063", "123456");
            }

            @Override
            public void onFinish() {
                // TODO: 2019/11/17
            }
        });
    }
}
