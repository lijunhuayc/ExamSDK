package com.ljh.custom.base_library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class DrawableTools {

    /**
     * 构建圆角矩形
     *
     * @param w     圆角半径
     * @param color 矩形颜色
     * @return
     */
    public static ShapeDrawable buildRoundRectDrawable(int w, int color) {
        int roc = w;
        float[] outerR = new float[]{roc, roc, roc, roc, roc, roc, roc, roc};
        ShapeDrawable draw = new ShapeDrawable(new RoundRectShape(outerR, null, null));
        draw.getPaint().setColor(color);
        return draw;
    }

    /**
     * 构建矩形
     *
     * @param color 矩形颜色
     * @return drawable
     */
    public static GradientDrawable buildRectDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        return drawable;
    }

    /**
     * 获取默认的带状态drawable
     *
     * @param normal
     * @param pressed
     * @return
     */
    public static StateListDrawable buildStateDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable sd = new StateListDrawable();
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    /**
     * 获取带状态drawable(针对带checked属性的view)
     *
     * @param normal
     * @param pressed
     * @return
     *///当对应的View处于不同的状态时，对应的bacdground跟着变化
    public static StateListDrawable buildCheckableStateDrawable(Drawable normal, Drawable pressed, Drawable checked) {
        StateListDrawable sd = new StateListDrawable();
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}, checked);
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    public static Drawable buildIntrinsicDrawable(Context context, int res) {
        return buildIntrinsicDrawable(context.getResources().getDrawable(res));
    }

    public static Drawable buildIntrinsicDrawable(Drawable drawable) {
        if (drawable == null) return null;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    /**
     * 不安全，独立出来
     *
     * @param bmp
     * @return
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable createDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

}
