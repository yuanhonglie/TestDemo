package com.example.homlee;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.homlee.activities.AirHockeyActivity;
import com.example.homlee.activities.BaseActivity;
import com.example.homlee.activities.SimpleOpenGLActivity;
import com.example.homlee.activities.UIActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_open_gl).setOnClickListener(this);
        findViewById(R.id.btn_air_hockey).setOnClickListener(this);
        findViewById(R.id.btn_ui_effect).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_gl:
                startActivity(SimpleOpenGLActivity.class);
                break;
            case R.id.btn_air_hockey:
                startActivity(AirHockeyActivity.class);
                break;
            case R.id.btn_ui_effect:
                startActivity(UIActivity.class);
                break;
            default:
                break;
        }
    }
}
