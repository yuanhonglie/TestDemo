package com.example.homlee;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.homlee.activities.BaseActivity;
import com.example.homlee.activities.RecyclerViewActivity;
import com.example.homlee.activities.UiAdaptationActivity;
import com.example.homlee.activities.GuanMingActivity;
import com.example.homlee.activities.OpenGlActivity;
import com.example.homlee.activities.RxJavaActivity;
import com.example.homlee.activities.UiActivity;
import com.example.homlee.activities.WanYueShanActivity;
import com.example.homlee.activities.WebViewActivity;
import com.example.homlee.mvp.SimpleLoginMvpActivity;
import com.homlee.rxbus2.MessageManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private WindowManager mWindowManager;
    private View mFloatView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_open_gl).setOnClickListener(this);
        findViewById(R.id.btn_ui_effect).setOnClickListener(this);
        findViewById(R.id.btn_rx_java).setOnClickListener(this);
        findViewById(R.id.btn_webview).setOnClickListener(this);
        findViewById(R.id.btn_guanming).setOnClickListener(this);
        findViewById(R.id.btn_wanyueshan).setOnClickListener(this);
        findViewById(R.id.btn_floatview).setOnClickListener(this);
        findViewById(R.id.btn_mvp).setOnClickListener(this);
        findViewById(R.id.btn_mvp_viewmodel).setOnClickListener(this);
        findViewById(R.id.btn_mvp_livedata).setOnClickListener(this);
        findViewById(R.id.btn_ui_adaptation).setOnClickListener(this);
        findViewById(R.id.btn_matrix_demo).setOnClickListener(this);
        findViewById(R.id.btn_recyclerview_demo).setOnClickListener(this);

        initData();
        Log.i(className, "onCreate: isTest = " + BuildConfig.isTest);
    }

    private void initData() {
        MessageManager.getInstance().sendMessage(0, "hello, world!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_gl:
                startActivity(OpenGlActivity.class);
                break;
            case R.id.btn_ui_effect:
                startActivity(UiActivity.class);
                break;
            case R.id.btn_rx_java:
                startActivity(RxJavaActivity.class);
                break;
            case R.id.btn_webview:
                startActivity(WebViewActivity.class);
                break;
            case R.id.btn_guanming:
                startActivity(GuanMingActivity.class);
                break;
            case R.id.btn_wanyueshan:
                startActivity(WanYueShanActivity.class);
                break;
            case R.id.btn_floatview:
                requestFloatViewPermission();
                break;
            case R.id.btn_mvp:
                startActivity(SimpleLoginMvpActivity.class);
                break;
            case R.id.btn_mvp_viewmodel:
                break;
            case R.id.btn_mvp_livedata:
                break;
            case R.id.btn_ui_adaptation://zhushi
                startActivity(UiAdaptationActivity.class);
                break;
            case R.id.btn_matrix_demo:
                //startActivity(MatrixActivity.class);
                startExternalActivity();
                break;
            case R.id.btn_recyclerview_demo:
                startActivity(RecyclerViewActivity.class);
                break;
            default:
                break;
        }
    }

    private void startExternalActivity() {
        Intent intent = new Intent();
        //intent.setClassName("com.google.android.gms", "com.google.android.gms.app.settings.GoogleSettingsLink");
        intent.setClassName("com.google.android.gms", "com.google.android.gms.app.settings.GoogleSettingsActivity");
//        intent.setClassName("com.royole.rydrawing", "com.royole.rydrawing.SplashActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void requestFloatViewPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                initFloatViewIfNeed();
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

        }else {
            //SDK在23以下，不用管.
            initFloatViewIfNeed();
        }
    }

    private void initFloatViewIfNeed() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mFloatView = LayoutInflater.from(this).inflate(R.layout.float_view_layout, null);
            mWindowManager.addView(mFloatView, getLayoutParams());
        }
    }

    private WindowManager.LayoutParams getLayoutParams() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;

        return params;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null) {
            mWindowManager.removeView(mFloatView);
        }
    }
}
