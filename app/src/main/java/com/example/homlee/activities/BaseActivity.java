package com.example.homlee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class BaseActivity extends Activity {
    protected String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
    }

    protected void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }
}
