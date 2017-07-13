package com.cdbbbsp.www.Utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 工具类
 * 创建人 xiaojun
 * 创建时间 2017/7/13-13:49
 */

public class MyUtils {
    static MyUtils mMyUtils;

    private MyUtils() {
    }

    public static MyUtils getInstance() {
        if (mMyUtils == null)
            mMyUtils = new MyUtils();
        return mMyUtils;
    }

    //get请求
    public <T> void doGet(String url, Map<String, String> params, final NetCallback callback) {
        OkHttpUtils.get().params(params).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.getError(e);
            }

            @Override
            public void onResponse(String response, int id) {
                callback.getData(response);
            }
        });
    }

    //post请求
    public <T> void doPost(String url, Callback<T> callback, Map<String, String> params) {
       // OkHttpUtils.post().params(params).url(url).build().execute(callback);
    }

    public interface NetCallback {
        void getData(String json);

        void getError(Exception e);
    }
}
