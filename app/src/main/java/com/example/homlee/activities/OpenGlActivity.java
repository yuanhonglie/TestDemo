package com.example.homlee.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.example.homlee.R;

/**
 * Created by homlee on 2018/6/23.
 */

public class OpenGlActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl);
        findViewById(R.id.btn_simple_opengl).setOnClickListener(this);
        findViewById(R.id.btn_air_hockey).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_simple_opengl:
                startActivity(SimpleOpenGlActivity.class);
                break;
            case R.id.btn_air_hockey:
                startActivity(AirHockeyActivity.class);
                break;
        }
    }
}
