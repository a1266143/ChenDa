package com.cdbbbsp.www.Activity.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdbbbsp.www.Activity.Main.Presenter.MainPresenter;
import com.cdbbbsp.www.Activity.Main.View.IView;
import com.cdbbbsp.www.Dagger.DaggerMainComponent;
import com.cdbbbsp.www.Dagger.MainModule;
import com.cdbbbsp.www.Entity.Event.Bean.AllCategoryBean;
import com.cdbbbsp.www.Entity.Event.CartEvent;
import com.cdbbbsp.www.Entity.Event.MenuEvent;
import com.cdbbbsp.www.Entity.Event.Refresh;
import com.cdbbbsp.www.Fragment.BaseFragment;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends SlidingActivity implements IView {

    @Inject
    SlidingMenu menu;
    @Inject
    View slidingMenuView;
    @Inject
    View.OnClickListener listener;
    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.sliding_left_layout_listview)
    ListView slidingListView;

    private ImageView iv_menu;
    private LinearLayout loadingLayout;
    private TextView tv_neterror;
    private MainModule.SlidingBaseAdapter adapter;
    private AllCategoryBean bean;
    private int currentFragment;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void menuBeClick(MenuEvent menuEvent) {//菜单按钮被点击
        menu.toggle();
    }

    @Subscribe
    public void cartBeClick(CartEvent cartEvent) {//购物车被点击
        Toast.makeText(this, "你点击了购物车", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,CartActivity.class));
    }

    @Subscribe
    public void refresh(Refresh refresh) {//重新获取分类列表被点击
        tv_neterror.setVisibility(GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        mPresenter.getAllCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTransparent(this);
        setEventBus();
        setDagger();
        setButterKnife();
        setBehindContentView(slidingMenuView);
        findCurrentLayoutView();
        getAllCategory();//请求分类

    }

    private void getAllCategory() {
        mPresenter.getAllCategory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void setEventBus() {
        EventBus.getDefault().register(this);
    }

    private void setButterKnife() {
        ButterKnife.bind(this, slidingMenuView);
    }

    private void setDagger() {
        DaggerMainComponent.builder().mainModule(new MainModule(getSlidingMenu(), this)).build().inject(this);
    }

    private void findCurrentLayoutView() {
        iv_menu = (ImageView) findViewById(R.id.activity_main_menu);
        loadingLayout = (LinearLayout) findViewById(R.id.activity_main_loadinglayout);
        tv_neterror = (TextView) findViewById(R.id.activity_main_tvNeterror);
        iv_menu.setOnClickListener(listener);
        findViewById(R.id.activity_main_ShoppingCart).setOnClickListener(listener);
        tv_neterror.setOnClickListener(listener);
        slidingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFragment(position);
            }
        });
    }

    private void showFragment(int position) {

    }

    private void setFragment(String categoryid) {
        getFragmentManager().beginTransaction().replace(R.id.activity_main_content, new BaseFragment(categoryid)).commitAllowingStateLoss();

    }

    @Override
    public void getAllCategoryNetError() {//获取所有类别失败
        tv_neterror.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(GONE);
    }

    @Override
    public void getCategoryData(final AllCategoryBean bean) {//获取到了所有类别
        iv_menu.setEnabled(true);
        loadingLayout.setVisibility(GONE);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        if (bean != null && bean.getSuccess().equals("1")) {
            this.bean = bean;
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            loadingLayout.setVisibility(GONE);
            List<AllCategoryBean.CategoryBean> list = bean.getData();
            if (list.size() != 0) {
                setFragment(bean.getData().get(0).getCategoryid());
            }
            adapter = new MainModule.SlidingBaseAdapter(this, bean);
            slidingListView.setAdapter(adapter);
            slidingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (adapter != null) {
                        adapter.setSelecedPosition(position);
                        menu.toggle();
                        if (MainActivity.this.bean != null) {
                            setFragment(bean.getData().get(position).getCategoryid());
                        }
                    }
                }
            });

        }

    }

    private void setTran(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //如果android版本是5.0及以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }



}
