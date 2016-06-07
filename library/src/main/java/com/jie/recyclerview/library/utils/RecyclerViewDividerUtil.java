package com.jie.recyclerview.library.utils;

import android.view.View;

import com.jie.recyclerview.library.divider.HorizontalDividerItemDecoration;
import com.jie.recyclerview.library.divider.VerticalDividerItemDecoration;
import com.jie.recyclerview.library.view.CustomRecyclerView;

/**
 * Created by liumingjie on 2016/06/07.
 */
public class RecyclerViewDividerUtil {

    /**
     * 设置XRecyclerView的横向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param color          颜色值
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示部分割线
     */
    public static void setHorDividerColor(CustomRecyclerView recyclerView, int color, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildHorDividerItemDecoration(recyclerView, size, color, -1, -1, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的横向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param colorResId     颜色资源ID
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setHorDividerColorResId(CustomRecyclerView recyclerView, int colorResId, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildHorDividerItemDecoration(recyclerView, size, -1, colorResId, -1, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的横向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param colorResId     颜色资源ID
     * @param size           分割线尺寸
     * @param leftMargin     左间距
     * @param rightMargin    右间距
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setHorDividerColorResId(CustomRecyclerView recyclerView, int colorResId, int size, int leftMargin, int rightMargin, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildHorDividerItemDecoration(recyclerView, size, -1, colorResId, -1, leftMargin, rightMargin, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的横向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param drawableResId  图片资源ID
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setHorDividerDrawableResId(CustomRecyclerView recyclerView, int drawableResId, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildHorDividerItemDecoration(recyclerView, size, -1, -1, drawableResId, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的纵向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param color          颜色值
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setVerDividerColor(CustomRecyclerView recyclerView, int color, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildVerDividerItemDecoration(recyclerView, size, color, -1, -1, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的纵向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param colorResId     颜色资源ID
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setVerDividerColorResId(CustomRecyclerView recyclerView, int colorResId, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildVerDividerItemDecoration(recyclerView, size, -1, colorResId, -1, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的纵向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param drawableResId  图片资源ID
     * @param size           分割线尺寸
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setVerDividerDrawableResId(CustomRecyclerView recyclerView, int drawableResId, int size, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildVerDividerItemDecoration(recyclerView, size, -1, -1, drawableResId, -1, -1, isNeedShowLast));
    }

    /**
     * 设置XRecyclerView的纵向分割线
     *
     * @param recyclerView   对应的XRecyclerView
     * @param colorResId     颜色资源ID
     * @param size           分割线尺寸
     * @param leftMargin     左间距
     * @param rightMargin    右间距
     * @param isNeedShowLast 是否需要显示底部分割线
     */
    public static void setVerDividerColorResId(CustomRecyclerView recyclerView, int colorResId, int size, int leftMargin, int rightMargin, boolean isNeedShowLast) {
        if (recyclerView == null)
            return;
        recyclerView.addItemDecoration(buildVerDividerItemDecoration(recyclerView, size, -1, colorResId, -1, leftMargin, rightMargin, isNeedShowLast));
    }

    private static HorizontalDividerItemDecoration buildHorDividerItemDecoration(View v, int size, int color, int colorResId, int drawableResId, int leftMargin, int rightMargin, boolean isShowLastDivider) {
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(v.getContext());
        if (size >= 0)
            builder.size(size);
        if (color > 0)
            builder.color(color);
        if (colorResId > 0)
            builder.colorResId(colorResId);
        if (drawableResId > 0)
            builder.drawable(drawableResId);
        if (leftMargin >= 0 && rightMargin >= 0)
            builder.margin(leftMargin, rightMargin);
        if (isShowLastDivider)
            builder.showLastDivider();
        return builder.build();

    }

    private static VerticalDividerItemDecoration buildVerDividerItemDecoration(View v, int size, int color, int colorResId, int drawableResId, int leftMargin, int rightMargin, boolean isShowLastDivider) {
        VerticalDividerItemDecoration.Builder builder = new VerticalDividerItemDecoration.Builder(v.getContext());
        if (size > 0)
            builder.size(size);
        if (color > 0)
            builder.color(color);
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

}
