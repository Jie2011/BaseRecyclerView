#  自定义recyclerview下拉刷新开源框架<br />
1，封装下拉刷新recyclerView控件和adapter层<br />
   （1）下拉刷新可以自定义刷新的icon <br />
   （2）可以添加多个headerview和footerview <br />
   （3）封装了adapter层，adapter继承BaseRecyclerViewAdapter<T>，T为泛型列表展示的数据model <br />
   
2，封装ItemDecoration层，参考了（https://github.com/yqritc/RecyclerView-FlexibleDivider），可以自己显示的调用FlexibleDividerDecoration    和VerticalDividerItemDecoration，这里利用工厂模式只是封装了简单的<br />
   （1）AbstractDividerFactory为抽象类封装了根据颜色和drawable绘制divider <br />
   （2）HorizontalDividerFactory绘制横向的divider <br />
   （3）VerticalDividerFactory绘制垂直方向的divider <br />
    

3，recycler使用方法：

        mRecyclerView = (CustomRecyclerView)this.findViewById(R.id.recyclerview);   //自定义recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);                                //设置LayoutManager

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);    //设置下拉刷新icon的方式
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);         //设加载更多的方式
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);              //设置下拉刷新的icon
        //添加横向的divider
        mRecyclerView.addItemDecoration(HorizontalDividerFactory.newInstance(this).createDividerByColorId(R.color.colorAccent, 1, false));
        View header =   LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        //添加headerview
        mRecyclerView.addHeaderView(header);

        /**
         * 设置下拉刷新和加载更多数据的接口回调
         */
        mRecyclerView.setLoadingListener(new CustomRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime ++;
                times = 0;
                new Handler().postDelayed(new Runnable(){
                    public void run() {

                        listData.clear();
                        for(int i = 0; i < 15 ;i++){
                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if(times < 2){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mRecyclerView.loadMoreComplete();
                            for(int i = 0; i < 15 ;i++){
                                listData.add("item" + (i + listData.size()) );
                            }
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.refreshComplete();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                }
                times ++;
            }
        });
        

     
