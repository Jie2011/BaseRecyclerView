package com.jie.recyclerview.library.divider;

import android.content.Context;

/**
 * Created by liumingjie on 2016/6/7.
 */
public class HorizontalDividerFactory extends AbstractDividerFactory{

    private static HorizontalDividerFactory mHorizontalDividerFactory;

    public synchronized static HorizontalDividerFactory newInstance(Context context) {
        if(mHorizontalDividerFactory == null){
            mHorizontalDividerFactory = new HorizontalDividerFactory(context);
        }
        return mHorizontalDividerFactory;
    }

    private HorizontalDividerFactory(Context context){
        super(context.getApplicationContext());
    }

    @Override
    protected FlexibleDividerDecoration.Builder createBuilder() {
        return new HorizontalDividerItemDecoration.Builder(mContext);
    }
}
