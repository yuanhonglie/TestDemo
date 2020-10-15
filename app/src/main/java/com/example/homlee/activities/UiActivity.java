package com.example.homlee.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.example.homlee.R;


public class UiActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_demo);
        findViewById(R.id.btn_editor).setOnClickListener(this);
        findViewById(R.id.btn_screen_args).setOnClickListener(this);
        findViewById(R.id.btn_shadow_text).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_screen_args:
                startActivity(ScreenArgsActivity.class);
                break;
            case R.id.btn_editor:
                startActivity(EditorEffectActivity.class);
                break;
            case R.id.btn_shadow_text:
                startActivity(ImageTextViewActivity.class);
                break;
            default:
                break;
        }
    }
}
