package com.shengqf.view.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/2/25
 * describe : 线性自动换行布局(流式布局)
 * 参考：https://github.com/hongyangAndroid/FlowLayout
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 以下代码是FlowLayout的宽或高设置为wrap_content的情况
         * 如果FlowLayout的宽高均为确定值时，最后的测量直接写成：
         * setMeasuredDimension(measureWidth，measureHeight);
         */

        /**
         * FlowLayout的宽度
         * a、当FlowLayout的宽设置为wrap_content时，width为FlowLayout里面所有行中最宽一行的宽度
         * b、当FlowLayout的宽设置为确定值时，width为measure的宽度measureWidth
         */
        int width = 0;

        /**
         * FlowLayout的高度
         * a、当FlowLayout的高设置为wrap_content时，height为FlowLayout里面所有行高的总合
         * b、当FlowLayout的宽设置为确定值时，height为measure的高度measureHeight
         */
        int height = 0;

        int lineWidth = 0;//记录FlowLayout里面每一行子view的宽度
        int lineHeight = 0;//记录FlowLayout里面每一行子view的高度

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                if (i == count - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }

            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams marginParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth()
                    + marginParams.leftMargin + marginParams.rightMargin;
            int childHeight = childView.getMeasuredHeight()
                    + marginParams.topMargin + marginParams.bottomMargin;

            if (lineWidth + childWidth > measureWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {//一行没填满，继续往后添加子view
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        int tempWidth = measureWidthMode == MeasureSpec.EXACTLY ?
                measureWidth : width + getPaddingLeft() + getPaddingRight();
        int tempHeight = measureHeightMode == MeasureSpec.EXACTLY ?
                measureHeight : height + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(tempWidth, tempHeight);
    }

    private List<View> lineViews = new ArrayList<>();//某一行子view的集合
    protected List<List<View>> mAllViews = new ArrayList<>();//所有行的集合
    protected List<Integer> mLineHeight = new ArrayList<>();//所有行中每一行子view的最大的高度的集合
    protected List<Integer> mLineWidth = new ArrayList<>();//所有行中每一行子view的宽度和的集合

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        lineViews.clear();

        int width = getWidth();//当宽设置为wrap_content时，值为屏幕宽度

        int lineWidth = 0;
        int lineHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams marginParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth()
                    + marginParams.leftMargin + marginParams.rightMargin;
            int childHeight = childView.getMeasuredHeight()
                    + marginParams.topMargin + marginParams.bottomMargin;

            if (childWidth + lineWidth > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mLineWidth.add(lineWidth);
                mAllViews.add(lineViews);

                lineWidth = 0;
                lineHeight = childHeight;
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(childView);
        }
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);
        mAllViews.add(lineViews);

        int left;
        int top = getPaddingTop();

        int lineNum = mAllViews.size();//行数

        for (int i = 0; i < lineNum; i++) {//遍历所有行
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //左对齐显示
            left = getPaddingLeft();
            //居中显示
            //left = (width - mLineWidth.get(i)) / 2 + getPaddingLeft();
            //右对齐显示
            //left = width - mLineWidth.get(i) + getPaddingLeft();

            for (int j = 0; j < lineViews.size(); j++) {//遍历某一行中的所有子view
                View childView = lineViews.get(j);
                if (childView.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams marginParams = (MarginLayoutParams) childView.getLayoutParams();
                int lc = left + marginParams.leftMargin;
                int tc = top + marginParams.topMargin;
                int rc = lc + childView.getMeasuredWidth();
                int bc = tc + childView.getMeasuredHeight();

                //摆放子view
                childView.layout(lc, tc, rc, bc);

                left += childView.getMeasuredWidth()
                        + marginParams.leftMargin + marginParams.rightMargin;
            }
            top += lineHeight;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}