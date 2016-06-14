package com.jie.recyclerview.library.divider;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jie.recyclerview.library.view.CustomRecyclerView;

import java.util.ArrayList;

/**
 * Created by liumingjie on 2016/06/07.
 */
public abstract class FlexibleDividerDecoration extends RecyclerView.ItemDecoration {

    private static final int DEFAULT_SIZE = 2;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    protected enum DividerType {
        DRAWABLE, PAINT, COLOR
    }

    protected DividerType mDividerType = DividerType.DRAWABLE;
    protected WrapVisibilityProvider mVisibilityProvider;
    protected PaintProvider mPaintProvider;
    protected ColorProvider mColorProvider;
    protected DrawableProvider mDrawableProvider;
    protected SizeProvider mSizeProvider;
    protected boolean mShowLastDivider;
    protected boolean mPositionInsideItem;
    protected Paint mPaint;

    protected FlexibleDividerDecoration(Builder builder) {
        if (builder.mPaintProvider != null) {
            mDividerType = DividerType.PAINT;
            mPaintProvider = builder.mPaintProvider;
        } else if (builder.mColorProvider != null) {
            mDividerType = DividerType.COLOR;
            mColorProvider = builder.mColorProvider;
            mPaint = new Paint();
            setSizeProvider(builder);
        } else {
            mDividerType = DividerType.DRAWABLE;
            if (builder.mDrawableProvider == null) {
                TypedArray a = builder.mContext.obtainStyledAttributes(ATTRS);
                final Drawable divider = a.getDrawable(0);
                a.recycle();
                mDrawableProvider = new DrawableProvider() {
                    @Override
                    public Drawable drawableProvider(int position, RecyclerView parent) {
                        return divider;
                    }
                };
            } else {
                mDrawableProvider = builder.mDrawableProvider;
            }
            mSizeProvider = builder.mSizeProvider;
        }

        mVisibilityProvider = new WrapVisibilityProvider(builder.mVisibilityProvider);
        mShowLastDivider = builder.mShowLastDivider;
        mPositionInsideItem = builder.mPositionInsideItem;
    }

    private void setSizeProvider(Builder builder) {
        mSizeProvider = builder.mSizeProvider;
        if (mSizeProvider == null) {
            mSizeProvider = new SizeProvider() {
                @Override
                public int dividerSize(int position, RecyclerView parent) {
                    return DEFAULT_SIZE;
                }
            };
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        CustomRecyclerView customRecyclerView = null;
        if (parent instanceof CustomRecyclerView) {
            customRecyclerView = (CustomRecyclerView) parent;
            mVisibilityProvider.setHeaderViews(customRecyclerView.getHeaderViews());
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }

        int itemCount = adapter.getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        int validChildCount = parent.getChildCount();
        int lastChildPosition = -1;
        for (int i = 0; i < validChildCount; i++) {
            View child = parent.getChildAt(i);
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

          /*  if (wasDividerAlreadyDrawn(childPosition, parent)) {
                // No need to draw divider again as it was drawn already by previous column
                continue;
            }*/

            int groupIndex = getGroupIndex(childPosition, parent);
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue;
            }
            Log.e("index","groupIndex:"+groupIndex);
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
    }

    @Override
    public void getItemOffsets(Rect rect, View v, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(v);
        int itemCount = parent.getAdapter().getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        if (!mShowLastDivider && position >= itemCount - lastDividerOffset) {
            // Don't set item offset for last line if mShowLastDivider = false
            return;
        }

        if (wasDividerAlreadyDrawn(position, parent)) {
            // No need to draw divider again as it was drawn already by previous column
            return;
        }
        int groupIndex = getGroupIndex(position, parent);

        if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)){
            return;
        }

        setItemOffsets(rect, groupIndex, parent);
    }

    /**
     * In the case mShowLastDivider = false,
     * Returns offset for how many views we don't have to draw a divider for,
     * for LinearLayoutManager it is as simple as not drawing the last child divider,
     * but for a GridLayoutManager it needs to take the span count for the last items into account
     * until we use the span count configured for the grid.
     *
     * @param parent RecyclerView
     * @return offset for how many views we don't have to draw a divider or 1 if its a
     * LinearLayoutManager
     */
    protected int getLastDividerOffset(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            for (int i = itemCount - 1; i >= 0; i--) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i;
                }
            }
        }

        return 1;
    }

    /**
     * Determines whether divider was already drawn for the row the item is in,
     * effectively only makes sense for a grid
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return true if the divider can be skipped as it is in the same row as the previous one.
     */
    private boolean wasDividerAlreadyDrawn(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanIndex(position, spanCount) > 0;
        }

        return false;
    }

    /**
     * Returns a group index for GridLayoutManager.
     * for LinearLayoutManager, always returns position.
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return group index of items
     */
    protected int getGroupIndex(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanGroupIndex(position, spanCount);
        }

        return position;
    }

    protected abstract Rect getDividerBound(int position, RecyclerView parent, View child);

    protected abstract void setItemOffsets(Rect outRect, int position, RecyclerView parent);

    /**
     * Interface for controlling divider visibility
     */
    public interface VisibilityProvider {

        /**
         * Returns true if divider should be hidden.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return True if the divider at position should be hidden
         */
        boolean shouldHideDivider(int position, RecyclerView parent);
    }

    /**
     * 过滤掉headerview的
     */
    protected class WrapVisibilityProvider implements VisibilityProvider{

        private VisibilityProvider visibilityProvider;
        private ArrayList<View> mHeaderViews = new ArrayList<>();

        public WrapVisibilityProvider(VisibilityProvider visibilityProvider){
            this.visibilityProvider = visibilityProvider;
        }

        public void setHeaderViews(ArrayList<View> headerViews) {
            mHeaderViews = headerViews;
        }

        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
            if(mHeaderViews != null && mHeaderViews.size()>0){
                for(int i = 0;i<mHeaderViews.size();i++){
                    if(position == i) {
                        return true;
                    }
                }
            }
            return visibilityProvider.shouldHideDivider(position,parent);
        }
    }

    /**
     * Interface for controlling paint instance for divider drawing
     */
    public interface PaintProvider {

        /**
         * Returns {@link Paint} for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Paint instance
         */
        Paint dividerPaint(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling divider color
     */
    public interface ColorProvider {

        /**
         * Returns {@link android.graphics.Color} value of divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Color value
         */
        int dividerColor(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling drawable object for divider drawing
     */
    public interface DrawableProvider {

        /**
         * Returns drawable instance for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Drawable instance
         */
        Drawable drawableProvider(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling divider size
     */
    public interface SizeProvider {

        /**
         * Returns size value of divider.
         * Height for horizontal divider, width for vertical divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Size of divider
         */
        int dividerSize(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling divider margin
     */
    public interface MarginProvider {

        /**
         * Returns top margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return top margin
         */
        int dividerLeftOrTopMargin(int position, RecyclerView parent);

        /**
         * Returns bottom margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return bottom margin
         */
        int dividerRightOrBottomMargin(int position, RecyclerView parent);
    }

    public abstract static class Builder<T extends Builder> {

        private Context mContext;
        protected Resources mResources;
        private PaintProvider mPaintProvider;
        private ColorProvider mColorProvider;
        private DrawableProvider mDrawableProvider;
        private SizeProvider mSizeProvider;
        private VisibilityProvider mVisibilityProvider = new VisibilityProvider() {
            @Override
            public boolean shouldHideDivider(int position, RecyclerView parent) {
                return false;
            }
        };
        private boolean mShowLastDivider = false;
        private boolean mPositionInsideItem = false;

        public Builder(Context context) {
            mContext = context;
            mResources = context.getResources();
        }

        protected MarginProvider mMarginProvider = new MarginProvider() {

            @Override
            public int dividerLeftOrTopMargin(int position, RecyclerView parent) {
                return 0;
            }

            @Override
            public int dividerRightOrBottomMargin(int position, RecyclerView parent) {
                return 0;
            }
        };

        public Builder margin(final int topMargin, final int bottomMargin) {
            return marginProvider(new MarginProvider() {
                @Override
                public int dividerLeftOrTopMargin(int position, RecyclerView parent) {
                    return topMargin;
                }

                @Override
                public int dividerRightOrBottomMargin(int position, RecyclerView parent) {
                    return bottomMargin;
                }
            });
        }

        public Builder margin(int verticalMargin) {
            return margin(verticalMargin, verticalMargin);
        }

        public Builder marginResId(@DimenRes int topMarginId, @DimenRes int bottomMarginId) {
            return margin(mResources.getDimensionPixelSize(topMarginId),
                    mResources.getDimensionPixelSize(bottomMarginId));
        }

        public Builder marginResId(@DimenRes int verticalMarginId) {
            return marginResId(verticalMarginId, verticalMarginId);
        }

        public T marginProvider(MarginProvider provider) {
            mMarginProvider = provider;
            return (T)this;
        }
        public T paint(final Paint paint) {
            return paintProvider(new PaintProvider() {
                @Override
                public Paint dividerPaint(int position, RecyclerView parent) {
                    return paint;
                }
            });
        }

        public T paintProvider(PaintProvider provider) {
            mPaintProvider = provider;
            return (T) this;
        }

        public T color(final int color) {
            return colorProvider(new ColorProvider() {
                @Override
                public int dividerColor(int position, RecyclerView parent) {
                    return color;
                }
            });
        }

        public T colorResId(@ColorRes int colorId) {
            return color(ContextCompat.getColor(mContext, colorId));
        }

        public T colorProvider(ColorProvider provider) {
            mColorProvider = provider;
            return (T) this;
        }

        public T drawable(@DrawableRes int id) {
            return drawable(ContextCompat.getDrawable(mContext, id));
        }

        public T drawable(final Drawable drawable) {
            return drawableProvider(new DrawableProvider() {
                @Override
                public Drawable drawableProvider(int position, RecyclerView parent) {
                    return drawable;
                }
            });
        }

        public T drawableProvider(DrawableProvider provider) {
            mDrawableProvider = provider;
            return (T) this;
        }

        public T size(final int size) {
            return sizeProvider(new SizeProvider() {
                @Override
                public int dividerSize(int position, RecyclerView parent) {
                    return size;
                }
            });
        }

        public T sizeResId(@DimenRes int sizeId) {
            return size(mResources.getDimensionPixelSize(sizeId));
        }

        public T sizeProvider(SizeProvider provider) {
            mSizeProvider = provider;
            return (T) this;
        }

        public T visibilityProvider(VisibilityProvider provider) {
            mVisibilityProvider = provider;
            return (T) this;
        }

        public T showLastDivider() {
            mShowLastDivider = true;
            return (T) this;
        }

        public T positionInsideItem(boolean positionInsideItem) {
            mPositionInsideItem = positionInsideItem;
            return (T) this;
        }

        protected void checkBuilderParams() {
            if (mPaintProvider != null) {
                if (mColorProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setColor method of Paint class to specify line color. Do not provider ColorProvider if you set PaintProvider.");
                }
                if (mSizeProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setStrokeWidth method of Paint class to specify line size. Do not provider SizeProvider if you set PaintProvider.");
                }
            }
        }

        public abstract FlexibleDividerDecoration build();
    }
}