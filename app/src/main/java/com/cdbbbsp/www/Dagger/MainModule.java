package com.cdbbbsp.www.Dagger;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdbbbsp.www.Activity.Main.MainActivity;
import com.cdbbbsp.www.Activity.Main.Presenter.MainPresenter;
import com.cdbbbsp.www.Entity.Event.Bean.AllCategoryBean;
import com.cdbbbsp.www.Entity.Event.CartEvent;
import com.cdbbbsp.www.Entity.Event.MenuEvent;
import com.cdbbbsp.www.Entity.Event.Refresh;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidth(10);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffset(MyUtils.getInstance().dip2px(context,200));
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

    @Singleton
    @Provides
    public MenuEvent providerMenuEvent(){
        return new MenuEvent();
    }

    @Singleton
    @Provides
    public CartEvent providerCartEvent(){
        return new CartEvent();
    }

    @Singleton
    @Provides
    public Refresh providerRefresh(){
        return new Refresh();
    }

    @Singleton
    @Provides
    public View.OnClickListener providerOnClickListener(final MenuEvent menu, final CartEvent cartEvent, final Refresh refresh){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //点击了菜单按钮
                    case R.id.activity_main_menu:
                        EventBus.getDefault().post(menu);
                        break;
                    case R.id.activity_main_ShoppingCart:
                        EventBus.getDefault().post(cartEvent);
                        break;
                    case R.id.activity_main_tvNeterror:
                        EventBus.getDefault().post(refresh);
                        break;
                }
            }
        };
    }

    @Singleton
    @Provides
    public MainPresenter providerMainPresenter(){
        return new MainPresenter((MainActivity)context);
    }

    public static class SlidingBaseAdapter extends BaseAdapter{

        private AllCategoryBean bean;
        private Context context;
        private int selectedPosition;
        public SlidingBaseAdapter(Context context,AllCategoryBean bean){
            this.bean = bean;
            this.context = context;
        }

        public void setSelecedPosition(int index){
            this.selectedPosition = index;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return bean.getData().size();
        }

        @Override
        public Object getItem(int position) {
            return bean.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sliding_list_layout,parent,false);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.sliding_list_layout_tv);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.sliding_list_layout_iv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(position == selectedPosition){
                viewHolder.iv.setVisibility(View.VISIBLE);
            }else{
                viewHolder.iv.setVisibility(View.INVISIBLE);
            }
            viewHolder.tv.setText(bean.getData().get(position).getCategoryname());
            return convertView;
        }

        static class ViewHolder{
            TextView tv;
            ImageView iv;
        }
    }

}
