package com.example.homlee.activities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.example.homlee.R;
import com.example.homlee.utils.MetricsUtils;

/**
 * Created by homlee on 2018/6/23.
 */

public class ScreenArgsActivity extends BaseActivity {
    private TextView tvScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_args);

        float testDimen = getResources().getDimension(R.dimen.test_dimen);
        int testDimen2 = getResources().getDimensionPixelSize(R.dimen.test_dimen);
        int testDimen3 = getResources().getDimensionPixelOffset(R.dimen.test_dimen);


        DisplayMetrics mMetrics = MetricsUtils.getDisplayMetrics();
        Configuration config = getResources().getConfiguration();

        String text = "density: " + mMetrics.density
                + "\ndensityDpi: " + mMetrics.densityDpi
                + "\nscaledDensity: " + mMetrics.scaledDensity
                + "\nwidthPixels: " + mMetrics.widthPixels
                + "\nheightPixels: " + mMetrics.heightPixels
                + "\nxdpi: " + mMetrics.xdpi
                + "\nydpi: " + mMetrics.ydpi
                + "\nmStatusBarHeight: " + MetricsUtils.getStatusBarHeight()
                + "\nsmallestScreenWidthDp: " + config.smallestScreenWidthDp
                + "\n\ntest_dimen: " + testDimen
                + "\ntest_dimen2: " + testDimen2
                + "\ntest_dimen3: " + testDimen3
                + "\ntest_dimen/density = " + testDimen3+"/"+mMetrics.density +" = "+(testDimen3/mMetrics.density)
                + "\nBuild.MODEL="+ Build.MODEL
                + "\nBuild.VERSION.SDK_INT="+Build.VERSION.SDK_INT;

        Log.i(className, text);
        tvScreen = (TextView) findViewById(R.id.tv_screen_args);
        tvScreen.setText(text);

    }


}
