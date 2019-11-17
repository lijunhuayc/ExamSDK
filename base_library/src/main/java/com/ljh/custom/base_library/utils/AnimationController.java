package com.ljh.custom.base_library.utils;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Desc: 动画控制工具类 工具类
 * Created by ${junhua.li} on 2016/05/10 10:51.
 * Email: lijunhuayc@sina.com
 */
public class AnimationController {
    private static final String TAG = AnimationController.class.getSimpleName();
    public final int rela1 = Animation.RELATIVE_TO_SELF;
    public final int rela2 = Animation.RELATIVE_TO_PARENT;
    public final int Default = -1;
    public final int Linear = 0;
    public final int Accelerate = 1;
    public final int Decelerate = 2;
    public final int AccelerateDecelerate = 3;
    public final int Bounce = 4;
    public final int Overshoot = 5;
    public final int Anticipate = 6;
    public final int AnticipateOvershoot = 7;

    // LinearInterpolator,AccelerateInterpolator,DecelerateInterpolator,AccelerateDecelerateInterpolator,
    // BounceInterpolator,OvershootInterpolator,AnticipateInterpolator,AnticipateOvershootInterpolator
    public AnimationController() {
    }

    private class MyAnimationListener implements AnimationListener {
        private View view;

        public MyAnimationListener(View view) {
            this.view = view;
        }

        public void onAnimationStart(Animation animation) {
            // this.view.setVisibility(View.VISIBLE);
        }

        public void onAnimationEnd(Animation animation) {
            this.view.setVisibility(View.GONE);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private void setEffect(Animation animation, int interpolatorType, long durationMillis, long delayMillis) {
        switch (interpolatorType) {
            case 0:
                animation.setInterpolator(new LinearInterpolator());
                break;
            case 1:
                animation.setInterpolator(new AccelerateInterpolator());
                break;
            case 2:
                animation.setInterpolator(new DecelerateInterpolator());
                break;
            case 3:
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case 4:
                animation.setInterpolator(new BounceInterpolator());
                break;
            case 5:
                animation.setInterpolator(new OvershootInterpolator());
                break;
            case 6:
                animation.setInterpolator(new AnticipateInterpolator());
                break;
            case 7:
                animation.setInterpolator(new AnticipateOvershootInterpolator());
                break;
            default:
                break;
        }
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
    }

    private void baseIn(View view, Animation animation, long durationMillis, long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        if(null != view){
            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);
        }else {
            Log.e(TAG, "this animation view is null.");
        }
    }

    private void baseOut(View view, Animation animation, long durationMillis, long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        animation.setAnimationListener(new MyAnimationListener(view));
        if(null != view){
            view.startAnimation(animation);
        }else {
            Log.e(TAG, "this animation view is null.");
        }
    }

    public void show(View view) {
        if(null != view){
            view.setVisibility(View.VISIBLE);
        }
    }

    public void hide(View view) {
        if(null != view){
            view.setVisibility(View.GONE);
        }
    }
    public void hideInVisible(View view) {
        if(null != view){
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void transparent(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public void fadeIn(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void fadeOut(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    /**
     * X右进
     */
    public void slideInRightX(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    /**
     * 左进
     */
    public void slideInLeftX(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, -1, rela2, 0, rela2, 0, rela2, 0);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    /**
     * X右出
     */
    public void slideOutRightX(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 1, rela2, 0, rela2, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    /**
     * 左出
     */
    public void slideOutLeftX(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    /**
     * Y上进
     */
    public void slideInUpY(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, -1, rela2, 0);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    /**
     * Y下进
     */
    public void slideInDownY(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1, rela2, 0);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    /**
     * Y上出
     */
    public void slideOutUpY(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0, rela2, -1);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    /**
     * Y下出
     */
    public void slideOutDownY(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0, rela2, 1);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void scaleIn(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, rela2, 0.5f, rela2, 0.5f);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void scaleOut(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, rela2, 0.5f, rela2, 0.5f);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void rotateCircle(View view, long durationMillis, float fromDegree, float toDegree) {
        RotateAnimation animation = new RotateAnimation(fromDegree, toDegree, rela1, 0.5f, rela1, 0.5f);
        animation.setFillAfter(true);
        baseIn(view, animation, durationMillis, 0);
    }

    public void rotateIn(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation = new RotateAnimation(-90, 0, rela1, 0, rela1, 1);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void rotateOut(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation = new RotateAnimation(0, 90, rela1, 0, rela1, 1);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void rotateFadeInLeft(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation1 = new RotateAnimation(-90, 0, rela1, 0, rela1, 1);
        AlphaAnimation animation2 = new AlphaAnimation(0, 1);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void rotateFadeOutRight(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation1 = new RotateAnimation(0, 90, rela1, 1, rela1, 1);
        AlphaAnimation animation2 = new AlphaAnimation(1, 0);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void scaleRotateIn(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, rela1, 0.5f, rela1, 0.5f);
        RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void scaleRotateOut(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation1 = new ScaleAnimation(1, 0, 1, 0, rela1, 0.5f, rela1, 0.5f);
        RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void slideFadeIn(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation1 = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
        AlphaAnimation animation2 = new AlphaAnimation(0, 1);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void slideFadeOut(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation1 = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
        AlphaAnimation animation2 = new AlphaAnimation(1, 0);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseOut(view, animation, durationMillis, delayMillis);
    }

}
