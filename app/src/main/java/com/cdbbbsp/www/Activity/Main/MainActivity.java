package com.cdbbbsp.www.Activity.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdbbbsp.www.Activity.Cart.CartActivity;
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
import com.cdbbbsp.www.Utils.FastBlurUtil;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
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
import jp.wasabeef.blurry.Blurry;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends SlidingActivity implements IView {

    public static Bitmap staticImage;
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
    private FrameLayout fm_shadow;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void menuBeClick(MenuEvent menuEvent) {//菜单按钮被点击
        menu.toggle();
    }

    @Subscribe
    public void cartBeClick(CartEvent cartEvent) {//购物车被点击
        if (StaticCode.staticList == null || StaticCode.staticList.size() == 0)
            Toast.makeText(this, "购物车还没有任何商品", Toast.LENGTH_SHORT).show();
        else
            startActivity(new Intent(this, CartActivity.class));
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
        StatusBarUtil.setColor(this, Color.parseColor("#3cba92"));
        setEventBus();
        setDagger();
        setButterKnife();
        setBehindContentView(slidingMenuView);
        findCurrentLayoutView();
        getAllCategory();//请求分类


        //Blurry.with(MainActivity.this).async().radius(100).from(getCapture(fm)).into(iv);

    }

    private ImageView iv;

    public void blur() {
        final FrameLayout fm = (FrameLayout) findViewById(R.id.fm);
        staticImage = FastBlurUtil.toBlur(getCapture(fm), 10);
        //iv.setImageBitmap(FastBlurUtil.toBlur(getCapture(fm),10));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Bitmap getCapture(View v) {
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null)
            return bitmap;
        else
            throw new NullPointerException("bitmap为null");
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
        iv = (ImageView) findViewById(R.id.iv);
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
        fm_shadow = (FrameLayout) findViewById(R.id.activity_main_fmshadow);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(10);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        fm_shadow.startAnimation(alphaAnimation);
        fm_shadow.startAnimation(alphaAnimation);
    }

    private void showFragment(int position) {

    }

    private void setFragment(String categoryid) {
        getFragmentManager().beginTransaction().replace(R.id.activity_main_content, BaseFragment.newInstance(categoryid)).commitAllowingStateLoss();

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
        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                if (fm_shadow.getAnimation()!=null)
                    fm_shadow.clearAnimation();
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    //设置动画持续时长
                    alphaAnimation.setDuration(300);
                    //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
                    alphaAnimation.setFillAfter(true);
                    fm_shadow.startAnimation(alphaAnimation);
            }
        });
        menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                if (fm_shadow.getAnimation()!=null){
                    fm_shadow.clearAnimation();
                }
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    //设置动画持续时长
                    alphaAnimation.setDuration(100);
                    //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
                    alphaAnimation.setFillAfter(true);
                    fm_shadow.startAnimation(alphaAnimation);
            }
        });
        if (bean != null && bean.getSuccess().equals("1")) {
            this.bean = bean;
            //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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

    private boolean isBack = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isBack == false){
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isBack = true;
                            Thread.sleep(2000);
                            isBack = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else{
                finish();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                },500);
            }
        }
        return true;
    }
}
