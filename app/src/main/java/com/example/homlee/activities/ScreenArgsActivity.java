package com.example.homlee.activities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.example.homlee.R;
import com.example.homlee.Utils.Metrics;

/**
 * Created by homlee on 2018/6/23.
 */

public class ScreenArgsActivity extends BaseActivity {
    private TextView tvScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_args);

        float test_dimen = getResources().getDimension(R.dimen.test_dimen);
        int test_dimen2 = getResources().getDimensionPixelSize(R.dimen.test_dimen);
        int test_dimen3 = getResources().getDimensionPixelOffset(R.dimen.test_dimen);


        DisplayMetrics mMetrics = Metrics.getDisplayMetrics();
        Configuration config = getResources().getConfiguration();

        String text = "density: " + mMetrics.density
                + "\ndensityDpi: " + mMetrics.densityDpi
                + "\nscaledDensity: " + mMetrics.scaledDensity
                + "\nwidthPixels: "  + mMetrics.widthPixels
                + "\nheightPixels: " + mMetrics.heightPixels
                + "\nxdpi: " + mMetrics.xdpi
                + "\nydpi: " + mMetrics.ydpi
                + "\nmStatusBarHeight: " + Metrics.getStatusBarHeight()
                + "\nsmallestScreenWidthDp: " + config.smallestScreenWidthDp
                + "\n\ntest_dimen: " + test_dimen
                + "\ntest_dimen2: " + test_dimen2
                + "\ntest_dimen3: " + test_dimen3
                + "\ntest_dimen/density = " + test_dimen3+"/"+mMetrics.density +" = "+(test_dimen3/mMetrics.density)
                + "\nBuild.MODEL="+ Build.MODEL
                + "\nBuild.VERSION.SDK_INT="+Build.VERSION.SDK_INT;

        Log.i(TAG, text);
        tvScreen = (TextView) findViewById(R.id.tv_screen_args);
        tvScreen.setText(text);

    }


}
