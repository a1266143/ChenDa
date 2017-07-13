package com.cdbbbsp.www.Activity.Main.Presenter;

import com.cdbbbsp.www.Activity.Main.View.IView;
import com.cdbbbsp.www.Entity.Event.Bean.AllCategoryBean;
import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-13:57
 */

public class MainPresenter {

    private IView view;
    public MainPresenter(IView view){
        this.view = view;
    }

    public void getAllCategory(){//获取所有分类
        //参数列表
        Map<String,String> params= new HashMap<>();
        MyUtils.getInstance().doGet(StaticCode.allCategoryUrl,params, new MyUtils.NetCallback() {
            @Override
            public void getData(String json) {
                AllCategoryBean bean = new Gson().fromJson(json,AllCategoryBean.class);
                view.getCategoryData(bean);
            }

            @Override
            public void getError(Exception e) {
                view.getAllCategoryNetError();
            }
        });
    }
}
