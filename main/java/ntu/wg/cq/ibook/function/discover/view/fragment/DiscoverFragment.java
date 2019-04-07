package ntu.wg.cq.ibook.function.discover.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.discover.presenter.DiscoverPresenter;
import ntu.wg.cq.ibook.function.discover.view.activity.BaActivity;
import ntu.wg.cq.ibook.function.discover.view.activity.BookListActivity;
import ntu.wg.cq.ibook.function.discover.view.activity.QAGameActivity;
import ntu.wg.cq.ibook.function.discover.view.activity.RankActivity;
import ntu.wg.cq.ibook.function.discover.view.adapter.BookListAdapter;
import ntu.wg.cq.ibook.function.search.view.activity.BookInfoActivity;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetFragment;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/7.
 */

public class DiscoverFragment extends BaseNetFragment {
    private TextView tvXH;
    private TextView tvKH;
    private TextView tvWY;
    private TextView tvLS;
    private TextView tvXZ;
    private TextView tvKB;
    private TextView tvDS;
    private TextView tvQT;
    private TextView tvSB;
    private TextView tvSD;
    //private TextView tvGame_1;
    private TextView tvGame_2;
    private TextView tvChange;
    private RecyclerView rv;
    private BookListAdapter adapter;
    private GridLayoutManager manager;
    private List<Book> list = new ArrayList<>();
    private DiscoverPresenter presenter;
    private String token;
    @Override
    protected DiscoverPresenter getPresenter() {
        return new DiscoverPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initData() {
        presenter = getPresenter();
        token = getApp().getSetting(Constants.TOKEN);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvXH = (TextView) view.findViewById(R.id.tv_xh);
        tvKH = (TextView) view.findViewById(R.id.tv_kh);
        tvWY = (TextView) view.findViewById(R.id.tv_wy);
        tvLS = (TextView) view.findViewById(R.id.tv_ls);
        tvXZ = (TextView) view.findViewById(R.id.tv_xz);
        tvKB = (TextView) view.findViewById(R.id.tv_kb);
        tvDS = (TextView) view.findViewById(R.id.tv_ds);
        tvQT = (TextView) view.findViewById(R.id.tv_other);
        tvSB = (TextView) view.findViewById(R.id.tv_sb);
        tvSD = (TextView) view.findViewById(R.id.tv_sd);
        //tvGame_1 = (TextView) view.findViewById(R.id.tv_game1);
        tvGame_2 = (TextView) view.findViewById(R.id.tv_game2);
        tvChange = (TextView)view.findViewById(R.id.tv_change);
        rv = (RecyclerView)view.findViewById(R.id.rv_tj);
        tvXH.setOnClickListener(this);
        tvKH.setOnClickListener(this);
        tvWY.setOnClickListener(this);
        tvLS.setOnClickListener(this);
        tvXZ.setOnClickListener(this);
        tvKB.setOnClickListener(this);
        tvDS.setOnClickListener(this);
        tvQT.setOnClickListener(this);
        //tvGame_1.setOnClickListener(this);
        tvGame_2.setOnClickListener(this);
        tvSD.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvSB.setOnClickListener(this);
    }

    @Override
    protected void doChange() {
       // manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        manager = new GridLayoutManager(getActivity(),3);
        adapter = new BookListAdapter(list,getActivity());
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) v.getTag();
                getApp().setItem(list.get(tag));
                startActivity(BookInfoActivity.class);
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        getCommend();
    }

    @Override
    protected void click(View v) {
        switch (v.getId()){
            case R.id.tv_xh:
                toRankActivity(1);
                break;
            case R.id.tv_kh:
                toRankActivity(2);
                break;
            case R.id.tv_wy:
                toRankActivity(3);
                break;
            case R.id.tv_xz:
                toRankActivity(5);
                break;
            case R.id.tv_ls:
                toRankActivity(4);
                break;
            case R.id.tv_ds:
                toRankActivity(6);
                break;
            case R.id.tv_kb:
                toRankActivity(7);
                break;
            case R.id.tv_other:
                toRankActivity(8);
                break;
            case R.id.tv_game2:
                startActivity(QAGameActivity.class);
                break;
            case R.id.tv_sd:
                startActivity(BookListActivity.class);
                break;
            case R.id.tv_change:
                getCommend();
                break;
            case R.id.tv_sb:
                startActivity(BaActivity.class);
                break;
        }
    }

    private void toRankActivity(int tag){
        Bundle bundle = new Bundle();
        bundle.putInt("tag",tag);
        startActivity(RankActivity.class,bundle);
    }

    private void getCommend(){
        if (!StringUtils.haveEmpty(token)){
            presenter.getCommend(getActivity(), token,new IResult() {
                @Override
                public void doChange(Object result) {
                    ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                    if (myResponse.getState()== Constants.SUCCESS){
                        list.clear();
                        List<Book> books = JSON.parseArray(myResponse.getMessage(), Book.class);
                        list.addAll(books);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }else {
            tvChange.setVisibility(View.GONE);
        }
    }
}
