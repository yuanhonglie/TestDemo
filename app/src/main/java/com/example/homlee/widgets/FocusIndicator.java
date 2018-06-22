package com.example.homlee.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by homlee on 2018/6/22.
 */

public class FocusIndicator extends View {

    private int mWidth, mHeight;
    private float mProgress;
    private Rect mInnerRect;
    private Rect mOutterRect;
    private Paint mPaint;
    private int mForegroundColor = Color.BLACK;
    private int mBackgroundColor = 0xffcecece;
    private ObjectAnimator mFocusAnimator;
    private ObjectAnimator mReverseAnimator;
    private boolean mFocusable = false;
    public FocusIndicator(Context context) {
        this(context, null);
    }

    public FocusIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context ctx) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mFocusAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 1f);
        mFocusAnimator.setDuration(300);
        mReverseAnimator = ObjectAnimator.ofFloat(this, "progress", 1f, 0f);
        mReverseAnimator.setDuration(300);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            int height = mHeight > 1 ? mHeight - 1 : mHeight;
            mInnerRect = new Rect(0, 0, mWidth, height);
            mOutterRect = new Rect(0, 0, (int)(mWidth*mProgress), mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(mInnerRect, mPaint);
        if (mOutterRect != null) {
            mPaint.setColor(mForegroundColor);
            canvas.drawRect(mOutterRect, mPaint);
        }

    }

    @Override
    public void setBackgroundColor(int color) {

    }

    public void setForegroundColor(int color) {

    }

    public void setProgress(float progress) {
        mProgress = progress;
        if (mWidth != 0 && mHeight != 0) {
            mOutterRect = new Rect(0, 0, (int)(mWidth*mProgress), mHeight);
            invalidate();
        }
    }

    public float getProgress() {
        return mProgress;
    }

    public void setFocus(boolean focus) {
        setFocus(focus, true);
    }

    public void setFocus(boolean focus, boolean animate) {
        if (focus != mFocusable) {
            mFocusable = focus;
        } else {
            return;
        }

        if (animate) {
            Animator animator = focus ? mFocusAnimator : mReverseAnimator;
            animator.start();
        } else {
            setProgress(focus ? 0f : 1f);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFocusAnimator.cancel();
        mReverseAnimator.cancel();
    }
}
