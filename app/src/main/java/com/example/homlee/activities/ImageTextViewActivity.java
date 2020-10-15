package com.example.homlee.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.homlee.R;
import com.example.homlee.widgets.ImageTextView;

public class ImageTextViewActivity extends BaseActivity {

    private static final String TAG = "ImageTextViewActivity";
    private static final int MSG_SHOW_TEXT = 100;
    private static final int MSG_DELAY_TIME = 1000;
    private static final int MAX_NUMBER = 15;
    private static final int MIN_NUMBER = -5;
    private ImageTextView mTextView1;
    private TextView mTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text_activity);
        mTextView1 = findViewById(R.id.tv_text1);
        mTextView2 = findViewById(R.id.tv_text2);
        mHandler.obtainMessage(MSG_SHOW_TEXT, 0).sendToTarget();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MSG_SHOW_TEXT) {
                int num = (Integer) msg.obj;
                if (num >= 0) {
                    //mTextView.setText("" + num);
                    //mTextView.setText("" + Integer.toHexString(num));
                    showText("" + Integer.toHexString(num).toUpperCase());
                } else {
                    showText("" + num);
                }
                Message m;
                if (num < MAX_NUMBER) {
                    m = obtainMessage(MSG_SHOW_TEXT, num + 1);
                } else {
                    m = obtainMessage(MSG_SHOW_TEXT, MIN_NUMBER);
                }
                sendMessageDelayed(m, MSG_DELAY_TIME);
            }
        }
    };

    private void showText(String s) {
        mTextView1.setText(s);
        mTextView2.setText(s);
        Typeface tf = mTextView2.getTypeface();
        if (tf != null) {
            //Log.i(TAG, "showText: typeface = " + tf + ", SANS_SERIF = " + Typeface.SANS_SERIF + ", SERIF = " + Typeface.SERIF + ", MONOSPACE = " + Typeface.MONOSPACE + ", BOLD = " + Typeface.DEFAULT_BOLD + ", DEFAULT = " + Typeface.DEFAULT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_SHOW_TEXT);
    }
}
