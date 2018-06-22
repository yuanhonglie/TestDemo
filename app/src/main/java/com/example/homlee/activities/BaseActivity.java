package com.example.homlee.activities;

import android.app.Activity;
import android.content.Intent;

public class BaseActivity extends Activity {

    protected void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }
}
