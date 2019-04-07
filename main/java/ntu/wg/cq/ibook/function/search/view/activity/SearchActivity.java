package ntu.wg.cq.ibook.function.search.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.search.presenter.SearchPresenter;
import ntu.wg.cq.ibook.function.search.view.adapter.BookListAdapter;
import ntu.wg.cq.ibook.function.user.view.activity.AddBookActivity;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.pojo.SearchResult;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/28.
 */

public class SearchActivity extends BaseNetActivity {
    private EditText edtKey;
    private TextView tvSearch;
    private TextView tvAddBook;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private BookListAdapter adapter;
    private String key;
    private List<Book> bookList = new ArrayList<>();
    private SearchPresenter presenter;
    @Override
    protected SearchPresenter getPresenter() {
        return new SearchPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        edtKey=findView(R.id.edt_key);
        tvSearch=findView(R.id.tv_search);
        tvAddBook=findView(R.id.tv_add_book);
        tvSearch.setOnClickListener(this);
        tvAddBook.setOnClickListener(this);
        recyclerView=findView(R.id.rv);
        refreshLayout=findView(R.id.srf);
    }

    @Override
    protected void initData() {
        presenter =getPresenter();
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
    }

    private void doRefresh(final boolean isRefresh, final RefreshLayout refreshlayout){
        presenter.doSearch(this,key,isRefresh, new IResult() {
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
                    tvAddBook.setVisibility(View.VISIBLE);
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
    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                key = edtKey.getText().toString();
                if(StringUtils.haveEmpty(key)){
                    showToast("请输入作者名或书名");
                }else {
                    loadingDialog(true);
                    doRefresh(true,null);
                }
                break;
            case R.id.tv_add_book:
                startActivity(AddBookActivity.class);
                break;
        }
    }
}
