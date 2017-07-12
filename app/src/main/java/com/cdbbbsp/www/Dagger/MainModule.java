package com.cdbbbsp.www.Dagger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cdbbbsp.www.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 李晓军 on 2017/7/12.
 */
@Module
public class MainModule {

    private SlidingMenu menu;
    private Context context;
    public MainModule(SlidingMenu menu, Context context){
        this.menu = menu;
        this.context = context;
    }

    @Singleton
    @Provides
    public SlidingMenu providerSlidingMenu(){
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidth(10);
        //menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffset(200);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        return menu;
    }

    @Singleton
    @Provides
    public View providerSlidingMenuView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slidingMenuView = inflater.inflate(R.layout.sliding_left_layout,null,false);
        return slidingMenuView;
    }

}
