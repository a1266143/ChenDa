package com.cdbbbsp.www.Activity.Image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cdbbbsp.www.Activity.Main.MainActivity;
import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

public class ImageActivity extends Activity {
    public static final String TAG = "tag";
    private AllGoodsBean.GoodsBean currentBean;
    private List<CardView> list;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity_image_ll)
    LinearLayout ll;
    @BindView(R.id.activity_image_re)
    RelativeLayout re;
    @BindView(R.id.activity_image_backimg)
    ImageView backImage;
    @OnClick(R.id.activity_image_backback)
    void backback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        if (MainActivity.staticImage!=null)
            backImage.setImageBitmap(MainActivity.staticImage);
        //获取传送过来的Bean
        currentBean = (AllGoodsBean.GoodsBean) getIntent().getExtras().getSerializable(TAG);
        setList();
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                CardView cardView = list.get(position);
                final ImageView imgview = (ImageView) cardView.getChildAt(0);
                Glide.with(ImageActivity.this).load(StaticCode.imagePrefix + currentBean.getImgs().get(position).getPath())
                        .into(imgview);
                container.addView(cardView);
                addIndicator(0);
                return cardView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.setPageMargin(MyUtils.getInstance().dip2px(this, 48));
        viewPager.setOffscreenPageLimit(list.size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addIndicator(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addIndicator(int position) {
        //添加indicator
        List<ImageView> indicatorList = new ArrayList<ImageView>();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(ImageActivity.this);

            if (position == i)
                imageView.setImageResource(R.drawable.ic_page_indicator_focused);
            else
                imageView.setImageResource(R.drawable.ic_page_indicator);
            indicatorList.add(imageView);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MyUtils.dip2px(ImageActivity.this, 8), MyUtils.dip2px(ImageActivity.this, 8));
        params.leftMargin = MyUtils.dip2px(ImageActivity.this, 4);
        params.rightMargin = MyUtils.dip2px(ImageActivity.this, 4);
        params.setLayoutDirection(LinearLayout.HORIZONTAL);
        ll.removeAllViews();
        for (int i = 0; i < indicatorList.size(); i++) {
            ll.addView(indicatorList.get(i), params);
        }
    }

    private void setList() {
        list = new ArrayList<>();
        for (int i = 0; i < currentBean.getImgs().size(); i++) {
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(viewPager.getLayoutParams());
            cardView.setRadius(MyUtils.dip2px(this, 10));
            ImageView img = new ImageView(this);
            img.setAdjustViewBounds(true);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            cardView.addView(img);
            list.add(cardView);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold,R.anim.fadeout);
    }
}
