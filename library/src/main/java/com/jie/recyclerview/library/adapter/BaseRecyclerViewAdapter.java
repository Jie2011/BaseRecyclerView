package com.jie.recyclerview.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jie.recyclerview.library.adapter.holder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by liumingjie on 2016/6/6.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context mContext;
    protected List<T> mList;

    public BaseRecyclerViewAdapter(){

    }

    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this(context);
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public abstract BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position){
        if(mList != null) {
            holder.setData(mList.get(position));
        }
    }

}
