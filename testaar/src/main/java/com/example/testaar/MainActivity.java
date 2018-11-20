package com.example.testaar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.homlee.rxbus2.MessageManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MessageManager.getInstance().sendMessage(0, "hello, " + getString(R.string.app_name));
    }
}
