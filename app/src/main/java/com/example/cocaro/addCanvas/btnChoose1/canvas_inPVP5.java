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

public class canvas_inPVP5 extends View{

    Bitmap[] arr_character_bitmap;
    Bitmap[] arr_member_bitmap = new Bitmap[3];
    int left = 0;
    int top = 0;
    int right = 50;
    int bottom = 50;

    public canvas_inPVP5(Context context) {
        super(context);
        init();
    }
    public canvas_inPVP5(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public canvas_inPVP5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public canvas_inPVP5(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        Bitmap image_character = BitmapFactory.decodeResource(this.getResources(),R.drawable.character);
        int col_member = 4;
        int row_member = 2;
        int width_member = image_character.getWidth()/col_member;
        int height_member = image_character.getHeight()/row_member;
        // 4*2 = 8 (character)
        int count_member = col_member*row_member;

        arr_character_bitmap = new Bitmap[count_member];
        for(int r = 0; r<row_member; r++){
            for(int c = 0; c<col_member; c++){
                arr_character_bitmap[c + r*col_member]
                        = Bitmap.createBitmap(image_character, c*width_member, r*height_member, width_member, height_member);
            }
        }

        createAnimation();
    }

    private void getCharacter(int stt_nv){
        Bitmap src = arr_character_bitmap[stt_nv];

        int width = src.getWidth()/3;
        int height = src.getHeight()/4;

        arr_member_bitmap[0] = Bitmap.createBitmap(src, 0, 1*height, width, height);
        arr_member_bitmap[1] = Bitmap.createBitmap(src, width, 1*height, width, height);
        arr_member_bitmap[2] = Bitmap.createBitmap(src, 2*width, 1*height, width, height);
    }

    public void createAnimation() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, TRANSLATION_X, -5);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(objectAnimator1);
        animatorSet.setDuration(1000);
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

        getCharacter(2);

        Rect rectangle = new Rect(left,top,right,bottom);
        canvas.drawBitmap(arr_member_bitmap[0], null, rectangle, null);
    }
}
