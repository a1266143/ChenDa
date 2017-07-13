package com.cdbbbsp.www.Activity.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ListView;

import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.R;
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
 * 购物车Activity
 */
public class CartActivity extends AppCompatActivity {

    @BindView(R.id.activity_cart_xRecyclerview)
    XRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        Map<String,String> maps = new HashMap<>();
        final List<AllGoodsBean.GoodsBean> realList = new ArrayList<>();
        final List<Integer> numberList = new ArrayList<>();
        for(int i=0;i<StaticCode.staticList.size();i++){
            AllGoodsBean.GoodsBean bean = StaticCode.staticList.get(i);
            maps.put(bean.getTitle(),bean.getGoodsid());
        }
        for (String key : maps.keySet()) {
            Log.e("xiaojun","key:"+key+"  value:"+maps.get(key));
            AllGoodsBean.GoodsBean bean = new AllGoodsBean.GoodsBean();
            bean.setTitle(key);
            bean.setGoodsid(maps.get(key));
            realList.add(bean);
        }
        for(int i=0;i<realList.size();i++){
            AllGoodsBean.GoodsBean bean = realList.get(i);
            int realNumber = 0;
            for(int a=0;a<StaticCode.staticList.size();a++){
                AllGoodsBean.GoodsBean newBean = StaticCode.staticList.get(a);
                if(bean.getGoodsid().equals(newBean.getGoodsid())){
                    realNumber++;
                }
            }
            numberList.add(realNumber);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        recyclerView.setAdapter(new CommonAdapter<AllGoodsBean.GoodsBean>(this,R.layout.listitem_cart, realList) {
            @Override
            protected void convert(ViewHolder holder, AllGoodsBean.GoodsBean goodsBean, int position) {
                holder.setText(R.id.listitem_cart_title,realList.get(position-1).getTitle());
                holder.setText(R.id.listitem_cart_number,"X"+numberList.get(position-1));
            }
        });
    }
}
