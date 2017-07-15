package com.cdbbbsp.www.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cdbbbsp.www.Activity.Cart.CartActivity;
import com.cdbbbsp.www.Entity.Event.Bean.CommitBean;
import com.cdbbbsp.www.Entity.Event.FinishEvent;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.activity_info_edtName)
    EditText edt_name;
    @BindView(R.id.activity_info_edtAdd)
    EditText edt_addr;
    @BindView(R.id.activity_info_edtPhone)
    EditText edt_phone;

    @OnClick(R.id.activity_info_back)
    void back() {
        finish();
    }

    @OnClick(R.id.activity_info_commit)
    void commit() {
        String addr = edt_addr.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        Log.e("xiaojun", "地址：" + addr + " 姓名：" + name + " 电话：" + phone);
        if (TextUtils.isEmpty(addr) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "信息不全，请确认后再提交", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!MyUtils.isChinaPhoneLegal(phone)) {
            Toast.makeText(this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        commitGoods(name, phone, addr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTran();
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

    }

    private void commitGoods(String name, String phone, String addr) {
        final AlertDialog dialog = new AlertDialog.Builder(this)//设置对话框标题
                .setMessage("正在提交信息，请稍后！")
                .setCancelable(false)
                .show();

        MyUtils.getInstance().doPost(StaticCode.commitUrl, getParams(name, phone, addr),
                new MyUtils.NetCallback() {
                    @Override
                    public void getData(String json) {
                        Log.e("xiaojun", "获取到数据:" + json);
                        CommitBean bean = new Gson().fromJson(json, CommitBean.class);
                        if (bean.getSuccess().equals("1")) {
                            startActivity(new Intent(InfoActivity.this, SuccessActivity.class));
                            EventBus.getDefault().post(new FinishEvent());
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(InfoActivity.this, "提交失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void getError(Exception e) {
                        Log.e("xiaojun", "网络错误");
                        Toast.makeText(InfoActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    private Map<String, String> getParams(String name, String phone, String address) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("address", address);
        //添加商品
        String goodsids = new Gson().toJson(CartActivity.realList);
        String countids = new Gson().toJson(CartActivity.numberList);
        params.put("goodsids[]", "fdsf");
        params.put("countids[]", countids);
        Log.e("goodsids", goodsids);
        Log.e("countids", countids);
        return params;
    }


    private void setTran() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //如果android版本是5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
