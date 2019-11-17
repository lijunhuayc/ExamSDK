package com.ljh.custom.base_library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.custom.base_library.R;
import com.ljh.custom.base_library.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GP62 on 2018/1/22.
 */
@Deprecated
public class FlowLayout extends ViewGroup {
    private Context mContext;
    private int usefulWidth; // the space of a line we can use(line's width minus the sum of left and right padding
    private int lineSpacing = 0; // the spacing between lines in flowlayout
    //    private int maxLines = 0; // the max lines in flowlayout
    List<View> childList = new ArrayList();
    List<Integer> lineNumList = new ArrayList();
    private boolean foldEnable = true;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        lineSpacing = mTypedArray.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
//        maxLines = mTypedArray.getInteger(R.styleable.FlowLayout_maxLine, 0);
        mTypedArray.recycle();
    }

    public boolean isFoldEnable() {
        return foldEnable;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineY = mPaddingTop;
        int lineHeight = 0;

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;
            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, lineY);
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin;
                spaceHeight = mlp.topMargin + mlp.bottomMargin;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineUsed + spaceWidth > widthSize) {
                //approach the limit of width and move to next line
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineHeight = 0;
            }
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
        }

        setMeasuredDimension(widthSize, heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + mPaddingBottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int lineX = mPaddingLeft;
        int lineY = mPaddingTop;
        int lineWidth = r - l;
        usefulWidth = lineWidth - mPaddingLeft - mPaddingRight;
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineHeight = 0;
        int lineNum = 0;

        lineNumList.clear();
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin;
                spaceHeight = mlp.topMargin + mlp.bottomMargin;
                left = lineX + mlp.leftMargin;
                top = lineY + mlp.topMargin;
                right = lineX + mlp.leftMargin + childWidth;
                bottom = lineY + mlp.topMargin + childHeight;
            } else {
                left = lineX;
                top = lineY;
                right = lineX + childWidth;
                bottom = lineY + childHeight;
            }
            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineUsed + spaceWidth > lineWidth) {
                //approach the limit of width and move to next line
                lineNumList.add(lineNum);
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineX = mPaddingLeft;
                lineHeight = 0;
                lineNum = 0;
                if (childLp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                    left = lineX + mlp.leftMargin;
                    top = lineY + mlp.topMargin;
                    right = lineX + mlp.leftMargin + childWidth;
                    bottom = lineY + mlp.topMargin + childHeight;
                } else {
                    left = lineX;
                    top = lineY;
                    right = lineX + childWidth;
                    bottom = lineY + childHeight;
                }
            }
            child.layout(left, top, right, bottom);
            lineNum++;
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
            lineX += spaceWidth;
        }
        // add the num of last line
        lineNumList.add(lineNum);
    }

    /**
     * resort child elements to use lines as few as possible
     */
    public void relayoutToCompress() {
        int childCount = this.getChildCount();
        if (0 == childCount) {
            //no need to sort if flowlayout has no child view
            return;
        }
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            count++;
        }
        View[] childs = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            childs[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            } else {
                spaces[n] = childWidth;
            }
            n++;
        }
        int[] compressSpaces = new int[count];
        for (int i = 0; i < count; i++) {
            compressSpaces[i] = spaces[i] > usefulWidth ? usefulWidth : spaces[i];
        }
        sortToCompress(childs, compressSpaces);
        this.removeAllViews();
        for (View v : childList) {
            this.addView(v);
        }
        childList.clear();
    }

    private void sortToCompress(View[] childs, int[] spaces) {
        int childCount = childs.length;
        int[][] table = new int[childCount + 1][usefulWidth + 1];
        for (int i = 0; i < childCount + 1; i++) {
            for (int j = 0; j < usefulWidth; j++) {
                table[i][j] = 0;
            }
        }
        boolean[] flag = new boolean[childCount];
        for (int i = 0; i < childCount; i++) {
            flag[i] = false;
        }
        for (int i = 1; i <= childCount; i++) {
            for (int j = spaces[i - 1]; j <= usefulWidth; j++) {
                table[i][j] = (table[i - 1][j] > table[i - 1][j - spaces[i - 1]] + spaces[i - 1]) ? table[i - 1][j] : table[i - 1][j - spaces[i - 1]] + spaces[i - 1];
            }
        }
        int v = usefulWidth;
        for (int i = childCount; i > 0 && v >= spaces[i - 1]; i--) {
            if (table[i][v] == table[i - 1][v - spaces[i - 1]] + spaces[i - 1]) {
                flag[i - 1] = true;
                v = v - spaces[i - 1];
            }
        }
        int rest = childCount;
        View[] restArray;
        int[] restSpaces;
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == true) {
                childList.add(childs[i]);
                rest--;
            }
        }

        if (0 == rest) {
            return;
        }
        restArray = new View[rest];
        restSpaces = new int[rest];
        int index = 0;
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == false) {
                restArray[index] = childs[i];
                restSpaces[index] = spaces[i];
                index++;
            }
        }
        table = null;
        childs = null;
        flag = null;
        sortToCompress(restArray, restSpaces);
    }

    /**
     * add some blank view to make child elements look in alignment
     */
    public void relayoutToAlign() {
        int childCount = this.getChildCount();
        if (0 == childCount) {
            //no need to sort if flowlayout has no child view
            return;
        }
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            count++;
        }
        View[] childs = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            childs[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            } else {
                spaces[n] = childWidth;
            }
            n++;
        }
        int lineTotal = 0;
        int start = 0;
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            if (lineTotal + spaces[i] > usefulWidth) {
                int blankWidth = usefulWidth - lineTotal;
                int end = i - 1;
                int blankCount = end - start;
                if (blankCount >= 0) {
                    if (blankCount > 0) {
                        int eachBlankWidth = blankWidth / blankCount;
                        MarginLayoutParams lp = new MarginLayoutParams(eachBlankWidth, 0);
                        for (int j = start; j < end; j++) {
                            this.addView(childs[j]);
                            BlankView blank = new BlankView(mContext);
                            this.addView(blank, lp);
                        }
                    }
                    this.addView(childs[end]);
                    start = i;
                    i--;
                    lineTotal = 0;
                } else {
                    this.addView(childs[i]);
                    start = i + 1;
                    lineTotal = 0;
                }
            } else {
                lineTotal += spaces[i];
            }
        }
        for (int i = start; i < count; i++) {
            this.addView(childs[i]);
        }
    }

    /**
     * use both of relayout methods together
     */
    public void relayoutToCompressAndAlign() {
        this.relayoutToCompress();
        this.relayoutToAlign();
    }

    /**
     * modify by lijunhua. 临时修改解决方案, 通过行数计算 n 行子view 所需高度来更新容器的高度实现折叠
     * PS: 只适用于子 view 样式相同的情况(子View必须使用固定高度,否则此自定义View原来本身就有个不知道原因的BUG,自适应高度时可能部分子View会被压缩)
     *
     * @param line_num
     */
    public void specifyLines(int line_num) {
        int uiFoldRowNum = 1;// UI折叠时显示的行数, UI变动时需修改此值
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (getChildCount() > 0) {
            int childMeasureHeight = 0;
            View child = getChildAt(0);
            int intW = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int intH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            child.measure(intW, intH);
            childMeasureHeight = child.getMeasuredHeight();

            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                childMeasureHeight += mlp.topMargin + mlp.bottomMargin;
            }
            int targetHeight = childMeasureHeight * line_num + lineSpacing * (line_num - 1) + getPaddingTop() + getPaddingBottom();
            int uiFoldRowHeight = childMeasureHeight * uiFoldRowNum + lineSpacing * (uiFoldRowNum - 1) + getPaddingTop() + getPaddingBottom();
            // TODO: 2018/7/10 0010  这里还需要计算切换显示不同行数该如何计算, 目前只使用指定行显示, 暂时未补充

            int intW2 = View.MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(getContext()), View.MeasureSpec.UNSPECIFIED);
            int intH2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            measure(intW2, intH2);
            int selfMeasuredMaxHeight = getMeasuredHeight();
            int newHeight;
            // line_num == 0 表示展开, targetHeight 就是自身实际高度
            if (line_num == 0 || targetHeight >= selfMeasuredMaxHeight) {
                newHeight = selfMeasuredMaxHeight;
                foldEnable = selfMeasuredMaxHeight > uiFoldRowHeight;//自身实际高度大于1行
            } else {
                newHeight = targetHeight;
                foldEnable = true;
            }
            lp.height = newHeight;
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            foldEnable = false;
        }
        setLayoutParams(lp);
    }

//    public void specifyLines(int line_num) {
//        int childNum = 0;
//        if (line_num > lineNumList.size()) {
//            line_num = lineNumList.size();
//        }
//        for (int i = 0; i < line_num; i++) {
//            childNum += lineNumList.get(i);
//        }
//        List<View> viewList = new ArrayList<>();
//        for (int i = 0; i < childNum; i++) {
//            viewList.add(getChildAt(i));
//        }
//        removeAllViews();
//        for (View v : viewList) {
//            addView(v);
//        }
//    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    class BlankView extends View {
        public BlankView(Context context) {
            super(context);
        }
    }
}
