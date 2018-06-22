package com.example.homlee.Utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by homlee on 2018/6/12.
 */

public class ResourcesReader {
    private ResourcesReader(){}

    public static String readText(Context context, int resId) {
        StringBuilder builder = new StringBuilder();
        InputStream in = null;
        try {
            in = context.getResources().openRawResource(resId);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine())!= null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return builder.toString();
    }
}
