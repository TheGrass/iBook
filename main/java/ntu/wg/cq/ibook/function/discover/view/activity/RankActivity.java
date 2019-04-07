package ntu.wg.cq.ibook.function.discover.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.discover.presenter.DiscoverPresenter;
import ntu.wg.cq.ibook.function.search.view.activity.BookInfoActivity;
import ntu.wg.cq.ibook.function.search.view.adapter.BookListAdapter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;

/**
 * Created by C_Q on 2018/3/20.
 */

public class RankActivity extends BaseNetActivity {
    private QMUITopBar topBar;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private Button btnBack;

    private int tag;
    private String title;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverPresenter presenter;
    private BookListAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    @Override
    protected DiscoverPresenter getPresenter() {
        return new DiscoverPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initView() {
        topBar = findView(R.id.top_bar);
        topBar.setTitle(title);
        btnBack = topBar.addLeftTextButton("返回",R.layout.empty_button);
        btnBack.setTextColor(getColorById(R.color.text_color_3));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findView(R.id.rv);
        refreshLayout = findView(R.id.srf);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getInt("tag");
        title = Constants.BOOK_CLAZZ[tag-1];
        presenter = getPresenter();
    }

    @Override
    protected boolean allowFullScreen() {
        return false;
    }

    @Override
    protected boolean allowScreenRoate() {
        return false;
    }

    @Override
    public void doBusiness() {
        loadingDialog(true);
        adapter = new BookListAdapter(bookList,this);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos =(Integer) v.getTag();
                getApp().setItem(bookList.get(pos));
                startActivity(BookInfoActivity.class);
            }
        });
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                doRefresh(false,refreshlayout);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                doRefresh(true,refreshlayout);
            }
        });
        doRefresh(true,null);
    }

    @Override
    public void click(View v) {

    }
    private void doRefresh(final boolean isRefresh, final RefreshLayout refreshlayout){
        presenter.getRank(this,tag,isRefresh, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if(myResponse.getState()== Constants.SUCCESS){
                    if(null!=refreshlayout){
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }
                    List<Book> books = JSON.parseArray(myResponse.getMessage(), Book.class);
                    if(isRefresh){
                        bookList.clear();
                    }
                    if((null!=refreshlayout)&&(books==null||books.size()==0)){
                        showToast("没东西了～别扯了");
                    }else {
                        bookList.addAll(books);
                        adapter.notifyDataSetChanged();
                    }
                    loadingDialog(false);
                }else{
                    if (null != refreshlayout) {
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }
                    loadingDialog(false);
                    showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                }
            }
        });
    }
}
