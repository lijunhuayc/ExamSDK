package com.ljh.custom.base_library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;

import com.ljh.custom.base_library.R;

/**
 * Desc: 两端对齐的TextView, 支持中英文混排
 * Created by Junhua.Li
 * Date: 2018/12/07 9:46
 */
public class AlignTextView extends AppCompatTextView {
    private boolean alignOnlyOneLine;

    public AlignTextView(Context context) {
        this(context, null);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context pContext, AttributeSet pAttrs) {
        TypedArray typedArray = pContext.obtainStyledAttributes(pAttrs, R.styleable.AlignTextView);
        alignOnlyOneLine = typedArray.getBoolean(R.styleable.AlignTextView_alignOnlyOneLine, false);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence content = getText();
        if (!(content instanceof String)) {
            super.onDraw(canvas);
            return;
        }
        String text = (String) content;
        Layout layout = getLayout();

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineBaseline = layout.getLineBaseline(i) + getPaddingTop();
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            if (alignOnlyOneLine && layout.getLineCount() == 1) {
                String line = text.substring(lineStart, lineEnd);
                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, this.getPaint());
                this.drawScaledText(canvas, line, lineBaseline, width);
            } else if (i == layout.getLineCount() - 1) {
                canvas.drawText(text.substring(lineStart), getPaddingLeft(), lineBaseline, this.getPaint());
                break;
            } else {
                String line = text.substring(lineStart, lineEnd);
                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, this.getPaint());
                this.drawScaledText(canvas, line, lineBaseline, width);
            }
        }
    }

    private void drawScaledText(Canvas canvas, String line, float baseLineY, float lineWidth) {
        if (line.length() < 1) {
            return;
        }
        float x = getPaddingLeft();
        boolean forceNextLine = line.charAt(line.length() - 1) == 10;
        int length = line.length() - 1;
        if (forceNextLine || length == 0) {
            canvas.drawText(line, x, baseLineY, this.getPaint());
            return;
        }
        float d = (getMeasuredWidth() - lineWidth - getPaddingLeft() - getPaddingRight()) / length;
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
            canvas.drawText(c, x, baseLineY, this.getPaint());
            x += cw + d;
        }
    }
}
