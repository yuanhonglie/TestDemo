package com.example.homlee;

import android.app.Application;

import com.example.homlee.utils.MetricsUtils;
import com.alibaba.android.arouter.launcher.ARouter;
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
        initLeakCanary();

        MetricsUtils.initialize(this);
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private boolean initLeakCanary() {
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return true;
        }
        RefWatcher watcher = LeakCanary.install(this);
        PresenterRefWatcher.install(watcher);
        return false;
    }


}
