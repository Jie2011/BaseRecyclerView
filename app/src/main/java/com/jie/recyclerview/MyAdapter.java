package com.jie.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jie.recyclerview.library.adapter.BaseRecyclerViewAdapter;
import com.jie.recyclerview.library.adapter.holder.BaseRecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by liumingjie on 15/11/26.
 */
public class MyAdapter extends BaseRecyclerViewAdapter<String> {

    public MyAdapter(Context context,ArrayList<String> datas) {
        super(context,datas);
    }
    //创建新View，被LayoutManager所调用
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(viewGroup,R.layout.item);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends BaseRecyclerViewHolder<String> {
        public TextView mTextView;

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
        }

        @Override
        public void initView() {
            mTextView = $(R.id.text);
        }

        @Override
        public void setData(String data) {
            mTextView.setText(data);
        }
    }
}
