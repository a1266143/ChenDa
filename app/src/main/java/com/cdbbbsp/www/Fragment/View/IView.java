package com.cdbbbsp.www.Fragment.View;

import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-15:06
 */

public interface IView {
    void getFirstData(AllGoodsBean bean);
    void getFirstNetError();
    void loadMoreData(AllGoodsBean bean);
    void loadMoreError();//加载更多失败
}
