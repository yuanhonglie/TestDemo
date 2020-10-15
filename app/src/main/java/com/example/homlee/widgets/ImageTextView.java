package com.example.homlee.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImageTextView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = "ImageTextView";
    private int height = 290;
    private String prevText;
    private List<Bitmap> bitmaps;
    private int bitmapsWidth;
    private int bitmapsHeight;
    private int lastMeasureWidth = -1;
    private Paint paint;
    private Matrix matrix;
    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ImageCache.initialize(context);
        bitmaps = new ArrayList<>(16);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        matrix = new Matrix();
    }

    public void setImageHeight(int h) {
        if (this.height != h) {
            this.height = h;
            requestLayout();
            invalidate();
        }
    }

    public int getImageHeight() {
        return height;
    }

    public void setText(String text) {
        if (TextUtils.equals(text, prevText)) {
            return;
        } else {
            bitmaps.clear();
            prevText = text;
            bitmaps = generateBitmapArray(text);
            if (bitmapsWidth != lastMeasureWidth) {
                requestLayout();
            }
            invalidate();
        }
    }

    private List<Bitmap> generateBitmapArray(String text) {
        List<Bitmap> bitmapList = new ArrayList<>();
        if (!TextUtils.isEmpty(text)) {
            bitmapsWidth = 0;
            bitmapsHeight = 0;
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                Bitmap bitmap = ImageCache.getInstance().get(ch);
                if (bitmap != null) {
                    bitmapsWidth += bitmap.getWidth();
                    bitmapsHeight = Math.max(bitmap.getHeight(), bitmapsHeight);
                    bitmapList.add(bitmap);
                }
            }
        }

        return bitmapList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        lastMeasureWidth = bitmapsWidth;
        int widthSize;
        int heightSize;

        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = Math.min(widthSpecSize, bitmapsWidth);
                break;
            case MeasureSpec.EXACTLY:
            default:
                widthSize = widthSpecSize;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = Math.min(heightSpecSize, bitmapsHeight);
                break;
            case MeasureSpec.EXACTLY:
            default:
                heightSize = heightSpecSize;
                break;
        }

        float scaleX = (float) widthSize / bitmapsWidth;
        float scaleY = (float) heightSize / bitmapsHeight;
        float minScale = Math.min(scaleX, scaleY);
        float dx = (widthSize - (bitmapsWidth * minScale)) / 2;
        float dy = (heightSize - (bitmapsHeight * minScale)) / 2;
        Log.i(TAG, "onMeasure: scale = " + minScale + ", dx = " + dx + ", dy = " + dy);
        matrix.setScale(minScale, minScale);
        matrix.postTranslate(dx, dy);

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, widthMode),
                MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.concat(matrix);
        for (Bitmap bitmap : bitmaps) {
            int dy = bitmapsHeight - bitmap.getHeight();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.translate(bitmap.getWidth(), dy);
        }
        canvas.restore();
    }
}
