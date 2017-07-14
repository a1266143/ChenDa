package com.cdbbbsp.www.Activity.Main;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import butterknife.OnClick;

/**
 * 购物车Activity
 */
public class CartActivity extends AppCompatActivity {

    @BindView(R.id.activity_cart_xRecyclerview)
    XRecyclerView recyclerView;
    @OnClick(R.id.activity_cart_back)
    void back(){
        finish();
    }
    @OnClick(R.id.actvity_cart_next)
    void next(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTran();
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
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new CommonAdapter<AllGoodsBean.GoodsBean>(this,R.layout.listitem_cart, realList) {
            @Override
            protected void convert(ViewHolder holder, AllGoodsBean.GoodsBean goodsBean, int position) {
                holder.setText(R.id.listitem_cart_title,goodsBean.getTitle());
                holder.setText(R.id.listitem_cart_number,""+numberList.get(position-1));
            }
        });
        recyclerView.setPullRefreshEnabled(false);
    }



    private void setTran(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //如果android版本是5.0及以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置华为等虚拟按键bar的颜色
            window.setNavigationBarColor(Color.parseColor("#50000000"));
        }
        //如果版本是android4.4
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
