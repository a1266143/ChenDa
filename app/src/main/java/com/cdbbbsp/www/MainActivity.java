package com.cdbbbsp.www;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cdbbbsp.www.Dagger.DaggerMainComponent;
import com.cdbbbsp.www.Dagger.MainModule;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends SlidingActivity {

    @Inject
    SlidingMenu menu;
    @Inject
    View slidingMenuView;
    @OnClick(R.id.sliding_left_layout_lajiaojiang)
    void lajiao(){
        Toast.makeText(this,"你点击了辣椒酱",Toast.LENGTH_SHORT).show();
        menu.toggle();
    }
    @OnClick(R.id.sliding_left_layout_jiangyancai)
    void jiangyancai(){

    }
    @OnClick(R.id.sliding_left_layout_doufuru)
    void doufuru(){

    }
    @OnClick(R.id.sliding_left_layout_shuichanfood)
    void shuichanfood(){

    }
    @OnClick(R.id.sliding_left_layout_yeshengjun)
    void yeshengjun(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDagger();
        setButterKnife();
        setBehindContentView(slidingMenuView);
    }

    private void setButterKnife(){
        ButterKnife.bind(this,slidingMenuView);
    }

    private void setDagger(){
        DaggerMainComponent.builder().mainModule(new MainModule(getSlidingMenu(),this)).build().inject(this);
    }
}
