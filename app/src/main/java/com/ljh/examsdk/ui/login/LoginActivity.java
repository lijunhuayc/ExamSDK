package com.ljh.examsdk.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ljh.custom.base_library.ExamSDK;
import com.ljh.custom.base_library.business.activity.ExamActivity;
import com.ljh.examsdk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.loading)
    ProgressBar mLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
//        if ("18382375012".equals(mUsername.getText().toString()) && "123456".equals(mPassword.getText().toString())) {
//            startActivity(new Intent(this, ExamActivity.class));
//        }
        startActivity(new Intent(this, ExamActivity.class));

        // TODO: 2019/11/17
        ExamSDK.init("");
    }
}
