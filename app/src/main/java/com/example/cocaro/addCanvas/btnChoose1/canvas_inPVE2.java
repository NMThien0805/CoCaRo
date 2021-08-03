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

public class canvas_inPVE2 extends View {
    int left = 0;
    int top = 0;
    int right = 60;
    int bottom = 90;

    int first = 0;

    public canvas_inPVE2(Context context) {
        super(context);
        init();
    }
    public canvas_inPVE2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public canvas_inPVE2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public canvas_inPVE2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        createAnimation();
    }

    public void createAnimation() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, TRANSLATION_X, 285);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(objectAnimator1);
//        animatorSet.playTogether(objectAnimator21, objectAnimator22);
        animatorSet.setDuration(2100);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(first == 0){
                    animatorSet.setStartDelay(400);
                    first = 1;
                }
                animatorSet.start();
            }
        });

        animatorSet.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bm_object = BitmapFactory.decodeResource(this.getResources(), R.drawable.kamehameha1);

        Rect rectangle = new Rect(left,top,right,bottom);
        canvas.drawBitmap(bm_object, null, rectangle, null);
    }
}
