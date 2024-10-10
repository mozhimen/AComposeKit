package com.mozhimen.composek.basic.ui.platform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.compose.ui.platform.AbstractComposeView;

/**
 * @ClassName AbstractComposeView2
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/9
 * @Version 1.0
 */
public abstract class AbstractComposeView2 extends AbstractComposeView{
    public AbstractComposeView2(Context context) {
        super(context);
    }

    public AbstractComposeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractComposeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void internalOnMeasure$ui_release(int widthMeasureSpec, int heightMeasureSpec) {
        super.internalOnMeasure$ui_release(widthMeasureSpec, heightMeasureSpec);
        Log.d("AbstractComposeView2>>>>>", "internalOnMeasure$ui_release: ");
    }

    @Override
    public void internalOnLayout$ui_release(boolean changed, int left, int top, int right, int bottom) {
        super.internalOnLayout$ui_release(changed, left, top, right, bottom);
    }
}
