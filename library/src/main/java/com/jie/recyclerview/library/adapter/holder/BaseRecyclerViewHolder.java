package com.jie.recyclerview.library.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liumingjie on 2016/6/7.
 */
public abstract class BaseRecyclerViewHolder<M> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    public BaseRecyclerViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        initView();
    }

    public abstract void setData(M data);

    public abstract void initView();

    protected <T extends View> T $(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

}