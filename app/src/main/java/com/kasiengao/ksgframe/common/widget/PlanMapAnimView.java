package com.kasiengao.ksgframe.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.kasiengao.ksgframe.R;

/**
 * @ClassName: PlanMapAnimView
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/27 16:19
 * @Description:
 */
public class PlanMapAnimView extends AppCompatImageView {

    private float mx;
    private float my;

    public PlanMapAnimView(@NonNull Context context) {
        this(context, null);
    }

    public PlanMapAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.setScaleType(ScaleType.CENTER);
        this.setImageResource(R.drawable.ic_car);


        post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = getDrawable();
                int intrinsicWidth = drawable.getIntrinsicWidth();

                int width = getWidth();

               int x =  (intrinsicWidth - width)/2;
                Log.d("Zzz","x = "+x);
                Log.d("Zzz","intrinsicWidth = "+intrinsicWidth);
                Log.d("Zzz","width = "+width);

                scrollBy(-x,0);


                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
                valueAnimator.addUpdateListener(animation -> {
                    Log.d("Zzz","width = "+animation.getAnimatedValue());
                    scrollBy(1,0);
                });
                valueAnimator.setDuration(500);
                valueAnimator.setRepeatCount(-1);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.start();
            }
        });
    }


}