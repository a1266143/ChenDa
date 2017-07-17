package com.cdbbbsp.www.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdbbbsp.www.Activity.Image.ImageActivity;
import com.cdbbbsp.www.Activity.Main.MainActivity;
import com.cdbbbsp.www.Entity.Event.Bean.AllGoodsBean;
import com.cdbbbsp.www.Fragment.Presenter.BaseFragmentPresenter;
import com.cdbbbsp.www.Fragment.View.IView;
import com.cdbbbsp.www.R;
import com.cdbbbsp.www.Utils.MyUtils;
import com.cdbbbsp.www.Utils.StaticCode;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-9:24
 */
public class BaseFragment extends Fragment implements IView {

    private String categoryId;
    private int currentPage = 0;
    private List<AllGoodsBean.GoodsBean> list;
    private BaseFragmentPresenter mPresenter;
    private CommonAdapter<AllGoodsBean.GoodsBean> adapter;
    private Handler handler = new Handler();

    public static final String ARGUMENT = "categoryid";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // mArgument = getActivity().getIntent().getStringExtra(ARGUMENT);
        Bundle bundle = getArguments();
        if (bundle != null){
            categoryId = bundle.getString(ARGUMENT);
            mPresenter = new BaseFragmentPresenter(this);
        }

    }

    /**
     * 传入需要的参数，设置给arguments
     * @param argument
     * @return
     */
    public static BaseFragment newInstance(String argument)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        BaseFragment contentFragment = new BaseFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @BindView(R.id.layout_basefragment_xRecyclerView)
    XRecyclerView mXRecyclerView;
    @BindView(R.id.layout_basefragment_netError)
    TextView tv_neterror;
    @OnClick(R.id.layout_basefragment_netError)
    void netError(){
        mPresenter.getData(categoryId, currentPage);
        tv_neterror.setVisibility(GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_basefragment, container, false);
        ButterKnife.bind(this, view);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getData(categoryId, currentPage);
            }
        },200);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        manager.setAutoMeasureEnabled(true);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData(categoryId, currentPage);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore(categoryId, currentPage++);
            }
        });

        return view;
    }

    private void setRecyclerView() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setAutoMeasureEnabled(true);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        setRecyclerViewAdapter2();
        mXRecyclerView.setLoadingMoreEnabled(true);
        mXRecyclerView.refreshComplete();
    }

    private void setRecyclerViewAdapter2(){
        mXRecyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.listitem_layout,parent,false);
            return new MyViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Log.e("执行了","执行了");
            final AllGoodsBean.GoodsBean bean = list.get(position);
            holder.tv_title.setText(bean.getTitle());
            Glide.with(getActivity()).load(StaticCode.imagePrefix + bean.getImgs().get(0).getPath()).placeholder(R.mipmap.logo).into(holder.iv);
            //点击加号
            holder.ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("点击了","点击了"+position);
                    StaticCode.staticList.add(bean);
                    notifyItemChanged(position+1, list.size());
                    //notifyDataSetChanged();
                }
            });
            //点击图片
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImageActivity.TAG, bean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                    ((MainActivity) getActivity()).blur();
                }
            });
            setNumber(bean,holder.tv_number);//设置数量
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
            if (payloads == null || payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                Log.e("晓军","1");
            } else {
                AllGoodsBean.GoodsBean bean = list.get(position);
                setNumber(bean,holder.tv_number);
                Log.e("晓军","2");
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    //设置数量
    private void setNumber(AllGoodsBean.GoodsBean goods,TextView number){
        int goodsNumber = 0;
        for (int i = 0; i < StaticCode.staticList.size(); i++) {
            AllGoodsBean.GoodsBean goodsBean = StaticCode.staticList.get(i);
            if (goodsBean.getGoodsid().equals(goods.getGoodsid())) {
                goodsNumber++;
            }
        }
        if (goodsNumber == 0) {
            number.setVisibility(GONE);
        } else {
            number.setVisibility(View.VISIBLE);
            number.setText(goodsNumber + "");
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv_title,tv_number;
        private ImageButton ib_add;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.listitem_layout_iv);
            tv_title = (TextView) itemView.findViewById(R.id.listitem_layout_tv);
            tv_number = (TextView) itemView.findViewById(R.id.listitem_layout_number);
            ib_add = (ImageButton) itemView.findViewById(R.id.listitem_layout_add);
        }
    }

    private void setRecycleAdapter(){
        mXRecyclerView.setAdapter(adapter = new CommonAdapter<AllGoodsBean.GoodsBean>(getActivity(), R.layout.listitem_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final AllGoodsBean.GoodsBean goods, final int position) {
                holder.setText(R.id.listitem_layout_tv, goods.getTitle());
                holder.setOnClickListener(R.id.listitem_layout_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StaticCode.staticList.add(goods);
                        notifyItemChanged(position, position);
                    }
                });
                holder.setOnClickListener(R.id.listitem_layout_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImageActivity.TAG, goods);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                        ((MainActivity) getActivity()).blur();
                    }
                });
                ImageView img = holder.getView(R.id.listitem_layout_iv);
                TextView number = holder.getView(R.id.listitem_layout_number);
                int goodsNumber = 0;
                for (int i = 0; i < StaticCode.staticList.size(); i++) {
                    AllGoodsBean.GoodsBean goodsBean = StaticCode.staticList.get(i);
                    if (goodsBean.getGoodsid().equals(goods.getGoodsid())) {
                        goodsNumber++;
                    }
                }
                if (goodsNumber == 0) {
                    number.setVisibility(GONE);
                } else {
                    number.setVisibility(View.VISIBLE);
                    number.setText(goodsNumber + "");
                }
                Log.e("地址", StaticCode.imagePrefix + goods.getImgs().get(0).getPath());
                Glide.with(getActivity()).load(StaticCode.imagePrefix + goods.getImgs().get(0).getPath()).placeholder(R.mipmap.ic_launcher).into(img);
            }

        });

        mXRecyclerView.getItemAnimator().setChangeDuration(0);
    }


    @Override
    public void getFirstData(final AllGoodsBean bean, RequestCall call) {
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bean != null && bean.getSuccess().equals("1")) {//如果数据获取成功
                        list = bean.getData();
                        setRecyclerView();
                    }
                }
            });
        }


    }

    @Override
    public void getFirstNetError() {
        Log.e("xiaojun", "网络错误");
        mXRecyclerView.refreshComplete();
        tv_neterror.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMoreData(AllGoodsBean bean,RequestCall call) {//加载更多数据
        if(getActivity()!=null){
            if(bean!=null||bean.getData().size()==0){
                Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                mXRecyclerView.loadMoreComplete();
                if(currentPage!=0)
                    currentPage--;
                return;
            }
            for (AllGoodsBean.GoodsBean goods : bean.getData()) {
                list.add(goods);
            }
            if (adapter != null)
                adapter.notifyDataSetChanged();
            mXRecyclerView.loadMoreComplete();
        }

    }

    @Override
    public void loadMoreError() {
        Toast.makeText(getActivity(), "网络错误,请稍后再试", Toast.LENGTH_SHORT).show();
        mXRecyclerView.loadMoreComplete();
    }
}
