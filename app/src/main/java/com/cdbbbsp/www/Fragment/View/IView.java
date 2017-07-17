package com.cdbbbsp.www.Fragment.View;

import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.zhy.http.okhttp.request.RequestCall;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-15:06
 */

public interface IView {
    void getFirstData(AllGoodsBean bean, RequestCall call);
    void getFirstNetError();
    void loadMoreData(AllGoodsBean bean,RequestCall call);
    void loadMoreError();//加载更多失败
}
