package com.jie.recyclerview.library.divider;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liumingjie on 2016/06/07.
 */
public class VerticalDividerItemDecoration extends FlexibleDividerDecoration {

    private MarginProvider mMarginProvider;

    protected VerticalDividerItemDecoration(Builder builder) {
        super(builder);
        mMarginProvider = builder.mMarginProvider;
    }

    /*@Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        CustomRecyclerView customRecyclerView = null;
        if (parent instanceof CustomRecyclerView) {
            customRecyclerView = (CustomRecyclerView) parent;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }

        int itemCount = adapter.getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        int validChildCount = parent.getChildCount();
        int lastChildPosition = -1;
       *//* if (customRecyclerView != null) {
            validChildCount = validChildCount - customRecyclerView.getHeadersCount();
        }*//*
        for (int i = 0; i <= validChildCount; i++) {

            View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            int childPosition = parent.getChildAdapterPosition(child);

            if (childPosition < lastChildPosition) {
                // Avoid remaining divider when animation starts
                continue;
            }
            lastChildPosition = childPosition;

            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
                // Don't draw divider for last line if mShowLastDivider = false
                continue;
            }

            int groupIndex = getGroupIndex(childPosition, parent);
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue;
            }
            Rect bounds = getDividerBound(groupIndex, parent, child);
            switch (mDividerType) {
                case DRAWABLE:
                    Drawable drawable = mDrawableProvider.drawableProvider(groupIndex, parent);
                    drawable.setBounds(bounds);
                    drawable.draw(c);
                    break;
                case PAINT:
                    mPaint = mPaintProvider.dividerPaint(groupIndex, parent);
                    c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
                case COLOR:
                    mPaint.setColor(mColorProvider.dividerColor(groupIndex, parent));
                    mPaint.setStrokeWidth(mSizeProvider.dividerSize(groupIndex, parent));
                    c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
            }
        }
    }*/

    @Override
    protected Rect getDividerBound(int position, RecyclerView parent, View child) {
        Rect bounds = new Rect(0, 0, 0, 0);
        int transitionX = (int) ViewCompat.getTranslationX(child);
        int transitionY = (int) ViewCompat.getTranslationY(child);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        bounds.top = child.getTop() +
                mMarginProvider.dividerLeftOrTopMargin(position, parent);
        bounds.bottom = child.getBottom() -
                mMarginProvider.dividerRightOrBottomMargin(position, parent);

        int dividerSize = getDividerSize(position, parent);
        if (mDividerType == DividerType.DRAWABLE) {
            // set left and right position of divider
            if (mPositionInsideItem) {
                bounds.right = child.getRight() + params.leftMargin + transitionX;
                bounds.left = bounds.right - dividerSize;
            } else {
                bounds.left = child.getRight() + params.leftMargin + transitionX;
                bounds.right = bounds.left + dividerSize;
            }
        } else {

            // set center point of divider
            if (mPositionInsideItem) {
                bounds.left = child.getRight() + params.leftMargin - dividerSize / 2 + transitionX;
            } else {
                bounds.left = child.getRight() + params.leftMargin + dividerSize / 2 + transitionX;
            }
            bounds.right = bounds.left;
        }
        return bounds;
    }

    @Override
    protected void setItemOffsets(Rect outRect, int position, RecyclerView parent) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, getDividerSize(position, parent), 0);
        }
    }

    private int getDividerSize(int position, RecyclerView parent) {
        if (mPaintProvider != null) {
            return (int) mPaintProvider.dividerPaint(position, parent).getStrokeWidth();
        } else if (mSizeProvider != null) {
            return mSizeProvider.dividerSize(position, parent);
        } else if (mDrawableProvider != null) {
            Drawable drawable = mDrawableProvider.drawableProvider(position, parent);
            return drawable.getIntrinsicWidth();
        }
        throw new RuntimeException("failed to get size");
    }


    public static class Builder extends FlexibleDividerDecoration.Builder<Builder> {

        public Builder(Context context) {
            super(context);
        }

        @Override
        public VerticalDividerItemDecoration build() {
            checkBuilderParams();
            return new VerticalDividerItemDecoration(this);
        }
    }
}