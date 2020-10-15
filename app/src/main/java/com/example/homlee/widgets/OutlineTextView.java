package com.example.homlee.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class OutlineTextView extends AppCompatTextView {
    private TextPaint outlinePaint;
    private TextPaint fillPaint;
    public OutlineTextView(Context context) {
        this(context, null);
    }

    public OutlineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        outlinePaint = new TextPaint();
        //自定义描边效果
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(Color.parseColor("#B4000000"));
        outlinePaint.setStrokeWidth(4);

        fillPaint = new TextPaint();
        fillPaint.set(outlinePaint);
        fillPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        //复制原来TextViewg画笔中的一些参数
        CharSequence cs = getText();
        if (cs != null) {
            String text = cs.toString();
            TextPaint paint = getPaint();
            outlinePaint.setTextSize(getTextSize());
            outlinePaint.setFlags(paint.getFlags());
            outlinePaint.setAlpha(paint.getAlpha());

            fillPaint.setTextSize(getTextSize());
            fillPaint.setFlags(paint.getFlags());
            fillPaint.setAlpha(paint.getAlpha());
            fillPaint.setColor(getTextColors().getDefaultColor());

            //在文本底层画出带描边的文本
            //float x = (getWidth() - outlinePaint.measureText(text)) / 2;
            canvas.drawText(text, 0, getBaseline(), fillPaint);
            canvas.drawText(text, 0, getBaseline(), outlinePaint);
            //canvas.drawText(text, x, getBaseline(), paint);
        }
        //super.onDraw(canvas);
    }

}
