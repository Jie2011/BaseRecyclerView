#  自定义recyclerview下拉刷新开源框架<br />
1，封装下拉刷新recyclerView控件和adapter层<br />
2，封装ItemDecoration层，参考了（https://github.com/yqritc/RecyclerView-FlexibleDivider）<br />

使用方法：

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
