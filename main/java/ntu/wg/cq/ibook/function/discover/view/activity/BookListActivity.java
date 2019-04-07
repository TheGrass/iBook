package ntu.wg.cq.ibook.function.discover.view.activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.custom.TopBar;
import ntu.wg.cq.ibook.function.discover.presenter.DiscoverPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/3/22.
 */

public class BookListActivity extends BaseNetActivity {
    private TopBar topBar;
    private TextView tvShare;
    private TextView tvPublic;
    private TextView tvPrivate;
    private TextView tvGet;
    private TextView tvRandom;
    private TextView tvSure;
    private DiscoverPresenter presenter;
    @Override
    protected DiscoverPresenter getPresenter() {
        return new DiscoverPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_booklist;
    }

    @Override
    protected void initView() {
        topBar = findView(R.id.top_bar);
        topBar.setTitle("书单");
        topBar.setLeftButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvShare = findView(R.id.tv_share);
        tvShare.setOnClickListener(this);
        tvPublic = findView(R.id.tv_sub2);
        tvPublic.setOnClickListener(this);
        tvPrivate = findView(R.id.tv_sub1);
        tvPrivate.setOnClickListener(this);
        tvGet = findView(R.id.tv_get);
        tvGet.setOnClickListener(this);
        tvRandom = findView(R.id.tv_sub3);
        tvRandom.setOnClickListener(this);
        tvSure = findView(R.id.tv_sub4);
        tvSure.setOnClickListener(this);
    }

    @Override
    protected void initData() {
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

    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.tv_share:
                if(tvPublic.getVisibility() == View.GONE){
                    tvPublic.setVisibility(View.VISIBLE);
                    tvPrivate.setVisibility(View.VISIBLE);
                }else {
                    tvPublic.setVisibility(View.GONE);
                    tvPrivate.setVisibility(View.GONE);
                }
                if (tvRandom.getVisibility() == View.VISIBLE){
                    tvRandom.setVisibility(View.GONE);
                    tvSure.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_get:
                if(tvRandom.getVisibility() == View.GONE){
                    tvRandom.setVisibility(View.VISIBLE);
                    tvSure.setVisibility(View.VISIBLE);
                }else {
                    tvRandom.setVisibility(View.GONE);
                    tvSure.setVisibility(View.GONE);
                }
                if (tvPublic.getVisibility() == View.VISIBLE){
                    tvPublic.setVisibility(View.GONE);
                    tvPrivate.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_sub1: //私有
                doShare(0);
                break;
            case R.id.tv_sub2: //公开
                doShare(1);
                break;
            case R.id.tv_sub3: //随机
                getShare("0");
                break;
            case R.id.tv_sub4: //引用
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
                builder.setTitle("书单")
                        .setPlaceholder("请输入书单码")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("获取", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                String key = builder.getEditText().getText().toString();
                                if (StringUtils.haveEmpty(key)){
                                    showToast("请输入书单码");
                                }else{
                                    dialog.dismiss();
                                    getShare(key);
                                }
                            }
                        }).show();
                break;
        }
    }
    private void doShare(int pub){
        loadingDialog(true);
        presenter.doShare(this, getApp().getSetting(Constants.TOKEN), pub, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if (myResponse.getState() == Constants.SUCCESS){
                    loadingDialog(false);
                    showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                }else {
                    loadingDialog(false);
                    showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                }
            }
        });
    }
    private void getShare(String sid){
        loadingDialog(true);
        presenter.getShare(this, getApp().getSetting(Constants.TOKEN), sid, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                loadingDialog(false);
                showToast(myResponse.getMessage());
            }
        });
    }
}
