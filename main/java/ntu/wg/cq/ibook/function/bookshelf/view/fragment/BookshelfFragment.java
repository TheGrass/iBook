package ntu.wg.cq.ibook.function.bookshelf.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.bookshelf.presenter.BookShelfPresenter;
import ntu.wg.cq.ibook.function.bookshelf.view.adapter.BookshelfAdapter;
import ntu.wg.cq.ibook.function.discover.view.activity.BaCardActivity;
import ntu.wg.cq.ibook.function.read.view.activity.ReadActivity;
import ntu.wg.cq.ibook.function.search.presenter.SearchPresenter;
import ntu.wg.cq.ibook.function.search.view.activity.SearchActivity;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetFragment;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.pojo.SearchResult;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.NetWorkUtils;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/6.
 */

public class BookshelfFragment extends BaseNetFragment {
    private RecyclerView recyclerView;
    private BookshelfAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvToSearch;
    private SmartRefreshLayout refreshLayout;
    private BookShelfPresenter presenter;
    private List<Book> data=new ArrayList<>();
    @Override
    protected BookShelfPresenter getPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initData() {
        presenter=getPresenter();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recyclerView=(RecyclerView)view.findViewById(R.id.rv);
        refreshLayout=(SmartRefreshLayout)view.findViewById(R.id.srf);
        tvToSearch=(TextView)view.findViewById(R.id.tv_to_search);
    }

    @Override
    protected void doChange() {
        tvToSearch.setOnClickListener(this);
        if(!StringUtils.haveEmpty(getApp().getSetting(Constants.TOKEN))){
            refreshLayout.setVisibility(View.VISIBLE);
            doRefresh(true,null);
            adapter=new BookshelfAdapter(data,getActivity());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog(true);
                    final Bundle bundle = new Bundle();
                    final Book book = data.get((Integer)v.getTag());
                    bundle.putString("title",book.getName());
                    bundle.putInt("bookId",book.getId());
                    String position  = getApp().getStringCache(getApp().getSetting(Constants.TOKEN)+book.getId());
                    bundle.putInt("position",StringUtils.haveEmpty(position)?0:Integer.valueOf(position));
                    String catalogStr = getApp().getStringCache(Constants.CATALOG_KEY+book.getId());
                    if(!StringUtils.haveEmpty(catalogStr)){
                        bundle.putString("catalog",catalogStr);
                        loadingDialog(false);
                        startActivity(ReadActivity.class,bundle);
                    }else {
                        new SearchPresenter().getCatalog(getActivity(), book.getId(), new IResult() {
                            @Override
                            public void doChange(Object result) {
                                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                                if (Constants.FAIL == myResponse.getState()){
                                    loadingDialog(false);
                                    showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                                }else {
                                    // List<Catalog> list = JSON.parseArray(msg.getMessage(),Catalog.class);
                                    if(null == myResponse.getMessage()){
                                        loadingDialog(false);
                                        showAppDialog(DIALOG_MODE_1,"获取目录失败");
                                    }else {
                                        getApp().putCache(Constants.CATALOG_KEY+book.getId(),myResponse.getMessage());
                                        bundle.putString("catalog",myResponse.getMessage());
                                        loadingDialog(false);
                                        startActivity(ReadActivity.class,bundle);
                                    }
                                }
                            }
                        });
                    }

                }
            });
            adapter.setOnDeleteClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem((Integer)v.getTag());
                }
            });
            adapter.setOnBaClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putString("name",data.get(pos).getName());
                    bundle.putInt("id",data.get(pos).getId());
                    startActivity(BaCardActivity.class,bundle);
                }
            });
            linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setEnableAutoLoadmore(false);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    doRefresh(true,refreshlayout);
                }
            });
        }
    }
    private void doRefresh(final boolean isRefresh,final RefreshLayout refreshlayout){
        if(NetWorkUtils.isNetWorkAvailable(getActivity())){
            presenter.getBookShelf(getActivity(), getApp().getSetting(Constants.TOKEN), new IResult() {
                @Override
                public void doChange(Object result) {
                    ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                    if(Constants.SUCCESS==myResponse.getState()){
                        List<Book> books = JSON.parseArray(myResponse.getMessage(), Book.class);
                        if(isRefresh){
                            data.clear();
                        }
                        data.addAll(books);
                        if(null!=refreshlayout){
                            refreshlayout.finishRefresh();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }else {
         showAppDialog(DIALOG_MODE_NO_NET);
        }
    }
    @Override
    protected void click(View v) {
        switch (v.getId()){
            case R.id.tv_to_search:
                startActivity(SearchActivity.class);
                break;
        }
    }

    protected void deleteItem(final int position){
        loadingDialog(true);
        presenter.delete(getActivity(), getApp().getSetting(Constants.TOKEN), data.get(position).getId(), new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if (Constants.SUCCESS == myResponse.getState()){
                    data.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                    loadingDialog(false);
                }else {
                    loadingDialog(false);
                    showToast(myResponse.getMessage());
                }
            }
        });

    }
}
