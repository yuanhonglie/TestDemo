package com.example.homlee;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.homlee.activities.BaseActivity;
import com.example.homlee.activities.GuanMingActivity;
import com.example.homlee.activities.OpenGLActivity;
import com.example.homlee.activities.RxJavaActivity;
import com.example.homlee.activities.UIActivity;
import com.example.homlee.activities.WebViewActivity;
import com.homlee.rxbus2.MessageManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_open_gl).setOnClickListener(this);
        findViewById(R.id.btn_ui_effect).setOnClickListener(this);
        findViewById(R.id.btn_rx_java).setOnClickListener(this);
        findViewById(R.id.btn_webview).setOnClickListener(this);
        findViewById(R.id.btn_guanming).setOnClickListener(this);
        initData();
    }

    private void initData() {
        MessageManager.getInstance().sendMessage(0, "hello, world!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_gl:
                startActivity(OpenGLActivity.class);
                break;
            case R.id.btn_ui_effect:
                startActivity(UIActivity.class);
                break;
            case R.id.btn_rx_java:
                startActivity(RxJavaActivity.class);
                break;
            case R.id.btn_webview:
                startActivity(WebViewActivity.class);
                break;
            case R.id.btn_guanming:
                startActivity(GuanMingActivity.class);
            default:
                break;
        }
    }
}
