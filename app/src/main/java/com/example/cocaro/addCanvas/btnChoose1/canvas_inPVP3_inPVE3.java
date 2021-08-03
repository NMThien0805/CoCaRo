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

public class canvas_inPVP3_inPVE3 extends View{

    int left = 0;
    int top = 0;
    int right = 130;
    int bottom = 130;

    public canvas_inPVP3_inPVE3(Context context) {
        super(context);
        init();
    }
    public canvas_inPVP3_inPVE3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public canvas_inPVP3_inPVE3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public canvas_inPVP3_inPVE3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        createAnimation();
    }

    public void createAnimation() {
        ObjectAnimator objectAnimator1 = null;
        ObjectAnimator objectAnimator2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            objectAnimator1 = ObjectAnimator.ofFloat(this, TRANSLATION_Z, 10);
//            objectAnimator2 = ObjectAnimator.ofFloat(this, TRANSLATION_Z, -1);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(objectAnimator1);
        animatorSet.setDuration(1000);
        animatorSet.setStartDelay(1500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });

        animatorSet.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bm_boom = BitmapFactory.decodeResource(this.getResources(),R.drawable.boooooom);

        Rect rectangle = new Rect(left,top,right,bottom);
        canvas.drawBitmap(bm_boom, null, rectangle, null);
    }
}
