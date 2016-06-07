package com.jie.recyclerview.library.divider;

import android.content.Context;

/**
 * Created by liumingjie on 2016/6/7.
 */
public abstract class AbstractDividerFactory {

    protected Context mContext;

    public AbstractDividerFactory(Context context){
        mContext = context;
    }

    /**
     * 根据colorId创建Divider
     * @param color  颜色id
     * @param size   大小
     * @param isNeedShowLast  是否需要最后一条divider
     * @return
     */
    public FlexibleDividerDecoration createDividerByColorId(int color, int size, boolean isNeedShowLast){
        return buildDividerItemDecoration(size, color, 0, 0, 0, isNeedShowLast);
    }

    /**
     * 根据drawableResId创建Divider
     * @param drawableResId  drawableId
     * @param size   大小
     * @param isNeedShowLast  是否需要最后一条divider
     * @return
     */
    public FlexibleDividerDecoration createDividerByDrawableResId(int drawableResId, int size, boolean isNeedShowLast){
        return buildDividerItemDecoration(size,0,drawableResId,0,0,isNeedShowLast);
    }

    /**
     * 根据colorId创建Divider
     * @param colorResId  颜色id
     * @param size   大小
     * @param leftMargin 左边距
     * @param rightMargin 右边距
     * @param isNeedShowLast  是否需要最后一条divider
     * @return
     */
    public FlexibleDividerDecoration createDividerColorResId(int colorResId, int size, int leftMargin, int rightMargin, boolean isNeedShowLast){
        return buildDividerItemDecoration(size,colorResId,0,leftMargin,rightMargin,isNeedShowLast);
    }


    /**
     * 根据drawableResId创建Divider
     * @param drawableResId  颜色id
     * @param size   大小
     * @param leftMargin 左边距
     * @param rightMargin 右边距
     * @param isNeedShowLast  是否需要最后一条divider
     * @return
     */
    public FlexibleDividerDecoration createDividerByDrawableResId(int drawableResId, int size, int leftMargin, int rightMargin, boolean isNeedShowLast){
        return buildDividerItemDecoration(size,0,drawableResId,leftMargin,rightMargin,isNeedShowLast);
    }

    protected  FlexibleDividerDecoration buildDividerItemDecoration(int size,  int colorResId, int drawableResId, int leftMargin, int rightMargin, boolean isShowLastDivider) {
        FlexibleDividerDecoration.Builder builder = createBuilder();
        if (size > 0)
            builder.size(size);
        if (colorResId > 0)
            builder.colorResId(colorResId);
        if (drawableResId > 0)
            builder.drawable(drawableResId);
        if (leftMargin > 0 && rightMargin > 0)
            builder.margin(leftMargin, rightMargin);
        if (isShowLastDivider)
            builder.showLastDivider();
        return builder.build();
    }

    protected abstract FlexibleDividerDecoration.Builder createBuilder();
}
