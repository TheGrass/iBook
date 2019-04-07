package ntu.wg.cq.ibook.function.discover.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.custom.TopBar;
import ntu.wg.cq.ibook.function.discover.presenter.BaPresenter;
import ntu.wg.cq.ibook.function.discover.view.adapter.BaListAdapter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Ba;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;

/**
 * Created by C_Q on 2018/3/28.
 */

public class BaActivity extends BaseNetActivity {
    private TopBar topBar;
    private BaPresenter presenter;
    private RecyclerView recyclerView;
    private BaListAdapter adapter;
    private GridLayoutManager manager;
    private List<Ba> data = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_ba;
    }

    @Override
    protected void initView() {
        topBar=findView(R.id.top_bar);
        recyclerView = findView(R.id.rv);
    }

    @Override
    protected void initData() {
        presenter = getPresenter();
        adapter = new BaListAdapter(data,this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer)v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("name",data.get(pos).getBaName());
                bundle.putInt("id",data.get(pos).getBid());
                startActivity(BaCardActivity.class,bundle);
            }
        });
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
        topBar.setTitle("书吧");
        topBar.setLeftButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manager = new GridLayoutManager(this,2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        presenter.getBas(this, getApp().getSetting(Constants.TOKEN), new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                loadingDialog(false);
                if (myResponse.getState() == Constants.SUCCESS){
                    data.addAll(JSON.parseArray(myResponse.getMessage(), Ba.class));
                    adapter.notifyDataSetChanged();
                }else {
                    showToast(myResponse.getMessage());
                }
            }
        });
    }

    @Override
    public void click(View v) {

    }

    @Override
    protected BaPresenter getPresenter() {
        return new BaPresenter();
    }
}
