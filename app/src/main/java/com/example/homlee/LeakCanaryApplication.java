package com.example.homlee;

import android.app.Application;

import com.example.homlee.utils.MetricsUtils;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by homlee on 2018/6/21.
 */

public class LeakCanaryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MetricsUtils.initialize(this);
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }


}
