package com.example.homlee.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.util.LruCache;

/**
 * @author hlyu
 */
public class ImageCache {
    private static final String TAG = "ImageCache";
    private static final int MAX_CACHE_SIZE = 30 * 1024 * 1024;
    private static final int DEFAULT_TEXT_SIZE = 290;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private LruCache<Character, Bitmap> mLruCache;
    private volatile static ImageCache mInstance;
    private Context mContext;
    private Paint mPaint;

    private ImageCache(Context context, int textSize, int textColor) {
        mContext = context;

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        mPaint.setTypeface(Typeface.SANS_SERIF);
        //mPaint.setFakeBoldText(true);

        mLruCache = new LruCache<Character, Bitmap>(MAX_CACHE_SIZE) {
            @Override
            protected int sizeOf(Character key, Bitmap value) {
                return value.getWidth() * value.getHeight() * 4;
            }

            @Override
            protected Bitmap create(Character key) {
                Log.i(TAG, "create: " + key);
                CharImageMap map = CharImageMap.getInstance();
                if (map.hasImageRes(key)) {
                    return createBitmapByRes(map.getImageRes(key));
                } else {
                    return createBitmapByChar(key);
                }
            }
        };
    }

    private Bitmap createBitmapByRes(int id) {
        return BitmapFactory.decodeResource(mContext.getResources(), id);
    }

    private Bitmap createBitmapByChar(Character key) {
        String text = "" + key;

        int width = (int) (mPaint.measureText(text) + 1);
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, 1, bounds);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float height = Math.abs(metrics.bottom) + Math.abs(metrics.top) + Math.abs(metrics.leading) + 1;
        Log.i(TAG, "createBitmapByChar: width = " + width + ", bounds.width() = " + bounds.width());
        Log.i(TAG, "createBitmapByChar: ascent = " + metrics.ascent + ", bottom = " + metrics.bottom + ", descent = " + metrics.descent + ", top = " + metrics.top + ", leading = " + metrics.leading);
        Bitmap bitmap = Bitmap.createBitmap(Math.max(bounds.width(), width), (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //canvas.drawColor(Color.GREEN);
        int baseline = (int) (height - Math.abs(metrics.bottom));
        canvas.drawText(text, 0, baseline, mPaint);
        return bitmap;
    }

    public static void initialize(Context context, int textSize, int textColor) {
        if (mInstance == null) {
            synchronized (ImageCache.class) {
                if (mInstance == null) {
                    mInstance = new ImageCache(context, textSize, textColor);
                }
            }
        }
    }

    public static void initialize(Context context) {
        initialize(context, DEFAULT_TEXT_SIZE, DEFAULT_TEXT_COLOR);
    }

    public static ImageCache getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("call ImageCache.initialize(Context) first!!");
        }
        return mInstance;
    }

    public Bitmap get(Character ch) {
        return mLruCache.get(ch);
    }
}
