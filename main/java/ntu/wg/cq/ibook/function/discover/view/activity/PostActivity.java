package ntu.wg.cq.ibook.function.discover.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.custom.TopBar;
import ntu.wg.cq.ibook.function.discover.presenter.BaPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/4/1.
 */

public class PostActivity extends BaseNetActivity {
    private TopBar topBar;
    private EditText edtTitle;
    private EditText edtContent;
    private TextView tvCount;
    private int bid;
    private int count = 50;
    private BaPresenter presenter;
    private String token;
    private Activity activity = this;
    @Override
    protected BaPresenter getPresenter() {
        return new BaPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        topBar = findView(R.id.top_bar);
        topBar.setTitle("发帖");
        topBar.setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.setRightButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                if (StringUtils.haveEmpty(title,content)){
                    showToast("请完善标题或内容");
                }else if (StringUtils.haveEmpty(token)){
                    showAppDialog(DIALOG_MODE_TO_LOGIN);
                }else {
                    loadingDialog(true);
                    presenter.postCrd(activity, bid, token,StringUtils.urlEncoding(title),StringUtils.urlEncoding(content), new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            loadingDialog(false);
                            if (myResponse.getState() == Constants.SUCCESS){
                                showToast(myResponse.getMessage());
                                finish();
                            }else {
                                showToast(myResponse.getMessage());
                            }
                        }
                    });
                }
            }
        });
        edtTitle = findView(R.id.title);
        edtContent =findView(R.id.edt_content);
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCount.setText((count-s.length())+"");
            }
        });
        tvCount = findView(R.id.count);
    }

    @Override
    protected void initData() {
        presenter = getPresenter();
        token = getApp().getSetting(Constants.TOKEN);
        Bundle bundle = getIntent().getExtras();
        bid = bundle.getInt("bid");
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
    }
}
