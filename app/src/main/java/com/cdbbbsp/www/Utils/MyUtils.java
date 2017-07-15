package com.cdbbbsp.www.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static Bitmap getCapture(View view) {
// 设置控件允许绘制缓存
        view.setDrawingCacheEnabled(true);
// 获取控件的绘制缓存（快照）
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static void setTransparentStatusBar(Activity activity) {
        //5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //4.4到5.0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //获取屏幕宽度或者高度
    public int getScreenWidthOrHeight(Context context, boolean isWidth) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        if (isWidth == true) {
            return width;
        } else
            return height;
    }

    //get请求
    public  void doGet(String url, Map<String, String> params, final NetCallback callback) {
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
    public  void doPost(String url, Map<String, String> params, final NetCallback callback) {
        // OkHttpUtils.post().params(params).url(url).build().execute(callback);
        OkHttpUtils.post().params(params).url(url).build().execute(new StringCallback() {
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

    public interface NetCallback {
        void getData(String json);

        void getError(Exception e);
    }
}
