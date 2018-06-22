package com.example.homlee;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by homlee on 2018/6/21.
 */

public class LeakCanaryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }


}
