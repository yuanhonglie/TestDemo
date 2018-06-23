package com.example.homlee.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.homlee.R;


public class UIActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_demo);
        findViewById(R.id.btn_editor).setOnClickListener(this);
        findViewById(R.id.btn_screen_args).setOnClickListener(this);
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
            default:
                break;
        }
    }
}
