package com.example.cocaro.addCanvas.btnChoose1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cocaro.R;

public class canvas_inPVE4 extends View {
    int left = 0;
    int top = 0;
    int right = 130;
    int bottom = 100;

    public canvas_inPVE4(Context context) {
        super(context);
    }
    public canvas_inPVE4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public canvas_inPVE4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public canvas_inPVE4(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bm_object = BitmapFactory.decodeResource(this.getResources(), R.drawable.rock);

        Rect rectangle = new Rect(left,top,right,bottom);
        canvas.drawBitmap(bm_object, null, rectangle, null);
    }
}
