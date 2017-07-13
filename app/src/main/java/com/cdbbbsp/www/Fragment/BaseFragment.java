package com.cdbbbsp.www.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.Fragment.Presenter.BaseFragmentPresenter;
import com.cdbbbsp.www.Fragment.View.IView;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-9:24
 */
public class BaseFragment extends Fragment implements IView{

    private String categoryId;
    private int currentPage = 0;
    private List<AllGoodsBean.GoodsBean> list;
    private BaseFragmentPresenter mPresenter;
    public BaseFragment(String categoryId){
        this.categoryId = categoryId;
        mPresenter = new BaseFragmentPresenter(this);
        Log.e("xiaojun","调用了BaseFragment构造方法");
    }

    @BindView(R.id.layout_basefragment_xRecyclerView)
    XRecyclerView mXRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_basefragment,container,false);
        ButterKnife.bind(this,view);
       mPresenter.getData(categoryId,currentPage);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData(categoryId,currentPage);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore(categoryId,currentPage++);
            }
        });
        //mPresenter.
        //setRecyclerView();
        return view;
    }


    private void getData(){

    }

    private void setRecyclerView(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        manager.setAutoMeasureEnabled(true);
        StaggeredGridLayoutManager manager1 = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(manager1);
        mXRecyclerView.setAdapter(new CommonAdapter<AllGoodsBean.GoodsBean>(getActivity(),R.layout.listitem_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final AllGoodsBean.GoodsBean goods, final int position) {
                holder.setText(R.id.listitem_layout_tv,list.get(0).getTitle());
                holder.setOnClickListener(R.id.listitem_layout_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Toast.makeText(getActivity(),"你点击了添加按钮",Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                    }
                });
                ImageView img = holder.getView(R.id.listitem_layout_iv);
                Log.e("地址","https://test.buoou.com/upload"+goods.getImgs().get(0).getPath());
                Glide.with(getActivity()).load("https://test.buoou.com/upload"+goods.getImgs().get(0).getPath()).placeholder(R.mipmap.ic_launcher).into(img);
            }

        });
        mXRecyclerView.refreshComplete();
    }


    @Override
    public void getFirstData(AllGoodsBean bean) {
        if(bean.getSuccess().equals("1")){//如果数据获取成功
            list = bean.getData();
            setRecyclerView();
        }
    }

    @Override
    public void getFirstNetError() {
        Log.e("xiaojun","网络错误");
    }

    @Override
    public void loadMoreData(AllGoodsBean bean) {//加载更多数据

    }

    @Override
    public void loadMoreError() {
        if (currentPage!=0)
            currentPage--;
        Toast.makeText(getActivity(),"网络错误,请稍后再试",Toast.LENGTH_SHORT).show();
    }
}
