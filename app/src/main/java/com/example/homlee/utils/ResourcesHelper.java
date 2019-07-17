package com.example.homlee.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by homlee on 2018/6/12.
 */

public class ResourcesHelper {
    private static final String TAG = "ResourcesHelper";
    private ResourcesHelper(){}

    public static String readText(Context context, int resId) {
        StringBuilder builder = new StringBuilder();
        InputStream in = null;
        try {
            in = context.getResources().openRawResource(resId);
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                builder.append(line);
                builder.append('\n');
            }
        } catch (IOException e) {
            Log.i(TAG, "readText: error1 = " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "readText: error2 = " + e.getMessage());
            }

        }

        return builder.toString();
    }
}
