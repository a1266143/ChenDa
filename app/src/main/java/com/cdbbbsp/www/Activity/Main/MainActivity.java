package com.cdbbbsp.www.Activity.Main;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.cdbbbsp.www.Fragment.DfrFragment;
import com.cdbbbsp.www.Fragment.JycFragment;
import com.cdbbbsp.www.Fragment.LjjFragment;
import com.cdbbbsp.www.Fragment.SclsFragment;
import com.cdbbbsp.www.Fragment.YsjFragment;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.StaticCode;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public void menuBeClick(MenuEvent menuEvent){//菜单按钮被点击
        menu.toggle();
    }

    @Subscribe
    public void cartBeClick(CartEvent cartEvent){//购物车被点击
        Toast.makeText(this,"你点击了购物车",Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void refresh(Refresh refresh){//重新获取分类列表被点击
        tv_neterror.setVisibility(GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        mPresenter.getAllCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setEventBus();
        setDagger();
        setButterKnife();
        setBehindContentView(slidingMenuView);
        findCurrentLayoutView();
        getAllCategory();//请求分类
    }

    private void getAllCategory(){
        mPresenter.getAllCategory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void setEventBus(){
        EventBus.getDefault().register(this);
    }

    private void setButterKnife(){
        ButterKnife.bind(this,slidingMenuView);
    }

    private void setDagger(){
        DaggerMainComponent.builder().mainModule(new MainModule(getSlidingMenu(),this)).build().inject(this);
    }

    private void findCurrentLayoutView(){
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

    private void showFragment(int position){

    }

    private void setFragment(){
        for(int i=0;i<bean.getData().size();i++){
            getFragmentManager().beginTransaction().add(R.id.activity_main_content,new BaseFragment(bean.getData().get(i).getCategoryid())).commitAllowingStateLoss();
        }

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
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        if(bean!=null&&bean.getSuccess().equals("1")){
            this.bean = bean;
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            loadingLayout.setVisibility(GONE);
            List<AllCategoryBean.CategoryBean> list = bean.getData();
            if(list.size()!=0){
                setFragment();
            }
            slidingListView.setAdapter(new MainModule.SlidingBaseAdapter(this,bean));
            slidingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (adapter!=null){
                        adapter.setSelecedPosition(position);
                        menu.toggle();
                        if(MainActivity.this.bean!=null){
                            setFragment();
                        }
                    }
                }
            });

        }

    }
}
