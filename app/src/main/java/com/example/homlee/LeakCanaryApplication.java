package com.example.homlee;

import android.app.Application;

import com.example.homlee.Utils.Metrics;
import com.homlee.mvp.leakcanary.PresenterRefWatcher;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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
        RefWatcher watcher = LeakCanary.install(this);
        PresenterRefWatcher.install(watcher);
        Metrics.initialize(this);
    }


}
