package ntu.wg.cq.ibook.function.discover.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.custom.TopBar;
import ntu.wg.cq.ibook.function.discover.presenter.BaPresenter;
import ntu.wg.cq.ibook.function.discover.view.adapter.CardAdapter;
import ntu.wg.cq.ibook.function.discover.view.adapter.CommentAdapter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Card;
import ntu.wg.cq.ibook.pojo.Comment;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;
import ntu.wg.cq.ibook.utils.ViewUtils;

/**
 * Created by C_Q on 2018/4/1.
 */

public class BaCardActivity extends BaseNetActivity {
    private Activity activity = this;
    private String token;
    private String name;
    private int id;
    private TopBar topBar;
    private BaPresenter presenter;
    private TextView tvBaName;
    private List<Card> data = new ArrayList<>();
    private CardAdapter adapter;
    private LinearLayoutManager manager;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView tvPost;

    // start  评论
    private ImageView ivExit;
    private List<Comment> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout rlComment;
    private Button btnSend;
    private EditText edtComment;
    private SmartRefreshLayout srf;
    private RecyclerView rv;
    private TextView title;
    private TextView content;
    private int cid;
    //end
    @Override
    protected BaPresenter getPresenter() {
        return new BaPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_ba_card;
    }

    @Override
    protected void initView() {
        tvBaName = findView(R.id.tv_book_name);
        tvPost = findView(R.id.post);
        tvPost.setOnClickListener(this);
        recyclerView=findView(R.id.rv);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout=findView(R.id.srf);
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
        topBar = findView(R.id.top_bar);
        topBar.setTitle("书吧");
        topBar.setLeftButton("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.setRightButton("关注", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.haveEmpty(token)){
                    showAppDialog(DIALOG_MODE_TO_LOGIN);
                }else {
                    ViewUtils.setUnClick(topBar.getRightButton(),5);
                    presenter.doFollow(activity, token, id, new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            showToast(myResponse.getMessage());
                        }
                    });
                }
            }
        });
        //评论 start
        ivExit = findView(R.id.iv_exit);
        ivExit.setOnClickListener(this);
        title = findView(R.id.title);
        content = findView(R.id.content);
        rlComment = findView(R.id.comment);
        rlComment.setOnClickListener(this);
        btnSend = findView(R.id.send);
        btnSend.setOnClickListener(this);
        edtComment = findView(R.id.edt_comment);
        srf = findView(R.id.smrf);
        srf.setEnableRefresh(true);
        srf.setEnableAutoLoadmore(true);
        srf.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                doRefreshComment(false,refreshlayout);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                doRefreshComment(true,refreshlayout);
            }
        });
        rv = findView(R.id.rcv);
        commentAdapter = new CommentAdapter(comments,this);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setAdapter(commentAdapter);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        //end
    }

    @Override
    protected void initData() {
        token = getApp().getSetting(Constants.TOKEN);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getInt("id");
        presenter = getPresenter();
        adapter = new CardAdapter(data,this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                loadingDialog(true);
                rlComment.setVisibility(View.VISIBLE);
                title.setText(StringUtils.urlDecode(data.get(pos).getTitle()));
                content.setText(StringUtils.urlDecode(data.get(pos).getContent()));
                cid = data.get(pos).getCid();
                doRefreshComment(true,null);
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
        tvBaName.setText(name);
        loadingDialog(true);
        doRefresh(true,null);
    }

    private void doRefreshComment(final boolean isRefresh, final RefreshLayout refreshlayout){
        presenter.getComments(this, cid, isRefresh, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if (myResponse.getState()== Constants.SUCCESS){
                    if(null!=refreshlayout){
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }
                    List<Comment> temp = JSON.parseArray(myResponse.getMessage(), Comment.class);
                    if (isRefresh){
                        comments.clear();
                    }
                    if ((null!=refreshlayout)&&(temp==null||temp.size()==0)){
                        showToast("没东西了～别扯了");
                    }else {
                        comments.addAll(temp);
                        commentAdapter.notifyDataSetChanged();
                    }
                    loadingDialog(false);
                }else {
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
    private void doRefresh(final boolean isRefresh, final RefreshLayout refreshlayout){
        presenter.getCards(this,id,isRefresh, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if (myResponse.getState()== Constants.SUCCESS){
                    if(null!=refreshlayout){
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadmore();
                    }
                    List<Card> cards = JSON.parseArray(myResponse.getMessage(), Card.class);
                    if (isRefresh){
                        data.clear();
                    }
                    if ((null!=refreshlayout)&&(cards==null||cards.size()==0)){
                        showToast("没东西了～别扯了");
                    }else {
                        data.addAll(cards);
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
    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.post:
                Bundle bundle = new Bundle();
                bundle.putInt("bid",id);
                startActivity(PostActivity.class,bundle);
                break;
            case R.id.comment:
                //rlComment.setVisibility(View.GONE);
                break;
            case R.id.send:
                String content = edtComment.getText().toString();
                if (content.length()<1){
                    showToast("请输入评论");
                }else {
                    loadingDialog(true);
                    presenter.postCom(this, cid, token, StringUtils.urlEncoding(content), new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            doRefreshComment(true,null);
                            loadingDialog(false);
                            if (myResponse.getState()==Constants.SUCCESS){
                                edtComment.setText("");
                                showToast(myResponse.getMessage());
                            }else {
                                showToast(myResponse.getMessage());
                            }
                        }
                    });
                }
                break;
            case R.id.iv_exit:
                rlComment.setVisibility(View.GONE);
                break;
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(rlComment.getVisibility() == View.VISIBLE){
                rlComment.setVisibility(View.GONE);
            }else if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出书吧");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        doRefresh(true,null);
    }
}
