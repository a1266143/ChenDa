package com.cdbbbsp.www.Fragment.Presenter;

import android.util.Log;

import com.cdbbbsp.www.Fragment.View.IView;
import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
import com.google.gson.Gson;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-15:06
 */

public class BaseFragmentPresenter {
    private IView iView;
    private RequestCall call;

    public BaseFragmentPresenter(IView iView){
        this.iView = iView;
    }

    public void getData(String categoryid,int page){//获取数据
        Map<String,String> params = new HashMap<>();
        params.put("categoryid",categoryid);
        params.put("start",""+page);
        call = MyUtils.getInstance().doGet(StaticCode.pageGoodsUrl,params, new MyUtils.NetCallback() {
            @Override
            public void getData(String json) {
                AllGoodsBean bean = new Gson().fromJson(json,AllGoodsBean.class);
                iView.getFirstData(bean,call);
            }

            @Override
            public void getError(Exception e) {
                iView.getFirstNetError();
            }
        });
    }

    public void loadMore(String categoryid,int page){//上拉加载更多
        Map<String,String> params = new HashMap<>();
        params.put("categoryid",categoryid);
        params.put("start",""+page);
        call = MyUtils.getInstance().doGet(StaticCode.pageGoodsUrl,params, new MyUtils.NetCallback(){
            @Override
            public void getData(String json) {
                AllGoodsBean bean = new Gson().fromJson(json,AllGoodsBean.class);
                iView.loadMoreData(bean,call);
            }

            @Override
            public void getError(Exception e) {
                iView.loadMoreError();
            }
        });
    }
}
