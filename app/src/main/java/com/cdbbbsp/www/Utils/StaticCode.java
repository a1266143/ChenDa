package com.cdbbbsp.www.Utils;

import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-13:40
 */

public class StaticCode {
    public static Map<String, String> statusCodeMap;
    public static List<AllGoodsBean.GoodsBean> staticList ;
    static {
        statusCodeMap = new HashMap();
        statusCodeMap.put("0", "请求失败");
        statusCodeMap.put("1", "请求成功");
        statusCodeMap.put("2", "分类不存在");
        statusCodeMap.put("404", "不存在的URL");
        statusCodeMap.put("405", "参数错误");
        staticList = new ArrayList<>();
    }


    private static final String baseUrl = "https://test.buoou.com";
    //所有分类
    public static final String allCategoryUrl = baseUrl + "/cdagriculture/category/allCategory.do";
    //分类下的具体商品url
    public static final String pageGoodsUrl = baseUrl + "/cdagriculture/goods/pageGoods.do";

}
