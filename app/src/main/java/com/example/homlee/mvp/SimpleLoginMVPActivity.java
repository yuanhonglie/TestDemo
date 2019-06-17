package com.example.homlee.mvp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.homlee.R;
import com.example.homlee.activities.BaseActivity;
import com.example.homlee.mvp.model.User;

/**
 * @author leo.yuan
 */
public class SimpleLoginMVPActivity extends BaseActivity implements View.OnClickListener, SimpleLoginContract.View {
    private SimpleLoginContract.Presenter mPresenter;
    private TextView mTextView;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        initData();
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initData() {
        mPresenter = new SimpleLoginPresenter(this);
    }

    private void initView() {
        mUsername = findViewById(R.id.et_name);
        mPassword = findViewById(R.id.et_pwd);
        mTextView = findViewById(R.id.tv_status);
        findViewById(R.id.btn_fetch).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fetch:
                String name = mUsername.getText().toString();
                String pwd = mPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "用户名和密码不能为空！！！", Toast.LENGTH_SHORT);
                } else {
                    mPresenter.login(name, pwd);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        mTextView.setText("登陆中，请稍等。。。");
    }

    @Override
    public void hideLoading() {
        mTextView.setText("");
    }

    @Override
    public void showError(String message) {
        mTextView.setText("出错了！" + message);
    }

    @Override
    public void showSuccess() {
        mTextView.setText("登陆成功！");
    }
}
