package com.jie.recyclerview.library.divider;

import android.content.Context;

/**
 * Created by liumingjie on 2016/6/7.
 */
public class VerticalDividerFactory extends AbstractDividerFactory{
    private static VerticalDividerFactory mVerticalDividerFactory;

    public synchronized static VerticalDividerFactory newInstance(Context context) {
        if(mVerticalDividerFactory == null){
            mVerticalDividerFactory = new VerticalDividerFactory(context);
        }
        return mVerticalDividerFactory;
    }

    private VerticalDividerFactory(Context context) {
        super(context.getApplicationContext());
    }

    @Override
    protected FlexibleDividerDecoration.Builder createBuilder() {
        return new VerticalDividerItemDecoration.Builder(mContext);
    }
}
